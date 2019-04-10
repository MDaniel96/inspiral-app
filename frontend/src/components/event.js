import React from "react";
import axios from 'axios';
import { Input, Form, FormGroup, Button, Col } from 'reactstrap';

class Event extends React.Component {

    constructor(props) {
        super(props);

        var date = new Date(this.props.event.date);
        this.state = {
            date: {
                year: date.getFullYear(),
                month: date.getMonth(),
                day: date.getDay()
            },
            applyToggled: false,
        }

        this.toggleApply = this.toggleApply.bind(this);
        this.applyEvent = this.applyEvent.bind(this);

        this.emailInput = React.createRef();

        console.log("lat: " + this.props.event.lat + " long: " + this.props.event.lon);
    }

    getMonthName(monthNumber) {
        const monthNames = ["Jan", "Feb", "Mar", "April", "May", "June",
                            "July", "Aug", "Sep", "Oct", "Nov", "Dec"];
        return monthNames[monthNumber];
    }

    applyEvent() {
        this.toggleApply();
        if (this.emailInput.current.value)
            axios.post(`http://localhost:8080/training/${this.props.event.id}/apply/${this.emailInput.current.value}`,)
                .then(res => { console.log(res) }); 

                
    }

    toggleApply() {
        this.setState({
            applyToggled: !this.state.applyToggled
        })
    }

    render() {
        return (
            <div class="example-1 event_card fadeIn col-lg-6" key={`event_item${this.props.index}`}>
                <div class="event_wrapper map">
                    <div class="date">
                        <span class="day">{this.state.date.day}</span>
                        <span class="month">{this.getMonthName(this.state.date.month)}</span>
                        <span class="year">{this.state.date.year}</span>
                    </div>    
                    <iframe
                        title="location"
                        width="100%"
                        height="100%"
                        frameBorder="0"
                        scrolling="no"
                        marginHeight="0"
                        marginWidth="0"
                        src={`https://maps.google.com/maps?q=${this.props.event.lat},${this.props.event.lon}&hl=en;z=14&output=embed`}             
                    />
                    <div class="data">
                        <div class="content">
                            <span class="author">{this.props.event.trainername}</span>
                            <h1 class="event_title">{this.props.event.title}</h1>
                            <p class="event_text">{this.props.event.content}</p>
                            <label for={this.props.index} class="menu-button"><span></span></label>
                        </div>
                        <input type="checkbox" id={this.props.index} />
                        <ul class="menu-content">
                            <li>
                                {
                                    this.state.applyToggled ? (
                                        <Form className="fadeIn">
                                            <FormGroup row>
                                                <Col sm={{size: 6, offset: 0.5}} style={{marginTop:'2%'}}>
                                                    <input ref={this.emailInput} className="lead form-control" type="email" placeholder="Add meg az email címed"/>          
                                                </Col>
                                                <Button onClick={this.applyEvent} style={{marginTop:'2%'}} sm={2}>Mehet</Button>
                                            </FormGroup>
                                        </Form>
                                        ) : (
                                        <a onClick={this.toggleApply} className="fadeIn">Jelentkezem</a>
                                    )
                                }
                            </li>
                        </ul>
                    </div>
                </div>
            </div> 
        )
      }


}

export default Event;