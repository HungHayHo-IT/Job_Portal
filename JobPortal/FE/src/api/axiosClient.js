import axios from 'axios';

const axiosClient = axios.create({
  baseURL: 'http://localhost:8082/api/v1',
  timeout: 10000, // Huỷ sau 10s nếu server không phản hồi
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosClient.interceptors.response.use(
  (response) => {
    // Trả về trực tiếp data thay vì bọc trong object response
    if (response && response.data) return response.data;
    return response;
  },
  (error) => {
    // Xử lý lỗi tập trung ở đây
    console.error("API Error:", error.response || error.message);
    return Promise.reject(error);
  }
);

export default axiosClient;