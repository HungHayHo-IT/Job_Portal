import { Routes, Route } from "react-router-dom";
import Navbar from "./components/layout/Navbar";
import Footer from "./components/layout/Footer";
import HeroSection from "./components/home/Hero";
import { CompaniesList } from "./components/home/CompaniesList";
import { CTASection } from "./components/home/CTASection";
import { Contact } from "lucide-react";
import ContactUsPage from "./pages/ContactUsPage";

// Tạm thời tách nội dung trang chủ ra một function
function Home() {
  return (
    <>
      <HeroSection />
      <main>
        <CompaniesList />
        <CTASection />
      </main>
    </>
  );
}

function App() {
  return (
    <div className="min-h-screen bg-[#0B0F19] font-sans flex flex-col">
      <Navbar />
      
      {/* Vùng nội dung sẽ thay đổi theo URL */}
      <div className="flex-grow">
        <Routes>
          {/* Định nghĩa các đường dẫn */}
          <Route path="/" element={<Home />} />
          <Route path="/contact-us" element={<ContactUsPage/>} />
          <Route path="/jobs" element={<div className="text-white p-10">Trang tìm việc (Sắp ra mắt)</div>} />
          <Route path="/companies" element={<div className="text-white p-10">Trang công ty (Sắp ra mắt)</div>} />
        </Routes>
      </div>

      <Footer />
    </div>
  );
}

export default App;