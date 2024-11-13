import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";
import './NewPage.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFan, faLightbulb, faSnowflake, faBell, faUserCircle } from '@fortawesome/free-solid-svg-icons';
import mqtt from 'mqtt';
import axios from 'axios';

function NewPage() {
  const [data, setData] = useState([]); // Store sensor data
  const [devices, setDevices] = useState({  warningled: false});
  const [dustCount, setDustCount] = useState(0);
  const [chartType, setChartType] = useState("all");
  const latestData = data[data.length - 1] || { dust: 0};
  const [isWarningVisible, setIsWarningVisible] = useState(false);
  const [warningTimer, setWarningTimer] = useState(null);

  
  // Connect to MQTT broker and subscribe to topics
  useEffect(() => {
    const fetchDustCount = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/count-warning');
        setDustCount(response.data); // Cập nhật giá trị dustCount
      } catch (error) {
        console.error("Error fetching dust count:", error);
      }
    };
    fetchDustCount();

    const client = mqtt.connect('tcp://172.20.10.6:1885', {
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

        if (newData.dust >= 60) {
          setIsWarningVisible(true);
          client.publish('device/action', JSON.stringify({ device: 'WarningLedBlink', status: 'On' }));
        } else {
          setIsWarningVisible(false);
          client.publish('device/action', JSON.stringify({ device: 'WarningLedBlink', status: 'Off' }));
        }

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
      setData([{ dust: 0 }]);
    }

    const storedDeviceStatus = localStorage.getItem('lastDeviceStatus');
    if (storedDeviceStatus) {
      setDevices(JSON.parse(storedDeviceStatus));
    }
    
    
    return () => {
      client.end();
    };
  }, []);

  useEffect(() => {
    let interval;
    if (isWarningVisible) {
      interval = setInterval(() => {
        setIsWarningVisible((prev) => !prev); // Đảo ngược trạng thái
      }, 500000); // Thay đổi thời gian nhấp nháy tại đây
    } else {
      setIsWarningVisible(false); // Đảm bảo tắt cảnh báo khi không cần
    }

    return () => {
      clearInterval(interval); // Xóa interval khi component unmount hoặc khi trạng thái thay đổi
    };
  }, [isWarningVisible]);

  // Toggle device control using MQTT
  const toggleDevice = (device) => {
    const client = mqtt.connect('tcp://172.20.10.6:1885', {
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
    const minValue = { dust: 0 };
    const maxValue = { dust: 100 }; // Bạn có thể điều chỉnh giá trị tối đa cho bụi
    const minColor = { 
      dust: '#99ff99' // Màu xanh lục nhạt cho bụi
    };
    const maxColor = {  
      dust: '#006600' // Màu xanh lục đậm cho bụi
    };
  
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

        <div className="info-box-container2">
          <div className="info-box2 dust" style={{ backgroundColor: getInfoBoxColor('dust', latestData.dust) }}>
            <h3 style={{ fontSize: '40px' }}>🌫️ Wind</h3>
            <p style={{ fontSize: '40px' }}>{latestData.dust} m/s</p>
          </div>

          {/* <div className="info-box2 dust-count">
            <h3>Wind Warning Count</h3>
            <p>{dustCount}</p>
          </div> */}

          <div className="statistics2">
            <h2>Statistics</h2>
            
            <ResponsiveContainer width="100%" height={400}>
              <LineChart data={data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="timestamp" />
                <YAxis yAxisId="left" />
                <YAxis yAxisId="right" orientation="right" />
                <Tooltip />
                <Legend />
                {(chartType === "all" || chartType === "dust") && (
                  <Line yAxisId="left" type="monotone" dataKey="dust" stroke="#00ff00" strokeWidth={4} /> 
                )}
                
              </LineChart>
            </ResponsiveContainer>

          </div>

          {/* Thêm ô Warning */}
          <div className={`info-box2 warning-box ${isWarningVisible ? 'blink' : ''}`}>
            <h3 style={{ fontSize: '40px', textAlign: 'center', justifyContent: 'center', alignItems: 'center' }}>⚠️ WARNING!!!</h3>
            <p style={{ fontSize: '30px', textAlign: 'center', justifyContent: 'center', alignItems: 'center' }}>Check Wind Levels!</p>


          </div>
        </div>

        {/* Main Content */}
        <div className="statistics-and-controls">
          

          {/* Control Devices */}
          {/* <div className="control-devices">
            <h2>Control Warning Led</h2>
            <div className="device-buttons">
              <div className={`device ${devices.warningled ? "active" : ""}`}>
                <FontAwesomeIcon icon={faLightbulb} className={`icon ${devices.warningled ? "fan-on" : ""}`} style={{ fontSize: '30px' }} />
                <span>Warning Led</span>
                <label className="switch">
                  <input type="checkbox" checked={devices.fan} onChange={() => toggleDevice('WarningLed')} />
                  <span className="slider round"></span>
                </label>
              </div>

              
            </div>
          </div> */}
        </div>
      </div>
    </div>
  );
}

export default NewPage;


