import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, TextField, IconButton, Button } from '@mui/material';
import ReactPaginate from 'react-paginate';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell, faUserCircle, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios'; // Import axios
import './History.css'; // Import file CSS

function History() {
  const [searchTerm, setSearchTerm] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [itemsPerPage, setItemsPerPage] = useState(10); // Số dòng hiển thị mặc định
  const [data, setData] = useState([]); // Dữ liệu lịch sử từ API
  const [sortOrder, setSortOrder] = useState('asc'); // Trạng thái sắp xếp (asc hoặc desc)
  const [sortField, setSortField] = useState('id');
  const [sortDirection, setSortDirection] = useState('asc'); // Trạng thái sắp xếp ID
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    fetchHistoryData();
  }, [sortField, sortDirection, itemsPerPage, currentPage]);

  const fetchHistoryData = (page = currentPage) => {
    axios.get('http://localhost:8080/api/history_actions/search', {
      params: {
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

  // const handleSearch = async (event) => {
  //   const searchValue = event.target.value;
  //   setSearchTerm(searchValue);
  //   setCurrentPage(0); // Reset về trang đầu tiên khi tìm kiếm

  //   // Nếu ô tìm kiếm trống, hãy lấy lại dữ liệu gốc
  //   if (searchValue.trim() === '') {
  //     fetchHistoryData();
  //   } else {
  //     try {
  //       const response = await axios.get(`http://localhost:8080/api/history_actions/search?query=${searchValue}`);
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

  const handleSearch = () => {
    setCurrentPage(0);
    fetchHistoryData();
  };
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


  const handlePageChange = ({ selected }) => {
    setCurrentPage(selected);
  };

  const handleItemsPerPageChange = (event) => {
    setItemsPerPage(parseInt(event.target.value, 10));
    setCurrentPage(0); // Reset về trang đầu tiên khi thay đổi số dòng hiển thị
  };

  // const handleSort = () => {
  //   const sortedData = [...data].sort((a, b) => {
  //     if (sortOrder === 'asc') {
  //       return a.id - b.id;
  //     } else {
  //       return b.id - a.id;
  //     }
  //   });
  //   setData(sortedData);
  //   setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc'); // Chuyển đổi giữa asc và desc
  // };

  // const filteredData = data.filter(item =>
  //   item.id.toString().includes(searchTerm) ||
  //   item.device.toLowerCase().includes(searchTerm.toLowerCase()) ||
  //   item.action.toLowerCase().includes(searchTerm.toLowerCase()) ||
  //   item.createdAt.toString().includes(searchTerm)
  // );

  const filteredData = data; // Sử dụng toàn bộ dữ liệu từ API

  // Calculate the number of pages based on the total data length
  const pageCount = Math.ceil(data.length / itemsPerPage);

  // Tính toán dữ liệu hiển thị cho trang hiện tại
  // const indexOfLastItem = (currentPage + 1) * itemsPerPage;
  // const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  // const displayedData = filteredData.slice(indexOfFirstItem, indexOfLastItem);

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
        {/* Header */}
        <header className="header">
          <h1>History</h1>
          <div className="header-icons">
            <span className="icon"><FontAwesomeIcon icon={faBell} /></span>
            <span className="icon"><FontAwesomeIcon icon={faUserCircle} /></span>
          </div>
        </header>

        {/* Content */}
        <div className="statistics-stat">
          <div className="filter-bar1">
            <TextField
              className='search1'
              label="Search"
              value={searchTerm}
              onChange={handleQuerySearch}
              margin="normal"
            />

            <IconButton className="search-button" onClick={handleSearch}>
              Search
            </IconButton>
          </div>  

          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell onClick={() => handleSort('id')}>ID {sortField === 'id' ? (sortDirection === 'asc' ? <FontAwesomeIcon icon={faSortUp} /> : <FontAwesomeIcon icon={faSortDown} />) : null}</TableCell>
                  <TableCell>Thiết bị</TableCell>
                  <TableCell>Hành động</TableCell>
                  <TableCell>Thời gian</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.map((item) => (
                  <TableRow key={item.id}>
                    <TableCell>{item.id}</TableCell>
                    <TableCell>{item.device}</TableCell>
                    <TableCell>{item.action}</TableCell>
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

          {/* Di chuyển "Page Size" xuống dưới */}
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

export default History;
