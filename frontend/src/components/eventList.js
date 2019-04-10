import React from "react";
import ScrollableAnchor from "react-scrollable-anchor";
import Event from './event';
import axios from 'axios';
import Entry from './entry';



class EventListSection extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
        events: [],
        eventCount: 4,
    }

    this.paginate = this.paginate.bind(this);
    this.moreEvents = this.moreEvents.bind(this);
  }

  componentWillMount() {
      this.refreshEvents();
  }

  refreshEvents() {
    axios.get(`http://localhost:8080/training/all`)
    .then(res => {
        this.setState({ events: res.data });
    })
  }

  moreEvents() {
      this.setState({
          eventCount: this.state.eventCount + 2
      })
  }

  paginate(array, page_size, page_number) {
    --page_number;
    return array.slice(page_number * page_size, (page_number + 1) * page_size);
  }

  render() {
    return (
      <ScrollableAnchor id="trainings">
        <section className="content-section">
          <div className="container">
            <div className="content-section-heading text-center">
              <h3 className="text-secondary mb-0">Események</h3>
              <h2 className="mb-5">Tréningeim</h2>
            </div>

            <div className="row">
                {
                this.paginate(this.state.events, this.state.eventCount, 1).map((event, index) => ( 
                    <Event 
                        event={event}
                        index={index}
                        token={this.props.token}
                    />
                    )
                )}
            </div>
            {
              this.state.eventCount < this.state.events.length
              ? (
                <a onClick={this.moreEvents} className="text-secondary btn">További események</a>
              ) : ( <div></div> )
            } 

          </div>

        </section>
      </ScrollableAnchor>
    )
  }
  
}

export default EventListSection;
