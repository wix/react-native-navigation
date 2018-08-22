const _ = require('lodash');
const React = require('react');
const { Component } = require('react');
const { View, Text, Platform } = require('react-native');
const { Navigation } = require('react-native-navigation');
const Button = require('./Button');
const testIDs = require('../testIDs');

class PushedScreen extends Component {
  static get options() {
    return {
      _statusBar: {
        visible: false,
        drawBehind: true
      },
      topBar: {
        testID: testIDs.TOP_BAR_ELEMENT,
        rightButtons: {
          id: 'singleBtn',
          text: 'single',
          testID: testIDs.TOP_BAR_BUTTON
        },
        rightButtonColor: 'red',
      },
      layout: {
        backgroundColor: '#f5fcff'
      },
      bottomTabs: {
        visible: false
      }
    };
  }

  constructor(props) {
    super(props);
    if (this.props.simulateLongRunningTask) {
      this.simulateLongRunningTask();
    }
    this.onClickPush = this.onClickPush.bind(this);
    this.onClickPop = this.onClickPop.bind(this);
    this.onClickPopPrevious = this.onClickPopPrevious.bind(this);
    this.onClickPopToFirstPosition = this.onClickPopToFirstPosition.bind(this);
    this.onClickPopToRoot = this.onClickPopToRoot.bind(this);
    this.onClickSetStackRoot = this.onClickSetStackRoot.bind(this);
    this.state = { disabled: false };
  }

  simulateLongRunningTask() {
    // tslint:disable-next-line
    for (let i = 0; i < Math.pow(2, 25); i++);
  }

  listeners = [];

  componentDidMount() {
    this.listeners.push(
      Navigation.events().registerComponentDidAppearListener((event) => {
        if (this.state.previewComponentId === event.componentId) {
          this.setState({ disabled: event.type === 'ComponentDidAppear' });
        }
      })
    );
    if (Platform.OS === 'ios') {
      // this.listeners.push(
      //   Navigation.events().registerNativeEventListener((name, params) => {
      //     if (name === 'previewContext') {
      //       const { previewComponentId } = params;
      //       this.setState({ previewComponentId });
      //     }
      //   })
      // );
    }
  }

  componentWillUnmount() {
    this.listeners.forEach(listener => listener.remove && listener.remove());
  }

  render() {
    const stackPosition = this.getStackPosition();
    return (
      <View style={styles.root}>
        <Text testID={testIDs.PUSHED_SCREEN_HEADER} style={styles.h1}>{`Pushed Screen`}</Text>
        <Text style={styles.h2}>{`Stack Position: ${stackPosition}`}</Text>
        <Button title='Push' testID={testIDs.PUSH_BUTTON} onPress={this.onClickPush} />
        {Platform.OS === 'ios' && (
          <Navigation.Element elementId='PreviewElement'>
            <Button testID={testIDs.SHOW_PREVIEW_BUTTON} onPress={this.onClickPush} onPressIn={this.onClickShowPreview} title='Push Preview' />
          </Navigation.Element>
        )}
        <Button title='Pop' testID={testIDs.POP_BUTTON} onPress={this.onClickPop} />
        <Button title='Pop Previous' testID={testIDs.POP_PREVIOUS_BUTTON} onPress={this.onClickPopPrevious} />
        <Button title='Pop To Root' testID={testIDs.POP_TO_ROOT} onPress={this.onClickPopToRoot} />
        <Button title='Set Stack Root' testID={testIDs.SET_STACK_ROOT_BUTTON} onPress={this.onClickSetStackRoot} />
        <Button title='Push and Wait for Render' testID={testIDs.PUSH_BUTTON_WAIT_FOR_RENDER} onPress={this.onClickPushWaitForRender} />
        {stackPosition > 2 && <Button title='Pop To Stack Position 1' testID={testIDs.POP_STACK_POSITION_ONE_BUTTON} onPress={this.onClickPopToFirstPosition} />}
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
      </View>
    );
  }

  onClickShowPreview = async () => {
    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        passProps: {
          stackPosition: this.getStackPosition() + 1,
          previousScreenIds: _.concat([], this.props.previousScreenIds || [], this.props.componentId)
        },
        options: {
          topBar: {
            title: {
              text: `Pushed ${this.getStackPosition() + 1}`
            }
          },
          animations: {
            push: {
              enable: false
            }
          },
          preview: {
            elementId: 'PreviewElement',
            height: 400,
            commit: true,
            actions: [{
              id: 'action-cancel',
              title: 'Cancel'
            }]
          }
        }
      }
    });
  }

  async onClickPush() {
    if (this.state.disabled) {
      return;
    }

    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        passProps: {
          stackPosition: this.getStackPosition() + 1,
          previousScreenIds: _.concat([], this.props.previousScreenIds || [], this.props.componentId)
        },
        options: {
          topBar: {
            title: {
              text: `Pushed ${this.getStackPosition() + 1}`
            }
          }
        }
      }
    });
  }

  async onClickPop() {
    await Navigation.pop(this.props.componentId);
  }

  async onClickPopPrevious() {
    await Navigation.pop(_.last(this.props.previousScreenIds));
  }

  async onClickPopToFirstPosition() {
    await Navigation.popTo(this.props.previousScreenIds[0]);
  }

  async onClickPopToRoot() {
    await Navigation.popToRoot(this.props.componentId);
  }

  async onClickSetStackRoot() {
    await Navigation.setStackRoot(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        passProps: {
          stackPosition: this.getStackPosition() + 1,
          previousScreenIds: _.concat([], this.props.previousScreenIds || [], this.props.componentId)
        },
        options: {
          animations: {
            setStackRoot: {
              enable: false
            }
          },
          topBar: {
            title: {
              text: `Pushed ${this.getStackPosition() + 1}`
            }
          }
        }
      }
    });
  }

  onClickPushWaitForRender = async () => {
    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        passProps: {
          stackPosition: this.getStackPosition() + 1,
          previousScreenIds: _.concat([], this.props.previousScreenIds || [], this.props.componentId),
          simulateLongRunningTask: true
        },
        options: {
          layout: {
            backgroundColor: 'transparent'
          },
          topBar: {
            title: {
              text: `Pushed ${this.getStackPosition() + 1}`
            }
          },
          animations: {
            push: {
              waitForRender: true
            }
          }
        }
      }
    });
  }

  getStackPosition() {
    return this.props.stackPosition || 1;
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

module.exports = PushedScreen;
