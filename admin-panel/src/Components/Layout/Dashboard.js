import React from 'react';
import Trips from '../trips/Trips';
import Sidebar from '../layout/Sidebar';
import Vehicle from '../vehicles/Vehicles';
import './Dashboard.css';

export default () =>{
  return (
        <div className="test">

            <div className="col-md-10">
               <Trips />
               <br />
               <Vehicle />
            </div>

            <div className="col-md-2">
                <Sidebar />
            </div>

        </div>
    );
};
