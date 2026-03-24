import { Star, ArrowRight } from 'lucide-react';

const toNumber = (value, fallback) => {
  if (value === null || value === undefined) return fallback;
  const n = typeof value === 'number' ? value : Number(value);
  return Number.isFinite(n) ? n : fallback;
};

export const CompanyCard = ({ company }) => {
  const name = company?.name ?? 'Unknown';
  const rating = toNumber(company?.rating, 0);
  const industry = company?.industry ?? 'Unspecified';
  const size = company?.size ?? 'N/A';
  const employees = company?.employees;
  const founded = company?.founded;
  const locations = company?.locations ?? '';
  const logo = company?.logo ?? '';

  const firstLetter = name ? name.charAt(0).toUpperCase() : 'C';
  const mainLocation = locations ? locations.split(',')[0].trim() : 'Remote';
  const hasLogo = typeof logo === 'string' && logo.trim().length > 0;
  const openPositions = (name.length % 20) + 5;

  return (
    <div className="bg-[#161B22] border border-gray-800 rounded-xl p-6 flex flex-col hover:border-blue-500/50 transition-colors duration-300">
      {/* Header Card: Logo & Rating */}
      <div className="flex justify-between items-start mb-6">
        <div className="w-12 h-12 bg-gray-800 rounded-lg overflow-hidden flex items-center justify-center text-xl font-bold text-gray-400 uppercase">
          {hasLogo ? (
            <img
              src={logo}
              alt={`${name} logo`}
              className="w-full h-full object-contain"
            />
          ) : (
            firstLetter
          )}
        </div>
        <div className="flex items-center gap-1 bg-yellow-500/10 px-2 py-1 rounded text-yellow-500 text-sm font-medium">
          <Star size={14} fill="currentColor" />
          <span>{rating.toFixed(1)}</span>
        </div>
      </div>

      {/* Thông tin chính */}
      <h3 className="text-xl font-semibold text-white mb-1 line-clamp-1" title={name}>
        {name}
      </h3>
      <p className="text-blue-500 text-sm font-medium mb-6 line-clamp-1">{industry}</p>

      {/* Grid thống kê */}
      <div className="grid grid-cols-2 gap-y-4 gap-x-2 text-sm mb-8 grow">
        <div>
          <p className="text-gray-500 text-xs uppercase font-semibold mb-1">Company Size</p>
          <p className="text-gray-300">{size}</p>
        </div>
        <div>
          <p className="text-gray-500 text-xs uppercase font-semibold mb-1">Employees</p>
          <p className="text-gray-300">
            {employees !== null && employees !== undefined
              ? `${employees.toLocaleString()}+`
              : 'N/A'}
          </p>
        </div>
        <div>
          <p className="text-gray-500 text-xs uppercase font-semibold mb-1">Founded</p>
          <p className="text-gray-300">{founded !== null && founded !== undefined ? founded : 'N/A'}</p>
        </div>
        <div>
          <p className="text-gray-500 text-xs uppercase font-semibold mb-1">Location</p>
          <p className="text-gray-300 truncate pr-2" title={mainLocation}>{mainLocation}</p>
        </div>
      </div>

      {/* Footer Card */}
      <div className="flex items-center justify-between pt-4 border-t border-gray-800 mt-auto">
        <div className="flex items-center gap-2">
          <div className="w-2 h-2 rounded-full bg-green-500 animate-pulse"></div>
          <span className="text-sm text-gray-300">{openPositions} open positions</span>
        </div>
        <button className="text-gray-400 hover:text-white transition-colors" aria-label="View details">
          <ArrowRight size={18} />
        </button>
      </div>
    </div>
  );
};

