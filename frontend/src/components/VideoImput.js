import React, { Component } from 'react';
import Webcam from 'react-webcam';
import { loadModels, getFullFaceDescription, createMatcher } from '../api/face';

// Import face profile
const JSON_PROFILE = require('../descriptor/trainers.json');

const inputSize = 160;

class VideoInput extends Component {
  constructor(props) {
    super(props);

    this.passwordInput = React.createRef();
    this.webcam = React.createRef();

    this.state = {
      fullDesc: null,
      detections: null,
      descriptors: null,
      faceMatcher: null,
      match: null,
      facingMode: null,
      email: null,
    };

    this.SendLogin = this.SendLogin.bind(this);
    this.abortLogin = this.abortLogin.bind(this);
  }

  componentWillMount = async () => {
    await loadModels();
    this.setState({ faceMatcher: await createMatcher(JSON_PROFILE) });
    this.setInputDevice();
  };

  setInputDevice = () => {
    navigator.mediaDevices.enumerateDevices().then(async devices => {
      let inputDevice = await devices.filter(
        device => device.kind === 'videoinput'
      );
      if (inputDevice.length < 2) {
        await this.setState({
          facingMode: 'user'
        });
      } else {
        await this.setState({
          facingMode: { exact: 'environment' }
        });
      }
      this.startCapture();
    });
  };

  startCapture = () => {
    this.interval = setInterval(() => {
      this.capture();
      this.detectEmail();
    }, 1000);
  };

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  capture = async () => {
    if (!!this.webcam.current) {
      await getFullFaceDescription(
        this.webcam.current.getScreenshot(),
        inputSize
      ).then(fullDesc => {
        if (!!fullDesc) {
          this.setState({
            detections: fullDesc.map(fd => fd.detection),
            descriptors: fullDesc.map(fd => fd.descriptor)
          });
        }
      });

      if (!!this.state.descriptors && !!this.state.faceMatcher) {
        let match = await this.state.descriptors.map(descriptor =>
          this.state.faceMatcher.findBestMatch(descriptor)
        );
        this.setState({ match });
      }
    }
  };

  detectEmail() {
    let { detections, match } = this.state;
    if (!!detections && this.state.email === null) {
        detections.map((detection, i) => {
          if (!!match && !!match[i])  
              if (match[i]._label !== "unknown")
                this.setState({
                    email: match[i]._label
                })
        });
    }
  }

  SendLogin(e) {
      e.preventDefault();
      this.props.Login(this.state.email, this.passwordInput.current.value);
  }

  abortLogin(e) {
      e.preventDefault();

      this.props.abortLogin();
  }


  render() {

    return (
     
        <div>
            <Webcam
                audio={false}
                ref={this.webcam}
                screenshotFormat="image/jpeg"
                className="Webcam"
            />
            {
                this.state.email === null ? (
                    <div>
                        <div class="sk-cube-grid">
                            <div class="sk-cube sk-cube1"></div>
                            <div class="sk-cube sk-cube2"></div>
                            <div class="sk-cube sk-cube3"></div>
                            <div class="sk-cube sk-cube4"></div>
                            <div class="sk-cube sk-cube5"></div>
                            <div class="sk-cube sk-cube6"></div>
                            <div class="sk-cube sk-cube7"></div>
                            <div class="sk-cube sk-cube8"></div>
                            <div class="sk-cube sk-cube9"></div>
                        </div>
                        <div>
                            <input type="submit" className="inpSubmit btn-info btn-sm fadeIn fourth" value="Mégse" onClick={this.abortLogin}/>
                        </div>
                    </div>

                ) : (
                    <div>
                        <input type="text" ref={this.emailInput} id="login" className="inpText fadeIn first" name="login" value={this.state.email} readOnly/>
                        <input type="password" ref={this.passwordInput} id="password" className="inpText fadeIn second" ref={this.passwordInput} name="login" placeholder="jelszó"/>
                        <input type="submit" className="inpSubmit fadeIn third" value="Mehet" onClick={this.SendLogin}/>
                    </div>
                )
            }

            
        </div>
    
    );
  }
}

export default VideoInput;