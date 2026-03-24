import Navbar from "./components/layout/Navbar";
import HeroSection from "./components/home/Hero"; // Đổi tên import cho khớp
import { CompaniesList } from "./components/home/CompaniesList";
import { CTASection } from "./components/home/CTASection";
import Footer from "./components/layout/Footer"; // Thêm import Footer

function App() {
  return (
    <div className="min-h-screen bg-[#0B0F19] font-sans">
      <Navbar />
      <HeroSection />
      <main>
        <CompaniesList />
        <CTASection />
      </main>
      <Footer />
    </div>
  );
}

export default App;