import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Spinner from '../layout/Spinner';
import classnames from 'classnames';
import Maps from './GoogleMapsContainer';
import './TripDetails.css';

 class TripDetails extends Component {
  render() {
      const {trip} = this.props;
      if(trip){
          if(this.props.user && this.props.vehicle){
          var {user} = trip.user;
          var {vehicle} = trip.vehicle;
          for(var i = 0; i<this.props.user.length; i++)
          {
              if(this.props.user[i].email === trip.user){
                    user = this.props.user[i];
                    break;
              }
          }
          for(i=0; i<this.props.vehicle.length; i++){
              if(this.props.vehicle[i].id === trip.vehicle._key.path.segments[6]){
                    vehicle = this.props.vehicle[i];
                    break;
              }
          }
          if(trip.Active === "False" && user)
          {
                console.log("False");
                return (
                    <div>
                        <div className="row">
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
                                <button className="btn btn-success">
                                    Accept
                                </button>
                            </div>
                            </div>              
                        </div>
                        <hr />
                        <div className="card">
                            <h3 className="card-header align">
                                {user.name}
                            </h3>
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
                                        })}>${parseFloat(trip.balance).toFixed(2)}</span>
                                    </h3>
                                </div>
                            </div>
                                {/* @todo - balanceform */}


                            <hr />
                            <ul className="list-group">
                                <li className="list-group-item align">Contact Email: {user.email}</li>
                                <li className="list-group-item align">Contact Phone: {user.phone}</li>
                            </ul>

                            <hr />
                            <div className="row">
                                <div className="col-md-8 col-sm-6 align">
                                    <h4>
                                        Car Info:{' '} <span className="text-secondary">{vehicle.id}</span>
                                    </h4> 
                                </div>
                                <div className="col-md-4 col-sm-6 align">
                                    <h3 className="pull-right">
                                        Maintenance?: <span className={classnames({
                                            'text-danger':vehicle.maintenance==="false",
                                            'text-success': vehicle.maintenance === "true"
                                        })}>{vehicle.maintenance.toUpperCase()}</span>
                                    </h3>
                                </div>
                            </div>

                            <hr />
                            <ul className="list-group">
                                <li className="list-group-item align">Make: {vehicle.make}</li>
                                <li className="list-group-item align">Model: {vehicle.model}</li>
                            </ul>
                        </div>
                     </div>  
                     <div className="row">
                        <div className="col-md-12 col-sm-6 align">
                             <Maps />
                        </div> 
                    </div>                            
                   </div>
                );
          }
          else if(user){
                 return (
                    <div>
                        <div className="row">
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
                                <button className="btn btn-danger">
                                    Cancel
                                </button>
                            </div>
                            </div>              
                        </div>
                        <hr />
                        <div className="card">
                            <h3 className="card-header align">
                                {user.name}
                            </h3>
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
                                        })}>${parseFloat(trip.balance).toFixed(2)}</span>
                                    </h3>
                                </div>
                            </div>
                                {/* @todo - balanceform */}


                            <hr />
                            <ul className="list-group">
                                <li className="list-group-item align">Contact Email: {user.email}</li>
                                <li className="list-group-item align">Contact Phone: {user.phone}</li>
                            </ul>

                            <hr />
                            <div className="row">
                                <div className="col-md-8 col-sm-6 align">
                                    <h4>
                                        Car Info:{' '} <span className="text-secondary">{vehicle.id}</span>
                                    </h4> 
                                </div>
                                <div className="col-md-4 col-sm-6 align">
                                    <h3 className="pull-right">
                                        Maintenance?: <span className={classnames({
                                            'text-danger':vehicle.maintenance==="false",
                                            'text-success': vehicle.maintenance === "true"
                                        })}>{vehicle.maintenance.toUpperCase()}</span>
                                    </h3>
                                </div>
                            </div>

                            <hr />
                            <ul className="list-group">
                                <li className="list-group-item align">Make: {vehicle.make}</li>
                                <li className="list-group-item align">Model: {vehicle.model}</li>
                            </ul>

                            <hr />
                    </div>
                  </div>
                  <div className="row">
                        <div className="col-md-12 col-sm-6 align">
                             <Maps />
                        </div> 
                    </div>   
                </div>
            );
          }
          else{
              return <Spinner />;
          }
        }
        else{
            return <Spinner />;
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
                {collection: 'Users', storeAs: 'user'},
                {collection: 'Vehicles', storeAs: 'vehicle'}
            ]),
            connect(({firestore: {ordered}}, props) => ({
                trip: ordered.trip && ordered.trip[0],                //Retrieving state of the trip object and storing it as a prop
                user: ordered.user,
                vehicle: ordered.vehicle
            }
        )
    )
)(TripDetails);



