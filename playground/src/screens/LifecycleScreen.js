const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button');
const Navigation = require('../services/Navigation');
const Screens = require('./Screens');
const {
  PUSH_TO_TEST_DID_DISAPPEAR_BTN,
  DISMISS_MODAL_BTN,
  SCREEN_POPPED_BTN,
  POP_BTN
} = require('../testIDs');

class LifecycleScreen extends React.Component {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Lifecycle Screen'
        }
      }
    }
  }
  state = {
    text: 'nothing yet'
  };

  constructor(props) {
    super(props);
    this.showUnmountAndDisappearAlerts = true;
    Navigation.events().bindComponent(this);
  }

  componentDidAppear() {
    this.setState({ text: 'didAppear' });
  }

  componentDidDisappear() {
    this.showUnmountAndDisappearAlerts && alert('didDisappear'); // eslint-disable-line no-alert
  }

  componentWillUnmount() {
    setTimeout(() => {
      this.showUnmountAndDisappearAlerts && alert('componentWillUnmount'); // eslint-disable-line no-alert
    }, 100); 
  }

  navigationButtonPressed(id) {
    alert(`navigationButtonPressed: ${id}`); // eslint-disable-line no-alert
  }

  render() {
    return (
      <Root componentId={this.props.componentId} footer={this.state.text}>
        <Button label='Push to test didDisappear' testID={PUSH_TO_TEST_DID_DISAPPEAR_BTN} onPress={this.push} />
        {!this.props.isModal && <Button label='Screen popped events' testID={SCREEN_POPPED_BTN} onPress={this.screenPoppedEvent} />}
        {this.renderCloseButton()}
      </Root>
    );
  }

  renderCloseButton = () => this.props.isModal ?
        <Button label='Dismiss' testID={DISMISS_MODAL_BTN} onPress={this.dismiss} /> :
        <Button label='Pop' testID={POP_BTN} onPress={this.pop} />;

  push = () => Navigation.push(this, Screens.Pushed);
  screenPoppedEvent = async () => {
    this.showUnmountAndDisappearAlerts = false;
    const unregister = Navigation.events().registerScreenPoppedListener((event) => {
      alert('Screen popped event')
      unregister.remove();
    });
    await Navigation.pop(this);
  }
  pop = () => Navigation.pop(this);
  dismiss = () => Navigation.dismissModal(this);
}
module.exports = LifecycleScreen;
