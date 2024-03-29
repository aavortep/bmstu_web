import './App.css';
import { Route, Routes, Navigate, BrowserRouter as Router } from 'react-router-dom';
import HomePage from './pages/home/home.js';
import LoginPage from './pages/login/login.js';
import SigninPage from './pages/signin/signin.js';

function App() {
  return (
    <div className="App">
      <main>
        <Router>
          <Routes>
            <Route path='/home' element={<HomePage/>} />
            <Route path='/login' element={<LoginPage/>} />
            <Route path='/signin' element={<SigninPage/>} />
            <Route exact path='/' element={<Navigate replace to='/home' />} />
          </Routes>
        </Router>
      </main>
    </div>
  );
}

export default App;
