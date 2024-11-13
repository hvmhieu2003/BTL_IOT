import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell, faUserCircle, faFileAlt, faCode, faBook } from '@fortawesome/free-solid-svg-icons';
import './Profile.css';
import avt from '../../assets/anh_dai_dien.jpg';

function Profile() {

  return (
    <div className="app">
      {/* Sidebar */}
      <div className="sidebar">
        <h2>Home IoT System</h2>
        <nav>
          <Link to="/dashboard">Dashboard</Link>
          <Link to="/newpage">Bài 5</Link>
          <Link to="/statistics">Data Sensor</Link>
          <Link to="/history">Action History</Link>
          <Link to="/profile">Profile</Link>
        </nav>
      </div>

      <div className="main-content">
        {/* Header */}
        <header className="header">
          <h1>Profile</h1>
          <div className="header-icons">
            <span className="icon"><FontAwesomeIcon icon={faBell} /></span>
            <span className="icon"><FontAwesomeIcon icon={faUserCircle} /></span>
          </div>
        </header>

        {/* Profile Content */}
        <div className="container">
          <div className="profile-container">
            {/* Profile Picture */}
            <div className="profile-picture">
              <img src={avt} alt="Profile" className="rounded-profile-picture" />
            </div>

            {/* Profile Details */}
            <div className="profile-details">
              <h2>Hoàng Văn Minh Hiếu</h2>
              <p><strong>Lớp:</strong> D21CNPM2</p>
              <p><strong>Mã SV:</strong> B21DCCN051</p>
              <p><strong>SĐT:</strong> 0972165893</p>
              <p><strong>Email:</strong> hvmhieu2003@gmail.com</p>
              
              {/* Links Section */}
              <div className="profile-links">
                <div className="link-box">
                  <a href="https://docs.google.com/document/d/1MD1SgXr4vl2oUysWcq80H3zO1_JxI3-9/edit?usp=sharing&ouid=108703374629916228705&rtpof=true&sd=true" target="_blank" rel="noopener noreferrer">
                    Báo cáo
                  </a>
                </div>
                <div className="link-box">
                  <a href="https://github.com/hvmhieu2003/BTL_IOT" target="_blank" rel="noopener noreferrer">
                    GitHub
                  </a>
                </div>
                <div className="link-box">
                  <a href="https://api.postman.com/collections/39033111-35194488-ddf6-465e-b357-93ff462877d2?access_key=PMAT-01JAW2VE7MRJDDS3J1AYCVVXG6" target="_blank" rel="noopener noreferrer">
                    API Documentation
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>


      </div>
    </div>
  );
}

export default Profile;
