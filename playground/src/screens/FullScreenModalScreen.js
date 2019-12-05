const concat = require('lodash/concat');
const last = require('lodash/last');
const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('./../services/Navigation');
const Screens = require('./Screens');
const {
  PUSH_BTN,
  MODAL_SCREEN_HEADER,
  MODAL_BTN,
  DISMISS_MODAL_BTN
} = require('../testIDs');

class FullScreenModalScreen extends React.Component {
  static options() {
    return {
      statusBar: {
        visible: false
      },
      topBar: {
        testID: MODAL_SCREEN_HEADER,
        title: {
          text: 'Modal'
        }
      }
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId} footer={`Modal Stack Position: ${this.getModalPosition()}`}>
        <Button label='Show Modal' testID={MODAL_BTN} onPress={this.showModal} />
        <Button label='Dismiss Modal' testID={DISMISS_MODAL_BTN} onPress={this.dismissModal} />
        <Button label='Push' testID={PUSH_BTN} onPress={this.push} />
      </Root>
    );
  }

  showModal = () => {
    Navigation.showModal({
      component: {
        name: Screens.Modal,
        passProps: {
          modalPosition: this.getModalPosition() + 1,
          previousModalIds: concat([], this.props.previousModalIds || [], this.props.componentId)
        }
      }
    });
  }

  dismissModal = async () => await Navigation.dismissModal(this.props.componentId);

  push = () => Navigation.push(this, Screens.Pushed, {
    statusBar: {
      drawBehind: true,
      visible: false,
      backgroundColor: 'transparent',
      style: 'dark'
    },
    topBar: {
      drawBehind: true,
      visible: false
    }
  });

  getModalPosition = () => this.props.modalPosition || 1;

  getPreviousModalId = () => last(this.props.previousModalIds);
}
module.exports = FullScreenModalScreen;
