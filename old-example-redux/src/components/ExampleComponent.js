import React, {Component} from 'react';
import {
  Text
} from 'react-native';

export default class ExampleComponent extends Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <Text>I am a regular non-redux aware component</Text>
    );
  }
}
