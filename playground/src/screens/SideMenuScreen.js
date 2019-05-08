const React = require('react');
const { Component } = require('react');

const { View, Text, Button, Platform } = require('react-native');

const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

class SideMenuScreen extends Component {

  constructor(props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  componentDidAppear() {
    console.log('RNN', `SMS.componentDidAppear ${this.props.side}`);
  }

  componentDidDisappear() {
    console.log('RNN', `SMS.componentDidDisappear ${this.props.side}`);
  }

  render() {
    const testID = this.props.side === 'left' ? testIDs.HIDE_LEFT_SIDE_MENU_BUTTON : testIDs.HIDE_RIGHT_SIDE_MENU_BUTTON;
    return (
      <View style={styles.root} testID={this.props.testID}>
        <Text testID={testIDs.SIDE_BAR}>Side Bar</Text>
        <Text style={styles.h1}>{`This is a ${this.props.side} side menu screen`}</Text>
        <Button title='Hide Side Menu' testID={testID} onPress={() => this.hideSideMenu()} />
        <Button title='Push' testID={this.props.side + testIDs.SIDE_MENU_PUSH_BUTTON} onPress={() => this.pushAndCloseSideMenu()} />
      </View>
    );
  }

  hideSideMenu() {
    Navigation.mergeOptions(this.props.componentId, {
      sideMenu: {
        [this.props.side]: {
          visible: false
        }
      }
    });
  }

  pushAndCloseSideMenu() {
    if (Platform.OS === 'ios') {
      this.hideSideMenu();
    }
    Navigation.push('tab1Stack', {
      component: {
        name: 'navigation.playground.TextScreen',
        options: {
          sideMenu: {
            left: {
              visible: false,
              enabled: false
            }
          }
        }
      }
    });
  }
}
module.exports = SideMenuScreen;

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
  }
};
