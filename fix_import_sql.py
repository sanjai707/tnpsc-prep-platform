import os

path = r'D:\E-drive\TNPSC-app\import-polity-batch.sql'
oldcols = 'INSERT INTO questions (topic, subject, category, questionEn, questionTa, optionAEn, optionATa, optionBEn, optionBTa, optionCEn, optionCTa, optionDEn, optionDTa, correctAnswer) VALUES '
newcols = 'INSERT INTO questions (topic, subject, category, question_en, question_ta, option_a_en, option_a_ta, option_b_en, option_b_ta, option_c_en, option_c_ta, option_d_en, option_d_ta, correct_answer, explanation_en, explanation_ta, tnpsc_tip_en, tnpsc_tip_ta, difficulty) VALUES '

with open(path, 'r', encoding='utf-8') as f:
    lines = f.readlines()

changed = 0
out_lines = []
for line in lines:
    if oldcols in line and 'correct_answer' not in line:
        line = line.replace(oldcols, newcols, 1)
        if line.rstrip().endswith("');"):
            line = line.rstrip()[:-3] + "', NULL, NULL, NULL, NULL, 'MEDIUM');\n"
        changed += 1
    out_lines.append(line)

with open(path, 'w', encoding='utf-8') as f:
    f.writelines(out_lines)

text = ''.join(out_lines)
print('changed', changed)
print('new cols count', text.count(newcols))
print("A count", text.count("'A', NULL, NULL, NULL, NULL, 'MEDIUM');"))
print("B count", text.count("'B', NULL, NULL, NULL, NULL, 'MEDIUM');"))
print("C count", text.count("'C', NULL, NULL, NULL, NULL, 'MEDIUM');"))
print("D count", text.count("'D', NULL, NULL, NULL, NULL, 'MEDIUM');"))
