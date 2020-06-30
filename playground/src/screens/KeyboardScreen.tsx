import React from 'react';
import { View, ScrollView, Dimensions, StyleSheet, Image, Text, TextInput } from 'react-native';
import { Navigation, NavigationComponentProps } from 'react-native-navigation';

const screenWidth = Dimensions.get('window').width;

export default class KeyboardScreen extends React.Component<NavigationComponentProps> {
  static options() {
    return {
      bottomTabs: {
        drawBehind: true,
      },
      topBar: {
        title: {
          text: 'Keyboard',
        },
      },
    };
  }

  render() {
    return (
      <View style={styles.root}>
        <ScrollView>
          <Image style={styles.image} source={require('../../img/2048.jpeg')} />
          <Text style={{ margin: 8 }}>Keyboard e2e</Text>
          <TextInput placeholder="Input 1" />
          <TextInput placeholder="Input 2" onFocus={this.hideTabs} onBlur={this.showTabs} />
          {/* <Text>{LOREM_IPSUM}</Text> */}
        </ScrollView>
      </View>
    );
  }

  hideTabs = () => {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        visible: false,
      },
    });
  };

  showTabs = () => {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        visible: true,
      },
    });
  };
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    backgroundColor: '#E3DCC3',
  },
  image: {
    height: 400,
    width: screenWidth,
    resizeMode: 'cover',
  },
});
