import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { register } from '../api/authService';
import { useLanguage } from '../hooks/useLanguage';
import { useAuth } from '../contexts/AuthContext';

const RegisterPage = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const lang = useLanguage();
  const { login: authLogin } = useAuth();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      const data = await register({ name, email, password });
      const token = data.token || data.accessToken || null;
      const user = data.user || { name, email };
      if (token) {
        authLogin(token, user);
      }
      window.localStorage.setItem('tnpsc_email', email);
      navigate('/home');
    } catch (err) {
      setError('Unable to create account. Please try again.');
    }
  };

  const labels = {
    en: { title: 'Create premium practice', subtitle: 'Start building your TNPSC streak today.', name: 'Name', email: 'Email', password: 'Password', submit: 'Register', login: 'Already have an account?' },
    ta: { title: 'விளையாட்டு கணக்கு உருவாக்கு', subtitle: 'இன்று உங்கள் TNPSC ஸ்ட்ரீக்கை தொடங்கவும்.', name: 'பெயர்', email: 'மின்னஞ்சல்', password: 'கடவுச்சொல்', submit: 'பதிவு செய்ய', login: 'ஏற்கனவே ஒரு கணக்கு உள்ளதா?' },
  };

  return (
    <section className="auth-page">
      <div className="auth-panel">
        <h1>{labels[lang].title}</h1>
        <p>{labels[lang].subtitle}</p>
        <form onSubmit={handleSubmit} className="auth-form">
          <label>
            {labels[lang].name}
            <input value={name} onChange={(e) => setName(e.target.value)} type="text" required />
          </label>
          <label>
            {labels[lang].email}
            <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required />
          </label>
          <label>
            {labels[lang].password}
            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" required />
          </label>
          {error && <div className="form-error">{error}</div>}
          <button type="submit" className="primary-button">{labels[lang].submit}</button>
        </form>
        <div className="bottom-note">
          <span>{labels[lang].login}</span>
          <Link to="/login">{lang === 'en' ? 'Sign in' : 'உள்நுழைய'}</Link>
        </div>
      </div>
    </section>
  );
};

export default RegisterPage;
