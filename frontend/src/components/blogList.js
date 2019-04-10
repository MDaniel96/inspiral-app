import React from "react";
import ScrollableAnchor from "react-scrollable-anchor";
import axios from 'axios';
import Entry from './entry';
import { MDBContainer, MDBModal, MDBModalBody, MDBModalHeader, MDBModalFooter, MDBInput } from 'mdbreact';
import { CustomInput, Form, FormGroup, Label } from 'reactstrap';

import Searchbar from "./searchbar";


var SpeechRecognition = (
  window.SpeechRecognition ||
  window.webkitSpeechRecognition
);
const recognition = new SpeechRecognition();

recognition.continous = true;
recognition.interimResults = true;
recognition.lang = 'hu';

class BlogListSection extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      entries: [],
      blogCount: 4,
      isModalOpen: false,
      file: '',
      language: 'hu',
      listening: false,
    }

    this.paginate = this.paginate.bind(this);
    this.moreBlogs = this.moreBlogs.bind(this);
    this.saveEntry = this.saveEntry.bind(this);
    this.handleToggle = this.handleToggle.bind(this);
    this.onFileChange = this.onFileChange.bind(this);
    this.onDeleteEntry = this.onDeleteEntry.bind(this);
    this.searchEntry = this.searchEntry.bind(this);
    this.toggleLanguage = this.toggleLanguage.bind(this);
    this.toggleListening = this.toggleListening.bind(this);
    this.handleListen = this.handleListen.bind(this);

    this.titleInput = React.createRef();
    this.contentInput = React.createRef();
  }

  componentWillMount() {
    this.refreshBlogs();
  }

  onDeleteEntry(id) {
    var newEntries = this.state.entries.filter(e=>e.id!==id);
        this.setState({
            entries: newEntries
        });

    axios({
        method: 'delete',
        url: `http://localhost:8080/admin/entry/${id}`,
        headers: { 'Authorization': 'Bearer ' + this.props.token }
      })
      .then(res => { })
  }

  refreshBlogs() {
    axios.get(`http://localhost:8080/entry/all`)
    .then(res => {
        this.setState({ entries: res.data });
    })
  }

  paginate(array, page_size, page_number) {
    --page_number;
    return array.slice(page_number * page_size, (page_number + 1) * page_size);
  }

  moreBlogs() {
    this.setState({
      blogCount: this.state.blogCount + 2
    });
  }

  getBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        let encoded = reader.result.replace(/^data:(.*;base64,)?/, '');
        if ((encoded.length % 4) > 0) {
          encoded += '='.repeat(4 - (encoded.length % 4));
        }
        resolve(encoded);
      };
      reader.onerror = error => reject(error);
    });
  }

  saveEntry() {
    this.handleToggle();

    this.getBase64(this.state.file).then(
      data => 
        axios.post(`http://localhost:8080/admin/entry`, 
        { 
            title: this.titleInput.current.value,
            date: new Date(),
            content: this.contentInput.current.value,
            image: data
        }, {
            headers: { 'Authorization': 'Bearer ' + this.props.token }
        })
        .then(res => {          
            let { entries } = this.state;
            entries.push(res.data);
            this.setState({ entries: entries, }); 
        })
    );

    this.setState({ file: '' });
  }

  onFileChange(e) {
    this.setState({file:e.target.files[0]})
  }

  handleToggle() {
    this.setState({
      isModalOpen: !this.state.isModalOpen
    });
  }

  searchEntry(keyword) {
    if (keyword==="")
      this.refreshBlogs();
    else {
      axios.get(`http://localhost:8080/entry/search/${keyword}`)
      .then(res => {
          this.setState({ entries: res.data });
      })
    } 
  }

  toggleLanguage(e) {
    e.preventDefault();
    let newLang;
    this.state.language === 'hu' ? newLang = 'en' : newLang = 'hu';
    recognition.lang = newLang;
    this.setState({
      language: newLang
    });
  }

  toggleListening(e) {
    e.preventDefault();
    this.setState({
      listening: !this.state.listening
    }, this.handleListen)
  }

  handleListen() {
    if (this.state.listening) {
        recognition.start();
        recognition.onend = () => {
            if (this.state.listening)
                recognition.start();
        }
    } else {
        recognition.stop();

    }


    let finalTranscript = '';
    recognition.onresult = event => {
        let interimTranscript = '';

        for (let i = event.resultIndex; i < event.results.length; i++) {
            const transcript = event.results[i][0].transcript;
            if (event.results[i].isFinal) finalTranscript += transcript + ' ';
            else interimTranscript += transcript;
        }
        this.contentInput.current.value = finalTranscript + interimTranscript;
    }
  } 

  render() {
    return (
      <ScrollableAnchor id="blog">
        <section className="content-section">
          <div className="container">
            <div className="content-section-heading text-center">
              <h3 className="text-secondary mb-0">Blog</h3>
              <h2 className="mb-5">Blogbejegyzéseim</h2>
              {
                this.props.token ? (
                    <a onClick={this.handleToggle} className="btn-lg btn-info fadeIn">Új bejegyzés</a>
                ) : (
                    <div></div>
                )
              }
            </div>

            <Searchbar
              searchEntry={this.searchEntry}
            />

            <div className="row">
              {
                this.paginate(this.state.entries, this.state.blogCount, 1).map((entry, index) => ( 
                  <Entry 
                    entry={entry}
                    index={index}
                    token={this.props.token}
                    onDeleteEntry={this.onDeleteEntry}
                  />
                )
              )}
            </div>
            {
              this.state.blogCount < this.state.entries.length
              ? (
                <a onClick={this.moreBlogs} className="text-secondary btn">További bejegyzések</a>
              ) : ( <div></div> )
            }         
          </div>

          <MDBContainer>
            <MDBModal isOpen={this.state.isModalOpen} toggle={this.handleToggle} size="lg">
              <MDBModalHeader toggle={this.handleToggle} className="ModalHeader text-info" >
                  Új bejegyzés hozzáadása
              </MDBModalHeader>
              <MDBModalBody>
                <Form>
                  <FormGroup>
                    <Label for="title">Cím</Label>
                      <input ref={this.titleInput} className="lead form-control" type="text" name="title" id="title" />
                  </FormGroup>
                  <FormGroup>
                    <label for="content">Tartalom</label>
                    <textarea  rows="7" ref={this.contentInput} className="lead form-control" type="text" name="content" id="content"/>
                    {
                      this.state.listening ? (
                        <div>
                          <a onClick={this.toggleListening} className="btn btn-danger btn-sm fadeIn">Stop</a>
                          <a onClick={this.toggleLanguage} className="btn btn-secondary btn-sm fadeIn">{this.state.language}</a>
                        </div>
                      ) : (        
                        <a onClick={this.toggleListening} className="btn btn-info btn-sm fadeIn">Beszédfelismerés</a>
                      )
                    }
                  </FormGroup>
                  <FormGroup>
                    <Label for="picbrowser">Kép tallózás</Label>
                    <CustomInput type="file" id="picbrowser" name="avatar" onChange={this.onFileChange}
                      accept="image/png, image/jpeg" {...(this.state.file.name ? { label: this.state.file.name } : { label: ''}) }
                    />
                  </FormGroup>                       
                </Form>
              </MDBModalBody>
              <MDBModalFooter>    
                <a onClick={this.saveEntry} className="btn btn-info fadeIn">Mentés</a>
              </MDBModalFooter>
            </MDBModal>
          </MDBContainer>

        </section>
      </ScrollableAnchor>
    )
  }
  
}

export default BlogListSection;
