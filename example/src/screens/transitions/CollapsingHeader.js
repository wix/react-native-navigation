import React from 'react';
import {StyleSheet, View, Text, ScrollView} from 'react-native';

class CollapsingHeader extends React.Component {

  static navigatorStyle = {
    drawUnderTabBar: true,
    navBarButtonColor: '#ffffff',
    navBarTextColor: '#ffffff',
    collapsingToolBarImage: require('../../../img/gyro_header.jpg'),
    collapsingToolBarCollapsedColor: '#0f2362',
    navBarBackgroundColor: '#eeeeee'
  };

  render() {
    return (
      <ScrollView style={styles.container}>
        <View>
          {[...new Array(40)].map((a, index) => (
            <Text key={`row_${index}`} style={styles.button}>Row {index}</Text>
          ))}
        </View>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
});

export default CollapsingHeader;
