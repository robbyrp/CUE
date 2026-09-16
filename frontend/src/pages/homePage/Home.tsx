import Header from '../../common/components/Header/Header';
import Footer from '../../common/components/Footer/Footer';
import Hero from './components/Hero/Hero';
import Shows from './components/Shows/Shows';
import About from './components/About/About';
import Questions from './components/Questions/Questions';
import Contact from './components/Contact/Contact';

function Home() {
    return (
        <>
            <Header />
            <Hero />
            <Shows />
            <About />
            <Questions />
            <Contact />
            <Footer />
        </>
    )
}

export default Home;
