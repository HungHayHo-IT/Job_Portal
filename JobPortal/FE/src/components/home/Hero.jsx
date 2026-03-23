import React from 'react';
import { Search, MapPin } from 'lucide-react';

export default function Hero() {
  return (
    <header className="px-8 md:px-16 pt-24 pb-16 max-w-5xl">
      <h1 className="text-5xl md:text-7xl font-bold text-white leading-tight mb-6">
        Find your next <span className="text-cyan-400 italic">career chapter.</span>
      </h1>
      <p className="text-gray-400 text-lg md:text-xl max-w-2xl mb-12">
        The architectural authority in high-end recruitment. We treat job seeking as a professional milestone.
      </p>

      {/* Search Box */}
      <div className="flex flex-col md:flex-row bg-[#111727] border border-gray-800 rounded-2xl md:rounded-full p-2 gap-2 max-w-4xl">
        <div className="flex items-center flex-1 px-4 py-2 text-gray-400">
          <Search size={20} className="mr-3" />
          <input 
            type="text" 
            placeholder="Job title or keyword" 
            className="bg-transparent w-full outline-none text-white placeholder-gray-500"
          />
        </div>
        <div className="hidden md:block w-px bg-gray-800 my-2"></div>
        <div className="flex items-center flex-1 px-4 py-2 text-gray-400">
          <MapPin size={20} className="mr-3" />
          <input 
            type="text" 
            placeholder="City or remote" 
            className="bg-transparent w-full outline-none text-white placeholder-gray-500"
          />
        </div>
        <button className="bg-blue-600 hover:bg-blue-500 text-white px-8 py-3 rounded-xl md:rounded-full font-semibold transition mt-2 md:mt-0">
          SEARCH JOBS
        </button>
      </div>

      {/* Quick Search Tags */}
      <div className="flex items-center gap-3 mt-8 text-sm">
        <span className="text-gray-500 font-medium uppercase text-xs tracking-wider">Quick Search:</span>
        {['Design', 'Engineering', 'Marketing', 'Sales'].map(tag => (
          <button key={tag} className="px-4 py-1.5 rounded-full border border-gray-700 text-gray-300 hover:border-cyan-400 hover:text-cyan-400 transition">
            {tag}
          </button>
        ))}
      </div>
    </header>
  );
}