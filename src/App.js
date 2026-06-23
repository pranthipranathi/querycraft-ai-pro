import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Register from './components/Register';
import Dashboard from './components/Dashboard';

function App() {
  const [currentPage, setCurrentPage] = useState('login');
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    if (token && username) {
      setUser({ username });
      setCurrentPage('dashboard');
    }
  }, []);

  const handleLogin = (userData) => {
    setUser(userData);
    setCurrentPage('dashboard');
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setUser(null);
    setCurrentPage('login');
  };

  if (currentPage === 'login') {
    return (
      <Login
        onLogin={handleLogin}
        switchToRegister={() => setCurrentPage('register')}
      />
    );
  }

  if (currentPage === 'register') {
    return (
      <Register
        onRegister={handleLogin}
        switchToLogin={() => setCurrentPage('login')}
      />
    );
  }

  if (currentPage === 'dashboard' && user) {
    return (
      <Dashboard
        user={user}
        onLogout={handleLogout}
      />
    );
  }

  return <div>Loading...</div>;
}

export default App;