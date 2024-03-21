import axios from "axios";

export const MyComponent = () => {

    var baseUrl = "http://localhost:8080/";
    var response = "";

    const sayHello = () => {
        axios.get(baseUrl + "api\\hello").then((response) => {
            response = response.data;
            console.log(response);
          });
    };
  
    return (
        <div>
              <button onClick={sayHello}>Get API response</button>
              <text>{response}</text>
            </div>
    )
  };