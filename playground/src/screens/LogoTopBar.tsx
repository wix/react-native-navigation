import React from 'react';
import { StyleSheet, View, Image, Text } from 'react-native';
import { NavigationComponent } from 'react-native-navigation';

export default class CustomTopBar extends NavigationComponent {
  render() {
    return (
      <View collapsable={false} style={styles.container}>
        <View style={styles.logoContainer}>
          <Image
            source={require('../../img/navigator.png')}
            // @ts-ignore nativeID isn't included in react-native Image props.
            nativeID={`topBarLogo`}
            resizeMode={'contain'}
            style={styles.logo}
            fadeDuration={0}
          />
        </View>
        <Text style={styles.text}>React Native Navigation</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
  logoContainer: {
    flexDirection: 'column',
    paddingVertical: 8,
  },
  logo: {
    height: '100%',
    width: 0,
    aspectRatio: 1,
  },
  text: {
    alignSelf: 'center',
    color: 'black',
    marginStart: 16,
    fontSize: 18,
  },
});
