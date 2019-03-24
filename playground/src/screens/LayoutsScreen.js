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
      left: {...component(Screens.SideMenuLeft)},
      center: stack({
          component: {
            id: 'SideMenuCenter',
            name: Screens.SideMenuCenter
          }
        }),
      right: {...component(Screens.SideMenuRight)}
    }
  });

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
