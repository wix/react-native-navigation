const React = require('react');

const { Text, Button, View, Alert, Platform } = require('react-native');
const { Navigation } = require('react-native-navigation');
const { component } = require('../commons/Layouts');
const Screens = require('./Screens');

const {
  OVERLAY_ALERT_HEADER,
  DISMISS_BTN,
  SET_ROOT_BUTTON,
  SET_INTERCEPT_TOUCH
} = require('../testIDs');

class OverlayAlert extends React.PureComponent {
  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.title} testID={OVERLAY_ALERT_HEADER}>Test view</Text>
        <Button title='Dismiss' testID={DISMISS_BTN} onPress={this.dismiss} />
        <Button title='Set Root' testID={SET_ROOT_BUTTON} onPress={this.setRoot} />
        <Button title='Set Intercept touch' testID={SET_INTERCEPT_TOUCH} onPress={this.setInterceptTouch} />
      </View>
    );
  }

  dismiss = () => Navigation.dismissOverlay(this.props.componentId);
  setRoot = () => Navigation.setRoot({ root: component(Screens.Pushed) });

  setInterceptTouch = () => Navigation.mergeOptions(this.props.componentId, {
    overlay: {
      interceptTouchOutside: false
    }
  });
}

const styles = {
  root: {
    position: 'absolute',
    backgroundColor: 'green',
    alignItems: 'center',
    height: 160,
    bottom: 0,
    left: 0,
    right: 0
  },
  title: {
    marginTop: 8
  }
};

module.exports = OverlayAlert;
