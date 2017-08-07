import _ from 'lodash';
import React, { Component } from 'react';
import {
  StyleSheet,
  ScrollView,
  View,
  Text,
  Button
} from 'react-native';

import Navigation from 'react-native-navigation';

class ScrollViewScreen extends Component {
  static navigationOptions = {
    topBarHideOnScroll: true
  }
  
  constructor(props) {
    super(props);
  }
  
  render() {
    return (
      <ScrollView testID="scrollView" contentContainerStyle={styles.contentContainer}>
        <View>
          <Text>{'Hello'}</Text>
        </View>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  contentContainer: {
    paddingVertical: 20,
    height: 1500
  }
});
export default ScrollViewScreen;
