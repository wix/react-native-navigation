import React from 'react';
import { StyleSheet, View, Text, Image, SafeAreaView, ScrollView } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';
import { color } from 'react-native-reanimated';
import Root from '../components/Root';

interface Props extends NavigationComponentProps {
  title: string;
  text: string;
  clickable: boolean;
}

export default class CustomTopBar extends React.Component<Props> {
  render() {
    return (
      <ScrollView contentContainerStyle={{ flexGrow: 1, alignItems: 'center', padding:16 }}>
        <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
          <Text style={{ color: 'white' }} ellipsizeMode={'tail'} numberOfLines={1}>
            Ast elit et nulla tempor dolore minim est ea nostrud et eiusmod aliquip
          </Text>
          <Image style={{ tintColor: 'white' }} source={require('../../img/two.png')} />
        </View>
      </ScrollView>
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
