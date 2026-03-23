// src/components/home/CTASection.jsx
import React from 'react';

export const CTASection = () => {
  return (
    <section className="py-16 px-6 max-w-7xl mx-auto">
      {/* Khối nền xanh bo góc */}
      <div className="bg-[#1A73E8] rounded-3xl p-10 md:p-16 lg:p-20">
        <div className="max-w-2xl">
          {/* Tiêu đề */}
          <h2 className="text-3xl md:text-5xl font-bold text-white mb-6 leading-tight">
            Elevate your practice.<br className="hidden md:block" /> Stay informed.
          </h2>
          
          {/* Mô tả */}
          <p className="text-blue-100 text-lg mb-10 leading-relaxed">
            Join 15,000+ architects receiving weekly insights on industry movement,
            salary trends, and exclusive studio invitations.
          </p>
          
          {/* Form đăng ký */}
          <form 
            className="flex flex-col sm:flex-row gap-4" 
            onSubmit={(e) => e.preventDefault()}
          >
            <input
              type="email"
              placeholder="professional@email.com"
              className="grow bg-[#0F5BB5] text-white placeholder-blue-300 px-6 py-4 rounded-xl outline-none focus:ring-2 focus:ring-white/50 transition-all border border-blue-400/30"
              required
            />
            <button
              type="submit"
              className="bg-white text-[#1A73E8] font-bold px-8 py-4 rounded-xl hover:bg-gray-50 transition-colors whitespace-nowrap shadow-lg"
            >
              Subscribe Now
            </button>
          </form>
        </div>
      </div>
    </section>
  );
};