import React, { Component } from "react";
import "./styles.css";

class Input extends Component {
  state = {
    inputValue: "",
  };

  handleChange = (event) => {
    console.log(event.target.selectionStart);
    this.setState({ inputValue: event.target.value });
    if (this.props.onChange) this.props.onChange(this.state.inputValue);
  };

  render() {
    const { type, size, maxLength, placeholder } = this.props;

    return (
      <input
        type={type}
        value={this.state.inputValue}
        name="input-form"
        onChange={this.handleChange}
        className={`input ${size}`}
        maxLength={maxLength}
        placeholder={placeholder}
      />
    );
  }
}

export default Input;
