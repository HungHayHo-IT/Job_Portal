import { ChevronLeft, ChevronRight } from 'lucide-react';
import { useCompanies } from '../../hooks/useCompanies';
import { CompanyCard } from './CompanyCard';

export const CompaniesList = () => {
  const { companies, loading, error } = useCompanies();

  return (
    <section className="py-16 px-6 max-w-7xl mx-auto">
      <div className="flex justify-between items-end mb-10">
        <div>
          <h4 className="text-blue-500 text-sm font-bold tracking-wider uppercase mb-2">
            Industry Leaders
          </h4>
          <h2 className="text-3xl md:text-4xl font-bold text-white">
            Top Architectural Firms
          </h2>
        </div>
        
        {/* Navigation Buttons */}
        <div className="hidden md:flex gap-3">
          <button className="w-10 h-10 rounded-full border border-gray-700 flex items-center justify-center text-gray-400 hover:text-white hover:border-gray-500 transition-colors">
            <ChevronLeft size={20} />
          </button>
          <button className="w-10 h-10 rounded-full border border-gray-700 flex items-center justify-center text-gray-400 hover:text-white hover:border-gray-500 transition-colors">
            <ChevronRight size={20} />
          </button>
        </div>
      </div>

      {/* Trạng thái tải dữ liệu */}
      {loading && <p className="text-gray-400 text-center py-10">Đang tải danh sách công ty...</p>}
      {error && <p className="text-red-500 text-center py-10">Lỗi: {error}</p>}

      {/* Grid hiển thị */}
      {!loading && !error && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {companies.slice(0, 3).map((company) => (
            <CompanyCard key={company.id} company={company} />
          ))}
        </div>
      )}
    </section>
  );
};