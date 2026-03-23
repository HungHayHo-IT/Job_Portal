import React from 'react';
import { Globe, Github, Twitter } from 'lucide-react';

export default function Footer() {
  return (
    <footer className="px-8 md:px-16 py-16 border-t border-gray-800/50 bg-[#070a11]">
      <div className="flex flex-col lg:flex-row justify-between gap-12 mb-12">
        <div className="max-w-xs">
          <div className="text-xl font-bold text-white tracking-wide mb-4">
            Career<span className="text-cyan-400">Architect</span>
          </div>
          <p className="text-gray-500 text-sm leading-relaxed">
            Redefining the standard of professional placement through architectural design and intentional connections.
          </p>
        </div>
        
        <div className="flex gap-16">
          <div>
            <h5 className="text-white font-semibold text-xs tracking-widest uppercase mb-4">Platform</h5>
            <ul className="space-y-3 text-sm text-gray-500">
              <li><a href="#" className="hover:text-cyan-400 transition">Job Search</a></li>
              <li><a href="#" className="hover:text-cyan-400 transition">Companies</a></li>
              <li><a href="#" className="hover:text-cyan-400 transition">Careers</a></li>
            </ul>
          </div>
          <div>
            <h5 className="text-white font-semibold text-xs tracking-widest uppercase mb-4">Company</h5>
            <ul className="space-y-3 text-sm text-gray-500">
              <li><a href="#" className="hover:text-cyan-400 transition">About Us</a></li>
              <li><a href="#" className="hover:text-cyan-400 transition">Privacy</a></li>
              <li><a href="#" className="hover:text-cyan-400 transition">Terms</a></li>
            </ul>
          </div>
          <div>
            <h5 className="text-white font-semibold text-xs tracking-widest uppercase mb-4">Support</h5>
            <ul className="space-y-3 text-sm text-gray-500">
              <li><a href="#" className="hover:text-cyan-400 transition">Help Center</a></li>
              <li><a href="#" className="hover:text-cyan-400 transition">Community</a></li>
            </ul>
          </div>
        </div>
      </div>
      
      <div className="flex flex-col md:flex-row justify-between items-center pt-8 border-t border-gray-800/50 text-gray-600 text-xs">
        <p>© 2024 CareerArchitect. The Architectural Authority.</p>
        <div className="flex gap-4 mt-4 md:mt-0">
          <Globe size={16} className="hover:text-cyan-400 cursor-pointer transition" />
          <Twitter size={16} className="hover:text-cyan-400 cursor-pointer transition" />
          <Github size={16} className="hover:text-cyan-400 cursor-pointer transition" />
        </div>
      </div>
    </footer>
  );
}