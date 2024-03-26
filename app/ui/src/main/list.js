import React from 'react';

export const List = ({ name, children }) => {
  return (
    <div >
      <div>
        <h2 >{name}</h2>
      </div>
      {children}
    </div>
  );
};