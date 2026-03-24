import React from 'react';
import { Link, NavLink } from 'react-router-dom'; // Import thư viện

export default function Navbar() {
  return (
    <nav className="flex items-center justify-between py-6 px-8 md:px-16 border-b border-gray-800/50">
      <div className="flex items-center gap-8">
        {/* Dùng Link cho các vị trí không cần trạng thái "active" như logo */}
        <Link to="/" className="text-xl font-bold text-white tracking-wide">
          Career<span className="text-cyan-400">Architect</span>
        </Link>
        
        <div className="hidden md:flex gap-6 text-sm text-gray-400 font-medium">
          {/* Dùng NavLink để xử lý UI khi menu đang được chọn */}
          <NavLink 
            to="/jobs" 
            className={({ isActive }) => 
              isActive 
                ? "text-cyan-400 border-b border-cyan-400 pb-1" 
                : "hover:text-white transition"
            }
          >
            Job Search
          </NavLink>
          
          <NavLink 
            to="/companies" 
            className={({ isActive }) => 
              isActive 
                ? "text-cyan-400 border-b border-cyan-400 pb-1" 
                : "hover:text-white transition"
            }
          >
            Companies
          </NavLink>
        </div>
      </div>
      
      {/* ... code nút Login/Register giữ nguyên ... */}
      <div className="flex items-center gap-6 text-sm font-medium">
        <button className="text-gray-300 hover:text-white transition">Login</button>
        <button className="bg-cyan-500 hover:bg-cyan-400 text-gray-900 px-5 py-2 rounded-full transition font-semibold">
          Register
        </button>
      </div>
    </nav>
  );
}