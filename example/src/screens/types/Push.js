import React, {Component} from 'react';
import {StyleSheet, View, Text, Button, Alert} from 'react-native';

class Push extends Component {

  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  onNavigatorEvent(event) {
    if (event.type === 'PreviewActionPress') {
      if (event.id === 'action-cancel') {
        Alert.alert('Cancelled');
      }
      if (event.id === 'action-delete-sure') {
        Alert.alert('Deleted');
      }
    }
  }

  onPushAnother = () => {
    this.props.navigator.push({
      screen: 'example.Types.Push',
      title: `Screen ${this.props.count || 1}`,
      passProps: {
        count: this.props.count ? this.props.count + 1 : 2
      }
    });
  };

  onResetTo = () => {
    this.props.navigator.resetTo({
      label: 'Navigation',
      screen: 'example.Types',
      icon: require('../../../img/list.png'),
      title: 'Navigation Types'
    });
  };

  onPopToRoot = () => {
    this.props.navigator.popToRoot();
  };

  render() {
    return (
      <View style={styles.container}>
        <Text>Pushed Screen</Text>
        <View style={styles.button}>
          <Button
            onPress={this.onPushAnother}
            title="Push Another Screen"/>
        </View>
        <View style={styles.button}>
          <Button
            onPress={this.onResetTo}
            title="Reset Stack"/>
        </View>
        <View style={styles.button}>
          <Button
            onPress={this.onPopToRoot}
            title="Pop To Root"/>
        </View>
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

export default Push;
