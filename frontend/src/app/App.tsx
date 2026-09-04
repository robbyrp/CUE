import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ExplorePage from '../pages/explorePage/ExplorePage';
import PerformancePage from '../pages/performancePage/PerformancePage';
import MyActivityPage from '../pages/myActivityPage/MyActivityPage'
import CreatePerformancePage from '../pages/createPerformancePage/createPerformancePage';
import LoginPage from '../pages/loginPage/LoginPage';
import AuthProvider from '../auth/AuthContext'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* <Route path='/' element={<Home />} /> */}
          <Route path='/' element={< ExplorePage />} />
          <Route path='/login' element={<LoginPage />} />
          <Route path='/profil/vizualizare' element={<MyActivityPage />} />
          <Route path='/spectacole/adauga' element={<CreatePerformancePage />} />
          <Route path='/spectacole/:id' element={< PerformancePage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )

}

export default App
