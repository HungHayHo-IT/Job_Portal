import { useEffect, useState } from 'react';
import { companyApi } from '../api/companyApi';

export const useCompanies = () => {
  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const abortController = new AbortController();

    const fetchCompanies = async () => {
      try {
        setLoading(true);
        setError(null);

        const data = await companyApi.getAllCompanies({
          signal: abortController.signal,
        });

        setCompanies(data);
      } catch (err) {
        if (err?.name !== 'CanceledError' && err?.name !== 'AbortError') {
          setError(err?.message || 'Lỗi khi tải dữ liệu từ server');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchCompanies();
    return () => abortController.abort();
  }, []);

  return { companies, loading, error };
};

