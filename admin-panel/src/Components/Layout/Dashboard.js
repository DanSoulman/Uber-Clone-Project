import React from 'react';
import Trips from '../trips/Trips';
import Sidebar from '../layout/Sidebar';
import Vehicle from '../vehicles/Vehicles';
import './Dashboard.css';

export default () =>{
  return (
      <div>
        <div className="test">
            <div className="col-md-10">
               <Trips />
            </div>
            <div className="col-md-2">
                <Sidebar />
            </div>
        </div>
        <br />
        <br />
        <div className="test">
            <div className="col-md-10">
                <Vehicle />
            </div>
        </div>
      </div>
  );
};
