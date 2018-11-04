import React from 'react';
import loader from './loader.gif';

export default function spinner() {
  return (
    <div>
      <img src={loader} alt="Loading..." 
      style={{width: '35px',margin: 'auto', display: 'block' }}/>
    </div>
  )
}
