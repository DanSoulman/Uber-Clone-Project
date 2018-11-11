import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Spinner from '../layout/Spinner';

class EditClient extends Component{
    constructor(props){
        super(props);
        //Create refs

        this.nameInput = React.createRef();
        this.emailInput = React.createRef();
        this.phoneInput = React.createRef();
     }

    onSubmit = e => {
        e.preventDefault();
        const {user, firestore, history} = this.props;

        //update user
        const updateClient = {
            name: this.nameInput.current.value,
            email: this.emailInput.current.value,
            phone: this.phoneInput.current.value,
        }

        firestore.update({collection: 'Users', doc: user.id},
                         updateClient).then(history.push('/'));

    }
        
   render() {
        const {user} = this.props;
         if(user){
            console.log(this.props);
            return(
                <div>
                <div className="row">
                  <div className="col-md-6">
                    <Link to="/" className="btn btn-link">
                      <i className="fas fa-arrow-circle-left" /> Back To Dashboard
                    </Link>
                  </div>
                </div>        
                <div className="card">
                  <div className="card-header"><strong>Edit Client</strong></div>
                  <div className="card-body">
                    <form onSubmit={this.onSubmit}>
                      <div className="form-group">
                        <label htmlFor="firstName"><strong>Name:</strong></label>
                        <input
                          type="text"
                          className="form-control"
                          name="Name"
                          minLength="2"
                          required
                          ref = {this.nameInput}
                          defaultValue={user.name}
                        />
                      </div>
        
                      <div className="form-group">
                        <label htmlFor="email"><strong>Email:</strong></label>
                        <input
                          type="email"
                          className="form-control"
                          name="email"
                          required
                          ref = {this.emailInput}
                          defaultValue={user.email}
                        />
                      </div>
        
                      <div className="form-group">
                        <label htmlFor="phone"><strong>Phone:</strong></label>
                        <input
                          type="text"
                          className="form-control"
                          name="phone"
                          minLength="10"
                          required
                          ref = {this.phoneInput}
                          defaultValue={user.phone}
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

EditClient.propTypes = {
    firestore: PropTypes.object.isRequired
}

            export default compose(
                firestoreConnect( props => [
                    {collection: 'Users', storeAs: 'user', doc: props.match.params.id}
                ]),
                connect(({firestore: {ordered}}, props) => ({
                    user: ordered.user && ordered.user[0]
                }
            )
        )
    )(EditClient);