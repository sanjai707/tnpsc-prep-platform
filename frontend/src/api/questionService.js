import api from './axiosClient';

export const fetchDailyQuestions = async (topics = []) => {
  const params = {};
  if (topics.length > 0 && !topics.includes('Mixed Practice')) {
    // send as comma-separated string so backend can parse reliably
    params.topics = topics.join(',');
  }
  const url = '/questions/daily';
  const response = await api.get(url, { params });
  return response.data;
};

export const submitAnswer = async (payload) => {
  const response = await api.post('/questions/submit', payload);
  return response.data;
};

export const fetchDailyInsights = async () => {
  const url = '/insights/today';
  const response = await api.get(url);
  return response.data;
};
