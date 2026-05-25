const PracticeCard = ({ title, description, cta, onClick }) => {
  return (
    <div className="practice-card">
      <div>
        <h2>{title}</h2>
        <p>{description}</p>
      </div>
      <button className="primary-button" onClick={onClick}>{cta}</button>
    </div>
  );
};

export default PracticeCard;
