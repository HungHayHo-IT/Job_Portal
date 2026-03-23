import { CompaniesList } from "./components/home/CompaniesList";
import { CTASection } from "./components/home/CTASection";
import Navbar from "./components/layout/Navbar";


function App() {
  return (
    // Màu nền tối theo thiết kế
    <div className="min-h-screen bg-[#0B0F19] font-sans">
      <Navbar />
      <HeroSection />
      <CompaniesList />
      <CTASection />
      <Footer/>
    </div>
  );
}

export default App;