import React from 'react';
import Trips from '../trips/Trips';
import Sidebar from '../layout/Sidebar';

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
