import React from 'react';
import {StyleSheet, View, Text} from 'react-native';

class Modal extends React.Component {

  render() {
    return (
      <View style={styles.container}>
        <Text>Modal Screen</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#ffffff',
  },
});

export default Modal;
