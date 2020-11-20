import React from 'react';
import { StyleSheet, View, TouchableOpacity, Alert } from 'react-native';
import { Navigation, NavigationComponentProps } from 'react-native-navigation';
import { Assets, Image } from 'react-native-ui-lib';

interface Props extends NavigationComponentProps {
  title: string;
  timesCreated?: number;
}

let timesCreated = 0;
console.log('RoundedButton file init');

export default class RoundedButton extends React.Component<Props> {
  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
    timesCreated = props.timesCreated ?? timesCreated + 1;
    console.log('RoundedButton -> constructor -> timesCreated', timesCreated);
    console.log('RoundedButton constructor');
  }

  render() {
    console.log('RoundedButton render');
    console.log('RoundedButton -> render -> Assets.icons.settings', Assets.icons.search);

    return (
      <View style={styles.button}>
        <TouchableOpacity
          onPress={() => Alert.alert(this.props.title, `Times created: ${timesCreated}`)}
        >
          {/* <Image source={Assets.icons.settings} width={40} height={40} /> */}
          <Image style={styles.button} source={require('../../img/Icon-87.png')} fadeDuration={0} />
        </TouchableOpacity>
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
    // borderRadius: 20,
    // backgroundColor: Colors.primary,
    justifyContent: 'center',
  },
  text: {
    color: 'white',
    alignSelf: 'center',
  },
});
