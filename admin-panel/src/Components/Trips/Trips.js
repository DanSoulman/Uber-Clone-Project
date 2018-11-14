import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Timestamp from 'react-timestamp';
import Spinner from '../layout/Spinner';
import './Trips.css';

 class Trips extends Component {
    state = {
        totalOwed: null  
        
    }
    static getDerivedStateFromProps(props, state) {
        const {trips} = props;

        if(trips){
            //Add Balances
            const total = trips.reduce((total, trip) => {
                return total + parseFloat(trip.balance.toString());
            }, 0)
            return {totalOwed: total}
        }
        return null
    }
    render() {
      const {trips} = this.props ; 
      const {totalOwed} = this.state;
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
                        <h5 className="text-right text-secondary">
                        Total Owed:{' '}
                        <span className='text-primary'>
                            ${parseFloat(totalOwed).toFixed(2)}
                        </span></h5>
                    </div>
                </div>

                <table className="table table-stripped">
                    <thead className="thead-inverse">
                        <tr>
                            <th> Date </th>
                            <th> Location</th>
                            <th>Active</th>
                            <th>Balance</th>
                            <th />
                        </tr>
                    </thead>
                    <tbody>
                        {trips.map(trip => (
                            <tr key={trip.id}>
                                <td><Timestamp time={trip.Date.seconds} format='full' /></td>
                                <td>{trip.drop._lat} {trip.drop._long}</td>
                                 <td>{trip.Active}</td>
                                 <td>{trip.balance}</td>
                                <td>
                                    <Link to={`/trip/${trip.id}`}
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
        return <h1><Spinner /></h1>
    }
  }
}

Trips.propTypes = {
    firestore: PropTypes.object.isRequired,
    trips: PropTypes.array
}

export default compose(
    firestoreConnect([{ collection: 'Trips'}]),
    connect((state, props) => ({
        trips: state.firestore.ordered.Trips //Retrieving state of the trip object and storing it as a prop
    }))
)(Trips);


