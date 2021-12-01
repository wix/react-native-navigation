import React from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Screens from './Screens';
import Navigation from './../services/Navigation';
import { stack, component } from './../commons/Layouts';
import testIDs from '../testIDs';

const {
  SIDE_MENU_INSIDE_BOTTOM_TABS_BTN,
  PUSH_BTN,
  PUSHED_BOTTOM_TABS,
  MODAL_BTN,
  SIDE_MENU_TAB,
  FLAT_LIST_BTN,
  HIDE_TABS_PUSH_BTN,
  SECOND_TAB_BAR_BTN,
  SET_BADGE_BTN,
} = testIDs;

export default class SecondBottomTabScreen extends React.Component<NavigationComponentProps> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Second Tab',
        },
      },
      bottomTab: {
        icon: require('../../img/star.png'),
        text: 'Tab 2',
        testID: SECOND_TAB_BAR_BTN,
        dotIndicator: {
          visible: true,
          color: 'green',
        },
      },
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Push" onPress={this.push} />
        <Button label="Push BottomTabs" testID={PUSH_BTN} onPress={this.pushBottomTabs} />
        <Button label="Push Modal" testID={MODAL_BTN} onPress={this.pushModal} />
        <Button label="SetBadge" testID={SET_BADGE_BTN} onPress={this.setBadge} />

        <Button label="Push ScrollView" onPress={this.pushScrollView} />
        <Button
          label="SideMenu inside BottomTabs"
          testID={SIDE_MENU_INSIDE_BOTTOM_TABS_BTN}
          onPress={this.sideMenuInsideBottomTabs}
        />
        <Button
          label="Hide Tabs on Push"
          testID={HIDE_TABS_PUSH_BTN}
          onPress={this.hideTabsOnPush}
        />
      </Root>
    );
  }

  hideTabsOnPush = () =>
    Navigation.push(
      this,
      component(Screens.Pushed, {
        bottomTabs: { visible: false },
      })
    );

  push = () => Navigation.push(this, Screens.Pushed);

  pushModal = async () => await Navigation.push(this, Screens.Modal);

  setBadge = () =>
    Navigation.mergeOptions(this, {
      bottomTab: {
        badge: 'Badge',
      },
    });

  pushBottomTabs = () =>
    Navigation.push(this, {
      bottomTabs: {
        children: [
          component(Screens.Pushed, {
            bottomTab: {
              icon: require('../../img/whatshot.png'),
              text: 'Tab A',
              testID: PUSHED_BOTTOM_TABS,
            },
          }),
          component(Screens.Pushed, {
            bottomTab: {
              icon: require('../../img/star.png'),
              text: 'Tab B',
            },
          }),
        ],
      },
    });

  pushScrollView = () => Navigation.push(this, Screens.ScrollViewScreen);

  sideMenuInsideBottomTabs = () => {
    Navigation.showModal({
      bottomTabs: {
        children: [
          {
            sideMenu: {
              left: { ...component(Screens.SideMenuLeft) },
              center: stack(Screens.SideMenuCenter),
              options: {
                bottomTab: {
                  text: 'SideMenu',
                  icon: require('../../img/sideMenu.png'),
                  testID: SIDE_MENU_TAB,
                },
              },
            },
          },
          {
            sideMenu: {
              left: { ...component(Screens.SideMenuLeft) },
              center: stack(Screens.FlatListScreen),
              options: {
                bottomTab: {
                  text: 'FlatList',
                  icon: require('../../img/list.png'),
                  testID: FLAT_LIST_BTN,
                },
              },
            },
          },
        ],
      },
    });
  };
}
