import React from 'react';
import { StyleSheet, View, Text, Image } from 'react-native';
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
        <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
          <Text ellipsizeMode={'tail'} numberOfLines={1}>
            Ast elit et nulla tempor dolore minim est ea nostrud et eiusmod aliquip
          </Text>
          <Image source={require('../../img/two.png')} />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    alignSelf: 'baseline',
  },
  text: {
    alignSelf: 'flex-start',
    color: 'black',
    fontSize: 16,
  },
});
