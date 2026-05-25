import { useLocation, useNavigate } from 'react-router-dom';
import { useLanguage } from '../hooks/useLanguage';

const ResultPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const state = location.state || { summary: { correct: 0, answered: 0 } };
  const { summary } = state;
  const lang = useLanguage();
  const accuracy = summary.answered ? Math.round((summary.correct / summary.answered) * 100) : 0;

  return (
    <section className="result-page">
      <div className="result-card">
        <h1>{lang === 'en' ? 'Session Complete' : 'அமர்வு முடிந்தது'}</h1>
        <div className="result-summary">
          <div>
            <strong>{summary.correct}</strong>
            <span>{lang === 'en' ? 'Correct answers' : 'சரியான விடைகள்'}</span>
          </div>
          <div>
            <strong>{summary.answered - summary.correct}</strong>
            <span>{lang === 'en' ? 'Incorrect answers' : 'தவறான விடைகள்'}</span>
          </div>
          <div>
            <strong>{accuracy}%</strong>
            <span>{lang === 'en' ? 'Session accuracy' : 'அமர்வு துல்லியம்'}</span>
          </div>
        </div>
        <button className="primary-button" onClick={() => navigate('/home')}>{lang === 'en' ? 'Back to Home' : 'முகப்பிற்கு'}</button>
      </div>
    </section>
  );
};

export default ResultPage;
