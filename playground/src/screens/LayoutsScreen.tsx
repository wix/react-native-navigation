import React from 'react';
import { Options, NavigationComponent } from 'lib/src';

import Root from '../components/Root';
import Button from '../components/Button';
import testIDs from '../testIDs';
import Screens from './Screens';
import { Navigation } from 'lib/src';
import { stack } from '../commons/Layouts';

const {
  WELCOME_SCREEN_HEADER,
  STACK_BTN,
  BOTTOM_TABS_BTN,
  BOTTOM_TABS,
  SIDE_MENU_BTN,
  SPLIT_VIEW_BUTTON,
  TOP_TABS_BTN,
  SET_BADGE_BTN,
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
        <Button label="Set Badge" testID={SET_BADGE_BTN} onPress={() => this.setBadge('NEW')} />
        <Button label="TopTabs" testID={TOP_TABS_BTN} onPress={this.topTabs} />
        <Button
          label="SplitView"
          testID={SPLIT_VIEW_BUTTON}
          platform="ios"
          onPress={this.splitView}
        />
      </Root>
    );
  }

  // stack = () => Navigation.showModal(stack(Screens.Stack));
  stack = () =>
    Navigation.showModal({
      stack: {
        id: 'MASTER_ID',
        children: [
          {
            component: {
              name: Screens.Stack,
              options: {
                topBar: {
                  title: {
                    text: `Stack`,
                  },
                },
              },
            },
          },
        ],
        options: {
          topBar: {
            title: {
              text: `stack`,
            },
          },
        },
      },
    });

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

  setBadge = (badge: string) => {
    Navigation.mergeOptions(this.props.componentId, {
      //@ts-ignore
      topTab: {
        title: `Layouts`,
        badge: badge,
      },
    });
  };

  topTabs = () => {
    let screen = {
      Layouts: Screens.Layouts,
      Options: Screens.Options,
      Navigation: Screens.Navigation,
      FlatListScreen: Screens.FlatListScreen,
      StatusBarFirstTab: Screens.StatusBarFirstTab,
    };
    const topTabsChildren = Object.values(screen)
      .filter((value) => typeof value === 'string')
      .map((name) => ({
        component: {
          name: name,
          options: {
            topTab: {
              title: name,
              badge: `99+`,
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
              options: {
                //@ts-ignore
                topTabs: {
                  tabMode: 'auto',
                },
                topBar: {
                  title: {
                    text: `Stack`,
                  },
                },
              },
            },
          },
        ],
        options: {
          //@ts-ignore
          modalPresentationStyle: `overCurrentContext`,
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
