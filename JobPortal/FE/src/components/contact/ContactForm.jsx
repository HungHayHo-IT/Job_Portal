// FE/src/components/contact/ContactForm.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

// Thành phần UI Input tùy chỉnh để tái sử dụng
const FormInput = ({ label, id, type, placeholder, required, children, ...props }) => (
  <div className="mb-6 flex-1">
    <label htmlFor={id} className="block text-sm font-medium text-gray-400 mb-2">
      {label} {required && <span className="text-red-500">*</span>}
    </label>
    {children ? (
      children
    ) : (
      <input
        type={type}
        id={id}
        name={id}
        placeholder={placeholder}
        required={required}
        className="w-full px-4 py-3 rounded-lg border border-gray-700 bg-[#0B0F19] text-white placeholder:text-gray-600 focus:border-cyan-500 focus:ring-2 focus:ring-cyan-500 transition"
        {...props}
      />
    )}
  </div>
);

export default function ContactForm() {
  const navigate = useNavigate();
  const [activeRole, setActiveRole] = useState('Other'); // Mặc định chọn "Other"
  const [message, setMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitSuccess, setSubmitSuccess] = useState(null);
  const maxChars = 500;

  const roles = ['Job Seeker', 'Employer', 'Other'];

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setSubmitSuccess(null);

    const formData = {
      fullName: e.target.fullName.value,
      email: e.target.email.value,
      role: activeRole,
      subject: e.target.subject.value,
      message,
    };

    try {
      // Mô phỏng yêu cầu API
      console.log('Sending form data:', formData);
      await new Promise(resolve => setTimeout(resolve, 1500)); // Delay mô phỏng
      setSubmitSuccess(true);
      setMessage(''); // Xóa tin nhắn
      e.target.reset(); // Đặt lại biểu mẫu
    } catch (error) {
      console.error('Error submitting form:', error);
      setSubmitSuccess(false);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    navigate('/'); // Chuyển hướng về trang chủ khi hủy
  };

  return (
    <form onSubmit={handleSubmit}>
      {submitSuccess === true && (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-6 text-sm">
          Message sent successfully! We will get back to you soon.
        </div>
      )}
      {submitSuccess === false && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-6 text-sm">
          An error occurred. Please try again later.
        </div>
      )}

      <div className="flex flex-col md:flex-row gap-6 mb-6">
        <FormInput label="Full Name" id="fullName" type="text" placeholder="John Doe" required />
        <FormInput label="Email Address" id="email" type="email" placeholder="john@example.com" required />
      </div>

      <div className="mb-6">
        <label className="block text-sm font-medium text-gray-400 mb-2">
          I am a <span className="text-red-500">*</span>
        </label>
        <div className="flex flex-col sm:flex-row gap-4">
          {roles.map((role) => (
            <button
              key={role}
              type="button"
              onClick={() => setActiveRole(role)}
              className={`flex-1 flex items-center justify-center gap-2 px-6 py-3 rounded-lg border text-sm font-medium transition ${
                activeRole === role
                  ? 'border-cyan-500 bg-[#162030] text-cyan-400'
                  : 'border-gray-700 bg-[#0B0F19] text-gray-400 hover:border-gray-600 hover:text-white'
              }`}
            >
              {activeRole === role && (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-5 w-5"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                    clipRule="evenodd"
                  />
                </svg>
              )}
              {role}
            </button>
          ))}
        </div>
      </div>

      <FormInput label="Subject" id="subject" type="select" required>
        <select
          id="subject"
          name="subject"
          required
          className="w-full px-4 py-3 rounded-lg border border-gray-700 bg-[#0B0F19] text-white focus:border-cyan-500 focus:ring-2 focus:ring-cyan-500 transition"
        >
          <option value="" disabled selected>
            Select a subject
          </option>
          <option value="technical">Technical Issue</option>
          <option value="billing">Billing</option>
          <option value="feedback">Feedback</option>
        </select>
      </FormInput>

      <div className="mb-6 relative">
        <FormInput label="Message" id="message" required>
          <textarea
            id="message"
            name="message"
            placeholder="Please describe your issue or inquiry in detail..."
            required
            maxLength={maxChars}
            rows={6}
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            className="w-full px-4 py-3 rounded-lg border border-gray-700 bg-[#0B0F19] text-white placeholder:text-gray-600 focus:border-cyan-500 focus:ring-2 focus:ring-cyan-500 transition"
          />
        </FormInput>
        <span className="absolute bottom-2 right-2 text-xs text-gray-600">
          Character count: {message.length}/{maxChars}
        </span>
      </div>

      <div className="flex flex-col sm:flex-row gap-4 mt-8">
        <button
          type="button"
          onClick={handleCancel}
          className="flex-1 px-8 py-3 rounded-lg border border-gray-700 text-gray-400 font-medium hover:border-gray-600 hover:text-white transition disabled:opacity-50"
          disabled={isSubmitting}
        >
          Cancel
        </button>
        <button
          type="submit"
          className="flex-1 px-8 py-3 rounded-lg bg-cyan-500 text-gray-900 font-semibold hover:bg-cyan-400 transition flex items-center justify-center disabled:bg-cyan-600"
          disabled={isSubmitting}
        >
          {isSubmitting ? (
            <>
              <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-gray-900" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Sending...
            </>
          ) : (
            'Send Message'
          )}
        </button>
      </div>
    </form>
  );
}