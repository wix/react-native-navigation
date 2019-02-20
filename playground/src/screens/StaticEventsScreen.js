const React = require('react');
const Navigation = require('../services/Navigation');
const Root = require('../components/Root');
const Button = require('../components/Button');
const {
  PUSH_BTN,
  POP_BTN,
  STATIC_EVENTS_OVERLAY_BTN
} = require('../testIDs');
const Screens = require('./Screens');

class StaticEventsScreen extends React.Component {
  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Show Overlay' testID={STATIC_EVENTS_OVERLAY_BTN} onPress={this.showEventsOverlay} />
        <Button label='Push' testID={PUSH_BTN} onPress={this.push} />
        <Button label='Pop' testID={POP_BTN} onPress={this.pop} />
      </Root>
    );
  }

  showEventsOverlay = () => Navigation.showOverlay(Screens.EventsOverlay, {
    overlay: {
      interceptTouchOutside: false
    }
  });
  push = () => Navigation.push(this, Screens.Pushed);
  pop = () => Navigation.pop(this);
}

module.exports = StaticEventsScreen;
