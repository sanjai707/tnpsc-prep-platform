import { useEffect, useState } from 'react';
import { fetchStats } from '../api/analyticsService';
import MetricCard from '../components/MetricCard';
import { useLanguage } from '../hooks/useLanguage';

const StatsPage = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const lang = useLanguage();

  useEffect(() => {
    async function loadStats() {
      try {
        const response = await fetchStats();
        setStats(response);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    }
    loadStats();
  }, []);

  return (
    <section className="stats-page">
      <div className="stats-header">
        <h1>{lang === 'en' ? 'Performance Dashboard' : 'செயல்திறன் டாஷ்போர்டு'}</h1>
        <p>{lang === 'en' ? 'Your progress and weak-topic insights in one place.' : 'உங்கள் முன்னேற்றம் மற்றும் பலவீன் தலைப்புகள்.'}</p>
      </div>
      <div className="metric-grid">
        <MetricCard label={lang === 'en' ? 'Attempts' : 'மீட்டல்கள்'} value={stats?.totalAttempts ?? '--'} subText={lang === 'en' ? 'Questions answered' : 'பதிலளிக்கப்பட்ட கேள்விகள்'} />
        <MetricCard label={lang === 'en' ? 'Correct' : 'சரியானவை'} value={stats?.correctAttempts ?? '--'} subText={lang === 'en' ? 'Right attempts' : 'சரியான முயற்சிகள்'} />
        <MetricCard label={lang === 'en' ? 'Accuracy' : 'துல்லியம்'} value={loading ? '...' : `${stats?.accuracy ?? 0}%`} subText={lang === 'en' ? 'Overall rate' : 'மொத்த வீதம்'} />
        <MetricCard label={lang === 'en' ? 'Streak' : 'ஸ்ட்ரீக்'} value={stats?.streakCount ?? '--'} subText={lang === 'en' ? 'Daily streak' : 'தினசரி தொடர்ச்சி'} />
      </div>
      <div className="weak-topic-panel">
        <h2>{lang === 'en' ? 'Weak topics' : 'பலவீன் தலைப்புகள்'}</h2>
        {stats?.weakTopics?.length ? (
          <div className="weak-topic-list">
            {stats.weakTopics.map((topic) => (
              <div className="weak-topic-item" key={topic.topic}>
                <div>{topic.topic}</div>
                <div>{`${topic.correct}/${topic.total}`}</div>
              </div>
            ))}
          </div>
        ) : (
          <p>{lang === 'en' ? 'No weak topics yet. Keep practicing.' : 'இன்னும் பலவீன் தலைப்புகள் இல்லை. பயிற்சி தொடரவும்.'}</p>
        )}
      </div>
    </section>
  );
};

export default StatsPage;
