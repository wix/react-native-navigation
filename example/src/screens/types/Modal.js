import React, {Component} from 'react';
import {StyleSheet, View, Text, Button} from 'react-native';

class Modal extends Component {

  onPushScreen = () => {
    this.props.navigator.push({
      screen: 'example.Types.Modal',
      title: `Screen ${this.props.count || 1}`,
      passProps: {
        count: this.props.count ? this.props.count + 1 : 2
      }
    });
  };

  onResetTo = () => {
    this.props.navigator.resetTo({
      screen: 'example.Types.Modal',
      icon: require('../../../img/list.png'),
      title: 'Modal'
    });
  };

  onPopToRoot = () => {
    this.props.navigator.popToRoot();
  };

  render() {
    return (
      <View style={styles.container}>
        <Text>Modal Screen</Text>
        <View style={styles.button}>
          <Button
            onPress={this.onPushScreen}
            title="Push Screen"/>
        </View>
        <View style={styles.button}>
          <Button
            onPress={this.onResetTo}
            title="Reset Stack"/>
        </View>
        {this.props.count > 1 && <View style={styles.button}>
          <Button
            onPress={this.onPopToRoot}
            title="Pop To Root"/>
        </View>}
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
  button: {
    marginTop: 16
  }
});

export default Modal;
