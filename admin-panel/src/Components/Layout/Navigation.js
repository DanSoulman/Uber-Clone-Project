import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {firebaseConnect} from 'react-redux-firebase';
import './Navigation.css';
import logo from './logo.JPG';

class Navigation extends Component {
    state = {
        isAuthenticated: false
    }

    static getDerivedStateFromProps(props, state){
        const {auth} = props;

        if(auth.uid){
            return {isAuthenticated: true}
        }
        else{
            return {isAuthenticated: false}
        }
    }

    onLogoutClick = (e) => {
        e.preventDefault();

        const {firebase} = this.props;
        firebase.logout();
    };

  render() {
      const {isAuthenticated} = this.state;
      const {auth} = this.props;
    return (
        <div className="Container">
        <nav className="navbar navbar-expand-md navbar-dark bg-primary mb-4">
            <h2>
                <Link to="/" className="navbar-brand">
                    {' '}
                    <img src={logo} alt="Logo Error" 
                    style={{width: '35px',margin: 'auto', display: 'inline' }}
                
                />
                       {' '} Admin Panel{'      '}
                </Link>
            </h2>
            <button 
            className= "navbar-toggler"
            type="button"
            data-toggle="collapse"
            data-target="#navbarMain">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarMain">
               <ul className="navbar-nav mr-auto">
               {isAuthenticated ? (
                   <li className="nav-item">
                   <Link to="/" className="nav-link">
                     <h4> Dashboard </h4>
                   </Link>
                </li>
               ): null}
                </ul>
                {isAuthenticated ? (
                    <ul className="navbar-nav ml-auto">
                        <li className="nav-item">
                            <a href="#!" className="nav-link btn btn-outline-info">
                             {auth.email}
                            </a>
                        </li>
                        <li className="nav-item">
                            <a href="#!" className="nav-link btn btn-outline-secondary" onClick={this.onLogoutClick}>
                             Logout
                            </a>
                        </li>                     
                    </ul>
               ): null}
            </div>
        </nav>
        </div>
    );
  }
}

Navigation.propTypes = {
    firebase: PropTypes.object.isRequired,
    auth: PropTypes.object.isRequired
}

export default compose(
    firebaseConnect(),
    connect((state, props) => ({
        auth: state.firebase.auth
    }))
)(Navigation);