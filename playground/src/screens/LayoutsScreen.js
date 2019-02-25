const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const {
  WELCOME_SCREEN_HEADER,
  STACK_BTN,
  BOTTOM_TABS_BTN,
  BOTTOM_TABS,
  SIDE_MENU_BTN
} = require('../testIDs');
const Screens = require('./Screens');
const Navigation = require('../services/Navigation');
const {stack, component} = require('../commons/Layouts');

class LayoutsScreen extends React.Component {
  static options() {
    return {
      topBar: {
        testID: WELCOME_SCREEN_HEADER,
        title: {
          text: 'React Native Navigation'
        }
      }
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Stack' testID={STACK_BTN} onPress={this.stack} />
        <Button label='BottomTabs' testID={BOTTOM_TABS_BTN} onPress={this.bottomTabs} />
        <Button label='SideMenu' testID={SIDE_MENU_BTN} onPress={this.sideMenu} />
      </Root>
    );
  }

  stack = () => Navigation.showModal(Screens.StackScreen);

  bottomTabs = () => Navigation.showModal({
    bottomTabs: {
      children: [
        stack(Screens.FirstBottomTabsScreen),
        stack({
          component: {
            name: Screens.SecondBottomTabsScreen
          }
        }, 'SecondTab'
        )
      ],
      options: {
        bottomTabs: {
          testID: BOTTOM_TABS
        }
      }
    }
  });

  sideMenu = () => Navigation.showModal({
    sideMenu: {
      left: {...component(Screens.SideMenuLeft)},
      center: {
        ...stack({
          component: {
            id: 'SideMenuCenter',
            name: Screens.SideMenuCenter
          }
        })
      },
      right: {...component(Screens.SideMenuRight)}
    }
  });

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
                          }
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

  onClickShowPreview = async ({reactTag}) => {
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
  };
}

module.exports = LayoutsScreen;
