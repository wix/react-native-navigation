const React = require('react');
const { Component } = require('react');
const { View, Text, TouchableOpacity } = require('react-native');
const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

class StaticLifecycleOverlay extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: 'nothing yet',
      events: []
    };
    this.listeners = [];
    this.listeners.push(Navigation.events().registerComponentDidAppearListener((componentId, componentName) => {
      this.setState({
        events: [...this.state.events, { event: 'componentDidAppear', componentId, componentName }]
      });
    }));
    this.listeners.push(Navigation.events().registerComponentDidDisappearListener((componentId, componentName) => {
      this.setState({
        events: [...this.state.events, { event: 'componentDidDisappear', componentId, componentName }]
      });
    }));
    this.listeners.push(Navigation.events().registerCommandListener((name, params) => {
      // console.log('RNN', `name: ${JSON.stringify(name)}`);
      // console.log('RNN', `params: ${JSON.stringify(params)}`);
    }));
  }

  componentWillUnmount() {
    this.listeners.forEach(listener => listener.remove());
    this.listeners = [];
    alert('Overlay Unmounted');
  }

  render() {
    const events = this.state.events.map((event, idx) =>
      (
        <View key={`${event.componentId}${idx}`}>
          <Text style={styles.h2}>{`${event.event} | ${event.componentName}`}</Text>
        </View>
      ));
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Static Lifecycle Events Overlay`}</Text>
        <View style={styles.events}>
          {events}
        </View>
        {this.renderDismissButton()}
      </View>
    );
  }

  renderDismissButton = () => {
  return (
    <TouchableOpacity
      style={styles.dismissBtn}
      testID={testIDs.DISMISS_BUTTON}
      onPress={() => Navigation.dismissOverlay(this.props.componentId)}
    >
      <Text style={{ color: 'red', alignSelf: 'center' }}>X</Text>
    </TouchableOpacity>
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
  dismissBtn: {
    position: 'absolute',
    width: 25,
    height: 25,
    backgroundColor: 'white',
    justifyContent: 'center'
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
