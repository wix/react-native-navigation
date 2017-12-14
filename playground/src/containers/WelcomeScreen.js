const React = require('react');
const { Component } = require('react');
const { View, Text, Button } = require('react-native');

const testIDs = require('../testIDs');

const Navigation = require('react-native-navigation');

class WelcomeScreen extends Component {
  static get navigationOptions() {
    return {
      topBar: {
        largeTitle: false
      }
    };
  }
  constructor(props) {
    super(props);
    this.onClickPush = this.onClickPush.bind(this);
    this.onClickShowModal = this.onClickShowModal.bind(this);
    this.onClickLifecycleScreen = this.onClickLifecycleScreen.bind(this);
    this.onClickPushOptionsScreen = this.onClickPushOptionsScreen.bind(this);
    this.onClickPushOrientationMenuScreen = this.onClickPushOrientationMenuScreen.bind(this);
    this.onClickBackHandler = this.onClickBackHandler.bind(this);
    this.onClickPushTopTabsScreen = this.onClickPushTopTabsScreen.bind(this);
  }

  render() {
    return (
      <View style={styles.root}>
        <Text testID={testIDs.WELCOME_SCREEN_HEADER} style={styles.h1}>{`React Native Navigation!`}</Text>
        <Button title="Switch to tab based app" testID={testIDs.TAB_BASED_APP_BUTTON} onPress={this.onClickSwitchToTabs} />
        <Button title="Switch to app with side menus" testID={testIDs.TAB_BASED_APP_SIDE_BUTTON} onPress={this.onClickSwitchToSideMenus} />
        <Button title="Push Lifecycle Screen" testID={testIDs.PUSH_LIFECYCLE_BUTTON} onPress={this.onClickLifecycleScreen} />
        <Button title="Push" testID={testIDs.PUSH_BUTTON} onPress={this.onClickPush} />
        <Button title="Push Options Screen" testID={testIDs.PUSH_OPTIONS_BUTTON} onPress={this.onClickPushOptionsScreen} />
        <Button title="Push Top Tabs screen" testID={testIDs.PUSH_TOP_TABS_BUTTON} onPress={this.onClickPushTopTabsScreen} />
        <Button title="Back Handler" testID={testIDs.BACK_HANDLER_BUTTON} onPress={this.onClickBackHandler} />
        <Button title="Show Modal" testID={testIDs.SHOW_MODAL_BUTTON} onPress={this.onClickShowModal} />
        <Button title="Show Redbox" testID={testIDs.SHOW_REDBOX_BUTTON} onPress={this.onClickShowRedbox} />
        <Button title="Orientation" testID={testIDs.ORIENTATION_BUTTON} onPress={this.onClickPushOrientationMenuScreen} />
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>
      </View>
    );
  }

  onClickSwitchToTabs() {
    Navigation.setRoot({
      bottomTabs: [
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is tab 1',
              myFunction: () => 'Hello from a function!'
            }
          }
        },
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is tab 2'
            }
          }
        }
      ]
    });
  }

  onClickSwitchToSideMenus() {
    Navigation.setRoot({
      bottomTabs: [
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is a side menu center screen tab 1'
            }
          }
        },
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is a side menu center screen tab 2'
            }
          }
        },
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is a side menu center screen tab 3'
            }
          }
        }
      ],
      sideMenu: {
        left: {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is a left side menu screen'
            }
          }
        },
        right: {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is a right side menu screen'
            }
          }
        }
      }
    });
  }

  async onClickPush() {
    await Navigation.push(this.props.containerId, {
      name: 'navigation.playground.PushedScreen'
    });
  }

  onClickLifecycleScreen() {
    Navigation.push(this.props.containerId, {
      name: 'navigation.playground.LifecycleScreen'
    });
  }

  async onClickShowModal() {
    await Navigation.showModal({
      container: {
        name: 'navigation.playground.ModalScreen'
      }
    });
  }

  onClickShowRedbox() {
    undefined();
  }

  onClickPushOptionsScreen() {
    Navigation.push(this.props.containerId, {
      name: 'navigation.playground.OptionsScreen'
    });
  }

  onClickPushTopTabsScreen() {
    Navigation.push(this.props.containerId, {
      topTabs: [
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is top tab 1'
            }
          }
        },
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is top tab 2'
            }
          }
        },
        {
          container: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is top tab 3'
            }
          }
        }
      ]
    });
  }

  onClickBackHandler() {
    Navigation.push(this.props.containerId, {
      name: 'navigation.playground.BackHandlerScreen'
    });
  }

  onClickPushOrientationMenuScreen() {
    Navigation.push(this.props.containerId, {
      name: 'navigation.playground.OrientationSelectScreen'
    });
  }
}

module.exports = WelcomeScreen;

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 30
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
};
