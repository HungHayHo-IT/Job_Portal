// FE/src/pages/ContactUsPage.jsx
import React from 'react';
import ContactForm from '../components/contact/ContactForm'; // Tệp mới chúng ta sẽ tạo

export default function ContactUsPage() {
  return (
    <div className="container mx-auto p-10 flex flex-col items-center">
      <h1 className="text-4xl font-bold text-white mb-2">Contact Us</h1>
      {/* Câu mô tả mới bạn yêu cầu để lấp đầy khoảng trống */}
      <p className="text-lg text-gray-400 mb-10 text-center max-w-lg">
        Our team is here to support you in every step of your architectural career journey. Please provide details, and we’ll get back to you soon.
      </p>
      
      <div className="bg-[#111625] p-8 rounded-2xl border border-gray-800 w-full max-w-4xl shadow-lg">
        <ContactForm />
      </div>
    </div>
  );
}