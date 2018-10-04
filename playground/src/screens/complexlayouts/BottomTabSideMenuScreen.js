const React = require('react');
const { Component } = require('react');
const { View, Button } = require('react-native');
const { Navigation } = require('react-native-navigation');
const testIDs = require('../../testIDs');

class BottomTabSideMenuScreen extends Component {
  static get options() {
    return {
      topBar: {
        title: {
          text: 'test',
        }
      }
    }
  }

  onOpenSideMenuPress = () => {
    Navigation.mergeOptions(this.props.componentId, {
      sideMenu: {
        left: {
          visible: true
        }
      }
    });
  }

  render() {
    return (
      <View style={styles.root}>
        <Button
          title="Open SideMenu"
          color="blue"
          onPress={this.onOpenSideMenuPress}
          testID={testIDs.OPEN_SIDE_MENU}
        />
      </View>
    );
  }
}
module.exports = BottomTabSideMenuScreen;
const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
  }
};
