import React, { Component } from 'react';
import './App.css';
import axios from 'axios';

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            temperature: '',
        };
    }


  componentDidMount() {
    axios
      .get('/status')
      .then(response => {console.log(response)})
      .catch(error => {console.log(error)});
  };

  handleChange = event => {
    this.setState({[event.target.name]: event.target.value});
  };

  handleClick = id => {
    console.log(id);
    axios
      .post('/status', {id: id})
      .then(response => {console.log(response.data)})
      .catch(error => {console.log(error)});
  };

  handleKeyPress = event => {
    if (event.key === 'Enter') {
      console.log("Enter pressed...")
      axios
        .post('/temperature', { temperature: parseInt(this.state.temperature) })
        .then(response => console.log(response))
        .catch(error => console.log(error));
    }
  };

  render() {
    return (
      <div className="App">
          Change temperature: {' '}
        <input
          type="number"
          name="temperature"
          value={this.state.temperature}
          onChange={this.handleChange}
          onKeyPress={this.handleKeyPress}
        />
        {' °C'}
        <div className="buttons">
            Lightswitches: {' '}
        <button onClick={() => this.handleClick(1)}>Light 1</button>{' '}
        <button onClick={() => this.handleClick(2)}>Light 2</button>{' '}
        <button onClick={() => this.handleClick(3)}>Light 3</button>{' '}
        <button onClick={() => this.handleClick(4)}>Light 4</button>{' '}
        <button onClick={() => this.handleClick(5)}>Light 5</button>{' '}
        <button onClick={() => this.handleClick(6)}>Light 6</button>{' '}
        <button onClick={() => this.handleClick(7)}>Light 7</button>{' '}
        <button onClick={() => this.handleClick(8)}>Light 8</button>{' '}
        <button onClick={() => this.handleClick(9)}>Light 9</button>{' '}
        </div>
      </div>
    );
  };
}

export default App;
