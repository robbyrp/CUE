import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ExplorePage from '../pages/explorePage/ExplorePage';
import PerformancePage from '../pages/performancePage/PerformancePage';
import MyActivityPage from '../pages/myActivityPage/MyActivityPage'
import CreatePerformancePage from '../pages/createPerformancePage/createPerformancePage';
import LoginPage from '../pages/loginPage/LoginPage';
import AuthProvider from '../auth/AuthContext'
import AdminRoute from '../auth/AdminRoute'
import { ROUTES } from '../utils/constants'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* <Route path={ROUTES.HOME} element={<Home />} /> */}
          <Route path={ROUTES.HOME} element={<ExplorePage />} />
          <Route path={ROUTES.LOGIN} element={<LoginPage />} />
          <Route path={ROUTES.PROFIL} element={<MyActivityPage />} />
          <Route path={ROUTES.ADAUGA_SPECTACOL} element={<AdminRoute><CreatePerformancePage /></AdminRoute>} />
          <Route path={ROUTES.SPECTACOL(':id')} element={<PerformancePage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )

}

export default App
