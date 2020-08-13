import React from 'react';
import { Options, NavigationComponent } from 'react-native-navigation';

import Root from '../components/Root';
import Button from '../components/Button';
import testIDs from '../testIDs';
import Screens from './Screens';
import { Navigation } from 'react-native-navigation';
import { stack } from '../commons/Layouts';

const {
  WELCOME_SCREEN_HEADER,
  STACK_BTN,
  BOTTOM_TABS_BTN,
  BOTTOM_TABS,
  SIDE_MENU_BTN,
  SPLIT_VIEW_BUTTON,
  TOP_TABS_BTN,
} = testIDs;

export default class LayoutsScreen extends NavigationComponent {
  static options(): Options {
    return {
      topBar: {
        testID: WELCOME_SCREEN_HEADER,
        title: {
          text: 'React Native Navigation',
        },
      },
      layout: {
        orientation: ['portrait', 'landscape'],
      },
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Stack" testID={STACK_BTN} onPress={this.stack} />
        <Button label="BottomTabs" testID={BOTTOM_TABS_BTN} onPress={this.bottomTabs} />
        <Button label="SideMenu" testID={SIDE_MENU_BTN} onPress={this.sideMenu} />
        <Button label="TopTabs" testID={TOP_TABS_BTN} platform="android" onPress={this.topTabs} />
        <Button
          label="SplitView"
          testID={SPLIT_VIEW_BUTTON}
          platform="ios"
          onPress={this.splitView}
        />
      </Root>
    );
  }

  stack = () => Navigation.showModal(stack(Screens.Stack));

  bottomTabs = () =>
    Navigation.showModal({
      bottomTabs: {
        children: [
          stack(Screens.FirstBottomTabsScreen),
          stack(
            {
              component: {
                name: Screens.SecondBottomTabsScreen,
              },
            },
            'SecondTab'
          ),
        ],
        options: {
          bottomTabs: {
            testID: BOTTOM_TABS,
          },
        },
      },
    });

  sideMenu = () =>
    Navigation.showModal({
      sideMenu: {
        left: {
          component: {
            id: 'left',
            name: Screens.SideMenuLeft,
          },
        },
        center: stack({
          component: {
            id: 'SideMenuCenter',
            name: Screens.SideMenuCenter,
          },
        }),
        right: {
          component: {
            id: 'right',
            name: Screens.SideMenuRight,
          },
        },
      },
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
                    name: Screens.CocktailsListMasterScreen,
                  },
                },
              ],
            },
          },
          detail: {
            stack: {
              id: 'DETAILS_ID',
              children: [
                {
                  component: {
                    id: 'DETAILS_COMPONENT_ID',
                    name: Screens.CocktailDetailsScreen,
                  },
                },
              ],
            },
          },
          options: {
            layout: {
              orientation: ['landscape'],
            },
            splitView: {
              displayMode: 'visible',
            },
          },
        },
      },
    });
  };

  topTabs = () => {
    const topTabsChildren = Object.values(Screens)
      .filter((value) => typeof value === 'string')
      .map((name) => ({
        component: {
          name: name,
          options: {
            topBar: {
              title: {
                text: name,
              },
            },
            topTab: {
              title: name,
            },
          },
        },
      }));
    Navigation.showModal({
      stack: {
        children: [
          {
            //@ts-ignore
            topTabs: {
              children: topTabsChildren,
            },
          },
        ],
        options: {
          //@ts-ignore
          topTabs: {
            tabMode: 'auto',
          },
        },
      },
    });
  };

  onClickSearchBar = () => {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.SearchControllerScreen',
      },
    });
  };
}
