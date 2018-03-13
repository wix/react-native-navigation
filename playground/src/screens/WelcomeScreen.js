const React = require('react');
const { Component } = require('react');
const { View, Text, Button, Platform } = require('react-native');

const testIDs = require('../testIDs');

const { Navigation } = require('react-native-navigation');

class WelcomeScreen extends Component {
  static get options() {
    return {
      topBar: {
        largeTitle: false,
        drawBehind: true,
        visible: false,
        animate: false
      }
    };
  }
  constructor(props) {
    super(props);
    this.onClickPush = this.onClickPush.bind(this);
    this.onClickShowModal = this.onClickShowModal.bind(this);
    this.onClickLifecycleScreen = this.onClickLifecycleScreen.bind(this);
    this.onClickPushOptionsScreen = this.onClickPushOptionsScreen.bind(this);
    this.onClickPushExternalComponent = this.onClickPushExternalComponent.bind(this);
    this.onClickPushOrientationMenuScreen = this.onClickPushOrientationMenuScreen.bind(this);
    this.onClickBackHandler = this.onClickBackHandler.bind(this);
    this.onClickPushTopTabsScreen = this.onClickPushTopTabsScreen.bind(this);
    this.onClickShowStaticLifecycleOverlay = this.onClickShowStaticLifecycleOverlay.bind(this);
    this.onClickProvidedId = this.onClickProvidedId.bind(this);
  }

  render() {
    return (
      <View style={styles.root} key={'root'}>
        <Text testID={testIDs.WELCOME_SCREEN_HEADER} style={styles.h1}>{`React Native Navigation!`}</Text>
        <Button title='Switch to tab based app' testID={testIDs.TAB_BASED_APP_BUTTON} onPress={this.onClickSwitchToTabs} />
        <Button title='Switch to app with side menus' testID={testIDs.TAB_BASED_APP_SIDE_BUTTON} onPress={this.onClickSwitchToSideMenus} />
        <Button title='Push Lifecycle Screen' testID={testIDs.PUSH_LIFECYCLE_BUTTON} onPress={this.onClickLifecycleScreen} />
        <Button title='Static Lifecycle Events' testID={testIDs.PUSH_STATIC_LIFECYCLE_BUTTON} onPress={this.onClickShowStaticLifecycleOverlay} />
        <Button title='Push' testID={testIDs.PUSH_BUTTON} onPress={this.onClickPush} />
        <Button title='Push Options Screen' testID={testIDs.PUSH_OPTIONS_BUTTON} onPress={this.onClickPushOptionsScreen} />
        <Button title='Push External Component' testID={testIDs.PUSH_EXTERNAL_COMPONENT_BUTTON} onPress={this.onClickPushExternalComponent} />
        {Platform.OS === 'android' && <Button title='Push Top Tabs screen' testID={testIDs.PUSH_TOP_TABS_BUTTON} onPress={this.onClickPushTopTabsScreen} />}
        {Platform.OS === 'android' && <Button title='Back Handler' testID={testIDs.BACK_HANDLER_BUTTON} onPress={this.onClickBackHandler} />}
        <Button title='Show Modal' testID={testIDs.SHOW_MODAL_BUTTON} onPress={this.onClickShowModal} />
        <Button title='Show Redbox' testID={testIDs.SHOW_REDBOX_BUTTON} onPress={this.onClickShowRedbox} />
        <Button title='Orientation' testID={testIDs.ORIENTATION_BUTTON} onPress={this.onClickPushOrientationMenuScreen} />
        <Button title='Provided Id' testID={testIDs.PROVIDED_ID} onPress={this.onClickProvidedId} />
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
      </View>
    );
  }

  onClickSwitchToTabs() {
    Navigation.setRoot({
      bottomTabs: {
        children: [
          {
            stack: {
              children: [
                {
                  component: {
                    name: 'navigation.playground.TextScreen',
                    passProps: {
                      text: 'This is tab 1',
                      myFunction: () => 'Hello from a function!'
                    },
                    options: {
                      topBar: {
                        visible: true,
                        title: 'React Native Navigation!'
                      }
                    }
                  }
                }
              ],
              options: {
                bottomTab: {
                  title: 'Tab 1',
                  icon: require('../images/one.png'),
                  testID: testIDs.FIRST_TAB_BAR_BUTTON
                },
                topBar: {
                  visible: false
                }
              }
            }
          },
          {
            stack: {
              children: [
                {
                  component: {
                    name: 'navigation.playground.TextScreen',
                    passProps: {
                      text: 'This is tab 2'
                    }
                  }
                }
              ],
              options: {
                bottomTab: {
                  title: 'Tab 2',
                  icon: require('../images/two.png'),
                  testID: testIDs.SECOND_TAB_BAR_BUTTON
                }
              }
            }
          }
        ],
        options: {
          bottomTabs: {
            tabColor: 'red',
            selectedTabColor: 'blue',
            fontFamily: 'HelveticaNeue-Italic',
            fontSize: 13,
            testID: testIDs.BOTTOM_TABS_ELEMENT
          }
        }
      }
    });
  }

  onClickSwitchToSideMenus() {
    Navigation.setRoot({
      sideMenu: {
        left: {
          component: {
            name: 'navigation.playground.SideMenuScreen',
            passProps: {
              side: 'left'
            }
          }
        },
        center: {
          bottomTabs: {
            children: [
              {
                stack: {
                  children: [
                    {
                      component: {
                        name: 'navigation.playground.TextScreen',
                        passProps: {
                          text: 'This is a side menu center screen tab 1'
                        }
                      }
                    }
                  ],
                  options: {
                    bottomTab: {
                      title: 'Tab 1',
                      icon: require('../images/one.png'),
                      testID: testIDs.FIRST_TAB_BAR_BUTTON
                    }
                  }
                }
              },
              {
                stack: {
                  children: [
                    {
                      component: {
                        name: 'navigation.playground.TextScreen',
                        passProps: {
                          text: 'This is a side menu center screen tab 2'
                        }
                      }
                    }
                  ],
                  options: {
                    bottomTab: {
                      title: 'Tab 2',
                      icon: require('../images/two.png'),
                      testID: testIDs.SECOND_TAB_BAR_BUTTON
                    }
                  }
                }
              },
              {
                stack: {
                  children: [
                    {
                      component: {
                        name: 'navigation.playground.TextScreen',
                        passProps: {
                          text: 'This is a side menu center screen tab 3'
                        }
                      }
                    }
                  ],
                  options: {
                    bottomTab: {
                      title: 'Tab 3',
                      icon: require('../images/three.png'),
                      testID: testIDs.SECOND_TAB_BAR_BUTTON
                    }
                  }
                }
              }
            ],
            options: {
              bottomTabs: {
                tabColor: 'red',
                selectedTabColor: 'blue',
                fontFamily: 'HelveticaNeue-Italic',
                fontSize: 13
              }
            }
          }
        },
        right: {
          component: {
            name: 'navigation.playground.SideMenuScreen',
            passProps: {
              side: 'right'
            }
          }
        }
      }
    });
  }

  async onClickPush() {
    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        options: {
          topBar: {
            title: 'pushed'
          }
        }
      }
    });
  }

  async onClickPushExternalComponent() {
    await Navigation.push(this.props.componentId, {
      externalComponent: {
        name: 'RNNCustomComponent',
        passProps: {
          text: 'This is an external component'
        },
        options: {
          topBar: {
            title: 'pushed',
            visible: true,
            testID: testIDs.TOP_BAR_ELEMENT
          }
        }
      }
    });
  }

  onClickLifecycleScreen() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.LifecycleScreen'
      }
    });
  }

  onClickShowStaticLifecycleOverlay() {
    Navigation.showOverlay({
      component: {
        name: 'navigation.playground.StaticLifecycleOverlay'
      }
    });
  }

  async onClickShowModal() {
    await Navigation.showModal({
      stack: {
        children: [
          {
            component: {
              name: 'navigation.playground.ModalScreen'
            }
          }
        ]
      }
    });
  }

  onClickShowRedbox() {
    undefined();
  }

  onClickPushOptionsScreen() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.OptionsScreen',
        options: {
          animated: false
        }
      }
    });
  }

  onClickPushTopTabsScreen() {
    Navigation.push(this.props.componentId, {
      topTabs: {
        children: [
          {
            component: {
              name: 'navigation.playground.TopTabOptionsScreen',
              passProps: {
                title: 'Tab 1',
                text: 'This is top tab 1'
              },
              options: {
                topTab: {
                  title: 'Tab 1'
                },
                topBar: {
                  title: 'One'
                }
              }
            }
          },
          {
            component: {
              name: 'navigation.playground.TopTabScreen',
              passProps: {
                title: 'Tab 2',
                text: 'This is top tab 2'
              },
              options: {
                topTab: {
                  title: 'Tab 2',
                  titleFontFamily: 'HelveticaNeue-Italic'
                },
                topBar: {
                  title: 'Two'
                }
              }
            }
          },
          {
            component: {
              name: 'navigation.playground.TopTabScreen',
              passProps: {
                title: 'Tab 3',
                text: 'This is top tab 3'
              },
              options: {
                topTab: {
                  title: 'Tab 3'
                },
                topBar: {
                  title: 'Three'
                }
              }
            }
          }
        ]
      },
      options: {
        topTabs: {
          selectedTabColor: '#12766b',
          unselectedTabColor: 'red',
          fontSize: 6
        }
      }
    });
  }

  onClickBackHandler() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.BackHandlerScreen'
      }
    });
  }

  onClickPushOrientationMenuScreen() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.OrientationSelectScreen'
      }
    });
  }

  onClickProvidedId() {
    Navigation.showModal({
      stack: {
        children: [
          {
            component: {
              name: 'navigation.playground.TextScreen',
              id: 'my unique id'
            }
          }
        ]
      }
    });
    Navigation.setOptions('my unique id', {
      topBar: {
        title: 'User provided id'
      }
    });
  }
}

module.exports = WelcomeScreen;

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white'
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
