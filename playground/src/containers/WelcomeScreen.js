const React = require('react');
const { Component } = require('react');
const { View, Text, Button } = require('react-native');
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
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`React Native Navigation!`}</Text>
        <Button title="Switch to tab based app" onPress={this.onClickSwitchToTabs} />
        <Button title="Switch to app with side menus" onPress={this.onClickSwitchToSideMenus} />
        <Button title="Push Lifecycle Screen" onPress={this.onClickLifecycleScreen} />
        <Button title="Push" onPress={this.onClickPush} />
        <Button title="Push Options Screen" onPress={this.onClickPushOptionsScreen} />
        <Button title="Back Handler" onPress={this.onClickBackHandler} />
        <Button title="Show Modal" onPress={this.onClickShowModal} />
        <Button title="Show Redbox" onPress={this.onClickShowRedbox} />
        <Button title="Orientation" onPress={this.onClickPushOrientationMenuScreen} />
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
