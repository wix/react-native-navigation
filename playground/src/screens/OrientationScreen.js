const React = require('react');
const { Component } = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const { Navigation } = require('react-native-navigation');
const Screens = require('./Screens');
const {
  DEFAULT_ORIENTATION_BTN,
  LANDSCAPE_PORTRAIT_ORIENTATION_BTN,
  LANDSCAPE_ORIENTATION_BTN,
  PORTRAIT_ORIENTATION_BTN
} = require('../testIDs');

class OrientationScreen extends Component {
  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Default' testID={DEFAULT_ORIENTATION_BTN} onPress={() => this.orientation('default')} />
        <Button label='Landscape and Portrait' testID={LANDSCAPE_PORTRAIT_ORIENTATION_BTN} onPress={() => this.orientation(['landscape', 'portrait'])} />
        <Button label='Portrait' testID={PORTRAIT_ORIENTATION_BTN} onPress={() => this.orientation('portrait')} />
        <Button label='Landscape' testID={LANDSCAPE_ORIENTATION_BTN} onPress={() => this.orientation(['landscape'])} />
      </Root>
    );
  }

  orientation(orientation) {
    Navigation.showModal({
      component: {
        name: Screens.OrientationDetect,
        passProps: {
          orientation
        }
      }
    });
  }
}

module.exports = OrientationScreen;
