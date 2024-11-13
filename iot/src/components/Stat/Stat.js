import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, TextField, IconButton, Button } from '@mui/material';
import ReactPaginate from 'react-paginate';
import axios from 'axios'; // Import axios để gọi API
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell, faUserCircle, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import './Stat.css';

function Stat() {
  const [data, setData] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [searchTemp, setSearchTemp] = useState('');
  const [searchHumidity, setSearchHumidity] = useState('');
  const [searchLight, setSearchLight] = useState('');
  const [searchDust, setSearchDust] = useState(''); // Thêm trạng thái tìm kiếm cho dust
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [currentPage, setCurrentPage] = useState(0);
  const [sortField, setSortField] = useState('id');
  const [sortDirection, setSortDirection] = useState('asc'); // Trạng thái sắp xếp ID
  const [totalPages, setTotalPages] = useState(0);

  const fetchHistoryData = (page = currentPage) => {
    axios.get('http://localhost:8080/api/history_sensor/search/bytype', {
      params: {
        temperature: searchTemp,
        humidity: searchHumidity,
        light: searchLight,
        dust: searchDust,
        query: searchTerm,
        sortField,
        sortDirection,
        pageNumber: page,
        pageSize: itemsPerPage,
      },
    })
    .then(response => {
      setData(response.data.content); // Đặt dữ liệu từ API
      setTotalPages(response.data.totalPages); // Cập nhật tổng số trang từ API
    })
    .catch(error => {
      console.error("Có lỗi khi lấy dữ liệu: ", error);
    });
  };

  useEffect(() => {
    fetchHistoryData();
  }, [sortField, sortDirection, itemsPerPage, currentPage]);

  const handleSort = (field) => {
    const newDirection = sortField === field && sortDirection === 'asc' ? 'desc' : 'asc';
    setSortField(field);
    setSortDirection(newDirection);
  };

  const handlePageClick = (data) => {
    const selectedPage = data.selected;
    setCurrentPage(selectedPage);
    fetchHistoryData(selectedPage); // Gọi fetchHistoryData với trang mới
  };

  const handlePreviousPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
      fetchHistoryData(currentPage - 1);
    }
  };

  const handleNextPage = () => {
    if (currentPage < totalPages - 1) {
      setCurrentPage(currentPage + 1);
      fetchHistoryData(currentPage + 1);
    }
  };

  // const handleSearch = async (event) => {
  //   const searchValue = event.target.value;
  //   setSearchTerm(searchValue);
  //   setCurrentPage(0); // Reset về trang đầu tiên khi tìm kiếm

  //   // Nếu ô tìm kiếm trống, hãy lấy lại dữ liệu gốc
  //   if (searchValue.trim() === '') {
  //     fetchHistoryData();
  //   } else {
  //     try {
  //       const response = await axios.get(`http://localhost:8080/api/history_sensor/search?query=${searchValue}`);
  //       setData(response.data); // Cập nhật dữ liệu với kết quả tìm kiếm từ API
  //     } catch (error) {
  //       console.error("Error searching data: ", error);
  //     }
  //   }
  // };

  const handleQuerySearch = (event) => {
    const searchValue = event.target.value;
    setSearchTerm(searchValue);
    setCurrentPage(0);
  };

  const handleSearchByType = () => {
    setCurrentPage(0);
    fetchHistoryData();
  };

  // // Hàm tìm kiếm với nhiều tiêu chí
  // const handleSearchByType = async () => {
  //   setCurrentPage(0); // Reset về trang đầu tiên khi tìm kiếm

  //   try {
  //     const response = await axios.get(
  //       `http://localhost:8080/api/history_sensor/search/bytype`,
  //       {
  //         params: {
  //           temperature: searchTemp,
  //           humidity: searchHumidity,
  //           light: searchLight,
  //           dust: searchDust,
  //         },
  //       }
  //     );
  //     setData(response.data); // Cập nhật dữ liệu với kết quả tìm kiếm từ API
  //   } catch (error) {
  //     console.error("Error searching data by type: ", error);
  //   }
  // };
  useEffect(() => {
    fetchHistoryData(currentPage);
  }, [currentPage, itemsPerPage, sortField, sortDirection]);

  // Calculate the number of pages based on the total data length
  const pageCount = Math.ceil(data.length / itemsPerPage);

  // Ensure currentPage is within valid range
  const validCurrentPage = Math.min(currentPage, pageCount - 1); // Ensure it does not exceed max pages

  return (
    <div className="app">
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
        <header className="header">
          <h1>Statistics</h1>
          <div className="header-icons">
            <span className="icon"><FontAwesomeIcon icon={faBell} /></span>
            <span className="icon"><FontAwesomeIcon icon={faUserCircle} /></span>
          </div>
        </header>

        <div className="statistics-stat">
          <div className="filter-bar">
            <TextField
              label="Search by Keyword"
              value={searchTerm}
              onChange={handleQuerySearch}
              fullWidth
              margin="normal"
            />

            <TextField
              label="Search by Temperature (°C)"
              value={searchTemp}
              onChange={(e) => setSearchTemp(e.target.value)}
              margin="normal"
              style={{ marginRight: 16 }}
            />

            <TextField
              label="Search by Humidity (%)"
              value={searchHumidity}
              onChange={(e) => setSearchHumidity(e.target.value)}
              margin="normal"
              style={{ marginRight: 16 }}
            />

            <TextField    
              label="Search by Light (Lux)"
              value={searchLight}
              onChange={(e) => setSearchLight(e.target.value)}
              margin="normal"
              style={{ marginRight: 16 }}
            />

            <TextField    
              label="Search by Wind (m/s)"
              value={searchDust}
              onChange={(e) => setSearchDust(e.target.value)}
              margin="normal"
            />

            <IconButton className="search-button" onClick={handleSearchByType}>
              Search
            </IconButton> {/* Nút để thực hiện tìm kiếm bằng nhiều điều kiện */}

          </div>

          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell onClick={() => handleSort('id')}>ID {sortField === 'id' ? (sortDirection === 'asc' ? <FontAwesomeIcon icon={faSortUp} /> : <FontAwesomeIcon icon={faSortDown} />) : null}</TableCell>
                  <TableCell onClick={() => handleSort('temperature')}>Nhiệt độ (°C) {sortField === 'temperature' ? (sortDirection === 'asc' ? <FontAwesomeIcon icon={faSortUp} /> : <FontAwesomeIcon icon={faSortDown} />) : null}</TableCell>
                  <TableCell onClick={() => handleSort('humidity')}>Độ ẩm (%) {sortField === 'humidity' ? (sortDirection === 'asc' ? <FontAwesomeIcon icon={faSortUp} /> : <FontAwesomeIcon icon={faSortDown} />) : null}</TableCell>
                  <TableCell onClick={() => handleSort('light')}>Ánh sáng (Lux) {sortField === 'light' ? (sortDirection === 'asc' ? <FontAwesomeIcon icon={faSortUp} /> : <FontAwesomeIcon icon={faSortDown} />) : null}</TableCell>
                  <TableCell onClick={() => handleSort('dust')}>Gió (m/s) {sortField === 'dust' ? (sortDirection === 'asc' ? <FontAwesomeIcon icon={faSortUp} /> : <FontAwesomeIcon icon={faSortDown} />) : null}</TableCell>
                  <TableCell>Thời gian Đo</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.map((item) => (
                  <TableRow key={item.id}>
                    <TableCell>{item.id}</TableCell>
                    <TableCell>{item.temperature}</TableCell>
                    <TableCell>{item.humidity}</TableCell>
                    <TableCell>{item.light}</TableCell>
                    <TableCell>{item.dust}</TableCell>
                    <TableCell>{item.createdAt}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          {/* Điều hướng trang */}
          <div className="pagination">
            <Button onClick={handlePreviousPage} disabled={currentPage === 0}>Trang trước</Button>
            <span>Trang {currentPage + 1} / {totalPages}</span>
            <Button onClick={handleNextPage} disabled={currentPage >= totalPages - 1}>Trang sau</Button>
          </div>

          <div className="page-size">
            <TextField
              label="Page Size"
              type="number"
              value={itemsPerPage}
              onChange={(e) => setItemsPerPage(parseInt(e.target.value, 10))}
              margin="normal"
              style={{ marginTop: 16 }}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default Stat;
