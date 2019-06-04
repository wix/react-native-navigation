const React = require('react');

const { Text, Button, TouchableOpacity, View, Alert, Platform } = require('react-native');
const { Navigation } = require('react-native-navigation');
const { component } = require('../commons/Layouts');
const Screens = require('./Screens');

const {
  OVERLAY_ALERT_HEADER,
  DISMISS_BTN,
} = require('../testIDs');

const styles = {
  root: {
    position: 'absolute',
    backgroundColor: 'blue',
    alignItems: 'center',
    height: 400,
    bottom: 0,
    left: 0,
    right: 0
  },
  title: {
    marginTop: 8
  }
};

class OverlayWithNestedTouchables extends React.PureComponent {
  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.title} testID={OVERLAY_ALERT_HEADER}>Test view nested touchables</Text>
        <Button title='Dismiss' testID={DISMISS_BTN} onPress={this.dismiss} />

        <TouchableOpacity style={{ backgroundColor: 'red' }} onPress={() => alert('Touch parent')}>
          <View style={{ height: 300, width: 300, alignItems: 'center', justifyContent: 'center' }}>
            <TouchableOpacity style={{ margin: 5, height: 90, width: 90, backgroundColor: 'orange', alignItems: 'center', justifyContent: 'center' }} onPress={() => alert('Touch child')}>
              <Text>favourites</Text>
            </TouchableOpacity>

            <TouchableOpacity style={{ margin: 5, height: 90, width: 90, backgroundColor: 'orange', alignItems: 'center', justifyContent: 'center' }} onPress={() => alert('Touch child')}>
              <Text>notifications</Text>
            </TouchableOpacity>

            <TouchableOpacity style={{ margin: 5, height: 90, width: 90, backgroundColor: 'orange', alignItems: 'center', justifyContent: 'center' }} onPress={() => alert('Touch child')}>
              <Text>settings</Text>
            </TouchableOpacity>

          </View>
        </TouchableOpacity>
      </View>
    );
  }

  dismiss = () => Navigation.dismissOverlay(this.props.componentId);
}

module.exports = OverlayWithNestedTouchables;
