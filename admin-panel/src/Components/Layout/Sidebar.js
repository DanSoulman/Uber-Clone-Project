import React from 'react';
import {Link} from 'react-router-dom';
import Button from "../CustomButtons/Button.jsx";
import withStyles from "@material-ui/core/styles/withStyles";
import Card from "../Card/Card.jsx";
import CardBody from "../Card/CardBody.jsx";
import cardImagesStyles from "../../assets/jss/material-dashboard-react/cardImagesStyles.jsx";

class Sidebar extends React.Component{
  render(){
    return (
      <div>
          <Link to="/" className="btn btn-success btn-block">
            <i className="fas fa-refresh"/>Refresh
          </Link>
          <Card style = {{width: "20rem"}}>
            <CardBody>
              <h4>Card title</h4>
              <p>
                Some quick example text to build on the card title and make up the
                bulk of the card's content.
              </p>
              <Button color="primary">Go somewhere</Button>
            </CardBody>
          </Card>
      </div>
    );
  }
}


export default Sidebar;