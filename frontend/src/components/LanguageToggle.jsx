import { useLanguage, setLanguage } from '../hooks/useLanguage';

const LanguageToggle = () => {
  const language = useLanguage();

  const toggle = () => {
    const next = language === 'en' ? 'ta' : 'en';
    setLanguage(next);
  };

  return (
    <button className="language-toggle" onClick={toggle}>
      {language === 'en' ? 'EN' : 'தமிழ்'}
    </button>
  );
};

export default LanguageToggle;
