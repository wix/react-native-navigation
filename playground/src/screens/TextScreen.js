const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

class TextScreen extends Component {
  static get options() {
    return {
      bottomTabs: {
        drawBehind: true,
        testID: testIDs.BOTTOM_TABS_ELEMENT
      },
      topBar: {
        testID: testIDs.TOP_BAR_ELEMENT
      }
    };
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1} testID={testIDs.CENTERED_TEXT_HEADER}>{this.props.text || 'Text Screen'}</Text>
        {this.renderTextFromFunctionInProps()}
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
        <Button title={'Set Tab Badge'} testID={testIDs.SET_TAB_BADGE_BUTTON} onPress={() => this.onClickSetBadge()} />
        <Button title={'Switch To Tab 2'} testID={testIDs.SWITCH_SECOND_TAB_BUTTON} onPress={() => this.onClickSwitchToTab()} />
        <Button title={'Switch To Tab 1 by componentID'} testID={testIDs.SWITCH_FIRST_TAB_BUTTON} onPress={() => this.onClickSwitchToTabByComponentID()} />
        <Button title='Hide Tab Bar' testID={testIDs.HIDE_BOTTOM_TABS_BUTTON} onPress={() => this.hideTabBar(false)} />
        <Button title='Show Tab Bar' testID={testIDs.SHOW_BOTTOM_TABS_BUTTON} onPress={() => this.hideTabBar(true)} />
        <Button title='Hide Tab Bar on Push' testID={testIDs.HIDE_BOTTOM_TABS_ON_PUSH_BUTTON} onPress={() => this.hideTabBarOnPush()} />
        <Button title='Show Left Side Menu' testID={testIDs.SHOW_LEFT_SIDE_MENU_BUTTON} onPress={() => this.showSideMenu('left')} />
        <Button title='Show Right Side Menu' testID={testIDs.SHOW_RIGHT_SIDE_MENU_BUTTON} onPress={() => this.showSideMenu('right')} />
        <Button title='Push' testID={testIDs.PUSH_BUTTON} onPress={this.onClickPush} />
        <Button title='Pop' testID={testIDs.POP_BUTTON} onPress={this.onClickPop} />
      </View>
    );
  }

  onClickPush = async () => {
    await Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen'
      }
    });
  }

  onClickPop = async () => {
    await Navigation.pop(this.props.componentId);
  }

  renderTextFromFunctionInProps() {
    if (!this.props.myFunction) {
      return undefined;
    }
    return (
      <Text style={styles.h1}>{this.props.myFunction()}</Text>
    );
  }

  onClickSetBadge() {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTab: {
        badge: `TeSt`
      }
    });
  }

  onClickSwitchToTab() {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        currentTabIndex: 1,
        visible: false,
        drawBehind: true,
        animate: true
      }
    });
  }

  onClickSwitchToTabByComponentID() {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        currentTabId: 'TAB1_ID'
      }
    });
  }

  hideTabBar(visible) {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        visible,
        drawBehind: true,
        animate: true
      }
    });
  }

  hideTabBarOnPush() {
    Navigation.push(this.props.componentId, {
      component: {
        name: 'navigation.playground.PushedScreen'
      }
    });
  }

  showSideMenu(side) {
    Navigation.mergeOptions(this.props.componentId, {
      sideMenu: {
        [side]: {
          visible: true
        }
      }
    });
  }

  onClickPop() {
    Navigation.pop(this.props.componentId);
  }
}

module.exports = TextScreen;

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#E3DCC3'
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10
  },
  h2: {
    fontSize: 12,
    textAlign: 'center',
    margin: 10
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
};
