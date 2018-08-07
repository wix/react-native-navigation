const React = require('react');
const { Component } = require('react');
const { View, Text, Platform, TouchableHighlight } = require('react-native');

const testIDs = require('../testIDs');
const Button = require('./Button');

const { Navigation } = require('react-native-navigation');

class WelcomeScreen extends Component {
  static get options() {
    return {
      _statusBar: {
        backgroundColor: 'transparent',
        style: 'dark',
        drawBehind: true
      },
      topBar: {
        title: {
          text: 'My Screen'
        },
        largeTitle: {
          visible: false,
        },
        drawBehind: true,
        visible: false,
        animate: false
      }
    };
  }

  render() {
    return (
      <View style={styles.bar}>
        <View style={{ width: 2, height: 2, borderRadius: 1, backgroundColor: 'red', alignSelf: 'center' }} />
        <View style={styles.root} key={'root'}>
          <Text testID={testIDs.WELCOME_SCREEN_HEADER} style={styles.h1}>{`React Native Navigation!`}</Text>
          <Button title='Switch to tab based app' testID={testIDs.TAB_BASED_APP_BUTTON} onPress={this.onClickSwitchToTabs} />
          <Button title='Switch to app with side menus' testID={testIDs.TAB_BASED_APP_SIDE_BUTTON} onPress={this.onClickSwitchToSideMenus} />
          {Platform.OS === 'ios' && <Button title='Switch to split view based app' testID={testIDs.SPLIT_VIEW_BUTTON} onPress={this.onClickSplitView} />}
          <Button title='Push Lifecycle Screen' testID={testIDs.PUSH_LIFECYCLE_BUTTON} onPress={this.onClickLifecycleScreen} />
          <Button title='Static Lifecycle Events' testID={testIDs.PUSH_STATIC_LIFECYCLE_BUTTON} onPress={this.onClickShowStaticLifecycleOverlay} />
          <Button title='Push' testID={testIDs.PUSH_BUTTON} onPress={this.onClickPush} />
          {Platform.OS === 'ios' && (
            <Navigation.Element elementId='PreviewElement'>
              <Button testID={testIDs.SHOW_PREVIEW_BUTTON} onPressIn={this.onClickShowPreview} title='Push Preview' />
            </Navigation.Element>
          )}
          <Button title='Push Options Screen' testID={testIDs.PUSH_OPTIONS_BUTTON} onPress={this.onClickPushOptionsScreen} />
          <Button title='Push External Component' testID={testIDs.PUSH_EXTERNAL_COMPONENT_BUTTON} onPress={this.onClickPushExternalComponent} />
          {Platform.OS === 'android' && <Button title='Push Top Tabs screen' testID={testIDs.PUSH_TOP_TABS_BUTTON} onPress={this.onClickPushTopTabsScreen} />}
          {Platform.OS === 'android' && <Button title='Back Handler' testID={testIDs.BACK_HANDLER_BUTTON} onPress={this.onClickBackHandler} />}
          <Button title='Show Modal' testID={testIDs.SHOW_MODAL_BUTTON} onPress={this.onClickShowModal} />
          <Button title='Show Redbox' testID={testIDs.SHOW_REDBOX_BUTTON} onPress={this.onClickShowRedbox} />
          <Button title='Orientation' testID={testIDs.ORIENTATION_BUTTON} onPress={this.onClickPushOrientationMenuScreen} />
          <Button title='Provided Id' testID={testIDs.PROVIDED_ID} onPress={this.onClickProvidedId} />
          <Button title='Complex Layout' testID={testIDs.COMPLEX_LAYOUT_BUTTON} onPress={this.onClickComplexLayout} />
          <Button title='Push SearchBar' testID={testIDs.SHOW_TOPBAR_SEARCHBAR} onPress={this.onClickSearchBar} />
          <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
        </View>
        <View style={{ width: 2, height: 2, borderRadius: 1, backgroundColor: 'red', alignSelf: 'center' }} />
      </View>
    );
  }

  onClickSwitchToTabs = () => {
    Navigation.setRoot({
      root: {
        bottomTabs: {
          children: [
            {
              stack: {
                id: 'TAB1_ID',
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
                          title: {
                            text: 'React Native Navigation!'
                          }
                        },
                        bottomTab: {
                          text: 'Tab 1',
                          icon: require('../images/one.png'),
                          selectedIcon: require('../images/one.png'),
                          testID: testIDs.FIRST_TAB_BAR_BUTTON
                        }
                      }
                    }
                  }
                ],
                options: {
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
                    text: 'Tab 2',
                    icon: require('../images/two.png'),
                    testID: testIDs.SECOND_TAB_BAR_BUTTON
                  }
                }
              }
            }
          ],
          options: {
            bottomTabs: {
              titleDisplayMode: 'alwaysShow',
              testID: testIDs.BOTTOM_TABS_ELEMENT
            }
          }
        }
      }
    });
  }

  onClickSwitchToSideMenus = () => {
    Navigation.setRoot({
      root: {
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
                    id: 'tab1Stack',
                    children: [
                      {
                        component: {
                          name: 'navigation.playground.TextScreen',
                          passProps: {
                            text: 'This is a side menu center screen tab 1'
                          },
                          // options: {
                          //   bottomTab: {
                          //     iconColor: 'red',
                          //     textColor: 'red',
                          //     selectedIconColor: 'purple',
                          //     selectedTextColor: 'purple',
                          //   }
                          // }
                        }
                      }
                    ],
                    options: {
                      bottomTab: {
                        iconColor: 'red',
                        textColor: 'red',
                        selectedIconColor: 'purple',
                        selectedTextColor: 'purple',
                        text: 'Tab 1',
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
                        text: 'Tab 2',
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
                        text: 'Tab 3',
                        icon: require('../images/three.png'),
                        testID: testIDs.SECOND_TAB_BAR_BUTTON
                      }
                    }
                  }
                }
              ],
              options: {
                bottomTab: {
                  textColor: '#AED581',
                  iconColor: '#AED581',
                  selectedTextColor: '#90CAF9',
                  selectedIconColor: '#90CAF9',
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
      }
    });
  }

  onClickPush = async () => {
    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        options: {
          topBar: {
            title: {
              text: 'pushed',
              color: '#0000ff',
              fontSize: 14
            },
            subtitle: {
              text: 'subtitle',
              fontSize: 10,
              color: '#00ff00'
            }
          }
        }
      }
    });
  }

  onClickPushExternalComponent = async () => {
    await Navigation.push(this.props.componentId, {
      externalComponent: {
        name: 'RNNCustomComponent',
        passProps: {
          text: 'This is an external component'
        },
        options: {
          topBar: {
            title: {
              text: 'pushed'
            },
            visible: true,
            testID: testIDs.TOP_BAR_ELEMENT
          }
        }
      }
    });
  }

  onClickLifecycleScreen = () => {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.LifecycleScreen'
      }
    });
  }

  onClickShowStaticLifecycleOverlay = () => {
    Navigation.showOverlay({
      component: {
        name: 'navigation.playground.StaticLifecycleOverlay',
        options: {
          overlay: {
            interceptTouchOutside: false
          }
        }
      }
    });
  }

  onClickShowModal = async () => {
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

  onClickShowRedbox = () => {
    undefined();
  }

  onClickShowPreview = async () => {
    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        options: {
          animations: {
            push: {
              enable: false
            }
          },
          preview: {
            elementId: 'PreviewElement',
            height: 300,
            commit: true,
            actions: [{
              id: 'action-cancel',
              title: 'Cancel'
            }, {
              id: 'action-delete',
              title: 'Delete',
              actions: [{
                id: 'action-delete-sure',
                title: 'Are you sure?',
                style: 'destructive'
              }]
            }]
          }
        }
      }
    });
  }

  onClickPushOptionsScreen = () => {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.OptionsScreen',
        options: {
          animations: {
            push: {
              enable: false
            }
          }
        }
      }
    });
  }

  onClickPushTopTabsScreen = () => {
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
                  title: {
                    text: 'One'
                  }
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
                  title: {
                    text: 'Two'
                  }
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
                  title: {
                    text: 'Three'
                  }
                }
              }
            }
          }
        ],
        options: {
          topTabs: {
            selectedTabColor: '#12766b',
            unselectedTabColor: 'red',
            fontSize: 6
          }
        }
      }
    });
  }

  onClickBackHandler = () => {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.BackHandlerScreen'
      }
    });
  }

  onClickPushOrientationMenuScreen = () => {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.OrientationSelectScreen'
      }
    });
  }

  onClickProvidedId = () => {
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
    Navigation.mergeOptions('my unique id', {
      topBar: {
        title: {
          text: 'User provided id'
        }
      }
    });
  }

  onClickComplexLayout = () => {
    Navigation.showModal({
      component: {
        name: 'navigation.playground.ComplexLayout'
      }
    });
  }

  onClickSplitView = () => {
    Navigation.setRoot({
      root: {
        splitView: {
          id: 'SPLITVIEW_ID',
          master: {
            stack: {
              id: 'MASTER_ID',
              children: [
                {
                  component: {
                    name: 'navigation.playground.WelcomeScreen'
                  },
                },
              ]
            },
          },
          detail: {
            stack: {
              id: 'DETAILS_ID',
              children: [
                {
                  component: {
                    name: 'navigation.playground.WelcomeScreen'
                  },
                },
              ]
            }
          },
          options: {
            displayMode: 'auto',
            primaryEdge: 'leading',
            minWidth: 150,
            maxWidth: 300,
          },
        },
      },
    });
  }
  onClickSearchBar = () => {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.SearchControllerScreen'
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
    backgroundColor: '#e8e8e8',
  },
  bar: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: '#e8e8e8',
    justifyContent: 'space-between'
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
