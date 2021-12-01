import React from 'react';
import { StyleSheet, View, TouchableOpacity, Text } from 'react-native';
import { Navigation, NavigationComponentProps } from 'react-native-navigation';
import Colors from '../commons/Colors';

interface Props extends NavigationComponentProps {
  title: string;
  timesCreated?: number;
}

let timesCreated = 0;
export default class RoundedButton extends React.Component<Props> {
  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
    timesCreated = props.timesCreated ?? timesCreated + 1;
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.button}>
          <TouchableOpacity
            // @ts-ignore
            onPress={() => alert(this.props.title, `Times created: ${timesCreated}`)}
          >
            <Text style={styles.text}>{this.props.title}</Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'transparent',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 4,
  },
  button: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: Colors.primary.light,
    justifyContent: 'center',
  },
  text: {
    color: 'white',
    alignSelf: 'center',
  },
});
