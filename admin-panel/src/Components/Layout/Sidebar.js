import React from 'react';
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firestoreConnect} from 'react-redux-firebase';
import Button from "../CustomButtons/Button.jsx";
import Card from "../Card/Card.jsx";
import CardBody from "../Card/CardBody.jsx";
import Spinner from '../layout/Spinner';
import './Sidebar.css';
class Sidebar extends React.Component{
constructor(props){
  super(props);

  this.costInput = React.createRef();
}


costSubmit = e => {
  e.preventDefault();
  
  const {settings, firestore, history} = this.props;

  const updateSettings = {
      costpermile: this.costInput.current.value
  };
  //Update in Firestore
  firestore.update({collection: 'settings', doc: settings.id}, 
                    updateSettings).then(history.push('/'));
};

  render(){
    const {user, settings, Vehicles} = this.props;
    console.log(this.props);
    if(user && settings && Vehicles){
      console.log(this.props);

      var top_user_index=0;
      var top_trips = 0;
      for(var i = 0; i < user.length; i++){
        if(user[i].trips > top_trips ){
            top_user_index = i;
            top_trips = user[i].trips;
        }
      }
      var top_customer = user[top_user_index];

      var top_car_index=0;
      var top_car = 0;
      for(i = 0; i<Vehicles.length; i++){
        if(Vehicles[i].rating > top_car){
          top_car_index = i;
          top_car = Vehicles[i].rating;
        }
      }

      top_car = Vehicles[top_car_index];

      if(top_customer != null && user.length > 1 && settings.length ===1 && Vehicles.length >= 1 ){
        return (
          <div>

              <Link to="/" className="btn btn-success btn-block">
                <i className="fas fa-refresh"/>Refresh
              </Link>

              <Card style = {{width: "22rem"}}>
                <CardBody>
                  <h4>
                      {' '}
                      <i className="fas fa-user"></i>Top Customer of Month{' '}
                  </h4> 
                  <h5>
                     <strong> {top_customer.name}</strong>
                  </h5>
                  <h6>
                    <strong>{top_customer.trips+' '}Trips</strong>
                  </h6>
                  <Link to={`/trip/edit/${top_customer.id}`}>
                    <Button color="blue">More Details</Button>
                   </Link>
                </CardBody>
              </Card>

              <Card style = {{width: "22rem"}}>
                <CardBody>
                <form onSubmit={this.costSubmit}>
                 <div className="input-group">
                  <label htmlFor="registration" className="align"><h5>Cost Per Mile </h5></label>
                  {'   '}
                      <input 
                          type = "text"
                          className="form-control"
                          name="costeUpdateAmount"
                          required
                          defaultValue = {settings[0].costpermile}
                          ref = {this.costInput}
                          >
                      </input>
                      <input 
                         type="submit" 
                         value="Update" 
                         className="btn btn-secondary btn-sm"
                      />
                       </div>
                  </form>    
                </CardBody>                       
              </Card>

              <Card style = {{width: "22rem"}}>
                <CardBody>
                  <h4>
                      {' '}
                      <i className="fas fa-car"></i>Top Car of Month{' '}
                  </h4> 
                  <h5>
                     <strong> {top_car.id}</strong>
                  </h5>
                  <h6>
                    <strong>{top_car.rating+' '}Stars Average</strong>
                  </h6>
                  <Link to={{pathname:`/vehicle/edit/${top_car.id}`, state: {vehicle: top_car} }}>
                    <Button color="blue">More Details</Button>
                   </Link>
                </CardBody>
              </Card>

          </div>
        );
      }
      else{
        return <Spinner />;
      }
    }
    else{
      return <Spinner />;
    }
  }
}

Sidebar.propTypes = {
  firestore: PropTypes.object.isRequired
}

export default compose(
  firestoreConnect( props => [
      {collection: 'Users', storeAs: 'user'},
      {collection: 'Vehicles', storeAs: 'Vehicles' },
      {collection: 'settings', storeAs: 'settings'}
  ]),
  connect(({firestore: {ordered}}, props) => ({
      user: ordered.user,
      settings: ordered.settings,
      Vehicles: ordered.Vehicles
  }
)
)
)(Sidebar);