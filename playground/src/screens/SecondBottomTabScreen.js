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
  FLAT_LIST_BTN
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
        text: 'Tab 2'
      }
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Push BottomTabs' testID={PUSH_BTN} onPress={this.pushBottomTabs} />
        <Button label='SideMenu inside BottomTabs' testID={SIDE_MENU_INSIDE_BOTTOM_TABS_BTN} onPress={this.sideMenuInsideBottomTabs} />
      </Root>
    );
  }

  pushBottomTabs = () => Navigation.push(this, {
    bottomTabs: {
      children: [
        component(Screens.Pushed, {
          bottomTab: {
            icon: require('../../img/whatshot.png'),
            text: 'Tab 1',
            testID: PUSHED_BOTTOM_TABS
          }
        }),
        component(Screens.Pushed, {
          bottomTab: {
            icon: require('../../img/star.png'),
            text: 'Tab 2'
          }
        })
      ]
    }
  });

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
