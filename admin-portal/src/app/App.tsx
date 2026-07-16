import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ExplorePage from '../pages/performanceCardsPage/ExplorePage';
import PerformancePage from '../pages/performancePage/PerformancePage'

function App() {
  return(
    <BrowserRouter>
      <Routes>
        {/* <Route path='/' element={<Home />} /> */}
        <Route path='/' element={< ExplorePage/>} />
        <Route path='spectacole/:id' element={< PerformancePage /> } />
      </Routes>

    </BrowserRouter>
  )
 
}

export default App
