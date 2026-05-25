import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useLanguage } from '../hooks/useLanguage';

const ProfilePage = () => {
  const { user } = useAuth();
  const lang = useLanguage();

  // derive some simple metrics from localStorage or user object
  const streak = (user?.streak ?? Number(window.localStorage.getItem('tnpsc_streak'))) || 0;
  const totalAttempted = (user?.totalAttempted ?? Number(window.localStorage.getItem('tnpsc_total'))) || 0;
  const accuracy = (user?.accuracy ?? Number(window.localStorage.getItem('tnpsc_accuracy'))) || 0;

  return (
    <section className="profile-page page-card">
      <div className="profile-header">
        <h1>{user?.name || (lang === 'en' ? 'Your Profile' : 'உங்கள் پروஃபைல்')}</h1>
        <p className="muted">{user?.email}</p>
      </div>
      <div className="profile-stats grid-3">
        <div className="stat-card">
          <div className="stat-title">{lang === 'en' ? 'Streak' : ' தொடர்ச்சி'}</div>
          <div className="stat-value">{streak}</div>
        </div>
        <div className="stat-card">
          <div className="stat-title">{lang === 'en' ? 'Questions Attempted' : 'மொத்த கேள்விகள்'}</div>
          <div className="stat-value">{totalAttempted}</div>
        </div>
        <div className="stat-card">
          <div className="stat-title">{lang === 'en' ? 'Accuracy' : 'துல்லியம்'}</div>
          <div className="stat-value">{accuracy}%</div>
        </div>
      </div>
      <div className="profile-actions">
        <button className="primary-button">Start Today's Practice</button>
      </div>
    </section>
  );
};

export default ProfilePage;
