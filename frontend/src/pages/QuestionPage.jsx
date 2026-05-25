import { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchDailyQuestions, submitAnswer } from '../api/questionService';
import { useLanguage } from '../hooks/useLanguage';
import CategorySelector from '../components/CategorySelector';
import ExplanationCard from '../components/ExplanationCard';

const QuestionPage = () => {
  const [questions, setQuestions] = useState([]);
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [index, setIndex] = useState(0);
  const [selected, setSelected] = useState(null);
  const [answerResult, setAnswerResult] = useState(null);
  const [showExplanation, setShowExplanation] = useState(false);
  const [summary, setSummary] = useState({ correct: 0, answered: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [sessionStarted, setSessionStarted] = useState(false);
  const navigate = useNavigate();
  const lang = useLanguage();

  useEffect(() => {
    async function loadQuestions() {
      try {
        const data = await fetchDailyQuestions();
        setQuestions(data);
      } catch (err) {
        setError('Unable to load practice questions right now.');
      } finally {
        setLoading(false);
      }
    }
    loadQuestions();
  }, []);

  const currentQuestion = questions[index];
  const answeredCount = index;

  const options = useMemo(() => {
    if (!currentQuestion) return [];
    return [
      { key: 'A', label: lang === 'en' ? currentQuestion.optionAEn : currentQuestion.optionATa },
      { key: 'B', label: lang === 'en' ? currentQuestion.optionBEn : currentQuestion.optionBTa },
      { key: 'C', label: lang === 'en' ? currentQuestion.optionCEn : currentQuestion.optionCTa },
      { key: 'D', label: lang === 'en' ? currentQuestion.optionDEn : currentQuestion.optionDTa },
    ];
  }, [currentQuestion, lang]);

  const filteredQuestions = useMemo(() => {
    if (!questions || questions.length === 0) return [];
    // If no categories selected, return full list
    if (!sessionStarted) return [];
    if (selectedCategories.length === 0) return questions;
    if (selectedCategories.includes('Mixed Practice')) return questions;
    return questions.filter((q) => selectedCategories.includes(q.category));
  }, [questions, selectedCategories, sessionStarted]);

  // create mixed difficulty ordering
  useEffect(() => {
    if (!filteredQuestions || filteredQuestions.length === 0) return;
    // balance difficulties: easy/medium/hard mix
    const easy = filteredQuestions.filter((q) => q.difficulty === 'easy');
    const med = filteredQuestions.filter((q) => q.difficulty === 'medium');
    const hard = filteredQuestions.filter((q) => q.difficulty === 'hard');

    const mixed = [];
    // simple round-robin taking proportions
    const maxLen = Math.max(easy.length, med.length, hard.length);
    for (let i = 0; i < maxLen; i++) {
      if (easy[i]) mixed.push(easy[i]);
      if (med[i]) mixed.push(med[i]);
      if (hard[i]) mixed.push(hard[i]);
    }
    setQuestions(mixed);
    setIndex(0);
  }, [filteredQuestions]);

  const handleSelection = async (value) => {
    if (!currentQuestion || selected) return;
    setSelected(value);
    try {
      const result = await submitAnswer({ questionId: currentQuestion.id, selectedAnswer: value });
      setAnswerResult(result);
      setSummary((prev) => ({
        correct: prev.correct + (result.correct ? 1 : 0),
        answered: prev.answered + 1,
      }));
      // small transition delay can improve perceived responsiveness
      setTimeout(() => setShowExplanation(true), 180);
    } catch (err) {
      setError('Submission failed. Please try again.');
    }
  };

  const handleNext = () => {
    setSelected(null);
    setAnswerResult(null);
    setShowExplanation(false);
    if (index + 1 >= questions.length) {
      navigate('/result', { state: { summary } });
      return;
    }
    setIndex(index + 1);
  };

  if (loading) {
    return <div className="page-card">Loading practice session…</div>;
  }

  if (error) {
    return <div className="page-card error-card">{error}</div>;
  }

  if (!sessionStarted) {
    return (
      <section className="page-card category-start">
        <h2>{lang === 'en' ? 'Start Practice' : 'பயிற்சி தொடங்கு'}</h2>
        <p className="muted">{lang === 'en' ? 'Choose topics to practice or select Mixed Practice for a daily balanced session.' : 'தேர்வுகளை தேர்ந்தெடுக்கவும் அல்லது தினசரி கலந்த பயிற்சிக்குப் için Mixed தேர்வு செய்யவும்.'}</p>
        <CategorySelector selected={selectedCategories} setSelected={setSelectedCategories} />
        <div style={{ marginTop: 16 }}>
          <button
            className="primary-button"
            onClick={() => {
              // default to Mixed Practice if nothing selected
              if (selectedCategories.length === 0) setSelectedCategories(['Mixed Practice']);
              setSessionStarted(true);
            }}
          >
            {lang === 'en' ? 'Start Session' : 'அமர்வை தொடங்கு'}
          </button>
        </div>
      </section>
    );
  }

  if (!currentQuestion) {
    return <div className="page-card">No questions are available right now.</div>;
  }

  return (
    <section className="question-page">
      <div className="session-header">
        <span>{lang === 'en' ? `Question ${index + 1} of ${questions.length}` : `கேள்வி ${index + 1} / ${questions.length}`}</span>
        <span>{lang === 'en' ? `Answered ${answeredCount}` : `பதில் சொல்லப்பட்டது ${answeredCount}`}</span>
      </div>
      <div className="question-card">
        <h2>{lang === 'en' ? currentQuestion.questionEn : currentQuestion.questionTa}</h2>
        <div className="options-grid">
          {options.map((option) => (
            <button
              key={option.key}
              className={`option-button ${selected === option.key ? 'selected' : ''} ${showExplanation && answerResult?.correctAnswer === option.key ? 'correct' : ''}`}
              onClick={() => handleSelection(option.key)}
              disabled={Boolean(selected)}
            >
              <span className='option-label'>{option.key}.</span>
              <span className="option-text">{option.label}</span>
            </button>
          ))}
        </div>
        {showExplanation && answerResult && (
          <ExplanationCard
            success={answerResult.correct}
            correctAnswer={answerResult.correctAnswer}
            explanation={lang === 'en' ? answerResult.explanationEn : answerResult.explanationTa}
            tip={lang === 'en' ? answerResult.tnpscTipEn : answerResult.tnpscTipTa}
            lang={lang}
          />
        )}
        <div className="question-actions">
          <button className="secondary-button" onClick={() => navigate('/home')}>{lang === 'en' ? 'Return Home' : 'முகப்பிற்கு'} </button>
          <button className="primary-button" onClick={handleNext}>{lang === 'en' ? (index + 1 >= questions.length ? 'Finish Session' : 'Next Question') : (index + 1 >= questions.length ? 'அமர்வை முடி' : 'அடுத்த கேள்வி')}</button>
        </div>
      </div>
      <div className="session-footer">
        <div className="progress-bar">
          <div className="progress" style={{ width: `${((index + 1) / Math.max(questions.length, 1)) * 100}%` }} />
        </div>
      </div>
    </section>
  );
};

export default QuestionPage;
