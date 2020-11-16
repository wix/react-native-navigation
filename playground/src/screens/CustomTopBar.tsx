import React from 'react';
import { StyleSheet, View, TouchableOpacity, Text, Alert } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  title: string;
  text: string;
  clickable: boolean;
}

export default class CustomTopBar extends React.Component<Props> {
  render() {
    return (
      <View collapsable={false} style={styles.container}>
        <TouchableOpacity
          disabled={!this.props.clickable}
          onPress={() => Alert.alert(this.props.title, 'Thanks for that :)')}
        >
          <Text style={styles.text}>{this.props.text}</Text>
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    alignSelf: 'center',
  },
  text: {
    alignSelf: 'center',
    color: 'black',
    fontSize: 16,
  },
});
