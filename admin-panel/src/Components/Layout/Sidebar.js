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

class Sidebar extends React.Component{
  render(){
    const {user} = this.props;
    if(user){
      console.log(user);
      var top_user_index=0;
      var top_trips = 0;
      for(var i = 0; i < user.length; i++){
        if(user[i].trips > top_trips ){
            top_user_index = i;
            top_trips = user[i].trips;
        }
      }
      var top_customer = user[top_user_index];
      console.log(top_customer);
      if(top_customer != null && user.length > 1 )
        return (
          <div>
              <Link to="/" className="btn btn-success btn-block">
                <i className="fas fa-refresh"/>Refresh
              </Link>
              <Card style = {{width: "20rem"}}>
                <CardBody>
                  <h4>
                      {' '}
                      <i className="fas fa-user"></i>Top Customer of Month{' '}
                  </h4> 
                  <h5>
                     <strong> {top_customer.name}</strong>
                  </h5>
                  <Link to={`/trip/edit/${top_customer.id}`}>
                    <Button color="info">More Details</Button>
                   </Link>
                </CardBody>
              </Card>
          </div>
        );
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
      {collection: 'Users', storeAs: 'user'}
  ]),
  connect(({firestore: {ordered}}, props) => ({
      user: ordered.user
  }
)
)
)(Sidebar);