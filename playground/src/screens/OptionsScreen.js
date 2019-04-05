const React = require('react');
const { Component } = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('../services/Navigation');
const Screens = require('./Screens');
const Colors = require('../commons/Colors');
const {
  CHANGE_TITLE_BTN,
  HIDE_TOP_BAR_BTN,
  SHOW_TOP_BAR_BTN,
  TOP_BAR,
  ROUND_BUTTON,
  BUTTON_ONE,
  LEFT_BUTTON,
  PUSH_BTN,
  HIDE_TOPBAR_DEFAULT_OPTIONS,
  SHOW_YELLOW_BOX_BTN,
  SET_REACT_TITLE_VIEW
} = require('../testIDs');

class Options extends Component {
  static options() {
    return {
      topBar: {
        visible: true,
        testID: TOP_BAR,
        title: {
          text: 'Styling Options'
        },
        rightButtons: [
          {
            id: 'ONE',
            testID: BUTTON_ONE,
            text: 'One',
            color: Colors.primary
          },
          {
            id: 'ROUND',
            testID: ROUND_BUTTON,
            component: {
              name: Screens.RoundButton,
              passProps: {
                title: 'Two'
              }
            }
          }
        ],
        leftButtons: [
          {
            id: 'LEFT',
            testID: LEFT_BUTTON,
            icon: require('../../img/clear.png'),
            color: Colors.primary
          }
        ]
      }
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Change title' testID={CHANGE_TITLE_BTN} onPress={this.changeTitle} />
        <Button label='Hide TopBar' testID={HIDE_TOP_BAR_BTN} onPress={this.hideTopBar} />
        <Button label='Show TopBar' testID={SHOW_TOP_BAR_BTN} onPress={this.showTopBar} />
        <Button label='Push' testID={PUSH_BTN} onPress={this.push} />
        <Button label='Hide TopBar in DefaultOptions' testID={HIDE_TOPBAR_DEFAULT_OPTIONS} onPress={this.hideTopBarInDefaultOptions} />
        <Button label='Set React Title View' testID={SET_REACT_TITLE_VIEW} onPress={this.setReactTitleView} />
        <Button label='Show Yellow Box' testID={SHOW_YELLOW_BOX_BTN} onPress={() => console.warn('Yellow Box')} />
      </Root>
    );
  }

  changeTitle = () => Navigation.mergeOptions(this, {
    topBar: {
      title: {
        text: 'Title Changed'
      }
    }
  });

  hideTopBar = () => Navigation.mergeOptions(this, {
    topBar: {
      visible: false
    }
  });

  showTopBar = () => Navigation.mergeOptions(this, {
    topBar: {
      visible: true
    }
  });

  push = () => Navigation.push(this, Screens.Pushed);

  hideTopBarInDefaultOptions = () => {
    Navigation.setDefaultOptions({
      topBar: {
        visible: false,
        title: {
          text: 'Default Title'
        }
      }
    });
  }

  setReactTitleView = () => Navigation.mergeOptions(this, {
    topBar: {
      title: {
        component: {
          name: Screens.ReactTitleView,
          alignment: 'center'
        }
      }
    }
  });

  //       <Button title='Switch to tab based app' testID={testIDs.TAB_BASED_APP_BUTTON} onPress={this.onClickSwitchToTabs} />
  //       <Button title='Switch to app with side menus' testID={testIDs.TAB_BASED_APP_SIDE_BUTTON} onPress={this.onClickSwitchToSideMenus} />
  //       {Platform.OS === 'ios' && <Button title='Switch to split view based app' testID={testIDs.SPLIT_VIEW_BUTTON} onPress={this.onClickSplitView} />}
  //       <Button title='Push Lifecycle Screen' testID={testIDs.PUSH_LIFECYCLE_BUTTON} onPress={this.onClickLifecycleScreen} />
  //       <Button title='Static Lifecycle Events' testID={testIDs.PUSH_STATIC_LIFECYCLE_BUTTON} onPress={this.onClickShowStaticLifecycleOverlay} />
  //       <Button title='Push' testID={testIDs.PUSH_BUTTON} onPress={this.onClickPush} />
  //       {false && <Button title='Push Context Screen' testID={testIDs.PUSH_CONTEXT_SCREEN_BUTTON} onPress={this.onClickPushContextScreen} />}
  //       {Platform.OS === 'ios' && <Button testID={testIDs.SHOW_PREVIEW_BUTTON} onPress={this.onClickPush} onPressIn={this.onClickShowPreview} title='Push Preview' />}
  //       <Button title='Push Options Screen' testID={testIDs.PUSH_OPTIONS_BUTTON} onPress={this.onClickPushOptionsScreen} />
  //       <Button title='Push External Component' testID={testIDs.PUSH_EXTERNAL_COMPONENT_BUTTON} onPress={this.onClickPushExternalComponent} />
  //       {Platform.OS === 'android' && <Button title='Push Top Tabs screen' testID={testIDs.PUSH_TOP_TABS_BUTTON} onPress={this.onClickPushTopTabsScreen} />}
  //       {Platform.OS === 'android' && <Button title='Back Handler' testID={testIDs.BACK_HANDLER_BUTTON} onPress={this.onClickBackHandler} />}
  //       <Button title='Show Modal' testID={testIDs.SHOW_MODAL_BUTTON} onPress={this.onClickShowModal} />
  //       <Button title='Show Redbox' testID={testIDs.SHOW_REDBOX_BUTTON} onPress={this.onClickShowRedbox} />
  //       <Button title='Orientation' testID={testIDs.ORIENTATION_BUTTON} onPress={this.onClickPushOrientationMenuScreen} />
  //       <Button title='Provided Id' testID={testIDs.PROVIDED_ID} onPress={this.onClickProvidedId} />
  //       <Button title='Complex Layout' testID={testIDs.COMPLEX_LAYOUT_BUTTON} onPress={this.onClickComplexLayout} />
  //       <Button title='Push SearchBar' testID={testIDs.SHOW_TOPBAR_SEARCHBAR} onPress={this.onClickSearchBar} />
  //       <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>

  onClickSwitchToTabs = () => {
    Navigation.setRoot({
      root: {
        bottomTabs: {
          id: 'BottomTabs',
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
                          animate: false,
                          title: {
                            text: 'React Native Navigation!'
                          }
                        },
                        bottomTab: {
                          text: 'Tab 1',
                          icon: require('../images/colored_tab_icon.png'),
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
                    iconColor: '#1B4C77',
                    selectedIconColor: '#0f0',
                    testID: testIDs.SECOND_TAB_BAR_BUTTON
                  }
                }
              }
            },
            {
              component: {
                name: 'navigation.playground.TextScreen',
                passProps: {
                  text: 'This is tab 3',
                  myFunction: () => 'Hello from a function!'
                },
                options: {
                  topBar: {
                    visible: true,
                    animate: false
                  },
                  bottomTab: {
                    text: 'Tab 3',
                    icon: require('../images/one.png'),
                    iconColor: '#1B4C77',
                    selectedIconColor: '#0f0',
                    selectedIcon: require('../images/one.png')
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
          layout: {

          },
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

  onClickPushContextScreen = async () => {
    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.ContextScreen',
      }
    })
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

  onClickShowPreview = async ({ reactTag }) => {
    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen',
        options: {
          animations: {
            push: {
              enabled: false
            }
          },
          preview: reactTag ? {
            reactTag,
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
          } : undefined,
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
              enabled: false
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

module.exports = Options;

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  bar: {
    flex: 1,
    flexDirection: 'column',
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
