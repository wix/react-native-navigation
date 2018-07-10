const _ = require('lodash');

const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

class ModalScreen extends Component {
  static get options() {
    return {
      statusBar: {
        visible: false,
        drawBehind: true,
        backgroundColor: 'transparent'
      },
      layout: {
        orientation: ['portrait'],
        backgroundColor: '#f5fcff'
      },
      _animations: {
        showModal: {
          waitForRender: true
        }
      }
    };
  }

  constructor(props) {
    super(props);
    // this.simulateLongRunningTask();
    this.onClickShowModal = this.onClickShowModal.bind(this);
    this.onClickDismissModal = this.onClickDismissModal.bind(this);
    this.onClickDismissPreviousModal = this.onClickDismissPreviousModal.bind(this);
    this.onClickDismissUnknownModal = this.onClickDismissUnknownModal.bind(this);
    this.onClickDismissAllPreviousModals = this.onClickDismissAllPreviousModals.bind(this);
    this.onClickDismissFirstInStack = this.onClickDismissFirstInStack.bind(this);
    this.onClickDismissAllModals = this.onClickDismissAllModals.bind(this);
    this.onClickPushScreen = this.onClickPushScreen.bind(this);
    this.onShowModalWithDeepStack = this.onShowModalWithDeepStack.bind(this);
    this.onClickModalLifecycle = this.onClickModalLifecycle.bind(this);
  }

  simulateLongRunningTask = () => {
    // tslint:disable-next-line
    for (let i = 0; i < Math.pow(2, 25); i++);
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1} testID={testIDs.MODAL_SCREEN}>{`Modal Screen`}</Text>
        <Text style={styles.footer}>{`Modal Stack Position: ${this.getModalPosition()}`}</Text>
        <Button title='Show Modal' testID={testIDs.SHOW_MODAL_BUTTON} onPress={this.onClickShowModal} />
        <Button title='Dismiss Modal' testID={testIDs.DISMISS_MODAL_BUTTON} onPress={this.onClickDismissModal} />
        <Button title='Dismiss Unknown Modal' testID={testIDs.DISMISS_UNKNOWN_MODAL_BUTTON} onPress={this.onClickDismissUnknownModal} />
        <Button title='Dismiss All Modals' testID={testIDs.DISMISS_ALL_MODALS_BUTTON} onPress={this.onClickDismissAllModals} />
        <Button title='Test Modal Lifecycle' testID={testIDs.MODAL_LIFECYCLE_BUTTON} onPress={this.onClickModalLifecycle} />
        <Button title='Push screen' testID={testIDs.PUSH_BUTTON} onPress={this.onClickPushScreen} />
        <Button title='Show Modal With Stack' testID={testIDs.MODAL_WITH_STACK_BUTTON} onPress={this.onShowModalWithDeepStack} />
        {this.getPreviousModalId() ? (<Button title='Dismiss Previous Modal' testID={testIDs.DISMISS_PREVIOUS_MODAL_BUTTON}
          onPress={this.onClickDismissPreviousModal} />) : undefined}
        {this.props.previousModalIds ? (<Button title='Dismiss ALL Previous Modals' testID={testIDs.DISMISS_ALL_PREVIOUS_MODAL_BUTTON}
          onPress={this.onClickDismissAllPreviousModals} />) : undefined}
        {this.props.previousModalIds ? (<Button title='Dismiss First In Stack' testID={testIDs.DISMISS_FIRST_MODAL_BUTTON}
          onPress={this.onClickDismissFirstInStack} />) : undefined}
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
      </View>
    );
  }

  onClickShowModal() {
    Navigation.showModal({
      component: {
        name: 'navigation.playground.ModalScreen',
        passProps: {
          modalPosition: this.getModalPosition() + 1,
          previousModalIds: _.concat([], this.props.previousModalIds || [], this.props.componentId)
        },
        options: {
          animated: false
        }
      }
    });
  }

  async onClickDismissModal() {
    await Navigation.dismissModal(this.props.componentId);
  }

  onClickDismissPreviousModal() {
    Navigation.dismissModal(this.getPreviousModalId());
  }

  onClickDismissUnknownModal() {
    Navigation.dismissModal('unknown');
  }

  onClickDismissAllPreviousModals() {
    _.forEach(this.props.previousModalIds, (id) => Navigation.dismissModal(id));
  }

  onClickDismissFirstInStack() {
    Navigation.dismissModal(_.head(this.props.previousModalIds));
  }

  onClickDismissAllModals() {
    Navigation.dismissAllModals();
  }

  onClickModalLifecycle() {
    Navigation.showModal({
      component: {
        name: 'navigation.playground.LifecycleScreen',
        passProps: {
          isModal: true,
        },
        options: {
          animated: false
        }
      }
    });
  }

  onClickPushScreen() {
    Navigation.push(this.props.componentId, {
      component: {
        name: `navigation.playground.PushedScreen`,
        passProps: {
          text: 'Pushed from modal'
        }
      }
    });
  }

  onShowModalWithDeepStack() {
    Navigation.showModal({
      stack: {
        children: [
          {
            component: {
              name: `navigation.playground.TextScreen`,
              passProps: {
                text: 'Screen 1'
              }
            }
          },
          {
            component: {
              name: `navigation.playground.TextScreen`,
              passProps: {
                text: 'Screen 2'
              }
            }
          }
        ]
      }
    });
  }

  getModalPosition() {
    return (this.props.modalPosition || 1);
  }

  getPreviousModalId() {
    return _.last(this.props.previousModalIds);
  }
}

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10
  },
  h2: {
    fontSize: 12,
    textAlign: 'center',
    margin: 10
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
};

module.exports = ModalScreen;
