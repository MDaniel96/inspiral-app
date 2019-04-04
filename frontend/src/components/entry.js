import React from "react";
import { MDBContainer, MDBBtn, MDBModal, MDBModalBody, MDBModalHeader, MDBModalFooter } from 'mdbreact';
import { MDBCard, MDBCardBody, MDBCardImage, MDBCol } from 'mdbreact';
import Comment from './comment';
import axios from 'axios';

class Entry extends React.Component {

    constructor(props) {
        super(props);

        this.commentInput = React.createRef();

        this.state = {
            isHovering: false,
            comments: [],
        };

        this.handleMouseHover = this.handleMouseHover.bind(this);
        this.SendComment = this.SendComment.bind(this);
        this.onDeleteComment = this.onDeleteComment.bind(this);
        this.DeleteEntry = this.DeleteEntry.bind(this);
    }

    refreshComments() {
        axios.get(`http://localhost:8080/entry/${this.props.entry.id}/comment`)
        .then(res => {
            this.setState({ comments: res.data });
        })
    }

    DeleteEntry() {
        this.props.onDeleteEntry(this.props.entry.id);
    }

    componentWillMount() {
        this.refreshComments();
    }

    onDeleteComment(id) {
        var newComments = this.state.comments.filter(e=>e.id!==id);
        this.setState({
            comments: newComments
        });

        axios({
            method: 'delete',
            url: `http://localhost:8080/admin/entry/comment/${id}`,
            headers: { 'Authorization': 'Bearer ' + this.props.token }
          })
          .then(res => { })
    }

    SendComment(e) {
        e.preventDefault();
        axios.post(`http://localhost:8080/entry/${this.props.entry.id}/comment`, 
            { 
                username: "noname",
                content: this.commentInput.current.value,
                date: new Date() 
            })
        .then(res => {          
            let { comments } = this.state;
            comments.push(res.data);
            this.setState({ comments: comments, }); 
        }); 
        this.commentInput.current.value = '';
    }

    handleMouseHover() {
        this.setState(this.toggleHoverState);
    }

    toggleHoverState(state) {
        return {
          isHovering: !state.isHovering,
        };
    }

    toggle = nr => () => {
        let modalNumber = 'modal' + nr
        this.setState({
          [modalNumber]: !this.state[modalNumber]
        });
    }

    render() {
        return (
            <div className="col-lg-6 top-buffer" key={`portfolio_item_${this.props.index}`}>
                <div onMouseEnter={this.handleMouseHover} onMouseLeave={this.handleMouseHover} className="portfolio-item" href="">
                <span className="caption">
                {
                    this.state.isHovering ? (
                        <span className="caption-content animated fadeIn">
                            {
                                this.props.token ? (
                                    <a className="btn btn-light top-right" onClick={this.DeleteEntry}>Törlés</a>
                                ) : (
                                    <div></div>
                                )
                            }
                            <h2>{this.props.entry.title}</h2>
                            <p className="mb-0">{this.props.entry.content.substring(0,200) + "..."}</p>
                            <MDBBtn onClick={this.toggle(4)} color="secondary btn-sm mt-4" onClick={this.toggle(4)}>Olvass tovább</MDBBtn>
                        </span> ) : ( <div></div> )    
                }

                </span> 
                <img className="img-fluid animated fadeIn" src={`data:image/jpeg;base64,${this.props.entry.image}`} alt="" /> 
                </div> 


                <MDBContainer>
                    <MDBModal isOpen={this.state.modal4} toggle={this.toggle(4)} size="lg">
                        <MDBModalHeader toggle={this.toggle(4)} className="ModalHeader" >
                            BLOG
                        </MDBModalHeader>
                        <MDBModalBody>
                            <MDBCol>
                                <MDBCard className="ModalCard">
                                    <MDBCardImage className="img-fluid ModalImg" waves src={`data:image/jpeg;base64,${this.props.entry.image}`} />
                                    <MDBCardBody>
                                        <h2>
                                            {this.props.entry.title}
                                            <div className="AuthorNote">
                                                {this.props.entry.trainername},&nbsp; 
                                                {this.props.entry.date}
                                            </div>
                                        </h2>
                                        <p className="lead mb-5">
                                            {this.props.entry.content}
                                        </p>
                                    </MDBCardBody>
                                    
                                </MDBCard>
                            </MDBCol>
                        </MDBModalBody>
                        <MDBModalFooter>    
                            <div className="detailBox">
                                <h5 className="titleBox">Kommentek</h5>
                            
                                <div className="actionBox">
                                    <ul className="commentList">
                                        {
                                            this.state.comments.map((comment, index) => {
                                                return (
                                                    <Comment
                                                        comment={comment}
                                                        index={index}
                                                        token={this.props.token}
                                                        onDeleteComment={this.onDeleteComment}
                                                    />
                                                )
                                            })
                                        }
                                    </ul>

                                    <form role="form" onSubmit={this.SendComment}>
                                        <div className="form-text">
                                            <textarea ref={this.commentInput} className="lead form-control" type="text" placeholder="Írj megjegyzést" />
                                        </div>
                                        <div className="form-button">
                                            <button className="btn btn-secondary">Küldés</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </MDBModalFooter>
                </MDBModal>
            </MDBContainer>


            </div>
        )
      }


}

export default Entry;