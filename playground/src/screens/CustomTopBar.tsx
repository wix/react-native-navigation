import React from 'react';
import { View, Text, Image } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  title: string;
  text: string;
  clickable: boolean;
}

export default class CustomTopBar extends React.Component<Props> {
  render() {
    return (
      <View style={{ flexDirection: 'row', flex: 1 }}>
        <View
          style={{
            flex: 1,
            justifyContent: 'center',
          }}
        >
          <Text ellipsizeMode={'tail'} numberOfLines={1} style={{ color: 'white' }}>
            Ast elit et nulla tempor dolore minim est ea nostrud et eiusmod aliquip
          </Text>
        </View>
        <View style={{ justifyContent: 'center' }}>
          <Image source={require('../../img/two.png')} style={{ tintColor: 'white' }} />
        </View>
      </View>
    );
  }
}
