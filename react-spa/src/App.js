/* eslint-disable react/jsx-no-comment-textnodes */
import React from 'react';
import './App.css';
import { Route, Switch, Redirect, BrowserRouter as Router } from 'react-router-dom';
import HomePage from './pages/home';
import LoginPage from './pages/login';
import SigninPage from './pages/signin';

class App extends React.Component {
    render() {
        return (
        <div>
            <h1 className='header_font' style={{marginLeft: 90}}>
                Добро пожаловать в HearBase!
            </h1>
            <main>
              <Router>
                <Switch>
                    <Route path='/home' component={HomePage} />
                    <Route path='/login' component={LoginPage} />
                    <Route path='/signin' component={SigninPage} />
                    <Route exact path='/' component={<Redirect replace to='/home' />} />
                </Switch>
              </Router>
            </main>
        </div>
    );
    }
}

export default App;