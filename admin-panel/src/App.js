import React, { Component } from 'react';
import Particles from 'react-particles-js';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
/*import {Provider} from 'react-redux';*/
/*import store from './store';*/
import './App.css';
import Navigation from './Components/Layout/Navigation';
import particleOptions from './Components/Layout/Particle';
import Dashboard from './Components/Layout/Dashboard';
/*import Login from './Components/Auth/Login';*/



class App extends Component {
  render() {
    return (
      /*<Provider store={store}>*/
      <Router>
         <div className="App">
           <Navigation />
           <div className="container">
           <Particles className="particles"
             params={particleOptions}
            />
            <Switch>
              <Route exact path="/" component={Dashboard} />
            </Switch>
            </div>                
        </div>
      </Router>
      /*</Provider>*/
    );
  }
}

export default App;
