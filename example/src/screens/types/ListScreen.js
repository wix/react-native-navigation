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

var {height, width} = Dimensions.get('window');

class ListScreen extends Component {
  constructor(props){
    super(props);
    this.data = [];
    const numberOfItems = 20;
    for (i = 0; i < numberOfItems; i++) {
      this.data.push({text:`cell ${i}`, tapCount: 0, id: i});
    }

  }

  render(){
    return (
      <ScrollView
        style={[{flex: 1, backgroundColor: 'transparent',}]}
        scrollEnabled={true}
        scrollsToTop={false}
        // onLayout={(e) => this.adjustCardSize(e)}
        scrollEventThrottle={100}
        automaticallyAdjustContentInsets={false}
        directionalLockEnabled={true}
        showsHorizontalScrollIndicator={false}
        showsVerticalScrollIndicator={false}>


        {_.map(this.data, (o) => {
          return (
            <View key={o.id} style={styles.cellContainer}>
              <Button title={o.text} onPress={() => {
                console.log('RANG', 'onPress');
              }}>
              </Button>
            </View>
          );
        })}


      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  cellContainer: {
    flex: 1,
    backgroundColor: 'green',
    paddingVertical: 30,
  }
});

module.exports = ListScreen;