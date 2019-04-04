const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Colors = require('../commons/Colors');
const Navigation = require('../services/Navigation');
const Screens = require('./Screens');
const {
  LEFT_SIDE_MENU_PUSH_BTN,
  CLOSE_LEFT_SIDE_MENU_BTN,
  LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN
} = require('../testIDs');

class SideMenuLeftScreen extends React.Component {
  render() {
    return (
      <Root componentId={this.props.componentId} style={{ backgroundColor: Colors.background }}>
        <Button label='Push' testID={LEFT_SIDE_MENU_PUSH_BTN} onPress={this.push} />
        <Button label='Push and Close' testID={LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN} onPress={this.pushAndClose} />
        <Button label='Close' testID={CLOSE_LEFT_SIDE_MENU_BTN} onPress={this.close} />
      </Root>
    );
  }

  push = () => Navigation.push('SideMenuCenter', Screens.Pushed);

  pushAndClose = () => Navigation.push('SideMenuCenter', {
    component: {
      name: Screens.Pushed,
      options: {
        sideMenu: {
          left: {
            visible: false
          }
        }
      }
    }
  });

  close = () => Navigation.mergeOptions(this, {
    sideMenu: {
      left: { visible: false }
    }
  });
}

module.exports = SideMenuLeftScreen;
