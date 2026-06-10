import json
import os
import re
from collections import Counter, defaultdict

INPUT_PATH = r"c:\Users\srisanjai\Downloads\tnpsc_polity_50_mcqs.json"
OUTPUT_SQL = 'import-polity-batch.sql'
REPORT_SUMMARY = 'validation-summary.md'
REPORT_DUPLICATES = 'duplicate-report.md'
REPORT_TRANSLATION = 'translation-report.md'

REQUIRED_FIELDS = [
    'topic', 'subject', 'category', 'questionEn', 'questionTa',
    'optionAEn', 'optionATa', 'optionBEn', 'optionBTa',
    'optionCEn', 'optionCTa', 'optionDEn', 'optionDTa', 'correctAnswer'
]
VALID_TOPICS = {
    'Fundamental Rights', 'Parliament', 'President', 'Judiciary',
    'DPSP', 'Constitutional Amendments', 'Emergency Provisions', 'Local Government'
}
VALID_ANSWERS = {'A', 'B', 'C', 'D'}
EXPECTED_SUBJECT = 'Polity'

SQL_TEMPLATE = "INSERT INTO questions ({columns}) VALUES\n" + ",\n".join(["({values})"])


def normalize_text(text):
    return re.sub(r"\s+", " ", text.strip()) if isinstance(text, str) else text


def quote_sql(value):
    if value is None:
        return 'NULL'
    escaped = value.replace("'", "''")
    return f"'{escaped}'"


def validate_record(record, index):
    missing = []
    for field in REQUIRED_FIELDS:
        if field not in record or record[field] is None:
            missing.append(field)
    invalid_answer = None
    if 'correctAnswer' in record and record.get('correctAnswer') not in VALID_ANSWERS:
        invalid_answer = record.get('correctAnswer')
    topic_issue = None
    if 'topic' in record and record.get('topic') not in VALID_TOPICS:
        topic_issue = record.get('topic')
    subject_issue = None
    if 'subject' in record and record.get('subject') != EXPECTED_SUBJECT:
        subject_issue = record.get('subject')
    return {
        'index': index,
        'missing_fields': missing,
        'invalid_answer': invalid_answer,
        'topic_issue': topic_issue,
        'subject_issue': subject_issue,
    }


def load_json(path):
    with open(path, 'r', encoding='utf-8') as f:
        return json.load(f)


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
    translation_issues = []
    suspicious_untranslated = []
    duplicate_questions_en = []
    duplicate_questions_ta = []

    question_en_counter = Counter()
    question_ta_counter = Counter()
    question_en_map = defaultdict(list)
    question_ta_map = defaultdict(list)

    topic_distribution = Counter()
    invalid_records = set()

    for idx, record in enumerate(records, start=1):
        record_id = idx
        text = record.get('questionEn', '')
        question_en_counter[text] += 1
        question_en_map[text].append(record_id)
        text_ta = record.get('questionTa', '')
        question_ta_counter[text_ta] += 1
        question_ta_map[text_ta].append(record_id)

        validation = validate_record(record, record_id)
        if validation['missing_fields']:
            schema_issues.append((record_id, validation['missing_fields']))
            invalid_records.add(record_id)
        if validation['invalid_answer'] is not None:
            answer_issues.append((record_id, validation['invalid_answer']))
            invalid_records.add(record_id)
        if validation['topic_issue'] is not None:
            topic_issues.append((record_id, validation['topic_issue']))
            invalid_records.add(record_id)
        if validation['subject_issue'] is not None:
            subject_issues.append((record_id, validation['subject_issue']))
            invalid_records.add(record_id)

        if not record.get('questionTa') or not record['questionTa'].strip():
            translation_issues.append((record_id, 'Missing questionTa'))
            invalid_records.add(record_id)
        for opt in ['optionATa', 'optionBTa', 'optionCTa', 'optionDTa']:
            if not record.get(opt) or not record[opt].strip():
                translation_issues.append((record_id, f'Missing {opt}'))
                invalid_records.add(record_id)

        if record.get('questionEn') and record.get('questionTa') and record['questionEn'].strip() == record['questionTa'].strip():
            suspicious_untranslated.append((record_id, 'questionTa same as questionEn'))
        for pair in [('optionAEn', 'optionATa'), ('optionBEn', 'optionBTa'), ('optionCEn', 'optionCTa'), ('optionDEn', 'optionDTa')]:
            if record.get(pair[0]) and record.get(pair[1]) and record[pair[0]].strip() == record[pair[1]].strip():
                suspicious_untranslated.append((record_id, f'{pair[0]} same as {pair[1]}'))

        topic_distribution[record.get('topic', 'MISSING')] += 1

    for text, count in question_en_counter.items():
        if count > 1:
            duplicate_questions_en.append((text, question_en_map[text]))
            for rid in question_en_map[text]:
                invalid_records.add(rid)
    for text, count in question_ta_counter.items():
        if count > 1:
            duplicate_questions_ta.append((text, question_ta_map[text]))
            for rid in question_ta_map[text]:
                invalid_records.add(rid)

    valid_records = total_records - len(invalid_records)
    ready_for_import = len(invalid_records) == 0

    # Generate SQL
    columns = ', '.join(REQUIRED_FIELDS)
    rows = []
    for record in records:
        values = ', '.join(quote_sql(normalize_text(record.get(field, ''))) for field in REQUIRED_FIELDS)
        rows.append(f"({values})")

    with open(OUTPUT_SQL, 'w', encoding='utf-8') as f:
        f.write('BEGIN;\n')
        for i, row in enumerate(rows):
            f.write(f"INSERT INTO questions ({columns}) VALUES {row};\n")
        f.write('COMMIT;\n')

    with open(REPORT_DUPLICATES, 'w', encoding='utf-8') as f:
        f.write('# Duplicate Report\n\n')
        f.write(f'Total duplicate questionEn groups: {len(duplicate_questions_en)}\n')
        f.write(f'Total duplicate questionTa groups: {len(duplicate_questions_ta)}\n\n')
        if duplicate_questions_en:
            f.write('## Exact duplicate questionEn\n')
            for text, ids in duplicate_questions_en:
                f.write(f'- IDs {ids}: "{text[:100]}"\n')
            f.write('\n')
        if duplicate_questions_ta:
            f.write('## Exact duplicate questionTa\n')
            for text, ids in duplicate_questions_ta:
                f.write(f'- IDs {ids}: "{text[:100]}"\n')
            f.write('\n')

    with open(REPORT_TRANSLATION, 'w', encoding='utf-8') as f:
        f.write('# Translation Report\n\n')
        f.write(f'Total records with translation issues: {len(translation_issues)}\n')
        f.write(f'Total suspicious untranslated issues: {len(suspicious_untranslated)}\n\n')
        if translation_issues:
            f.write('## Missing Tamil translations\n')
            for rid, note in translation_issues:
                f.write(f'- ID {rid}: {note}\n')
            f.write('\n')
        if suspicious_untranslated:
            f.write('## Suspicious untranslated fields\n')
            for rid, note in suspicious_untranslated:
                f.write(f'- ID {rid}: {note}\n')
            f.write('\n')

    with open(REPORT_SUMMARY, 'w', encoding='utf-8') as f:
        f.write('# Validation Summary\n\n')
        f.write(f'- Total records: {total_records}\n')
        f.write(f'- Reported totalQuestions: {expected_total}\n')
        f.write(f'- Valid records: {valid_records}\n')
        f.write(f'- Invalid records: {len(invalid_records)}\n')
        f.write(f'- Duplicate count (exact questionEn or questionTa): {len(invalid_records)}\n')
        f.write(f'- Ready for import: {'YES' if ready_for_import else 'NO'}\n\n')
        f.write('## Issues\n')
        if schema_issues:
            f.write(f'- Schema issues: {len(schema_issues)} records\n')
        if answer_issues:
            f.write(f'- Answer key issues: {len(answer_issues)} records\n')
        if topic_issues:
            f.write(f'- Topic issues: {len(topic_issues)} records\n')
        if subject_issues:
            f.write(f'- Subject issues: {len(subject_issues)} records\n')
        if translation_issues:
            f.write(f'- Translation issues: {len(translation_issues)} records\n')
        if suspicious_untranslated:
            f.write(f'- Suspicious untranslated fields: {len(suspicious_untranslated)} records\n')
        f.write('\n## Topic distribution\n')
        for topic, count in topic_distribution.items():
            f.write(f'- {topic}: {count}\n')

    print(f'Validation complete. Summary written to {REPORT_SUMMARY}')


if __name__ == '__main__':
    main()
