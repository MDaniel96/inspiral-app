import React from "react";
import ReactDOM from "react-dom";

import '@fortawesome/fontawesome-free/css/all.min.css';
import 'bootstrap-css-only/css/bootstrap.min.css';
import 'mdbreact/dist/css/mdb.css';

import "bootstrap/dist/css/bootstrap.min.css";
import "./scss/stylish-portfolio.css";

import Header from "./components/header";



class App extends React.Component {
  render() {
    return (
        <Header />    
    );
  }
}

const rootElement = document.getElementById("root");
ReactDOM.render(<App />, rootElement);
