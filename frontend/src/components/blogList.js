import React from "react";
import ScrollableAnchor from "react-scrollable-anchor";
import axios from 'axios';
import Entry from './entry';
import { MDBContainer, MDBModal, MDBModalBody, MDBModalHeader, MDBModalFooter } from 'mdbreact';
import { CustomInput, Form, FormGroup, Label, Input, FieldGroup } from 'reactstrap';

import Searchbar from "./searchbar";


class BlogListSection extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      entries: [],
      blogCount: 4,
      isModalOpen: false,
      file: '',
    }

    this.paginate = this.paginate.bind(this);
    this.moreBlogs = this.moreBlogs.bind(this);
    this.saveEntry = this.saveEntry.bind(this);
    this.handleToggle = this.handleToggle.bind(this);
    this.onFileChange = this.onFileChange.bind(this);
    this.onDeleteEntry = this.onDeleteEntry.bind(this);
    this.searchEntry = this.searchEntry.bind(this);

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
      blogCount: this.state.blogCount + 4
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
                    <textarea ref={this.contentInput} className="lead form-control" type="text" name="content" id="content"/>    
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
