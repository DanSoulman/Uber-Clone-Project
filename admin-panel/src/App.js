import React, { Component } from 'react';
import Particles from 'react-particles-js';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import {Provider} from 'react-redux';
import store from './store';
import './App.css';
import Navigation from './Components/Layout/Navigation';
import particleOptions from './Components/Layout/Particle'
import Login from './Components/Auth/Login';


class App extends Component {
  render() {
    return (
      <Provider store={store}>
      <Router>
         <div className="App">
           <Navigation />
           <Particles className="particles"
             params={particleOptions}
            />
            <Login />
        </div>
      </Router>
      </Provider>
    );
  }
}

export default App;
