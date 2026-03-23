import { useState, useEffect } from 'react';

export const useCompanies = () => {
  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        setLoading(true);
        // Gọi API đến backend Spring Boot của bạn
        const response = await fetch('http://localhost:8082/api/v1/companies');
        if (!response.ok) {
          throw new Error('Lỗi khi tải dữ liệu từ server');
        }
        const data = await response.json();
        setCompanies(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchCompanies();
  }, []);

  return { companies, loading, error };
};