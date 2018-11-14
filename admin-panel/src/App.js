import React, { Component } from 'react';
import Particles from 'react-particles-js';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import {Provider} from 'react-redux';
import store from './store';
import './App.css';
import Navigation from './Components/layout/Navigation';
import particleOptions from './Components/layout/Particle';
import Dashboard from './Components/layout/Dashboard';
//import Login from './Components/Auth/Login';
import TripDetails from './Components/trips/TripDetails';
import EditClient from './Components/trips/EditClient';
import VehicleDetails from './Components/vehicles/VehicleDetails';
import AllVehicles from './Components/vehicles/AllVehicles';
import EditVehicle from './Components/vehicles/EditVehicle';



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
              <Route exact path="/" component={Dashboard} />
              <Route exact path="/trip/:id" component={TripDetails} />
              <Route exact path="/trip/edit/:id" component={EditClient} />
              <Route exact path="/allvehicles/display" component={AllVehicles} /> 
              <Route exact path="/vehicle/:id" component={VehicleDetails} />
              <Route exact path="/vehicle/edit/:id" component={EditVehicle} />
            </Switch>
            </div>                
        </div>
      </Router>
    </Provider>
    );
  }
}

export default App;
