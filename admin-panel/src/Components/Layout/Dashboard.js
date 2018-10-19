import React from 'react';
import Trips from '../Trips/Trips';
import Sidebar from '../Layout/Sidebar';

export default () =>{
  return (
        <div className="row">
            <div className="col-md-10">
               <Trips />
            </div>
            <div className="col-md-2">
                <Sidebar />
            </div>
        </div>
  );
};
