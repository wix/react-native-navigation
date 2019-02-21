const _ = require('lodash');
const React = require('react');
const { Platform, Text } = require('react-native');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('./../services/Navigation');
const Screens = require('./Screens');
const {
  DECK_TRANSITION_BUTTON,
  PUSH_BTN,
  MODAL_SCREEN_HEADER,
  MODAL_BTN,
  DISMISS_MODAL_BTN,
  DISMISS_UNKNOWN_MODAL_BTN,
  MODAL_LIFECYCLE_BTN,
  DISMISS_PREVIOUS_MODAL_BTN,
  DISMISS_ALL_PREVIOUS_MODAL_BTN,
  DISMISS_ALL_MODALS_BTN,
  DISMISS_FIRST_MODAL_BTN,
} = require('../testIDs');

class ModalScreen extends React.Component {
  static options() {
    return {
      statusBar: {
        visible: false,
        drawBehind: true,
        backgroundColor: 'transparent'
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
    const { isDeck } = this.props;

    const dismissText = isDeck !== undefined ? `DismissDeck:${isDeck}` : 'Dismiss Modal';
    const deckText = isDeck !== undefined ? `DeckTransition:${isDeck}` : 'DeckTransition';

    return (
      <Root componentId={this.props.componentId} footer={`Modal Stack Position: ${this.getModalPosition()}`}>
        {isDeck !== undefined ? (
          <Text>I am a deck modal</Text>
        ) : null}
        <Button label='Show Modal' testID={MODAL_BTN} onPress={this.showModal} />
        <Button label={dismissText} testID={DISMISS_MODAL_BTN} onPress={this.dismissModal} />
        <Button label='Dismiss Unknown Modal' testID={DISMISS_UNKNOWN_MODAL_BTN} onPress={this.dismissUnknownModal} />
        <Button label='Modal Lifecycle' testID={MODAL_LIFECYCLE_BTN} onPress={this.modalLifecycle} />
        {this.getPreviousModalId() && (<Button label='Dismiss Previous Modal' testID={DISMISS_PREVIOUS_MODAL_BTN} onPress={this.dismissPreviousModal} />)}
        {this.props.previousModalIds && (<Button label='Dismiss All Previous Modals' testID={DISMISS_ALL_PREVIOUS_MODAL_BTN} onPress={this.dismissAllPreviousModals} />)}
        <Button label='Dismiss All Modals' testID={DISMISS_ALL_MODALS_BTN} onPress={this.dismissAllModals} />
        {this.props.previousModalIds && (<Button label='Dismiss First Modal' testID={DISMISS_FIRST_MODAL_BTN} onPress={this.dismissFirstModal} />)}
        <Button label='Push screen' testID={PUSH_BTN} onPress={this.pushScreen} />
        <Button label={deckText} testID={DECK_TRANSITION_BUTTON} onPress={this.onDeckTransition} />
        <Button label='DeckTransition:NO-SWIPE' onPress={this.onDeckTransitionNoSwipe} />
      </Root>
    );
  }

  showModal = () => {
    Navigation.showModal({
      component: {
        name: Screens.Modal,
        passProps: {
          modalPosition: this.getModalPosition() + 1,
          previousModalIds: _.concat([], this.props.previousModalIds || [], this.props.componentId)
        }
      }
    });
  }

  dismissModal = async () => await Navigation.dismissModal(this.props.componentId);
  
  onDeckTransition = () => this.deckTransition(true);
  onDeckTransitionNoSwipe = () => this.deckTransition(false);

  deckTransition = (enableDeckSwipeToDismiss) => {
    if (Platform.OS === 'android') {
      Navigation.mergeOptions(this, {
        layout: {
          backgroundColor: '#666666'
        }
      });
    }

    Navigation.showModal({
      component: {
        name: Screens.Modal,
        passProps: {
          isDeck: this.props.isDeck ? this.props.isDeck + 1 : 1
        },
        options: {
          modalPresentationStyle: 'overCurrentContext', // Required on android to force rendering of previous screen behind current modal
          animations: {
            showModal: {
              enable: true,
              enableDeck: true,
              enableDeckSwipeToDismiss,
              deckPresentDuration: 0.5,
              deckDismissDuration: 0.5
            },
            dismissModal: {
              enable: true,
              enableDeck: true,
              deckPresentDuration: 0.5,
              deckDismissDuration: 0.5
            }
          }
        }
      }
    });
  };

  dismissPreviousModal = () => Navigation.dismissModal(this.getPreviousModalId());

  dismissUnknownModal = () => Navigation.dismissModal('unknown');

  dismissAllPreviousModals = () => _.forEach(this.props.previousModalIds, (id) => Navigation.dismissModal(id));

  dismissFirstModal = () => Navigation.dismissModal(_.head(this.props.previousModalIds));

  dismissAllModals = () => Navigation.dismissAllModals();

  modalLifecycle = () => Navigation.showModal({
    component: {
      name: Screens.Lifecycle,
      passProps: { isModal: true }
    }
  });

  pushScreen = () => Navigation.push(this, Screens.Pushed);

  getModalPosition = () => this.props.modalPosition || 1;

  getPreviousModalId = () => _.last(this.props.previousModalIds);
}
module.exports = ModalScreen;
