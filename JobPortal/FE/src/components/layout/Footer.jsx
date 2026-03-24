import React from 'react';
import { Link } from 'react-router-dom';
import { Globe, Github, Twitter } from 'lucide-react';

export default function Footer() {
  return (
    <footer className="px-8 md:px-16 py-16 border-t border-gray-800/50 bg-[#070a11]">
      
      {/* TOP */}
      <div className="flex flex-col lg:flex-row justify-between gap-12 mb-12">
        
        {/* BRAND */}
        <div className="max-w-xs">
          <div className="text-xl font-bold text-white tracking-wide mb-4">
            Career<span className="text-cyan-400">Architect</span>
          </div>
          <p className="text-gray-500 text-sm leading-relaxed">
            Redefining the standard of professional placement through architectural design and intentional connections.
          </p>
        </div>

        {/* LINKS */}
        <div className="flex gap-16 flex-wrap">
          
          {/* PLATFORM */}
          <div>
            <h5 className="text-white font-semibold text-xs tracking-widest uppercase mb-4">
              Platform
            </h5>
            <ul className="space-y-3 text-sm text-gray-500">
              <li><Link to="/jobs" className="hover:text-cyan-400 transition">Job Search</Link></li>
              <li><Link to="/companies" className="hover:text-cyan-400 transition">Companies</Link></li>
              <li><Link to="/careers" className="hover:text-cyan-400 transition">Careers</Link></li>
            </ul>
          </div>

          {/* COMPANY */}
          <div>
            <h5 className="text-white font-semibold text-xs tracking-widest uppercase mb-4">
              Company
            </h5>
            <ul className="space-y-3 text-sm text-gray-500">
              <li><Link to="/about" className="hover:text-cyan-400 transition">About Us</Link></li>
              <li><Link to="/privacy" className="hover:text-cyan-400 transition">Privacy</Link></li>
              <li><Link to="/terms" className="hover:text-cyan-400 transition">Terms</Link></li>
            </ul>
          </div>

          {/* SUPPORT */}
          <div>
            <h5 className="text-white font-semibold text-xs tracking-widest uppercase mb-4">
              Support
            </h5>
            <ul className="space-y-3 text-sm text-gray-500">
              <li><Link to="/help" className="hover:text-cyan-400 transition">Help Center</Link></li>
              <li><Link to="/community" className="hover:text-cyan-400 transition">Community</Link></li>
            </ul>
          </div>

        </div>
      </div>

      {/* BOTTOM */}
      <div className="flex flex-col md:flex-row justify-between items-center pt-8 border-t border-gray-800/50 text-gray-600 text-xs">
        
        <p>© 2024 CareerArchitect. The Architectural Authority.</p>

        {/* SOCIAL */}
        <div className="flex gap-4 mt-4 md:mt-0">
          
          <a href="https://yourwebsite.com" target="_blank" rel="noopener noreferrer">
            <Globe size={16} className="hover:text-cyan-400 cursor-pointer transition" />
          </a>

          <a href="https://twitter.com" target="_blank" rel="noopener noreferrer">
            <Twitter size={16} className="hover:text-cyan-400 cursor-pointer transition" />
          </a>

          <a href="https://github.com" target="_blank" rel="noopener noreferrer">
            <Github size={16} className="hover:text-cyan-400 cursor-pointer transition" />
          </a>

        </div>
      </div>

    </footer>
  );
}