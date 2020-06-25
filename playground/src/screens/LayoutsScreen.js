const React = require('react');
const { Platform } = require('react-native');
const Root = require('../components/Root');
const Button = require('../components/Button')
const {
  WELCOME_SCREEN_HEADER,
  STACK_BTN,
  BOTTOM_TABS_BTN,
  BOTTOM_TABS,
  SIDE_MENU_BTN,
  SPLIT_VIEW_BUTTON
} = require('../testIDs');
const Screens = require('./Screens');
const Navigation = require('../services/Navigation');
const {stack} = require('../commons/Layouts');

class LayoutsScreen extends React.Component {
  static options() {
    return {
      topBar: {
        testID: WELCOME_SCREEN_HEADER,
        title: {
          text: 'React Native Navigation'
        }
      },
      layout: {
        orientation: ['portrait', 'landscape']
      }
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Stack' testID={STACK_BTN} onPress={this.stack} />
        <Button label='BottomTabs' testID={BOTTOM_TABS_BTN} onPress={this.bottomTabs} />
        <Button label='SideMenu' testID={SIDE_MENU_BTN} onPress={this.sideMenu} />
        <Button label='SplitView' testID={SPLIT_VIEW_BUTTON} platform='ios' onPress={this.splitView} />
      </Root>
    );
  }

  stack = () => Navigation.showModal(Screens.Stack);

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
      left: {
        component: {
          id: 'left',
          name: Screens.SideMenuLeft
        }
      },
      center: stack({
          component: {
            id: 'SideMenuCenter',
            name: Screens.SideMenuCenter
          }
        }),
      right: {
        component: {
          id: 'right',
          name: Screens.SideMenuRight
        }
      }
    }
  });

  splitView = () => {
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
                    name: Screens.CocktailsListMasterScreen
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
                    id: 'DETAILS_COMPONENT_ID',
                    name: Screens.CocktailDetailsScreen
                  },
                },
              ]
            }
          },
          options: {
            layout: {
              orientation: ['landscape']
            },
            splitView: {
              displayMode: 'visible'
            }
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
