import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Spinner from '../layout/Spinner';


class EditClient extends Component{
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
            console.log(user);
            console.log(vehicle);
        }
    }
        else{
             return <Spinner />
        }
    }
}




EditClient.propTypes = {
    firestore: PropTypes.object.isRequired
}

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
)(EditClient);