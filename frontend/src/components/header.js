import React from "react";
import axios from 'axios';


import Sidebar from "../components/sidebar";
import AboutSection from "../components/about";
import ServicesSections from "../components/services";
import CalloutSection from "../components/callout";
import BlogListSection from "../components/blogList";
import CallToAction from "../components/callToAction";
import MapSection from "../components/map";
import FooterSection from "../components/footer";
import ScrollToTop from "../components/scrollToTop";

import VideoImput from './VideoImput';

class Header extends React.Component {

  constructor(props) {
    super(props);

    this.emailInput = React.createRef();
    this.passwordInput = React.createRef();

    this.state = {
      token: '',
      trainer: '',
      notification: false,
      loginVisible: false,
      faceRecognise: true
    }

    this.Login = this.Login.bind(this);
    this.LoginTriggered = this.LoginTriggered.bind(this);
    this.Logout = this.Logout.bind(this);
    this.abortLogin = this.abortLogin.bind(this);
  }

  componentWillMount() {
     this.setState({
      token: localStorage.getItem('token')
    }, () => {
      this.getLoggedInTrainer();
    });
  }

  LoginTriggered() {
    this.setState({
      loginVisible: true
    })
  }

  Login(email, password) {
    axios.post(`http://localhost:8080/trainer/login`, {
       email: email,
       password: password 
      })
        .then(res => {
            this.setState({ token: res.data });
            this.getLoggedInTrainer();
            localStorage.setItem('token', this.state.token);
        })
        .catch(error => {});

    this.setState({
      loginVisible: false
    })

    this.NotifyToast();
  }

  abortLogin() {
    this.setState({
      loginVisible: false
    })

    this.NotifyToast();
  }

  NotifyToast() {
    this.setState({
      notification: true
    })

    setTimeout(() => {
      this.setState({notification: false});
    }, 5000)
  }

  Logout() {
    this.setState({
      token: '',
      trainer: ''
    })

    localStorage.setItem("token", "");
  }

  getLoggedInTrainer() {
    axios({
      method: 'get',
      url: `http://localhost:8080/admin/trainer`,
      headers: { 'Authorization': 'Bearer ' + this.state.token }
    })
    .then(res => {
      this.setState({
        trainer: res.data
      })
    })
  }


  render() {
    return (

      <div>
        <Sidebar />

        <header className="masthead d-flex" >
          <div className="container text-center my-auto">

            {
              this.state.token ? (
                <div>
                  <button onClick={this.Logout} className="btn btn-info btn-sm top-left fadeIn">
                    Kijelentkezés
                    <br></br>
                    {this.state.trainer.name} profiljából
                  </button>
                </div>
              ) : (
                <button onClick={this.LoginTriggered} className="btn btn-secondary btn-sm top-left fadeIn">Bejelentkezés</button>
              )
            }

            {
              this.state.notification && this.state.token ? (
                <div id="toast" className="show top-left" style={{ backgroundColor: "#4BB543" }}>
                  <div id="desc">Sikeres bejelentkezés {this.state.trainer.name} néven</div>
                </div>
              ) : (<div></div>)
            }

            {
              this.state.notification && !this.state.token ? (
                <div id="toast" className="show top-left" style={{ backgroundColor: "#cc3300" }}>
                  <div id="desc">Sikertelen bejelentkezés</div>
                </div>
              ) : (<div></div>)
            }

            {
              this.state.loginVisible ? (
                <div className="wrapper top-left fadeInDown">
                  <div id="formContent">
                    <h2 className="header2 active">Bejelentkezés</h2>
                    <form>
                     {/* <input type="text" ref={this.emailInput} id="login" className="inpText fadeIn first" name="login" placeholder="email"/>
                      <input type="password" ref={this.passwordInput} id="password" className="inpText fadeIn second" name="login" placeholder="jelszó"/>
                      <input type="submit" className="inpSubmit fadeIn third" value="Mehet" onClick={this.Login}/> 
                      <hr className="fadeIn fourth"></hr> */}
                      
                      {
                        this.state.faceRecognise ? (
                          <VideoImput
                            descriptorfile=""
                            Login={this.Login}
                            abortLogin={this.abortLogin}
                          />
                        ) : (
                          <input type="submit" className="inpSubmit fadeIn fourth" value="Bejelentkezés arcfelismeréssel" onClick={this.FaceLogin} />
                        )
                      }
                      
                    </form>
                  </div>
                </div>
              ) : ( 
                <div></div>
              )
            }
            </div>
          <div className="overlay" /> 
        </header>

        <AboutSection />
        {/*<ServicesSections />*/}
        <CalloutSection /> 
        <BlogListSection
          token={this.state.token}
        /> 
        {/* <CallToAction /> */}
        <MapSection />
        <FooterSection />
        <ScrollToTop />
      </div>
    )
  }
}
export default Header;
