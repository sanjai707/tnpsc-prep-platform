const MetricCard = ({ label, value, subText }) => {
  return (
    <div className="metric-card">
      <div className="metric-label">{label}</div>
      <div className="metric-value">{value}</div>
      {subText ? <div className="metric-subtext">{subText}</div> : null}
    </div>
  );
};

export default MetricCard;
