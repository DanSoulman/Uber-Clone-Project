import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Spinner from '../layout/Spinner';
import './EditVehicle.css';

class EditVehicle extends Component{
    constructor(props){
        super(props);
        //Create refs

        this.makeInput = React.createRef();
        this.modelInput = React.createRef();
        this.registrationInput = React.createRef();
        this.distanceInput = React.createRef();
        this.maintenanceInput = React.createRef();
     }

    onSubmit = e => {
        e.preventDefault();
        const {vehicle, firestore, history} = this.props;
         if(this.distanceInput.current.value>500)
        {
            this.maintenanceInput = "true";
        }
        else{
            this.maintenanceInput = "false";
        }

        //update user
        const updateVehicle = {
            make: this.makeInput.current.value,
            model: this.modelInput.current.value,
            registration: this.registrationInput.current.value,
            distance: this.distanceInput.current.value,
            maintenance: this.maintenanceInput
        }
        console.log(updateVehicle);
        firestore.update({collection: 'Vehicles', doc: vehicle.id},
                         updateVehicle).then(history.push('/'));

    }
        
   render() {
        const {vehicle} = this.props;
         if(vehicle){
            return(
                <div>
                <div className="test">
                  <div className="col-md-6">
                    <Link to="/" className="btn btn-link">
                      <i className="fas fa-arrow-circle-left" /> Back To Dashboard
                    </Link>
                  </div>
                </div>        
                <div className="card">
                  <div className="card-header"><h4>Edit Vehicle</h4></div>
                  <div className="card-body">
                    <form onSubmit={this.onSubmit}>
                      <div className="form-group align">
                        <label htmlFor="make"><strong>Make:</strong></label>
                        <input
                          type="text"
                          className="form-control"
                          name="make"
                          minLength="2"
                          required
                          ref = {this.makeInput}
                          defaultValue={vehicle.make}
                        />
                      </div>
        
                      <div className="form-group align">
                        <label htmlFor="model"><strong>Model:</strong></label>
                        <input
                          type="text"
                          className="form-control"
                          name="model"
                          required
                          ref = {this.modelInput}
                          defaultValue={vehicle.model}
                        />
                      </div>
        
                      <div className="form-group align">
                        <label htmlFor="registration"><strong>Registration:</strong></label>
                        <input
                          type="text"
                          className="form-control"
                          name="registration"
                          minLength="6"
                          required
                          ref = {this.registrationInput}
                          defaultValue={vehicle.registration}
                        />
                      </div>

                      <div className="form-group align">
                        <label htmlFor="distance"><strong>Distance:</strong></label>
                        <input
                          type="text"
                          className="form-control"
                          name="distance"
                          required
                          ref = {this.distanceInput}
                          defaultValue={vehicle.distance}
                        />
                      </div>

                      <input
                        type="submit"
                        value="Submit"
                        className="btn btn-primary btn-block"
                      />
                    </form>
                  </div>
                </div>
              </div>
            );
                        
    }
        else{
             return <Spinner />
        }
    }
}

EditVehicle.propTypes = {
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
    )(EditVehicle);

