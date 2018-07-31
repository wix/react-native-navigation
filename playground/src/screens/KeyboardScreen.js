const React = require('react');
const { Component } = require('react');
const {
  View,
  ScrollView,
  Dimensions,
  StyleSheet,
  Image,
  Text,
  Platform,
  TouchableHighlight,
  TextInput
} = require('react-native');

const testIDs = require('../testIDs');
const Button = require('./Button');

const { Navigation } = require('react-native-navigation');
let screenWidth = Dimensions.get('window').width;
const LOREM_IPSUM = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis' +
                    'nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum' +
                    'dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.';

class KeyboardScreen extends Component {
  static get options() {
    return {
      bottomTabs: {
        drawBehind: true,
      },
      topBar: {
        title: {
          text: 'Keyboard'
        }
      }
    };
  }

  render() {
    return (
      <View style={styles.root}>
        <ScrollView>
          <Image style={styles.image} source={require('../../img/2048.jpeg')} />
          <Text style={{margin: 8}}>Keyboard e2e</Text>
          <TextInput placeholder='Input 1'/>
          <TextInput
            placeholder='Input 2'
            onFocus={this.hideTabs}
            onBlur={this.showTabs}
            />
          {/* <Text>{LOREM_IPSUM}</Text> */}
        </ScrollView>
      </View>
    );
  }

  hideTabs = () => {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        visible: false
      }
    });
  }

  showTabs = () => {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        visible: true
      }
    });
  }
}

module.exports = KeyboardScreen;

const styles = StyleSheet.create({
  root: {
    flex: 1,
    backgroundColor: '#E3DCC3'
  },
  image: {
    height: 400,
    width: screenWidth,
    resizeMode: 'cover'
  }
});
