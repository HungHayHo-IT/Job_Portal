import { Star, ArrowRight } from 'lucide-react';

export const CompanyCard = ({ company }) => {
  return (
    <div className="bg-[#161B22] border border-gray-800 rounded-xl p-6 flex flex-col hover:border-blue-500/50 transition-colors duration-300">
      {/* Header Card: Logo & Rating */}
      <div className="flex justify-between items-start mb-6">
        <div className="w-12 h-12 bg-gray-800 rounded-lg flex items-center justify-center text-xl font-bold text-gray-400">
          {/* Lấy chữ cái đầu của tên làm Logo tạm nếu không có URL logo hợp lệ */}
          {company.name.charAt(0)}
        </div>
        <div className="flex items-center gap-1 bg-yellow-500/10 px-2 py-1 rounded text-yellow-500 text-sm font-medium">
          <Star size={14} fill="currentColor" />
          <span>{company.rating}</span>
        </div>
      </div>

      {/* Thông tin chính */}
      <h3 className="text-xl font-semibold text-white mb-1">{company.name}</h3>
      <p className="text-blue-500 text-sm font-medium mb-6">{company.industry}</p>

      {/* Grid thống kê (Dùng dữ liệu từ DTO) */}
      <div className="grid grid-cols-2 gap-y-4 gap-x-2 text-sm mb-8 flex-grow">
        <div>
          <p className="text-gray-500 text-xs uppercase font-semibold mb-1">Company Size</p>
          <p className="text-gray-300">{company.size}</p>
        </div>
        <div>
          <p className="text-gray-500 text-xs uppercase font-semibold mb-1">Employees</p>
          <p className="text-gray-300">{company.employees?.toLocaleString() || 'N/A'}+</p>
        </div>
        <div>
          <p className="text-gray-500 text-xs uppercase font-semibold mb-1">Founded</p>
          <p className="text-gray-300">{company.founded}</p>
        </div>
        <div>
          <p className="text-gray-500 text-xs uppercase font-semibold mb-1">Location</p>
          {/* Lấy địa điểm đầu tiên từ chuỗi locations */}
          <p className="text-gray-300 truncate pr-2">{company.locations?.split(',')[0]}</p>
        </div>
      </div>

      {/* Footer Card */}
      <div className="flex items-center justify-between pt-4 border-t border-gray-800 mt-auto">
        <div className="flex items-center gap-2">
          <div className="w-2 h-2 rounded-full bg-green-500"></div>
          {/* Giả lập số lượng vị trí đang tuyển */}
          <span className="text-sm text-gray-300">{Math.floor(Math.random() * 20) + 1} open positions</span>
        </div>
        <button className="text-gray-400 hover:text-white transition-colors">
          <ArrowRight size={18} />
        </button>
      </div>
    </div>
  );
};