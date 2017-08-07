const React = require('react');
const { Component } = require('react');

const { Text } = require('react-native');
const { connect } = require('remx/react-native');
const store = require('./store');

class MyContainer extends Component {
  render() {
    if (this.props.renderCountIncrement) {
      this.props.renderCountIncrement();
    }

    return this.renderText(this.props.printAge ? this.props.age : this.props.name);
  }

  renderText(txt) {
    return (
      <Text>{txt}</Text>
    );
  }
}

function mapStateToProps() {
  return {
    name: store.getters.getName(),
    age: store.getters.getAge()
  };
}

module.exports = connect(mapStateToProps)(MyContainer);
