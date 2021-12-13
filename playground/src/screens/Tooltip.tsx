import React from 'react';
import { StyleSheet, View, TouchableOpacity, Text } from 'react-native';
import { Navigation, NavigationComponentProps } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  title?: string;
  timesCreated?: number;
}

export default class Tooltip extends React.Component<Props> {
  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.button}>
          <Text style={styles.text}>{'Hey, This is some text that belongs to a tooltip!!!'}</Text>
          <TouchableOpacity
            // @ts-ignore
            onPress={() => alert('Tooltip Alert', `OK Clicked`)}
          >
            <Text style={styles.text}>{'OK'}</Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    width: 100,
    height: 100,
    backgroundColor: 'transparent',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 4,
  },
  button: {
    width: 40,
    height: 40,
    borderRadius: 12,
    justifyContent: 'center',
  },
  text: {
    color: 'white',
    alignSelf: 'center',
  },
});
