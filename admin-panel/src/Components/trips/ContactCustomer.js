import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import classnames from 'classnames';

class ContactCustomer extends Component{
    constructor(props){
        super(props);
        this.state = {
            value: 'Send Email text to Customer.'
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
    handleChange(event) {
        this.setState({value: event.target.value});
      }
    
      handleSubmit(event) {
        alert('An essay was submitted: ' + this.state.value);
        event.preventDefault();
      }
    render(){
        console.log(this.props);
        return(
            <div>
                <div className="test">
                        <div className="col-md-6">
                            <Link to="/" className="btn btn-link align">
                                <i className="fas fa-arrow-circle-left"></i>{' '}Back to Dashboard
                            </Link>
                        </div>  
                        <div className="col-md-6">
                        <div className="btn-group float-right">
                            <Link to={`/trip/edit/${this.props.location.state.user.id}`} className="btn btn-dark">
                                Edit
                            </Link>
                        </div>
                    </div>              
                </div>
                    <hr />
                    <div className="card">
                    <div className = "card-header dis">
                            <h3>
                                {this.props.location.state.user.name}
                            </h3>
                            <h5 className = "pull-right">
                                Customer Balance: <span className={classnames({
                                            'text-success':this.props.location.state.user.balance>0,
                                            'text-danger': this.props.location.state.user.balance === 0
                                        })}>${parseFloat(this.props.location.state.user.balance).toFixed(2)}</span>
                            </h5>
                    </div>
                    <div className="card-body">
                        <div className="row">
                            <div className="col-md-8 col-sm-6 align">
                                <h4>
                                    Passenger ID:{' '} <span className="text-secondary">{this.props.location.state.user.id}</span>
                                </h4> 
                            </div>
                        </div>
                        
                        <hr />
                        <ul className="list-group">
                            <li className="list-group-item align"><strong>Contact Email: {this.props.location.state.user.email}</strong></li>
                            <li className="list-group-item align"><strong>Contact Phone: {this.props.location.state.user.phone}</strong></li>
                        </ul>
                </div>  
            </div>  
        </div>
       );
    }
}

export default ContactCustomer;