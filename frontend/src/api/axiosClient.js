import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

let authToken = window.localStorage.getItem('tnpsc_token') || null;

export const setAuthToken = (token) => {
  authToken = token;
  if (token) {
    api.defaults.headers.common.Authorization = `Bearer ${token}`;
    window.localStorage.setItem('tnpsc_token', token);
  } else {
    delete api.defaults.headers.common.Authorization;
    window.localStorage.removeItem('tnpsc_token');
  }
};

// ensure default header from stored token
if (authToken) setAuthToken(authToken);

export default api;
