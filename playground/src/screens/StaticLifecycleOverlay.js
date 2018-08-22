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
    this.listeners.push(Navigation.events().registerComponentDidAppearListener((event) => {
      event.event = 'componentDidAppear';
      this.setState({
        events: [...this.state.events, { ...event }]
      });
    }));
    this.listeners.push(Navigation.events().registerComponentDidDisappearListener((event) => {
      event.event = 'componentDidDisappear';
      this.setState({
        events: [...this.state.events, { ...event }]
      });
    }));
    this.listeners.push(Navigation.events().registerCommandCompletedListener(({ commandId }) => {
      this.setState({
        events: [...this.state.events, { event: 'commandCompleted', commandId }]
      });
    }));
    this.listeners.push(Navigation.events().registerNavigationButtonPressedListener(({ componentId, buttonId }) => {
      this.setState({
        events: [...this.state.events, { event: 'navigationButtonPressed', buttonId, componentId }]
      });
    }));
    this.listeners.push(Navigation.events().registerModalDismissedListener(({ componentId }) => {
      this.setState({
        events: [...this.state.events, { event: 'modalDismissed', componentId }]
      });
    }));
  }

  componentWillUnmount() {
    this.listeners.forEach(listener => listener.remove());
    this.listeners = [];
    alert('Overlay Unmounted');
  }

  renderEvent(event) {
    if (event.commandId) {
      return <Text style={styles.h2}>{`${event.commandId}`}</Text>;
    } else if (event.componentName) {
      return <Text style={styles.h2}>{`${event.event} | ${event.componentName}`}</Text>;
    } else if (event.buttonId) {
      return <Text style={styles.h2}>{`${event.event} | ${event.buttonId}`}</Text>;
    } else {
      return <Text style={styles.h2}>{`${event.event} | ${event.componentId}`}</Text>;
    }
  }

  render() {
    const events = this.state.events.map((event, idx) =>
      (
        <View key={`${event.componentId}${idx}`}>
          {this.renderEvent(event)}
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
        onPress={() => Navigation.dismissOverlay(this.props.componentId)}
      >
        <Text testID={testIDs.DISMISS_BUTTON} style={{ color: 'red', alignSelf: 'center' }}>X</Text>
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
    width: 35,
    height: 35,
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
