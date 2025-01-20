import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import logo from './logo.svg';
import './App.css';

import HomePage from './pages/HomePage';
import AboutPage from './pages/AboutPage';
import UserListPage from './pages/UserListPage';
import ChatAppPage from './pages/ChatAppPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/userList" element={<UserListPage />}/>
        <Route path="/chat" element={<ChatAppPage />}/>

      </Routes>
    </Router>
  );
}

export default App;

