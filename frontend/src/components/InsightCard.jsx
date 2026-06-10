import React from 'react';

const InsightCard = ({ insight }) => {
  return (
    <article className="insight-card">
      <header className="insight-card__header">
        <h3 className="insight-card__topic">{insight.topic}</h3>
        <p className="insight-card__title">{insight.title}</p>
      </header>
      <section className="insight-card__body">
        <p className="insight-card__explanation">{insight.explanation}</p>
        {insight.tnpscTip && (
          <p className="insight-card__tip"><strong>TNPSC Tip:</strong> {insight.tnpscTip}</p>
        )}
      </section>
      <footer className="insight-card__meta">
  <p>Priority: {insight.priorityScore?.toFixed(1)}</p>
  <p>Weakness: {insight.weaknessLevel}</p>
</footer>
      <footer className="insight-card__quiz">
        <p className="insight-card__mini">Mini quiz: <em>{insight.miniQuiz}</em></p>
      </footer>
    </article>
  );
};

export default InsightCard;
