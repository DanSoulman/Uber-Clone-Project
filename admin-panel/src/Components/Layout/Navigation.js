import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './Navigation.css';


class Navigation extends Component {
  render() {
    return (
        <div className="Container">
        <nav className="navbar navbar-expand-md navbar-dark bg-primary mb-4">
            <h2>
                <Link to="/" className="navbar-brand">
                    {' '}
                    <i className="fas fa-user-shield"></i>
                    Admin Panel{' '}
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
                     <li className="nav-item">
                        <Link to="/" className="nav-link">
                            Dashboard
                        </Link>
                     </li>
               </ul>
            </div>
        </nav>
        </div>
    );
  }
}

export default Navigation;