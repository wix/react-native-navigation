import React from 'react';
import { StyleSheet, View, TouchableOpacity, Text, Alert } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  title: string;
}

export default class CustomTextButton extends React.Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity
          style={styles.button}
          onPress={() => Alert.alert(this.props.title, 'Thanks for that :)')}
        >
          <Text style={styles.text}>Press Me</Text>
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: 60,
    flexDirection: 'column',
    justifyContent: 'center',
  },
  button: {
    flex: 1,
    flexDirection: 'column',
  },
  text: {
    color: 'black',
  },
});
