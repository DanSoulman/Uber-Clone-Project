import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './Navigation.css';


class Navigation extends Component {
  render() {
    return (
        <nav className="navbar navbar-expand-md navbar-dark bg-primary">
            <div className="Container">
            <h2>
                <Link to="/" className="navbar-brand">
                    <i className="fas fa-user-shield"></i>
                    Admin Panel
                </Link>
            </h2>
            </div>
        </nav>
    );
  }
}

export default Navigation;