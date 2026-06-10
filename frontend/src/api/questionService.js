import api from './axiosClient';

export const fetchDailyQuestions = async (topics = []) => {
  const params = {};
  if (topics.length > 0 && !topics.includes('Mixed Practice')) {
    // send as comma-separated string so backend can parse reliably
    params.topics = topics.join(',');
  }
  const url = '/questions/daily';
  console.log('DEBUG ONLY - TEMPORARY CHANGE: fetchDailyQuestions called');
  console.log('DEBUG ONLY - TEMPORARY CHANGE: Request URL =', url, 'params =', params);
  const response = await api.get(url, { params });
  console.log('DEBUG ONLY - TEMPORARY CHANGE: Response =', response.data);
  return response.data;
};

export const submitAnswer = async (payload) => {
  const response = await api.post('/questions/submit', payload);
  return response.data;
};

export const fetchDailyInsights = async () => {
  const url = '/insights/today';
  console.log('DEBUG ONLY - TEMPORARY CHANGE: fetchDailyInsights called');
  console.log('DEBUG ONLY - TEMPORARY CHANGE: Request URL =', url);
  const response = await api.get(url);
  console.log('DEBUG ONLY - TEMPORARY CHANGE: Response =', response.data);
  return response.data;
};
