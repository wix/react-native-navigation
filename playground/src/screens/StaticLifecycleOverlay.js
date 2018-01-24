const React = require('react');
const { Component } = require('react');
const { View, Text } = require('react-native');
const Navigation = require('react-native-navigation');

class StaticLifecycleOverlay extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: 'nothing yet',
      events: []
    };
    Navigation.events().componentLifecycle(({ event, componentName, componentId }) => {
      console.log('guyca', `${event} ${componentName} [${componentId}]`);
      this.setState({
        events: [...this.state.events, { event, componentName, componentId }]
      });
    });
  }

  render() {
    const events = this.state.events.map((event) =>
      (
        <View key={event.componentId}>
          <Text style={styles.h2}>{`${event.event} | ${event.componentName}`}</Text>
        </View>
      ));
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Static Lifecycle Events`}</Text>
        <View style={styles.events}>
          {events}
        </View>
      </View>
    );
  }
}
module.exports = StaticLifecycleOverlay;

const styles = {
  root: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    height: 150,
    backgroundColor: '#c1d5e0ae',
    flexDirection: 'column'
  },
  events: {
    flexDirection: 'column',
    marginHorizontal: 2
  },
  h1: {
    fontSize: 14,
    textAlign: 'center',
    margin: 4
  },
  h2: {
    fontSize: 10
  }
};
