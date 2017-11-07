import _ from 'lodash';
import React, { Component } from 'react';
import {
  View,
  ScrollView,
  Dimensions,
  Text,
  StyleSheet,
  Button,
  PixelRatio
} from 'react-native';
import {NavigationToolBarIOS} from 'react-native-navigation';

const {width} = Dimensions.get('window');


class DummyScreen extends Component {

  static navigatorStyle = {
    drawUnderNavBar: true,
    navBarTranslucent:true,
    navBarNoBorder: true,
    navBarTextColor: 'black',
    navBarButtonColor: 'black',

  };

  render(){
    return (
      <View
        style={[{flex: 1, justifyContent: 'center', alignItems:'center', backgroundColor: this.props.bgColor,}]}>

        <Text>🤗</Text>
        <Text>{this.props.text}</Text>

        <NavigationToolBarIOS key='segmented' translucent={true} style={styles.toolBarStyle}>
          <Button title={"I Am NavigationToolBarIOS, Press Me"} onPress={() => alert('Thank You')}/>
        </NavigationToolBarIOS>


      </View>
    );
  }
}

const styles = StyleSheet.create({
  cellContainer: {
    flex: 1,
    paddingVertical: 30,
  },
  toolBarStyle: {
    top: 64,
    width: width,
    position: 'absolute',
    borderTopWidth: 0,
    height: 66,
    backgroundColor: 'transparent'

  }
});



module.exports = DummyScreen;