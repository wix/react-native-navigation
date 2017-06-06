import React, {Component, PropTypes} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Alert
} from 'react-native';
import {connect} from 'react-redux';
import * as counterActions from '../reducers/counter/actions';
import _ from 'lodash';

class SideMenu extends Component {

  constructor(props) {
    super(props);
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  onNavigatorEvent(event) {
      console.log('SideMenu', 'Unhandled event ' + event.id);
  }

  render() {
    return (
      <View style={styles.sideMenu}>
        <Text style={styles.title}>{this.props.title}</Text>

        <TouchableOpacity onPress={ this.onShowModalPress.bind(this) }>
          <Text style={styles.button}>Show modal</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPushScreenToFirstTab.bind(this) }>
          <Text style={styles.button}>Push screen to first tab</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPushScreenToSecondTab.bind(this) }>
          <Text style={styles.button}>Push screen to second tab</Text>
        </TouchableOpacity>
      </View>
    );
  }

  onShowModalPress() {
    this.props.navigator.showModal({
      title: "Modal Screen from SideMenu",
      screen: "example.PushedScreen",
      passProps: {
        str: 'This is a prop passed in \'navigator.showModal()\'!',
        obj: {
          str: 'This is a prop passed in an object!',
          arr: [
            {
              str: 'This is a prop in an object in an array in an object!'
            }
          ]
        },
        num: 1234
      }
    });
  }

  onPushScreenToFirstTab() {
    this.props.navigator.handleDeepLink({
      link: 'tab1/pushScreen/example.PushedScreen'
    });
  }

  onPushScreenToSecondTab() {
    this.props.navigator.handleDeepLink({
      link: 'tab2/pushScreen/example.PushedScreen'
    });
  }
}

const styles = StyleSheet.create({
  sideMenu: {
    flex: 1,
    width: 260,
    backgroundColor: '#efefef',
    padding: 20
  },
  title: {
    textAlign: 'center',
    marginBottom: 15
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    borderBottomWidth: 1,
    borderColor: 'grey',
    marginBottom: 10,
    marginTop:10,
    color: 'black'
  }
});

export default connect()(SideMenu);