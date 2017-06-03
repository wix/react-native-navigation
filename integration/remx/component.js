import React, { Component } from 'react';
import { Text } from 'react-native';

import { connect } from 'remx/react-native';

import * as store from './store';

class MyContainer extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    if (this.props.renderCountIncrement) {
      this.props.renderCountIncrement();
    }
    if (this.props.printAge) {
      return this.renderText(this.props.age);
    } else {
      return this.renderText(this.props.name);
    }
  }

  renderText(txt) {
    return (
      <Text>{txt}</Text>
    );
  }
}

function mapStateToProps(ownProps) {
  return {
    name: store.getters.getName(),
    age: store.getters.getAge()
  };
}

export default connect(mapStateToProps)(MyContainer);
