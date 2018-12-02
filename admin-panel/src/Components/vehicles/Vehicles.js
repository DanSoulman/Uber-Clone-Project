import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import './Vehicle.css';

 class Vehicles extends Component {

    render() {
      const {vehicle} = this.props ; 
      if(vehicle){/*Loading the table only if the Vehicle info is retrieved from database */

        return (
            <div>
                <div className = "row">
                    <div className="col-md-6">
                         <h2>
                            <i className="fas fa-car"></i>Vehicles Info{' '}
                        </h2> 
                    </div>
                    <div className="col-md-6">
                        <h5 className="text-right text-secondary">
                            <Link to={{pathname:`/allvehicles/display`, state:{vehicle: vehicle} }} className="btn btn-dark btn-block btn-sm">
                                <i className="fas fa-info-circle"/>{' '}All Vehicles
                            </Link>
                        </h5>
                    </div>
                </div>
                <table className="table table-stripped">
                    <thead className="thead-inverse">
                        <tr>
                            <th> Make </th>
                            <th> Model</th>
                            <th>Distance(Miles)</th>
                            <th>Maintenance</th>
                            <th />
                        </tr>
                    </thead>
                    <tbody>
                        {vehicle.map(v => (
                            <tr key={v.id}>
                                <td>{v.make}</td>
                                <td>{v.model}</td>
                                 <td>{v.distance}</td>
                                 <td>{v.maintenance}</td>
                                <td>
                                    <Link to={`/vehicle/${v.id}`}
                                    className="btn btn-secondary btn-sm">
                                        <i className="fas fa-arrow-circle-right"></i> Details
                                    </Link>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        );
    }
    else{
        return null;
    }
  }
}

Vehicles.propTypes = {
    firestore: PropTypes.object.isRequired,
    vehicle: PropTypes.array
}

export default compose(
    firestoreConnect([{ collection: 'Vehicles'}]),
    connect((state, props) => ({
        vehicle: state.firestore.ordered.Vehicles //Retrieving state of the trip object and storing it as a prop
    }))
)(Vehicles);


