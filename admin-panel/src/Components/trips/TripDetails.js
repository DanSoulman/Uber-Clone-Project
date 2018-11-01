import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Timestamp from 'react-timestamp';
import Spinner from '../layout/Spinner';

 class TripDetails extends Component {
  render() {
      const {trip} = this.props;
      if(trip){
          if(trip.Active === "False")
          {
                console.log("False");
                return (
                    <div>
                        <div className="row">
                            <div className="col-md-6">
                                <Link to="/" className="btn btn-link">
                                    <i className="fas fa-arrow-circle-left"></i>Back to Dashboard
                                </Link>
                            </div>  
                            <div className="col-md-6">
                            <div className="btn-group float-right">
                                <Link to={'/trip/edit/${trip.id}'} className="btn btn-dark">
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
                            <div className="card-header">
                                {trip.Active}
                            </div>
                        </div>
                    </div>
                );
          }
          else{
                 return (
                      <div>
                          <div className="row">
                              <div className="col-md-6">
                                  <Link to="/" className="btn btn-link">
                                      <i className="fas fa-arrow-circle-left"></i>Back to Dashboard
                                  </Link>
                              </div>  
                              <div className="col-md-6">
                              <div className="btn-group float-right">
                                  <Link to={'/trip/edit/${trip.id}'} className="btn btn-dark">
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
                              <div className="card-header">
                                  {trip.Active}
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

/*
We use props here as opposed to, only collections, since we have to 
get the id of the trip from the collections trips
*/
export default compose(
            firestoreConnect( props => [
                {collection: 'Trips', storeAs: 'trip', doc: props.match.params.id },
                {collection: 'Users', storeAs: 'user'},
                {collection: 'Vehicles'}
            ]),
            connect(({firestore: {ordered}}, props) => ({
                trip: ordered.trip && ordered.trip[0]//Retrieving state of the trip object and storing it as a prop
            }
        )
    )
)(TripDetails);

