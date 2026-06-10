import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const authHeader = config.headers?.Authorization;
  const hasAuth = !!authHeader;
  const tokenSnippet = authHeader ? authHeader.replace(/^Bearer\s+/, '').slice(0, 20) : null;
  console.log('DEBUG ONLY - REMOVE AFTER INVESTIGATION: axios outgoing request', {
    method: config.method,
    url: config.url,
    baseURL: config.baseURL,
    authHeaderPresent: hasAuth,
    tokenSnippet: tokenSnippet ? `${tokenSnippet}...` : null,
  });
  return config;
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
