import React from 'react';
import Trips from '../trips/Trips';
import Sidebar from '../layout/Sidebar';
import Vehicle from '../vehicles/Vehicles';

export default () =>{
  return (
      <div>
        <div className="row">
            <div className="col-md-10">
               <Trips />
            </div>
            <div className="col-md-2">
                <Sidebar />
            </div>
        </div>
        <br />
        <br />
        <div className="row">
            <div className="col-md-10">
                <Vehicle />
            </div>
        </div>
      </div>
  );
};
