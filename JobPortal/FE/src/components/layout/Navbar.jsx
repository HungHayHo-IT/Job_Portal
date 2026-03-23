import React from 'react';

export default function Navbar() {
  return (
    <nav className="flex items-center justify-between py-6 px-8 md:px-16 border-b border-gray-800/50">
      <div className="flex items-center gap-8">
        <div className="text-xl font-bold text-white tracking-wide">
          Career<span className="text-cyan-400">Architect</span>
        </div>
        <div className="hidden md:flex gap-6 text-sm text-gray-400 font-medium">
          <a href="#" className="text-cyan-400 border-b border-cyan-400 pb-1">Job Search</a>
          <a href="#" className="hover:text-white transition">Companies</a>
        </div>
      </div>
      <div className="flex items-center gap-6 text-sm font-medium">
        <button className="text-gray-300 hover:text-white transition">Login</button>
        <button className="bg-cyan-500 hover:bg-cyan-400 text-gray-900 px-5 py-2 rounded-full transition font-semibold">
          Register
        </button>
      </div>
    </nav>
  );
}