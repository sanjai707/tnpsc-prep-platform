import api from './axiosClient';

export const fetchDailyQuestions = async () => {
  const response = await api.get('/questions/daily');
  return response.data;
};

export const submitAnswer = async (payload) => {
  const response = await api.post('/questions/submit', payload);
  return response.data;
};
