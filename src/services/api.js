import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
};

export const queryAPI = {
  generateQuery: (data) => api.post('/query/generate', data),
};

export const schemaAPI = {
  getTables: () => api.get('/schema/tables'),
  getTableSchema: (tableName) => api.get(`/schema/tables/${tableName}`),
};

export const historyAPI = {
  getHistory: () => api.get('/history'),
  deleteHistory: (id) => api.delete(`/history/${id}`),
};

export default api;