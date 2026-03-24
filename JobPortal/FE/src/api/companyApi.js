import axiosClient from './axiosClient';

export const companyApi = {
  getAllCompanies: async (config) => {
    // axiosClient interceptor đã trả thẳng response.data
    return axiosClient.get('/companies', config);
  },
};

