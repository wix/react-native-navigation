const React = require('react');
const { Component } = require('react');

const { Text } = require('react-native');
const { connect } = require('react-redux');
const store = require('./MyStore');

class MyComponent extends Component {
  render() {
    if (this.props.renderCountIncrement) {
      this.props.renderCountIncrement();
    }

    return this.renderText(this.props.printAge ? this.props.age : this.props.name);
  }

  renderText(txt) {
    return <Text>{txt}</Text>;
  }
}

function mapStateToProps(state) {
  return {
    name: store.selectors.getName(state),
    age: store.selectors.getAge(state),
  };
}

module.exports = connect(mapStateToProps)(MyComponent);
