import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  AlertIOS
} from 'react-native';

export default class SideMenu extends Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.title}>Side Menu</Text>

        <TouchableOpacity onPress={ this.onReplaceTab2Press.bind(this) }>
          <Text style={styles.button}>Replace Tab#2 Root</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onModalPress.bind(this) }>
          <Text style={styles.button}>Show Modal Screen</Text>
        </TouchableOpacity>
      </View>
    );
  }
  onReplaceTab2Press() {
    this._toggleDrawer();
    // push/pop navigator actions affect the navigation stack of the current screen only.
    // since side menu actions are normally directed at sibling tabs, push/pop will
    // not help us. the recommended alternative is to use deep links for this purpose
    this.props.navigator.handleDeepLink({
      link: "tab2/example.PushedScreen"
    });
  }

  onModalPress() {
    this._toggleDrawer();
    this.props.navigator.showModal({
      title: "Modal",
      screen: "example.ModalScreen"
    });
  }

  _toggleDrawer() {
    this.props.navigator.toggleDrawer({
      to: 'closed',
      side: 'left',
      animated: true
    });
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: 'white',
    justifyContent: 'center',
    width: 300
  },
  title: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10,
    fontWeight: '500'
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10,
    color: 'blue'
  }
});
