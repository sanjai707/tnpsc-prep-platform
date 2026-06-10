import React, { useEffect, useState } from 'react';
import { fetchDailyInsights } from '../api/questionService';
import InsightCard from '../components/InsightCard';

const DailyInsightsPage = () => {
  console.log('DEBUG ONLY - REMOVE AFTER INVESTIGATION: DailyInsightsPage render');
  const [insights, setInsights] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let mounted = true;
    console.log('DEBUG ONLY - REMOVE AFTER INVESTIGATION: DailyInsightsPage useEffect mounted');
    console.log('DEBUG ONLY - REMOVE AFTER INVESTIGATION: fetchDailyInsights() about to execute');
    fetchDailyInsights()
      .then(data => {
        console.log('DEBUG ONLY - REMOVE AFTER INVESTIGATION: DailyInsightsPage received data', data);
        if (mounted) setInsights(data);
      })
      .catch(err => {
        console.error('DEBUG ONLY - REMOVE AFTER INVESTIGATION: DailyInsightsPage fetch error', err);
      })
      .finally(() => mounted && setLoading(false));
    return () => (mounted = false);
  }, []);

  return (
    <div className="daily-insights-page">
      <h1>Daily Insights</h1>
      <p className="muted">Today's Learning — concise TNPSC knowledge capsules</p>
      {loading && <p>Loading insights…</p>}
      <div className="insights-list">
        {insights.map((insight, idx) => (
          <InsightCard insight={insight} key={idx} />
        ))}
      </div>
    </div>
  );
};

export default DailyInsightsPage;
