import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Spinner from '../layout/Spinner';
import classnames from 'classnames';
import Map from './VehicleMap';
import './VehicleDetails.css';

 class VehicleDetails extends Component {
 
  render() {
      if(this.props.vehicle){
        const {vehicle} = this.props;
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
                                    <Link to={{pathname:`/vehicle/edit/${vehicle.id}`, state: {vehicle: vehicle} }} className="btn btn-dark">
                                        Edit
                                    </Link>
                                </div>
                            </div>
                        </div>

                        <hr />

                        <div className="card">
                        <div className = "card-header dis">
                                <h3>
                                    {vehicle.id}
                                </h3>
                                <h5 className = "pull-right">
                                    Rated <span className={classnames({
                                                'text-success':(parseFloat(vehicle.rating)>4.0),
                                                'text-danger': (parseFloat(vehicle.rating)<=4.0)})}>
                                                {parseFloat(vehicle.rating).toFixed(2)+' '}</span>
                                    stars
                                </h5>
                        </div>

                        <div className="card-body">
                            <div className="row">
                                <div className="col-md-8 col-sm-6 align">
                                    <h4>
                                        Distance Travelled:{' '} <span className="text-secondary">{vehicle.distance}{' '}miles</span>
                                    </h4> 
                                </div>
                                <div className="col-md-4 col-sm-6 align">
                                    <h4 className="pull-right">
                                        Needs Maintenance?: <span className={classnames({
                                            'text-danger':vehicle.maintenance==="false",
                                            'text-success': vehicle.maintenance === "true"
                                        })}>{vehicle.maintenance.toUpperCase()}</span>
                                    </h4>
                                </div>
                            </div>

                            <hr />

                            <ul className="list-group">
                                <li className="list-group-item align"><strong>Active: {vehicle.active.toString().toUpperCase()}</strong></li>
                                <li className="list-group-item align"><strong>Registration: {vehicle.registration}</strong></li>
                                <li className="list-group-item align"><strong>Make: {vehicle.make}</strong></li>
                                <li className="list-group-item align"><strong>Model: {vehicle.model}</strong></li>
                            </ul>
                        </div>
                        <div className="row">
                            <div className="col-md-12 col-sm-6 align">
                                <Map
                                    vehicle={vehicle}
                                 />
                        </div> 
                    </div>  
                 </div>  
               </div>
                );
        }
        else{
            return <Spinner />;
        }
      }
  }

VehicleDetails.propTypes = {
    firestore: PropTypes.object.isRequired
}

export default compose(
            firestoreConnect( props => [
                {collection: 'Vehicles', storeAs: 'vehicle', doc: props.match.params.id}
            ]),
            connect(({firestore: {ordered}}, props) => ({
                vehicle: ordered.vehicle && ordered.vehicle[0]
            }
        )
    )
)(VehicleDetails);



