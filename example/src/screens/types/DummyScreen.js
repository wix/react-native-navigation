import _ from 'lodash';
import React, { Component } from 'react';
import {
  View,
  ScrollView,
  Dimensions,
  Text,
  StyleSheet,
  Button
} from 'react-native';


class ListScreen extends Component {

  render(){
    return (
      <View
        style={[{flex: 1, justifyContent: 'center', alignItems:'center', backgroundColor: this.props.bgColor,}]}>

        <Text>🤗</Text>
        <Text>{this.props.text}</Text>


      </View>
    );
  }
}

const styles = StyleSheet.create({
  cellContainer: {
    flex: 1,
    paddingVertical: 30,
  }
});



module.exports = ListScreen;