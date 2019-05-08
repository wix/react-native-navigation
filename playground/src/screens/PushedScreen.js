const _ = require('lodash');
const React = require('react');
const { BackHandler } = require('react-native');
const Navigation = require('../services/Navigation');
const Root = require('../components/Root');
const Button = require('../components/Button');
const {
  PUSHED_SCREEN_HEADER,
  TOP_BAR_BTN,
  PUSH_BTN,
  POP_BTN,
  PUSH_NO_ANIM_BTN,
  POP_TO_FIRST_SCREEN_BTN,
  POP_TO_ROOT_BTN,
  ADD_BACK_HANDLER,
  REMOVE_BACK_HANDLER,
  SET_STACK_ROOT_BUTTON
} = require('../testIDs');
const Screens = require('./Screens');

class PushedScreen extends React.Component {
  static options() {
    return {
      topBar: {
        testID: PUSHED_SCREEN_HEADER,
        title: {
          text: 'Pushed Screen'
        },
        rightButtons: {
          id: 'singleBtn',
          text: 'single',
          testID: TOP_BAR_BTN
        }
      }
    };
  }

  constructor(props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  navigationButtonPressed({ buttonId }) {
    if (buttonId === 'backPress') alert('back button clicked')
  }

  render() {
    const stackPosition = this.getStackPosition();
    return (
      <Root componentId={this.props.componentId} footer={`Stack Position: ${stackPosition}`}>
        <Button label='Push' testID={PUSH_BTN} onPress={this.push} />
        <Button label='Pop' testID={POP_BTN} onPress={this.pop} />
        <Button label='Push Without Animation' testID={PUSH_NO_ANIM_BTN} onPress={this.pushWithoutAnimations} />
        {stackPosition > 2 && <Button label='Pop to First Screen' testID={POP_TO_FIRST_SCREEN_BTN} onPress={this.popToFirstScreen} />}
        <Button label='Pop to Root' testID={POP_TO_ROOT_BTN} onPress={this.popToRoot} />
        <Button label='Add BackHandler' testID={ADD_BACK_HANDLER} onPress={this.addBackHandler} />
        <Button label='Remove BackHandler' testID={REMOVE_BACK_HANDLER} onPress={this.removeBackHandler} />
        <Button label='Set Stack Root' testID={SET_STACK_ROOT_BUTTON} onPress={this.setStackRoot} />
      </Root>
    );
  }

  push = () => Navigation.push(this, {
    component: {
      name: Screens.Pushed,
      passProps: this.createPassProps(),
      options: {
        topBar: {
          title: {
            text: `Pushed ${this.getStackPosition() + 1}`
          }
        }
      }
    }
  });

  pop = () => Navigation.pop(this);

  pushWithoutAnimations = () => Navigation.push(this, {
    component: {
      name: Screens.Pushed,
      passProps: this.createPassProps(),
      options: {
        animations: {
          push: { enabled: false },
          pop: { enabled: false }
        }
      }
    }
  });

  popToFirstScreen = () => Navigation.popTo(this.props.previousScreenIds[0]);

  popToRoot = () => Navigation.popToRoot(this);

  setStackRoot = () => Navigation.setStackRoot(this, [
    {
      component: {
        name: Screens.Pushed,
        passProps: {
          stackPosition: this.getStackPosition() + 1,
          previousScreenIds: _.concat([], this.props.previousScreenIds || [], this.props.componentId)
        },
        options: {
          animations: {
            setStackRoot: {
              enabled: false
            }
          },
          topBar: {
            title: {
              text: `Pushed ${this.getStackPosition() + 1} a`
            }
          }
        }
      }
    },
    {
      component: {
        name: Screens.Pushed,
        passProps: {
          stackPosition: this.getStackPosition() + 1,
          previousScreenIds: _.concat([], this.props.previousScreenIds || [], this.props.componentId)
        },
        options: {
          animations: {
            setStackRoot: {
              enabled: false
            }
          },
          topBar: {
            title: {
              text: `Pushed ${this.getStackPosition() + 1} b`
            }
          }
        }
      }
    }
  ]);

  addBackHandler = () => BackHandler.addEventListener('hardwareBackPress', this.backHandler);

  removeBackHandler = () => BackHandler.removeEventListener('hardwareBackPress', this.backHandler);

  backHandler = () => {
    this.setState({
      backPress: 'Back button pressed!'
    });
    return true;
  };

  createPassProps = () => {
    return {
      stackPosition: this.getStackPosition() + 1,
      previousScreenIds: _.concat([], this.props.previousScreenIds || [], this.props.componentId)
    }
  };
  getStackPosition = () => this.props.stackPosition || 1;
}

module.exports = PushedScreen;
