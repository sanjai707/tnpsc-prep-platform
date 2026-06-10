import { Route, Routes, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import HomePage from './pages/HomePage';
import QuestionPage from './pages/QuestionPage';
import DailyInsightsPage from './pages/DailyInsightsPage';
import ResultPage from './pages/ResultPage';
import StatsPage from './pages/StatsPage';
import ProtectedRoute from './components/ProtectedRoute';
import LanguageToggle from './components/LanguageToggle';
import Header from './components/Header';
import { AuthProvider } from './contexts/AuthContext';
import ProfilePage from './pages/ProfilePage';

function App() {
  return (
    <AuthProvider>
      <div className="app-shell">
        <Header />
        <main className="page-container">
          <Routes>
            <Route path="/" element={<Navigate to="/home" replace />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/home" element={<ProtectedRoute><HomePage /></ProtectedRoute>} />
            <Route path="/practice" element={<ProtectedRoute><QuestionPage /></ProtectedRoute>} />
            <Route path="/insights" element={<ProtectedRoute><DailyInsightsPage /></ProtectedRoute>} />
            <Route path="/result" element={<ProtectedRoute><ResultPage /></ProtectedRoute>} />
            <Route path="/stats" element={<ProtectedRoute><StatsPage /></ProtectedRoute>} />
            <Route path="/profile" element={<ProtectedRoute><ProfilePage /></ProtectedRoute>} />
            <Route path="*" element={<Navigate to="/home" replace />} />
          </Routes>
        </main>
      </div>
    </AuthProvider>
  );
}

export default App;
