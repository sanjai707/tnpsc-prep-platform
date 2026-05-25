import { useEffect, useState } from 'react';

export const useLanguage = () => {
  const [language, setLanguage] = useState(window.localStorage.getItem('tnpsc_lang') || 'en');

  useEffect(() => {
    const handleLanguageChange = () => {
      setLanguage(window.localStorage.getItem('tnpsc_lang') || 'en');
    };
    window.addEventListener('language-change', handleLanguageChange);
    return () => {
      window.removeEventListener('language-change', handleLanguageChange);
    };
  }, []);

  return language;
};

export const setLanguage = (value) => {
  window.localStorage.setItem('tnpsc_lang', value);
  window.dispatchEvent(new Event('language-change'));
};
