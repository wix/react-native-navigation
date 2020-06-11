const React = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Screens = require('./Screens');
const Navigation = require('./../services/Navigation');
const { stack, component } = require('./../commons/Layouts');
const {
  SIDE_MENU_INSIDE_BOTTOM_TABS_BTN,
  PUSH_BTN,
  PUSHED_BOTTOM_TABS,
  SIDE_MENU_TAB,
  FLAT_LIST_BTN,
  HIDE_TABS_PUSH_BTN
} = require('../testIDs')

class SecondBottomTabScreen extends React.Component {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Second Tab'
        }
      },
      bottomTab: {
        icon: require('../../img/star.png'),
        text: 'Tab 2',
        dotIndicator: {
          visible: true,
          color: 'green'
        }
      }
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Push' onPress={this.push} />
        <Button label='Push BottomTabs' testID={PUSH_BTN} onPress={this.pushBottomTabs} />
        <Button label='Push ScrollView' onPress={this.pushScrollView} />
        <Button label='SideMenu inside BottomTabs' testID={SIDE_MENU_INSIDE_BOTTOM_TABS_BTN} onPress={this.sideMenuInsideBottomTabs} />
        <Button label='Hide Tabs on Push' testID={HIDE_TABS_PUSH_BTN} onPress={this.hideTabsOnPush} />
      </Root>
    );
  }

  hideTabsOnPush = () => Navigation.push(this, component(Screens.Pushed, {
    bottomTabs: { visible: false }
  }));

  push = () => Navigation.push(this, Screens.Pushed);

  pushBottomTabs = () => Navigation.push(this, {
    bottomTabs: {
      options: {
        _layout: {
          backgroundColor: '#99ff29'
        },
        _bottomTabs: {
          visible: false
        }
      },
      children: [
        component(Screens.Pushed, {
          bottomTab: {
            icon: require('../../img/whatshot.png'),
            text: 'Tab A',
            testID: PUSHED_BOTTOM_TABS
          }
        }),
        component(Screens.Pushed, {
          bottomTab: {
            icon: require('../../img/star.png'),
            text: 'Tab B'
          }
        })
      ]
    }
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
                  testID: SIDE_MENU_TAB
                }
              }
            }
          },
          {
            sideMenu: {
              left: { ...component(Screens.SideMenuLeft) },
              center: stack(Screens.FlatListScreen),
              options: {
                bottomTab: {
                  text: 'FlatList',
                  icon: require('../../img/list.png'),
                  testID: FLAT_LIST_BTN
                }
              }
            }
          }
        ]
      }
    });
  }
}

module.exports = SecondBottomTabScreen;
