import React from "react";

class Searchbar extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            searchInput: ''
        }

        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({searchInput: event.target.value});
        this.props.searchEntry(event.target.value);
    }

    render() {
        return (
            <div className="searchContainer">
                <input type="text" placeholder="Keresés..." value={this.state.searchInput} onChange={this.handleChange} />
                <div className="search"/>
            </div>            
        );
    }
}

export default Searchbar;