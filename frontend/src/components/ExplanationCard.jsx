import React from 'react';

const ExplanationCard = ({ correctAnswer, explanation, tip, success, lang }) => {
  return (
    <div className={`explanation-card ${success ? 'success' : 'failure'}`}>
      <div className="explain-head">
        <div className="result-badge">{success ? '✔' : '✖'}</div>
        <div className="result-text">{success ? (lang === 'en' ? 'Correct' : 'சரி') : (lang === 'en' ? 'Incorrect' : 'தவறு')}</div>
        <div className="correct-answer">{(lang === 'en' ? 'Answer' : 'பதில்')}: {correctAnswer}</div>
      </div>
      <div className="explain-body">
        <div className="explain-main">{explanation}</div>
        {tip && <div className="explain-tip">{tip}</div>}
      </div>
    </div>
  );
};

export default ExplanationCard;
