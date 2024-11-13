import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";
import './Dashboard.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFan, faLightbulb, faSnowflake, faBell, faUserCircle } from '@fortawesome/free-solid-svg-icons';
import mqtt from 'mqtt';

function Dashboard() {
  const [data, setData] = useState([]); // Store sensor data
  const [devices, setDevices] = useState({ fan: false, led: false, airconditioner: false });
  const [chartType, setChartType] = useState("all");
  const latestData = data[data.length - 1] || { temperature: 0, humidity: 0, light: 0 };

  // Connect to MQTT broker and subscribe to topics
  useEffect(() => {
    const client = mqtt.connect('tcp://192.168.1.49:1885', {
      username: 'Hoang_Van_Minh_Hieu',
      password: 'b21dccn051'
    });

    client.on('connect', () => {
      console.log('Connected to MQTT broker');
      client.subscribe('data_sensor');
      client.subscribe('device/action'); // Subscribe to device action messages
    });

    client.on('message', (topic, message) => {
      if (topic === 'data_sensor') {
        const newData = JSON.parse(message.toString());
        const currentTime = new Date().toLocaleString('vi-VN', {
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit',
          hour12: false,
        }).replace(/\//g, '-');

        setData((prevData) => {
          const updatedData = [...prevData.slice(-5), { ...newData, timestamp: currentTime }];
          localStorage.setItem('lastData', JSON.stringify(updatedData));
          return updatedData;
        });
      } else if (topic === 'device/action') {
        const deviceStatus = JSON.parse(message.toString());
        setDevices((prevState) => ({
          ...prevState,
          [deviceStatus.device.toLowerCase()]: deviceStatus.status === 'On',
        }));
      }
      client.on('message', (topic, message) => {
        console.log(`Received message from ${topic}: ${message.toString()}`);
        // Xử lý tiếp
      });
      
    });

    const storedData = localStorage.getItem('lastData');
    if (storedData) {
      setData(JSON.parse(storedData));
    } else {
      setData([{ temperature: 0, humidity: 0, light: 0 }]);
    }

    const storedDeviceStatus = localStorage.getItem('lastDeviceStatus');
    if (storedDeviceStatus) {
      setDevices(JSON.parse(storedDeviceStatus));
    }
    
    return () => {
      client.end();
    };
  }, []);

  // Toggle device control using MQTT
  const toggleDevice = (device) => {
    const client = mqtt.connect('tcp://192.168.1.49:1885', {
      username: 'Hoang_Van_Minh_Hieu',
      password: 'b21dccn051'
    });
  
    const currentDeviceState = !devices[device.toLowerCase()]; // Trạng thái mong muốn
    const message = JSON.stringify({ device, status: currentDeviceState ? 'On' : 'Off' });
  
    client.on('connect', () => {
      console.log('Connected to MQTT broker for device control');
      client.publish('device/action', message);
      console.log(message);
    });
  
    // Chờ phản hồi từ MQTT để xác nhận thiết bị đã bật/tắt
    client.on('message', (topic, message) => {
      if (topic === 'device/action') {
        const deviceStatus = JSON.parse(message.toString());
        if (deviceStatus.device.toLowerCase() === device.toLowerCase()) {
          // Cập nhật trạng thái thiết bị và lưu vào localStorage
          const updatedDevices = {
            ...devices,
            [deviceStatus.device.toLowerCase()]: deviceStatus.status === 'On',
          };
          setDevices(updatedDevices);
          localStorage.setItem('lastDeviceStatus', JSON.stringify(updatedDevices));
          client.end(); // Ngắt kết nối sau khi xử lý xong
        }
      }
    });
  };

  const getInfoBoxColor = (type, value) => {
    const minValue = { temperature: 0, humidity: 0, light: 0 };
    const maxValue = { temperature: 50, humidity: 100, light: 500 };
    const minColor = { temperature: '#ffcccc', humidity: '#cce6ff', light: '#ffffcc' };
    const maxColor = { temperature: '#ff3333', humidity: '#0066cc', light: '#ffcc00' };

    const getColor = (minColor, maxColor, percentage) => {
      const r1 = parseInt(minColor.slice(1, 3), 16);
      const g1 = parseInt(minColor.slice(3, 5), 16);
      const b1 = parseInt(minColor.slice(5, 7), 16);
      const r2 = parseInt(maxColor.slice(1, 3), 16);
      const g2 = parseInt(maxColor.slice(3, 5), 16);
      const b2 = parseInt(maxColor.slice(5, 7), 16);

      const r = Math.round(r1 + percentage * (r2 - r1));
      const g = Math.round(g1 + percentage * (g2 - g1));
      const b = Math.round(b1 + percentage * (b2 - b1));

      return `rgb(${r}, ${g}, ${b})`;
    };

    const range = maxValue[type] - minValue[type];
    const percentage = Math.min(Math.max((value - minValue[type]) / range, 0), 1); 

    return getColor(minColor[type], maxColor[type], percentage);
  };

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
          <h1>Dashboard</h1>
          <div className="header-icons">
            <span className="icon"><FontAwesomeIcon icon={faBell} /></span>
            <Link to="/profile" className="icon"><FontAwesomeIcon icon={faUserCircle} /></Link>
          </div>
        </header>

        {/* Info Boxes */}
        <div className="info-boxes">
          <div className="info-box temperature" style={{ backgroundColor: getInfoBoxColor('temperature', latestData.temperature) }}>
            <h3>🌡️ Temperature</h3>
            <p>{latestData.temperature}°C</p>
          </div>
          <div className="info-box humidity" style={{ backgroundColor: getInfoBoxColor('humidity', latestData.humidity) }}>
            <h3>💧 Humidity</h3>
            <p>{latestData.humidity}%</p>
          </div>
          <div className="info-box light" style={{ backgroundColor: getInfoBoxColor('light', latestData.light) }}>
            <h3>🔆 Light</h3>
            <p>{latestData.light} Lux</p>
          </div>
        </div>

        {/* Main Content */}
        <div className="statistics-and-controls">
          <div className="statistics">
            <h2>Statistics</h2>
            <label htmlFor="chartType">Select Chart: </label>
            <select id="chartType" value={chartType} onChange={(e) => setChartType(e.target.value)}>
              <option value="all">All</option>
              <option value="temperature">Temperature</option>
              <option value="humidity">Humidity</option>
              <option value="light">Light</option>
            </select>
            <ResponsiveContainer width="100%" height={400}>
              <LineChart data={data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="timestamp" />
                <YAxis yAxisId="left" />
                <YAxis yAxisId="right" orientation="right" />
                <Tooltip />
                <Legend />
                {(chartType === "all" || chartType === "temperature") && (
                  <Line yAxisId="left" type="monotone" dataKey="temperature" stroke="#ff0000" strokeWidth={4} /> 
                )}
                {(chartType === "all" || chartType === "humidity") && (
                  <Line yAxisId="left" type="monotone" dataKey="humidity" stroke="#0000ff" strokeWidth={4} /> 
                )}
                {(chartType === "all" || chartType === "light") && (
                  <Line yAxisId="right" type="monotone" dataKey="light" stroke="#ffff00" strokeWidth={4} /> 
                )}
              </LineChart>
            </ResponsiveContainer>

          </div>

          {/* Control Devices */}
          <div className="control-devices">
            <h2>Control Devices</h2>
            <div className="device-buttons">
              <div className={`device ${devices.fan ? "active" : ""}`}>
                <FontAwesomeIcon icon={faFan} className={`icon ${devices.fan ? "fan-on" : ""}`} style={{ fontSize: '30px' }} />
                <span>Fan</span>
                <label className="switch">
                  <input type="checkbox" checked={devices.fan} onChange={() => toggleDevice('Fan')} />
                  <span className="slider round"></span>
                </label>
              </div>

              <div className={`device ${devices.led ? "active" : ""}`}>
                <FontAwesomeIcon icon={faLightbulb} className={`icon ${devices.led ? "on" : ""}`} style={{ fontSize: '30px' }} />
                <span>Led</span>
                <label className="switch">
                  <input type="checkbox" checked={devices.led} onChange={() => toggleDevice('Led')} />
                  <span className="slider round"></span>
                </label>
              </div>

              <div className={`device ${devices.airconditioner ? "active" : ""}`}>
                <FontAwesomeIcon icon={faSnowflake} className={`icon ${devices.airconditioner ? "snowflake-on" : ""}`} style={{ fontSize: '30px' }} />
                <span>Air Conditioner</span>
                <label className="switch">
                  <input type="checkbox" checked={devices.airconditioner} onChange={() => toggleDevice('Airconditioner')} />
                  <span className="slider round"></span>
                </label>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
