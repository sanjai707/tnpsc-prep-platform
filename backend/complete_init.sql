-- Complete Database Initialization Script

-- 1. Drop existing database and user if they exist
DROP DATABASE IF EXISTS tnpsc_app;
DROP USER IF EXISTS tnpsc_user;

-- 2. Create user
CREATE USER tnpsc_user WITH PASSWORD 'tnpsc_password';

-- 3. Create database
CREATE DATABASE tnpsc_app OWNER tnpsc_user;

-- 4. Grant privileges
GRANT ALL PRIVILEGES ON DATABASE tnpsc_app TO tnpsc_user;

-- 5. Connect to the database and create tables
\c tnpsc_app

-- 6. Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    streak_count INTEGER DEFAULT 0,
    last_active_date DATE
);

-- 7. Create questions table
CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    topic VARCHAR(255),
    subject VARCHAR(255),
    question_en TEXT,
    question_ta TEXT,
    option_a_en TEXT,
    option_a_ta TEXT,
    option_b_en TEXT,
    option_b_ta TEXT,
    option_c_en TEXT,
    option_c_ta TEXT,
    option_d_en TEXT,
    option_d_ta TEXT,
    correct_answer VARCHAR(1),
    explanation_en TEXT,
    explanation_ta TEXT
);

-- 8. Create user_answers table
CREATE TABLE user_answers (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    question_id INTEGER NOT NULL REFERENCES questions(id),
    subject VARCHAR(255),
    selected_answer VARCHAR(1),
    is_correct BOOLEAN,
    attempted_at TIMESTAMP,
    answered_at TIMESTAMP
);

-- 9. Grant privileges to the user
ALTER SCHEMA public OWNER TO tnpsc_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO tnpsc_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO tnpsc_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO tnpsc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO tnpsc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO tnpsc_user;

-- 10. Insert test data
INSERT INTO questions (subject, topic, question_en, question_ta, option_a_en, option_a_ta, option_b_en, option_b_ta, option_c_en, option_c_ta, option_d_en, option_d_ta, correct_answer, explanation_en, explanation_ta) VALUES
('Polity', 'Indian Polity', 'Which part of the Indian Constitution contains the fundamental rights?', 'இந்திய அரசியலமைப்பின் எந்த பகுதி அடிப்படை உரிமைகள் கொண்டது?', 'Part I', 'பகுதி I', 'Part III', 'பகுதி III', 'Part V', 'பகுதி V', 'Part VII', 'பகுதி VII', 'B', 'Fundamental rights are enshrined in Part III of the Constitution.', 'அடிப்படை உரிமைகள் அரசியலமைப்பின் பகுதி III இல் உள்ளன.'),
('Polity', 'Indian Polity', 'Who appoints the Chief Election Commissioner of India?', 'இந்தியத்தின் தலைமை தேர்தல் ஆணையாளரை யார் நியமிக்கிறார்?', 'Prime Minister', 'தலைமை செயலர்', 'President', 'நிர்வாகர்', 'Parliament', 'மன்றம்', 'Supreme Court', 'உச்ச நீதிமன்றம்', 'B', 'The President appoints the Chief Election Commissioner based on the advice of the Council of Ministers.', 'தலைமை தேர்தல் ஆணையாளரை அமைச்சர்கள் குழுவின் ஆலோசனைப்படி குடியரசுத் தலைவர் நியமிக்கிறார்.'),
('Indian Polity', 'Which amendment introduced the Goods and Services Tax in India?', 'இந்தியாவில் சரக்குகள் மற்றும் சேவைகள் வரியை அறிமுகப்படுத்திய திருத்தம் எது?', '101st Amendment', '101வது திருத்தம்', '97th Amendment', '97வது திருத்தம்', '42nd Amendment', '42வது திருத்தம்', '73rd Amendment', '73வது திருத்தம்', 'A', 'GST was introduced through the 101st Constitutional Amendment Act, 2016.', 'GST 101வது அரசியலமைப்பு திருத்தச் சட்டம் மூலம் அறிமுகப்படுத்தப்பட்டது.'),
('Indian Polity', 'What is the minimum age for a person to become the President of India?', 'இந்திய குடியரசுத் தலைவராக ஆக குறைந்த பட்ச வயது என்ன?', '30 years', '30 வயது', '35 years', '35 வயது', '40 years', '40 வயது', '25 years', '25 வயது', 'B', 'The Constitution requires the President to be at least 35 years old.', 'அரசியலமைப்பு குடியரசுத் தலைவர் குறைந்தது 35 வயதில் இருக்க வேண்டும் என்று சொல்கிறது.'),
('Indian Polity', 'Which article guarantees the right to freedom of speech and expression?', 'பேச்சு மற்றும் வெளிப்பாடின் உரிமையை எந்த கட்டுரை உறுதிசெய்கிறது?', 'Article 14', 'கட்டுரை 14', 'Article 19', 'கட்டுரை 19', 'Article 21', 'கட்டுரை 21', 'Article 32', 'கட்டுரை 32', 'B', 'Article 19 protects freedom of speech and expression.', 'பேச்சு மற்றும் வெளிப்பாடு பற்றிய உரிமையை கட்டுரை 19 காப்பாற்றுகிறது.'),
('Indian Polity', 'What is the term of the Lok Sabha?', 'லோக் சபாவின் காலம் என்ன?', '4 years', '4 ஆண்டுகள்', '5 years', '5 ஆண்டுகள்', '6 years', '6 ஆண்டுகள்', '3 years', '3 ஆண்டுகள்', 'B', 'The Lok Sabha has a term of five years unless dissolved earlier.', 'லோக் சபாவுக்கு பைந்து வருட காலம் உள்ளது.'),
('Indian Polity', 'Who is the guardian of the Constitution?', 'அரசியலமைப்பின் காவலர் யார்?', 'Prime Minister', 'தலைமை செயலர்', 'President', 'நிர்வாகர்', 'Supreme Court', 'உச்ச நீதிமன்றம்', 'Attorney General', 'ஒழுங்கு ஆணையர்', 'C', 'The Supreme Court is considered the guardian of the Constitution.', 'உச்ச நீதிமன்றம் அரசியலமைப்பின் காவலராக கருதப்படுகிறது.'),
('Indian Polity', 'Which body resolves disputes between states?', 'மாநிலங்களுக்கிடையிலான மரியாதைகளை எது தீர்க்கிறது?', 'Parliament', 'மன்றம்', 'Supreme Court', 'உச்ச நீதிமன்றம்', 'President', 'நிர்வாகர்', 'Election Commission', 'தேர்தல் ஆணையம்', 'B', 'The Supreme Court adjudicates disputes between states.', 'மாநிலங்களுக்கிடையிலான மரியாதைகளை உச்ச நீதிமன்றம் தீர்த்துகொள்ளும்.'),
('Indian Polity', 'Under which article is the Right to Constitutional Remedies guaranteed?', 'அரசியலமைப்பு நிவாரண உரிமை எந்தக் கட்டுரையின் கீழ் உறுதி செய்யப்படுகிறது?', 'Article 32', 'கட்டுரை 32', 'Article 21', 'கட்டுரை 21', 'Article 19', 'கட்டுரை 19', 'Article 25', 'கட்டுரை 25', 'A', 'Article 32 guarantees the Right to Constitutional Remedies.', 'கட்டுரை 32 அரசியலமைப்பு நிவாரண உரிமையை உறுதிசெய்கிறது.'),
('Indian Polity', 'Who presides over a joint sitting of Parliament?', 'மன்றத்தின் இணைந்த அமர்வில் யார் தலைமை வகிக்கிறார்?', 'President', 'நிர்வாகர்', 'Prime Minister', 'தலைமை செயலர்', 'Speaker of Lok Sabha', 'லோக் சபா தலைவர்', 'Chief Justice of India', 'இந்திய உயர்நீதிமன்றத் தலைவர்', 'C', 'The Speaker of the Lok Sabha presides over joint sittings.', 'இணைந்த அமர்வில் லோக் சபா தலைவர் தலைமை வகிக்கிறார்.');

-- Ensure seeded questions have subject metadata when not explicitly assigned.
UPDATE questions SET subject = 'Polity' WHERE topic IN ('Indian Polity', 'Fundamental Rights', 'Parliament');
UPDATE questions SET subject = 'History' WHERE topic IN ('Ancient History', 'Medieval History', 'Modern History');
UPDATE questions SET subject = 'Science' WHERE topic IN ('Physics', 'Biology');
UPDATE questions SET subject = 'Economics' WHERE topic = 'Economics';
UPDATE questions SET subject = 'Current Affairs' WHERE topic = 'Current Affairs';
UPDATE questions SET subject = 'General' WHERE subject IS NULL;

-- Ensure seeded questions have subject metadata when not explicitly assigned.
UPDATE questions SET subject = 'Polity' WHERE topic IN ('Indian Polity', 'Fundamental Rights', 'Parliament');
UPDATE questions SET subject = 'History' WHERE topic IN ('Ancient History', 'Medieval History', 'Modern History');
UPDATE questions SET subject = 'Science' WHERE topic IN ('Physics', 'Biology');
UPDATE questions SET subject = 'Economics' WHERE topic = 'Economics';
UPDATE questions SET subject = 'Current Affairs' WHERE topic = 'Current Affairs';
UPDATE questions SET subject = 'General' WHERE subject IS NULL;
