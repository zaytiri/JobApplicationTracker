import React from 'react';
import Card from 'react-bootstrap/Card';

export const Element = ({ title }) => {
  return (
    <Card 
    style={{ borderRadius: '0.5rem', backgroundColor: '#edf2f7', width: '250px', height:"65px", marginBottom:"3px", textAlign:"center" }}>
      <Card.Body>
        <Card.Title>{title}</Card.Title>
      </Card.Body>
    </Card>
  );
};