import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Spinner from '../layout/Spinner';
import classnames from 'classnames';
import Maps from './Maps';
import './TripDetails.css';

 class TripDetails extends Component {
    state = {
        showBalanceUpdate: false, 
        balanceUpdateAmount: '',
    };

    balanceSubmit = e => {
        e.preventDefault();
        
        const {trip, firestore} = this.props;
        const {balanceUpdateAmount} = this.state;

        const tripBalanceUpdate = {
            balance: parseFloat(balanceUpdateAmount)
        };
        //Update in Firestore
        firestore.update({collection: 'Trips', doc: trip.id}, tripBalanceUpdate);
    };
  
  onButtonClick = f => {
      f.preventDefault();

      const {trip} = this.props;
      const {firestore} = this.props;
      var {tripStatus} = trip.Active; 
      var {activeStatus} = {};
      if(trip){
        if (trip.Active===false){
            tripStatus = {
                Active: true
            };
            firestore.update({collection: 'Trips', doc: trip.id}, tripStatus );
        }
        else{
            tripStatus = {
                Active: false
            };
            activeStatus = {
                active: false
            };
            firestore
            .update({collection: 'Trips', doc: trip.id}, tripStatus);
            firestore
            .update({collecton: 'Vehicles', doc:trip.vehicle}, activeStatus);
        }
    }
    else{
       console.log(this.props);
    }

      
   };

   updateVehicle(vehicle){
       var {activeStatus} = vehicle.active;
       var {vehicleInfo} = vehicle.id;
       const {trip} = this.props;
       if(trip.Active===false){
            activeStatus = {
                active: false
            };
        }
        else{
            activeStatus = {
                active: true
            };
        }
        vehicleInfo = {
            vehicle: vehicle.id
        };
        const {firestore} = this.props;
        firestore
        .update({collection: 'Vehicles', doc: vehicle.id}, activeStatus);
        firestore
        .update({collection: 'Trips', doc: trip.id}, vehicleInfo);
   }
  
  onChange = e => this.setState({[e.target.name]: e.target.value});

  render() {
      const {trip} = this.props;
      const {showBalanceUpdate, balanceUpdateAmount} = this.state;
      let balanceForm = '';
      //if balance form should display
      if(showBalanceUpdate){
         balanceForm = (
             <form onSubmit={this.balanceSubmit}>
                 <div className="input-group">
                     <input 
                        type = "text"
                        className="form-control"
                        name="balanceUpdateAmount"
                        placeholder="Add New Balance"
                        value={balanceUpdateAmount}
                        onChange={this.onChange}
                        ></input>
                        <div className="input-group-append">
                            <input 
                                type="submit" 
                                value="Update" 
                                className="btn btn-outline-dark"
                            />
                        </div>
                     </div>
                  </form>    
                 );
              } else {
                balanceForm = null;
            }

      if(trip&&this.props.vehicle&&this.props.user){
          var {user} = this.props;
          var {vehicle} = this.props;
          var vehicles = null;
          console.log(this.props);
          for(var i = 0; i<this.props.user.length; i++)
          {
              if(this.props.user[i].email === trip.user){
                    user = this.props.user[i];
                    break;
              }
          }
          if(trip.vehicle !== "NO VEHICLE")
            for(i=0; i<this.props.vehicle.length; i++){
                if(this.props.vehicle[i].id === trip.vehicle){
                        vehicles = this.props.vehicle[i];
                        break;
                }
            if(trip.Active===false)
                this.updateVehicle(vehicles);
          }
          else{
              for(i=0;i<this.props.vehicle.length; i++){
                  if(this.props.vehicle[i].active === false && this.props.vehicle[i].maintenance === "false"){
                    vehicles = this.props.vehicle[i];
                    this.updateVehicle(vehicles);
                    break;
                  }
              }
          }
          if(trip.Active===false)
          {
            console.log(trip.Active);
                 return (
                    <div>
                        <div className="test">
                            <div className="col-md-6">
                                <Link to="/" className="btn btn-link align">
                                    <i className="fas fa-arrow-circle-left"></i>{' '}Back to Dashboard
                                </Link>
                            </div>  
                            <div className="col-md-6">
                            <div className="btn-group float-right">
                                <Link to={`/trip/edit/${user.id}`} className="btn btn-dark">
                                    Edit
                                </Link>
                                <button 
                                   onClick={this.onButtonClick}
                                   className="btn btn-success">
                                      Accept
                                </button>
                            </div>
                        </div>              
                     </div>
                        <hr />
                        <div className="card">
                          <div className = "card-header dis">
                                <h3>
                                    {user.name}
                                </h3>
                                <h5 className = "pull-right">
                                    Customer Balance: <span className={classnames({
                                                'text-success':user.balance>0,
                                                'text-danger': user.balance === 0
                                            })}>${parseFloat(user.balance).toFixed(2)}</span>
                                </h5>
                          </div>
                        <div className="card-body">
                            <div className="row">
                                <div className="col-md-8 col-sm-6 align">
                                    <h4>
                                        Passenger ID:{' '} <span className="text-secondary">{user.id}</span>
                                    </h4> 
                                </div>
                                <div className="col-md-4 col-sm-6 align">
                                    <h3 className="pull-right">
                                        Balance: <span className={classnames({
                                            'text-danger':trip.balance>0,
                                            'text-success': trip.balance === 0
                                        })}>${parseFloat(trip.balance).toFixed(2)}</span>{' '}
                                        <small>
                                            <a 
                                                href="#!"
                                                onClick={() => 
                                                    this.setState({
                                                        showBalanceUpdate: !this.state.showBalanceUpdate
                                                    })
                                                }
                                            >
                                              <i className="fas fa-pencil-alt"></i>
                                        </a>
                                    </small>
                                </h3>
                                     {balanceForm}
                                </div>
                            </div>
                               
                            <hr />
                            <ul className="list-group">
                                <li className="list-group-item align"><strong>Contact Email: {user.email}</strong></li>
                                <li className="list-group-item align"><strong>Contact Phone: {user.phone}</strong></li>
                            </ul>

                            <hr />
                            <div className="row">
                                <div className="col-md-8 col-sm-6 align">
                                    <h4>
                                        Car Info:{' '} <span className="text-secondary">{vehicles.id}</span>
                                    </h4> 
                                </div>
                                <div className="col-md-4 col-sm-6 align">
                                    <h4 className="pull-right">
                                       Needs Maintenance?: <span className={classnames({
                                            'text-danger':vehicles.maintenance==="false",
                                            'text-success': vehicles.maintenance === "true"
                                        })}>{vehicles.maintenance.toUpperCase()}</span>
                                    </h4>
                                </div>
                            </div>

                            <hr />
                            <ul className="list-group">
                                <li className="list-group-item align"><strong>Registration: {vehicles.registration}</strong></li>
                                <li className="list-group-item align"><strong>Make: {vehicles.make}</strong></li>
                                <li className="list-group-item align"><strong>Model: {vehicles.model}</strong></li>
                            </ul>
                        </div>
                        <div className="row">
                            <div className="col-md-12 col-sm-6 align">
                            <Maps 
                                trips={trip}
                                vehicle={vehicles}
                                user={user}
                            />
                        </div> 
                    </div>  
                 </div>  
               </div>
                );
          }
          else{
                 return (
                    <div>
                        <div className="test">
                            <div className="col-md-6">
                                <Link to="/" className="btn btn-link">
                                    <i className="fas fa-arrow-circle-left"></i>{' '}Back to Dashboard
                                </Link>
                            </div>  
                            <div className="col-md-6">
                            <div className="btn-group float-right">
                                <Link to={`/trip/edit/${user.id}`} className="btn btn-dark">
                                    Edit
                                </Link>
                                <button 
                                    className="btn btn-danger" 
                                    onClick={this.onButtonClick}>
                                        Cancel
                                </button>
                            </div>
                            </div>              
                        </div>
                        <hr />
                        <div className="card">
                        <div className = "card-header dis">
                            <h3>
                              {user.name}
                            </h3>
                            <h5 className = "pull-right">
                                    Customer Balance: <span className={classnames({
                                                'text-success':user.balance>0,
                                                'text-danger': user.balance === 0
                                            })}>${parseFloat(user.balance).toFixed(2)}</span>
                            </h5>
                          </div>
                        <div className="card-body">
                            <div className="row">
                                <div className="col-md-8 col-sm-6 align">
                                    <h4>
                                        Passenger ID:{' '} <span className="text-secondary">{user.id}</span>
                                    </h4> 
                                </div>
                                <div className="col-md-4 col-sm-6 align">
                                    <h3 className="pull-right">
                                        Balance: <span className={classnames({
                                            'text-danger':trip.balance>0,
                                            'text-success': trip.balance === 0
                                        })}>${parseFloat(trip.balance).toFixed(2)}</span>{' '}
                                        <small>
                                            <a href="#!" onClick={() => this.setState({showBalanceUpdate:
                                            !this.state.showBalanceUpdate})}>
                                            <i className="fas fa-pencil-alt"></i>
                                            </a>
                                        </small>
                                    </h3>
                                    {balanceForm}
                                </div>
                            </div>

                            <hr />
                            <ul className="list-group">
                                <li className="list-group-item align"><strong>Contact Email: {user.email}</strong></li>
                                <li className="list-group-item align"><strong>Contact Phone: {user.phone}</strong></li>
                            </ul>

                            <hr />
                            <div className="test">
                                <div className="col-md-8 col-sm-6 align">
                                    <h4>
                                        Car Info:{' '} <span className="text-secondary">{vehicle.id}</span>
                                    </h4> 
                                </div>
                                <div className="col-md-4 col-sm-6 align">
                                    <h4 className="pull-right">
                                        Needs Maintenance?: <span className={classnames({
                                            'text-danger':vehicles.maintenance==="false",
                                            'text-success': vehicles.maintenance === "true"
                                        })}>{vehicles.maintenance.toUpperCase()}</span>
                                    </h4>
                                </div>
                            </div>

                            <hr />
                            <ul className="list-group">
                                <li className="list-group-item align"><strong>Registration: {vehicles.registration}</strong></li>
                                <li className="list-group-item align"><strong>Make: {vehicles.make}</strong></li>
                                <li className="list-group-item align"><strong>Model: {vehicles.model}</strong></li>
                            </ul>


                    </div>
                    <div className="test">
                        <div className="col-md-12 col-sm-6 align">
                        <Maps 
                            trips={trip}
                            vehicle={vehicles}
                            user={user}
                        />
                        </div> 
                    </div>  
                  </div>
                </div>
            );
          }
      }
      else{
          return <Spinner />;
      }

  }
}

TripDetails.propTypes = {
    firestore: PropTypes.object.isRequired
}

/*
We use props here as opposed to, only collections, since we have to 
get the id of the trip from the collections trips
*/
export default compose(
            firestoreConnect( props => [
                {collection: 'Trips', storeAs: 'trip', doc: props.match.params.id },
                {collection: 'Vehicles'},
                {collection: 'Users', storeAs: 'user'}
            ]),
            connect(({firestore: {ordered}}, props) => ({
                trip: ordered.trip && ordered.trip[0],                //Retrieving state of the trip object and storing it as a prop
                vehicle: ordered.Vehicles,
                user: ordered.user
            }
        )
    )
)(TripDetails);



