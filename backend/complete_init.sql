```sql
-- ============================================
-- TNPSC APP DATABASE INITIALIZATION
-- ============================================

-- ============================================
-- CREATE USER
-- ============================================

CREATE USER tnpsc_user 
WITH PASSWORD 'tnpsc_password';

-- ============================================
-- CREATE DATABASE
-- ============================================

CREATE DATABASE tnpsc_app
OWNER tnpsc_user;

-- ============================================
-- CONNECT DATABASE
-- ============================================

\c tnpsc_app

-- ============================================
-- USERS TABLE
-- ============================================

CREATE TABLE users (

    id SERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    email VARCHAR(255) UNIQUE NOT NULL,

    password VARCHAR(255) NOT NULL,

    streak_count INTEGER DEFAULT 0,

    last_active_date DATE
);

-- ============================================
-- QUESTIONS TABLE
-- ============================================

CREATE TABLE questions (

    id SERIAL PRIMARY KEY,

    category VARCHAR(100),

    topic VARCHAR(255),

    difficulty VARCHAR(20),

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

-- ============================================
-- USER ANSWERS TABLE
-- ============================================

CREATE TABLE user_answers (

    id SERIAL PRIMARY KEY,

    user_id INTEGER NOT NULL REFERENCES users(id),

    question_id INTEGER NOT NULL REFERENCES questions(id),

    selected_answer VARCHAR(1),

    is_correct BOOLEAN,

    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- GRANT PRIVILEGES
-- ============================================

ALTER SCHEMA public OWNER TO tnpsc_user;

GRANT ALL PRIVILEGES 
ON SCHEMA public TO tnpsc_user;

GRANT ALL PRIVILEGES 
ON ALL TABLES IN SCHEMA public TO tnpsc_user;

GRANT ALL PRIVILEGES 
ON ALL SEQUENCES IN SCHEMA public TO tnpsc_user;

ALTER DEFAULT PRIVILEGES 
IN SCHEMA public 
GRANT ALL ON TABLES TO tnpsc_user;

ALTER DEFAULT PRIVILEGES 
IN SCHEMA public 
GRANT ALL ON SEQUENCES TO tnpsc_user;

-- ============================================
-- SAMPLE QUESTIONS
-- ============================================

INSERT INTO questions (

    category,
    topic,
    difficulty,

    question_en,
    question_ta,

    option_a_en,
    option_a_ta,

    option_b_en,
    option_b_ta,

    option_c_en,
    option_c_ta,

    option_d_en,
    option_d_ta,

    correct_answer,

    explanation_en,
    explanation_ta

)

VALUES

(
    'Polity',
    'Indian Polity',
    'Easy',

    'Which part of the Indian Constitution contains Fundamental Rights?',
    'இந்திய அரசியலமைப்பின் எந்த பகுதியில் அடிப்படை உரிமைகள் உள்ளன?',

    'Part I',
    'பகுதி I',

    'Part III',
    'பகுதி III',

    'Part V',
    'பகுதி V',

    'Part VII',
    'பகுதி VII',

    'B',

    'Fundamental Rights are contained in Part III of the Constitution.',
    'அடிப்படை உரிமைகள் அரசியலமைப்பின் பகுதி III இல் உள்ளன.'
),

(
    'Polity',
    'Indian Polity',
    'Easy',

    'Who is known as the guardian of the Indian Constitution?',
    'இந்திய அரசியலமைப்பின் காவலர் யார்?',

    'Prime Minister',
    'பிரதமர்',

    'President',
    'குடியரசுத் தலைவர்',

    'Supreme Court',
    'உச்ச நீதிமன்றம்',

    'Parliament',
    'பாராளுமன்றம்',

    'C',

    'The Supreme Court protects and interprets the Constitution.',
    'உச்ச நீதிமன்றம் அரசியலமைப்பை பாதுகாத்து விளக்குகிறது.'
),

(
    'Polity',
    'Indian Polity',
    'Medium',

    'What is the minimum age to become President of India?',
    'இந்திய குடியரசுத் தலைவராக ஆக குறைந்தபட்ச வயது என்ன?',

    '25',
    '25',

    '30',
    '30',

    '35',
    '35',

    '40',
    '40',

    'C',

    'A person must be at least 35 years old to become President.',
    'குடியரசுத் தலைவராக ஆக குறைந்தது 35 வயது இருக்க வேண்டும்.'
);

-- ============================================
-- VERIFY DATA
-- ============================================

SELECT COUNT(*) FROM questions;
```
