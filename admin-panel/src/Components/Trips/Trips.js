import React, { Component } from 'react';
import {Link} from 'react-router-dom';

import './Trips.css';

 class Trips extends Component {
  render() {
      const tripInfo = [{
          id: '23441434',
          timestamp: {
              date: '11/10/2018',
              time: '11:31:00 AM'
          },
          location: {
              latitude: '51.8859709',
              longitude: '-8.4937638'
          },
          Active: 'False'
      }]

    if(tripInfo){/*Loading the table only if the trip info is retrieved from database */
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
                        {tripInfo.map(trip => (
                            <tr key={trip.id}>
                                <td>{trip.timestamp.date} {trip.timestamp.time}</td>
                                <td>{trip.location.latitude} {trip.location.longitude}</td>
                                <td>{trip.Active}</td>
                                <td>
                                    <Link to={`/trip/${tripInfo.id}`} 
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

export default Trips;
