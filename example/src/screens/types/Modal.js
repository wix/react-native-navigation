import React, {Component} from 'react';
import {StyleSheet, View, Text, Button, TouchableOpacity, Platform} from 'react-native';
import {Navigation} from 'react-native-navigation';

const CloseModalButton = ({text}) =>
<TouchableOpacity
  style={[styles.buttonContainer]}
  onPress={() => navigator.dismissModal()}
>
  <View style={styles.closeModalButton}>
    <Text style={styles.buttonText}>{text}</Text>
  </View>
</TouchableOpacity>;
Navigation.registerComponent('CloseModalButton', () => CloseModalButton);

class Modal extends Component {
  static navigatorButtons = {
    rightButtons: [
      {
        id: 'close-modal-button',
        component: Platform.OS === 'ios' ? 'CloseModalButton' : null,
        passProps: {
          text: 'Close'
        }
      }
    ]
  };

  componentWillMount() {
    navigator = this.props.navigator;
  }

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
  },
  buttonContainer: {
    width: 48,
    height: 48,
    justifyContent: 'center',
    alignItems: 'center'
  },
  closeModalButton: {
    backgroundColor: 'tomato',
    width: 50,
    height: 25,
    borderRadius: 2,
    overflow: 'hidden',
    justifyContent: 'center',
    alignItems: 'center'
  },
  buttonText: {
    color: 'white'
  }
});

export default Modal;
