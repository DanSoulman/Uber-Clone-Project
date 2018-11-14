import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Spinner from '../layout/Spinner';
import classnames from 'classnames';
import Map from './VehicleMap';

 class VehicleDetails extends Component {
 
  render() {
      if(this.props.vehicle){
        const {vehicle} = this.props;
        console.log(vehicle);
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
                                <Link to={{pathname:`/vehicle/edit/${vehicle.id}`, state: {vehicle: vehicle} }} className="btn btn-dark">
                                    Edit
                                </Link>
                            </div>
                        </div>              
                        </div>
                        <hr />
                        <div className="card">
                            <h3 className="card-header align">
                                {vehicle.id}
                            </h3>
                        <div className="card-body">
                            <div className="row">
                                <div className="col-md-8 col-sm-6 align">
                                    <h4>
                                        Distance Travelled:{' '} <span className="text-secondary">{vehicle.distance}</span>
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



