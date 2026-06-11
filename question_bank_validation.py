import json
import re
from collections import Counter, defaultdict

INPUT_PATH = r"c:\Users\srisanjai\Downloads\tnpsc_polity_50_mcqs.json"
OUTPUT_SQL = 'import-polity-batch.sql'
REPORT_SUMMARY = 'validation-summary.md'
REPORT_DUPLICATES = 'duplicate-report.md'
REPORT_TRANSLATION = 'translation-report.md'
REPORT_QUALITY = 'quality-report.md'

REQUIRED_FIELDS = [
    'topic', 'subject', 'category', 'questionEn', 'questionTa',
    'optionAEn', 'optionATa', 'optionBEn', 'optionBTa',
    'optionCEn', 'optionCTa', 'optionDEn', 'optionDTa', 'correctAnswer'
]
VALID_SUBJECTS = {
    'Polity': {
        'Fundamental Rights', 'Parliament', 'President', 'Judiciary',
        'DPSP', 'Constitutional Amendments', 'Emergency Provisions', 'Local Government'
    }
}
VALID_ANSWERS = {'A', 'B', 'C', 'D'}
MIN_QUESTION_LENGTH = 20

SQL_COLUMNS = [
    'subject', 'topic', 'question_en', 'question_ta',
    'option_a_en', 'option_a_ta', 'option_b_en', 'option_b_ta',
    'option_c_en', 'option_c_ta', 'option_d_en', 'option_d_ta',
    'correct_answer', 'explanation_en', 'explanation_ta'
]

OPTION_EN_FIELDS = ['optionAEn', 'optionBEn', 'optionCEn', 'optionDEn']
OPTION_TA_FIELDS = ['optionATa', 'optionBTa', 'optionCTa', 'optionDTa']
CORRECT_OPTION_MAP = {
    'A': 'optionAEn',
    'B': 'optionBEn',
    'C': 'optionCEn',
    'D': 'optionDEn'
}
DB_FIELD_MAP = {
    'subject': 'subject',
    'topic': 'topic',
    'questionEn': 'question_en',
    'questionTa': 'question_ta',
    'optionAEn': 'option_a_en',
    'optionATa': 'option_a_ta',
    'optionBEn': 'option_b_en',
    'optionBTa': 'option_b_ta',
    'optionCEn': 'option_c_en',
    'optionCTa': 'option_c_ta',
    'optionDEn': 'option_d_en',
    'optionDTa': 'option_d_ta',
    'correctAnswer': 'correct_answer',
    'explanationEn': 'explanation_en',
    'explanationTa': 'explanation_ta'
}


def normalize_text(text):
    if not isinstance(text, str):
        return ''
    return re.sub(r"\s+", " ", text.strip())


def normalize_for_duplicate(text):
    if not isinstance(text, str):
        return ''
    text = text.strip().lower()
    text = re.sub(r"[^\w\s]", "", text, flags=re.UNICODE)
    return re.sub(r"\s+", " ", text).strip()


def quote_sql(value):
    if value is None:
        return 'NULL'
    escaped = value.replace("'", "''")
    return f"'{escaped}'"


def is_blank(value):
    return value is None or str(value).strip() == ''


def validate_record(record, index):
    missing_fields = []
    for field in REQUIRED_FIELDS:
        if field not in record or is_blank(record.get(field)):
            missing_fields.append(field)

    invalid_answer = None
    if not is_blank(record.get('correctAnswer')) and record.get('correctAnswer') not in VALID_ANSWERS:
        invalid_answer = record.get('correctAnswer')

    subject_issue = None
    subject_value = normalize_text(record.get('subject'))
    if subject_value == '' or subject_value not in VALID_SUBJECTS:
        subject_issue = record.get('subject')

    topic_issue = None
    topic_value = normalize_text(record.get('topic'))
    if subject_value in VALID_SUBJECTS and topic_value not in VALID_SUBJECTS[subject_value]:
        topic_issue = record.get('topic')
    elif subject_value not in VALID_SUBJECTS and topic_value:
        topic_issue = record.get('topic')

    length_issue = None
    question_en = normalize_text(record.get('questionEn'))
    if question_en and len(question_en) < MIN_QUESTION_LENGTH:
        length_issue = len(question_en)

    missing_tamil = []
    if is_blank(record.get('questionTa')):
        missing_tamil.append('questionTa')
    for opt in OPTION_TA_FIELDS:
        if is_blank(record.get(opt)):
            missing_tamil.append(opt)

    suspicious_translations = []
    if question_en and normalize_text(record.get('questionTa')) and question_en == normalize_text(record.get('questionTa')):
        suspicious_translations.append('questionTa same as questionEn')
    for en_field, ta_field in zip(OPTION_EN_FIELDS, OPTION_TA_FIELDS):
        en_value = normalize_text(record.get(en_field))
        ta_value = normalize_text(record.get(ta_field))
        if en_value and ta_value and en_value == ta_value:
            suspicious_translations.append(f'{en_field} same as {ta_field}')

    duplicate_options = []
    option_values = [normalize_text(record.get(opt)) for opt in OPTION_EN_FIELDS]
    option_names = ['A', 'B', 'C', 'D']
    seen = {}
    for name, value in zip(option_names, option_values):
        if value:
            if value in seen:
                duplicate_options.append((name, seen[value]))
            else:
                seen[value] = name

    correct_option_issue = None
    correct_answer_value = normalize_text(record.get('correctAnswer'))
    if correct_answer_value in CORRECT_OPTION_MAP:
        correct_option_field = CORRECT_OPTION_MAP[correct_answer_value]
        if is_blank(record.get(correct_option_field)):
            correct_option_issue = correct_answer_value
    elif not is_blank(record.get('correctAnswer')):
        correct_option_issue = record.get('correctAnswer')

    is_valid = True
    if missing_fields or invalid_answer or subject_issue or topic_issue or length_issue or missing_tamil or duplicate_options or correct_option_issue:
        is_valid = False

    return {
        'index': index,
        'missing_fields': missing_fields,
        'invalid_answer': invalid_answer,
        'subject_issue': subject_issue,
        'topic_issue': topic_issue,
        'length_issue': length_issue,
        'missing_tamil': missing_tamil,
        'suspicious_translations': suspicious_translations,
        'duplicate_options': duplicate_options,
        'correct_option_issue': correct_option_issue,
        'is_valid': is_valid,
        'normalized_question': normalize_for_duplicate(question_en)
    }


def load_json(path):
    with open(path, 'r', encoding='utf-8') as f:
        return json.load(f)


def build_sql_value(record, field_name):
    raw_value = record.get(field_name)
    if is_blank(raw_value):
        return None
    return normalize_text(raw_value)


def main():
    data = load_json(INPUT_PATH)
    quality_report = None
    if isinstance(data, list) and data and isinstance(data[-1], dict) and 'qualityReport' in data[-1]:
        quality_report = data[-1]['qualityReport']
        records = data[:-1]
    else:
        records = data if isinstance(data, list) else []

    total_records = len(records)
    expected_total = quality_report.get('totalQuestions') if quality_report else None

    schema_issues = []
    answer_issues = []
    topic_issues = []
    subject_issues = []
    length_issues = []
    missing_tamil_issues = []
    suspicious_translations = []
    duplicate_options_issues = []
    correct_option_issues = []
    exact_duplicate_questions_en = []
    exact_duplicate_questions_ta = []
    semantic_duplicate_groups = []

    question_en_counter = Counter()
    question_ta_counter = Counter()
    normalized_question_counter = Counter()
    question_en_map = defaultdict(list)
    question_ta_map = defaultdict(list)
    normalized_question_map = defaultdict(list)
    topic_distribution = Counter()
    invalid_records = set()
    valid_record_ids = set()

    for idx, record in enumerate(records, start=1):
        record_id = idx
        validation = validate_record(record, record_id)

        if not validation['is_valid']:
            invalid_records.add(record_id)

        if validation['missing_fields']:
            schema_issues.append((record_id, validation['missing_fields']))
        if validation['invalid_answer'] is not None:
            answer_issues.append((record_id, validation['invalid_answer']))
        if validation['subject_issue'] is not None:
            subject_issues.append((record_id, validation['subject_issue']))
        if validation['topic_issue'] is not None:
            topic_issues.append((record_id, validation['topic_issue']))
        if validation['length_issue'] is not None:
            length_issues.append((record_id, validation['length_issue']))
        if validation['missing_tamil']:
            missing_tamil_issues.append((record_id, validation['missing_tamil']))
        if validation['suspicious_translations']:
            suspicious_translations.append((record_id, validation['suspicious_translations']))
        if validation['duplicate_options']:
            duplicate_options_issues.append((record_id, validation['duplicate_options']))
        if validation['correct_option_issue'] is not None:
            correct_option_issues.append((record_id, validation['correct_option_issue']))

        question_en = normalize_text(record.get('questionEn', ''))
        question_ta = normalize_text(record.get('questionTa', ''))
        normalized_question = validation['normalized_question']

        question_en_counter[question_en] += 1
        question_en_map[question_en].append(record_id)
        question_ta_counter[question_ta] += 1
        question_ta_map[question_ta].append(record_id)
        normalized_question_counter[normalized_question] += 1
        normalized_question_map[normalized_question].append(record_id)

        topic_distribution[normalize_text(record.get('topic')) or 'MISSING'] += 1

    for text, ids in question_en_map.items():
        if text and len(ids) > 1:
            exact_duplicate_questions_en.append((text, ids))
            invalid_records.update(ids)
    for text, ids in question_ta_map.items():
        if text and len(ids) > 1:
            exact_duplicate_questions_ta.append((text, ids))
            invalid_records.update(ids)
    for normalized, ids in normalized_question_map.items():
        if normalized and len(ids) > 1:
            semantic_duplicate_groups.append((normalized, ids))
            invalid_records.update(ids)

    valid_record_ids = {idx for idx in range(1, total_records + 1) if idx not in invalid_records}
    valid_records = len(valid_record_ids)
    ready_for_import = valid_records == total_records
    quality_score = round((valid_records / total_records) * 100, 2) if total_records else 0.0

    SQL_FIELD_ORDER = [
        'subject',
        'topic',
        'questionEn',
        'questionTa',

        'optionAEn',
        'optionATa',

        'optionBEn',
        'optionBTa',

        'optionCEn',
        'optionCTa',

        'optionDEn',
        'optionDTa',

        'correctAnswer',
        'explanationEn',
        'explanationTa'
    ]

    with open(OUTPUT_SQL, 'w', encoding='utf-8') as f:
        f.write('BEGIN;\n')

        for idx, record in enumerate(records, start=1):
            if idx not in valid_record_ids:
                continue

            values = []

            for field in SQL_FIELD_ORDER:
                sql_value = build_sql_value(record, field)
                values.append(quote_sql(sql_value))

            f.write(
                f"INSERT INTO questions ({', '.join(SQL_COLUMNS)}) "
                f"VALUES ({', '.join(values)});\n"
            )

        f.write('COMMIT;\n')

    with open(REPORT_DUPLICATES, 'w', encoding='utf-8') as f:
        f.write('# Duplicate Report\n\n')
        f.write(f'Total exact duplicate questionEn groups: {len(exact_duplicate_questions_en)}\n')
        f.write(f'Total exact duplicate questionTa groups: {len(exact_duplicate_questions_ta)}\n')
        f.write(f'Total semantic duplicate groups: {len(semantic_duplicate_groups)}\n\n')

        if exact_duplicate_questions_en:
            f.write('## Exact duplicate questionEn\n')
            for text, ids in exact_duplicate_questions_en:
                f.write(f'- IDs {ids}: "{text[:200]}"\n')
            f.write('\n')

        if exact_duplicate_questions_ta:
            f.write('## Exact duplicate questionTa\n')
            for text, ids in exact_duplicate_questions_ta:
                f.write(f'- IDs {ids}: "{text[:200]}"\n')
            f.write('\n')

        if semantic_duplicate_groups:
            f.write('## Semantic duplicate questions\n')
            for normalized, ids in semantic_duplicate_groups:
                f.write(f'- IDs {ids}: "{normalized[:200]}"\n')
            f.write('\n')

    with open(REPORT_TRANSLATION, 'w', encoding='utf-8') as f:
        f.write('# Translation Report\n\n')
        f.write(f'Total records with missing Tamil translation: {len(missing_tamil_issues)}\n')
        f.write(f'Total suspicious translation issues: {len(suspicious_translations)}\n\n')

        if missing_tamil_issues:
            f.write('## Missing Tamil translations\n')
            for rid, missing in missing_tamil_issues:
                f.write(f'- ID {rid}: missing {", ".join(missing)}\n')
            f.write('\n')

        if suspicious_translations:
            f.write('## Suspicious translations\n')
            for rid, notes in suspicious_translations:
                f.write(f'- ID {rid}: {", ".join(notes)}\n')
            f.write('\n')

    with open(REPORT_SUMMARY, 'w', encoding='utf-8') as f:
        f.write('# Validation Summary\n\n')
        f.write(f'- Total records: {total_records}\n')
        f.write(f'- Reported totalQuestions: {expected_total}\n')
        f.write(f'- Valid records: {valid_records}\n')
        f.write(f'- Invalid records: {len(invalid_records)}\n')
        f.write(f'- Ready for import: {'YES' if ready_for_import else 'NO'}\n')
        f.write(f'- Quality score: {quality_score}%\n\n')
        f.write('## Issues\n')
        if schema_issues:
            f.write(f'- Schema issues: {len(schema_issues)} records\n')
        if answer_issues:
            f.write(f'- Answer key issues: {len(answer_issues)} records\n')
        if subject_issues:
            f.write(f'- Subject issues: {len(subject_issues)} records\n')
        if topic_issues:
            f.write(f'- Topic issues: {len(topic_issues)} records\n')
        if length_issues:
            f.write(f'- Question length issues: {len(length_issues)} records\n')
        if missing_tamil_issues:
            f.write(f'- Missing Tamil translations: {len(missing_tamil_issues)} records\n')
        if duplicate_options_issues:
            f.write(f'- Duplicate options within records: {len(duplicate_options_issues)} records\n')
        if correct_option_issues:
            f.write(f'- Correct option mismatch: {len(correct_option_issues)} records\n')
        if suspicious_translations:
            f.write(f'- Suspicious untranslated fields: {len(suspicious_translations)} records\n')
        f.write('\n## Topic distribution\n')
        for topic, count in topic_distribution.items():
            f.write(f'- {topic}: {count}\n')

    with open(REPORT_QUALITY, 'w', encoding='utf-8') as f:
        f.write('# Quality Report\n\n')
        f.write(f'- Total records: {total_records}\n')
        f.write(f'- Valid records: {valid_records}\n')
        f.write(f'- Invalid records: {len(invalid_records)}\n')
        f.write(f'- Quality score: {quality_score}%\n')
        f.write(f'- Ready for import: {'YES' if ready_for_import else 'NO'}\n\n')
        f.write('## Topic distribution\n')
        for topic, count in topic_distribution.items():
            f.write(f'- {topic}: {count}\n')
        f.write('\n## Validation summary\n')
        f.write(f'- Exact duplicate questionEn groups: {len(exact_duplicate_questions_en)}\n')
        f.write(f'- Exact duplicate questionTa groups: {len(exact_duplicate_questions_ta)}\n')
        f.write(f'- Semantic duplicate groups: {len(semantic_duplicate_groups)}\n')
        f.write(f'- Missing Tamil translation records: {len(missing_tamil_issues)}\n')
        f.write(f'- Suspicious translation issues: {len(suspicious_translations)}\n')

    print(f'Validation complete. Summary written to {REPORT_SUMMARY}')


if __name__ == '__main__':
    main()
