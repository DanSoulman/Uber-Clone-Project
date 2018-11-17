import React, { Component } from 'react';
import Particles from 'react-particles-js';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import {Provider} from 'react-redux';
import store from './store';
import './App.css';
import Navigation from './Components/layout/Navigation';
import particleOptions from './Components/layout/Particle';
import Dashboard from './Components/layout/Dashboard';
import Login from './Components/auth/Login';
import TripDetails from './Components/trips/TripDetails';
import EditClient from './Components/trips/EditClient';
import VehicleDetails from './Components/vehicles/VehicleDetails';
import AllVehicles from './Components/vehicles/AllVehicles';
import EditVehicle from './Components/vehicles/EditVehicle';
import {UserIsAuthenticated, UserIsNotAuthenticated} from './helpers/Auth';



class App extends Component {
  render() {
    return (
    <Provider store={store}>
      <Router>
         <div className="App">
           <Navigation />
           <div className="container">
           <Particles className="particles"
             params={particleOptions}
            />
            <Switch>
              <Route exact path="/" component={UserIsAuthenticated(Dashboard)} />
              <Route exact path="/trip/:id" component={UserIsAuthenticated(TripDetails)} />
              <Route exact path="/trip/edit/:id" component={UserIsAuthenticated(EditClient)} />
              <Route exact path="/allvehicles/display" component={UserIsAuthenticated(AllVehicles)} /> 
              <Route exact path="/vehicle/:id" component={UserIsAuthenticated(VehicleDetails)} />
              <Route exact path="/vehicle/edit/:id" component={UserIsAuthenticated(EditVehicle)} />
              <Route exact path="/login" component={UserIsNotAuthenticated(Login)} />
            </Switch>
            </div>                
        </div>
      </Router>
    </Provider>
    );
  }
}

export default App;
