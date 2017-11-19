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

const Colors = [
  "#1abc9c",
  "#2ecc71",
  "#3498db",
  "#9b59b6",
  "#34495e",
  "#16a085",
  "#27ae60",
  "#2980b9",
  "#8e44ad",
  "#2c3e50",
  "#f1c40f",
  "#e67e22",
  "#e74c3c",
  "#ecf0f1",
  "#95a5a6",
  "#f39c12",
  "#d35400",
  "#c0392b",
  "#bdc3c7",
  "#7f8c8d"
];

class ListScreen extends Component {
  static navigatorStyle = {
    drawUnderNavBar: true,
    navBarTranslucent:true,
    navBarButtonColor: 'black',
    navBarTextColor: 'black'

  };

  constructor(props){
    super(props);
    this.data = [];
    const numberOfItems = 100;
    for (i = 0; i < numberOfItems; i++) {
      this.data.push({text:`cell ${i}`, tapCount: 0, id: i});
    }
  }

  _onButtonPressed(i, color) {
    this.props.navigator.push({
      screen: 'example.Types.DummyScreen',
      title: 'Dummy',
      passProps: {
        text: i,
        bgColor: color
      }
    });
  }

  render(){
    return (
      <ScrollView
        style={[{flex: 1, backgroundColor: 'transparent'}]}
        scrollEnabled={true}
        scrollsToTop={false}
        scrollEventThrottle={100}
        automaticallyAdjustContentInsets={false}
        directionalLockEnabled={true}
        showsHorizontalScrollIndicator={false}
        showsVerticalScrollIndicator={false}
        contentOffset={{y: -64}}>


        {_.map(this.data, (o, i) => {
          const color = getRandomColor(i);
          return (
            <View key={o.id} style={[styles.cellContainer, {backgroundColor: color}]}>
              <Button title={o.text} onPress={() => {
                this._onButtonPressed(i, color);
              }}>
              </Button>
            </View>
          );
        })}


      </ScrollView>
    );
  }
}

function getRandomColor(index) {
  return Colors[index % Colors.length];
}

const styles = StyleSheet.create({
  cellContainer: {
    flex: 1,
    paddingVertical: 30,
  }
});



module.exports = ListScreen;