import axios from "axios";
import React, { useState } from 'react';

export const MyComponent = () => {
  const [response, setResponse] = useState("");
  var baseUrl = "http://localhost:8080/";

  const sayHello = () => {
    axios.get(baseUrl + "api\\hello").then((response) => {
      setResponse(response.data);
      });
  };

  return (
      <div>
            <button onClick={sayHello}>Get API response</button>
            <h1>{response}</h1>
          </div>
  )
  };