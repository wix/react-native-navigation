const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const Navigation = require('react-native-navigation');
const testIDs = require('../testIDs');

class TextScreen extends Component {
  static get navigationOptions() {
    return {
      bottomTabs: {
        testID: testIDs.BOTTOM_TABS_ELEMENT
      }
    };
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1} testID={testIDs.CENTERED_TEXT_HEADER}>{this.props.text || 'Text Screen'}</Text>
        {this.renderTextFromFunctionInProps()}
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>
        <Button title={'Set Tab Badge'} testID={testIDs.SET_TAB_BADGE_BUTTON} onPress={() => this.onButtonPress()} />
        <Button title={'Switch To Tab 2'} testID={testIDs.SWITCH_SECOND_TAB_BUTTON} onPress={() => this.onClickSwitchToTab()} />
        <Button title="Hide Tab Bar" testID={testIDs.HIDE_BOTTOM_TABS_BUTTON} onPress={() => this.hideTabBar(true)} />
        <Button title="Show Tab Bar" testID={testIDs.SHOW_BOTTOM_TABS_BUTTON} onPress={() => this.hideTabBar(false)} />
        <Button title="Show Left Side Menu" testID={testIDs.SHOW_LEFT_SIDE_MENU_BUTTON} onPress={() => this.showSideMenu('left')} />
        <Button title="Show Right Side Menu" testID={testIDs.SHOW_RIGHT_SIDE_MENU_BUTTON} onPress={() => this.showSideMenu('right')} />
      </View>
    );
  }

  renderTextFromFunctionInProps() {
    if (!this.props.myFunction) {
      return undefined;
    }
    return (
      <Text style={styles.h1}>{this.props.myFunction()}</Text>
    );
  }

  onButtonPress() {
    Navigation.setOptions(this.props.containerId, {
      bottomTabs: {
        tabBadge: `TeSt`
      }
    });
  }

  onClickSwitchToTab() {
    Navigation.setOptions(this.props.containerId, {
      bottomTabs: {
        currentTabIndex: 1,
        hidden: true,
        animateHide: true
      }
    });
  }

  hideTabBar(hidden) {
    Navigation.setOptions(this.props.containerId, {
      bottomTabs: {
        hidden
      }
    });
  }

  showSideMenu(side) {
    Navigation.setOptions(this.props.containerId, {
      sideMenu: {
        [side]: {
          visible: true
        }
      }
    });
  }
}

module.exports = TextScreen;

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
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

