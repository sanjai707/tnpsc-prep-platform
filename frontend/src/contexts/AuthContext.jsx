import React, { createContext, useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api, { setAuthToken } from '../api/axiosClient';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => window.localStorage.getItem('tnpsc_token'));
  const [user, setUser] = useState(() => {
    try {
      const raw = window.localStorage.getItem('tnpsc_user');
      return raw ? JSON.parse(raw) : null;
    } catch (e) {
      return null;
    }
  });
  const navigate = useNavigate();

  useEffect(() => {
    if (token) setAuthToken(token);
    else setAuthToken(null);
  }, [token]);

  const login = (tokenValue, userInfo) => {
    setToken(tokenValue);
    window.localStorage.setItem('tnpsc_token', tokenValue);
    if (userInfo) {
      setUser(userInfo);
      window.localStorage.setItem('tnpsc_user', JSON.stringify(userInfo));
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    window.localStorage.removeItem('tnpsc_token');
    window.localStorage.removeItem('tnpsc_user');
    navigate('/login');
  };

  const value = { token, user, setUser, login, logout };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => useContext(AuthContext);

export default AuthContext;
