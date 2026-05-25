import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../api/authService';
import { useAuth } from '../contexts/AuthContext';
import { useLanguage } from '../hooks/useLanguage';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { login: authLogin } = useAuth();
  const lang = useLanguage();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      const data = await login({ email, password });
      const token = data.token || data.accessToken || null;
      const user = data.user || { email };
      if (token) {
        authLogin(token, user);
      }
      window.localStorage.setItem('tnpsc_email', email);
      navigate('/home');
    } catch (err) {
      setError('Invalid credentials. Please try again.');
    }
  };

  const labels = {
    en: { title: 'Welcome Back', subtitle: 'Your daily TNPSC practice starts here.', email: 'Email', password: 'Password', login: 'Log In', register: 'Create account' },
    ta: { title: 'திரும்ப வருக!', subtitle: 'உங்கள் தினசரி TNPSC பயிற்சி இங்கே தொடங்கி விடுகிறது.', email: 'மின்னஞ்சல்', password: 'கடவுச்சொல்', login: 'உள்நுழைய', register: 'கணக்கு உருவாக்கு' },
  };

  return (
    <section className="auth-page">
      <div className="auth-panel">
        <h1>{labels[lang].title}</h1>
        <p>{labels[lang].subtitle}</p>
        <form onSubmit={handleSubmit} className="auth-form">
          <label>
            {labels[lang].email}
            <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required />
          </label>
          <label>
            {labels[lang].password}
            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" required />
          </label>
          {error && <div className="form-error">{error}</div>}
          <button type="submit" className="primary-button">{labels[lang].login}</button>
        </form>
        <div className="bottom-note">
          <span>{lang === 'en' ? 'New here?' : 'புதியவர்?'}</span>
          <Link to="/register">{labels[lang].register}</Link>
        </div>
      </div>
    </section>
  );
};

export default LoginPage;
