import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Timestamp from 'react-timestamp';

import './Trips.css';

 class Trips extends Component {
  render() {
      const {trips} = this.props ; 


      /*[{
          id: '234242341',
          date: {
              Date: '11/10/2018',
              Time: '11:31:00 AM'
          },
          pickup: {
              lat: '51.8885119',
              long: '-8.5344533'
          },
          Active: 'False'

      },
    {
        id: '3424124214',
        date: {
            Date: '10/10/2018',
            Time: '12:31:00 AM'
        },
        pickup: {
            lat: '91.8885119',
            long: '-27.5344533'
        },
        Active: 'True'

    }];*/


    if(trips){/*Loading the table only if the trip info is retrieved from database */
        return (
            <div>
                <div className = "row">
                    <div className="col-md-6">
                         <h2>
                            {' '}
                            <i className="fas fa-users"></i>Trips Info{' '}
                        </h2> 
                    </div>
                    <div className="col-md-6">
                
                    </div>
                </div>

                <table className="table table-stripped">
                    <thead className="thead-inverse">
                        <tr>
                            <th> Date </th>
                            <th> Location</th>
                            <th>Active</th> 
                            <th />
                        </tr>
                    </thead>
                    <tbody>
                        {trips.map(trip => (
                            <tr key={trip.id}>
                                <td><Timestamp time={trip.Date.seconds} format='full' /></td>
                                <td>{trip.drop._lat} {trip.drop._long}</td>
                                 <td>{trip.Active}</td>
                                <td>
                                    <Link to={`/trip/${trips.id}`} 
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
        return <h1>Loading..</h1>
    }
  }
}

Trips.PropTypes = {
    firestore: PropTypes.object.isRequired,
    trips: PropTypes.array
}

export default compose(
    firestoreConnect([{ collection: 'Trips'}]),
    connect((state, props) => ({
        trips: state.firestore.ordered.Trips //Retrieving state of the trip object and storing it as a prop
    }))
)(Trips);


