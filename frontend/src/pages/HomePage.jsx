import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchStats } from '../api/analyticsService';
import PracticeCard from '../components/PracticeCard';
import MetricCard from '../components/MetricCard';
import { useLanguage } from '../hooks/useLanguage';

const HomePage = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
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

  const userEmail = window.localStorage.getItem('tnpsc_email') || '';
  const displayName = userEmail.split('@')[0];

  return (
    <section className="home-page">
      <div className="hero-card">
        <div>
          <p className="subtle-label">{lang === 'en' ? 'Daily streak' : 'தினசரி தொடர்ச்சி'}</p>
          <h1>{lang === 'en' ? 'Practice feels effortless.' : 'பயிற்சி எளிதாக அனுபவமாகும்.'}</h1>
          <p>{lang === 'en' ? 'Build a reliable habit with 10 focused questions every day.' : 'தினமும் 10 கவனமாகக் கேள்விகளுடன் நம்பிக்கையான பழக்கவழக்கத்தை உருவாக்கு.'}</p>
        </div>
      </div>
      <div className="dashboard-grid">
        <PracticeCard
          title={lang === 'en' ? 'Start Today' : 'இன்று தொடங்கு'}
          description={lang === 'en' ? 'A quick, premium practice session crafted for TNPSC success.' : 'TNPSC வெற்றி நோக்கி தயாரிக்கப்பட்ட விரைந்து பயிற்சி.'}
          cta={lang === 'en' ? 'Start Practice' : 'பயிற்சி தொடங்கு'}
          onClick={() => navigate('/practice')}
        />
        <PracticeCard
          title={lang === 'en' ? 'Track Progress' : 'முன்னேற்றத்தை கண்காணி'}
          description={lang === 'en' ? 'Review accuracy, weak topics, and session rhythm.' : 'தவறான தலைப்புகளைப் பார்வையிடவும்.'}
          cta={lang === 'en' ? 'View Stats' : 'புள்ளிகளை காண்க'}
          onClick={() => navigate('/stats')}
        />
      </div>
      <div className="metric-grid">
        <MetricCard label={lang === 'en' ? 'Streak' : 'ஸ்ட்ரீக்'} value={stats?.streakCount ?? '-'} subText={lang === 'en' ? 'Days of consistent learning' : 'தொடர் பயிற்சியின் நாட்கள்'} />
        <MetricCard label={lang === 'en' ? 'Accuracy' : 'துல்லியத்தன்மை'} value={loading ? '...' : `${stats?.accuracy ?? 0}%`} subText={lang === 'en' ? 'Overall performance' : 'மொத்த செயல்திறன்'} />
        <MetricCard label={lang === 'en' ? 'Attempts' : 'மீட்டல்கள்'} value={stats?.totalAttempts ?? 0} subText={lang === 'en' ? 'Questions solved' : 'தீர்த்த கேள்விகள்'} />
      </div>
      <div className="story-panel">
        <div className="story-header">
          <span>{lang === 'en' ? 'Hello' : 'வணக்கம்'}, {displayName}</span>
          <p>{lang === 'en' ? 'Ready for a focused sprint?' : 'கவனமான பயிற்சிக்கு தயாரா?'}</p>
        </div>
        <div className="story-actions">
          <button className="secondary-button" onClick={() => navigate('/stats')}>{lang === 'en' ? 'Study insights' : 'பகுப்பாய்வு'} </button>
          <button className="primary-button" onClick={() => navigate('/practice')}>{lang === 'en' ? 'Daily session' : 'தினசரி அமர்வு'}</button>
        </div>
      </div>
    </section>
  );
};

export default HomePage;
