import React from 'react';
import {
  Options,
  OptionsModalPresentationStyle,
  NavigationComponent,
  NavigationComponentProps,
} from 'react-native-navigation';

import Root from '../components/Root';
import Button from '../components/Button';
import testIDs from '../testIDs';
import Screens from './Screens';
import Navigation from '../services/Navigation';
import { stack } from '../commons/Layouts';
import { Text } from 'react-native';

const {
  WELCOME_SCREEN_HEADER,
  STACK_BTN,
  BOTTOM_TABS_BTN,
  BOTTOM_TABS,
  SIDE_MENU_BTN,
  KEYBOARD_SCREEN_BTN,
  SPLIT_VIEW_BUTTON,
} = testIDs;

interface State {
  componentDidAppear: boolean;
}

export default class LayoutsScreen extends NavigationComponent<NavigationComponentProps, State> {
  constructor(props: NavigationComponentProps) {
    super(props);
    Navigation.events().bindComponent(this);
    this.state = {
      componentDidAppear: false,
    };
  }
  componentWillAppear() {
    console.log('componentWillAppear:', this.props.componentId);
  }

  componentDidDisappear() {
    console.log('componentDidDisappear:', this.props.componentId);
  }

  componentDidAppear() {
    console.log('componentDidAppear:', this.props.componentId);
    this.setState({ componentDidAppear: true });
  }

  static options(): Options {
    return {
      bottomTabs: {
        visible: true,
      },
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
        <Button label="Keyboard" testID={KEYBOARD_SCREEN_BTN} onPress={this.openKeyboardScreen} />
        <Button
          label="SplitView"
          testID={SPLIT_VIEW_BUTTON}
          platform="ios"
          onPress={this.splitView}
        />
        <Text>{this.state.componentDidAppear && 'componentDidAppear'}</Text>
      </Root>
    );
  }

  stack = () => Navigation.showModal(stack(Screens.Stack, 'StackId'));

  bottomTabs = () => {
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
          {
            component: {
              name: Screens.Pushed,
              options: {
                bottomTab: {
                  selectTabOnPress: false,
                  text: 'Tab 3',
                  testID: testIDs.THIRD_TAB_BAR_BTN,
                },
              },
            },
          },
        ],
        options: {
          hardwareBackButton: {
            bottomTabsOnPress: 'previous',
          },
          bottomTabs: {
            testID: BOTTOM_TABS,
          },
        },
      },
    });
  };

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
        options: {
          layout: {
            orientation: ['portrait', 'landscape'],
          },
          modalPresentationStyle: OptionsModalPresentationStyle.pageSheet,
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

  openKeyboardScreen = async () => {
    await Navigation.push(this.props.componentId, Screens.KeyboardScreen);
  };
  onClickSearchBar = () => {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.SearchControllerScreen',
      },
    });
  };
}
