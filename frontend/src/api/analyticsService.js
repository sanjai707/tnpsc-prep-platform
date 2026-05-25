import api from './axiosClient';

export const fetchStats = async () => {
  const response = await api.get('/stats');
  return response.data;
};
