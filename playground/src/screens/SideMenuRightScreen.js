const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Colors = require('../commons/Colors');
const Navigation = require('../services/Navigation');
const {
  CLOSE_RIGHT_SIDE_MENU_BTN
} = require('../testIDs');

class SideMenuRightScreen extends React.Component {
  render() {
    return (
      <Root componentId={this.props.componentId} style={{backgroundColor: Colors.background}}>
        <Button label='Close' testID={CLOSE_RIGHT_SIDE_MENU_BTN} onPress={this.close} />
      </Root>
    );
  }

  close = () => Navigation.mergeOptions(this, {
    sideMenu: {
      right: { visible: false }
    }
  });
}

module.exports = SideMenuRightScreen;
