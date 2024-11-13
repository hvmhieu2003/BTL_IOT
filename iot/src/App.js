import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
import Dashboard from './components/Dashboard/Dashboard';
import Stat from './components/Stat/Stat'; // Import component Stat
import Profile from './components/Profile/Profile'; // Giả sử bạn có component Profile
import History from './components/History/History'; // Giả sử bạn có component History
import NewPage from './components/NewPage/NewPage';
function App() {
  return (
    <Router>
      
      <div className="App">
        <Routes>
          <Route path="/" exact element={<Dashboard />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/newpage" element={<NewPage />} />
          <Route path="/statistics" element={<Stat />} />
          <Route path="/history" element={<History />} />
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
