import React from 'react';
import { StyleSheet, Image, View } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';

export default class StatusBarOptions extends React.Component<NavigationComponentProps> {
  static options() {
    return {
      statusBar: {
        translucent: true,
        drawBehind: true,
      },
      topBar: {
        elevation: 0,
        drawBehind: true,
        background: {
          color: 'transparent',
        },
        title: {
          text: 'StatusBar Options',
          color: 'white',
        },
        backButton: {
          color: 'white',
        },
      },
    };
  }

  render() {
    return (
      <View style={style.container}>
        <Image style={style.image} source={require('../../img/city.png')} fadeDuration={0} />
        <Root componentId={this.props.componentId} style={style.root}>
          <Button label="Full Screen Modal" onPress={this.fullScreenModal} />
          <Button label="Push" onPress={this.push} />
          <Button label="BottomTabs" onPress={this.bottomTabs} />
          <Button label="Open Left" onPress={() => this.open('left')} />
          <Button label="Open Right" onPress={() => this.open('right')} />
        </Root>
      </View>
    );
  }

  fullScreenModal = () => Navigation.showModal(Screens.FullScreenModal);
  push = () => Navigation.push(this, Screens.Pushed);
  bottomTabs = () => Navigation.showModal(Screens.StatusBarBottomTabs);
  open = (side: 'left' | 'right') =>
    Navigation.mergeOptions(this, {
      sideMenu: {
        [side]: { visible: true },
      },
    });
}

const style = StyleSheet.create({
  root: {
    paddingTop: 0,
    paddingHorizontal: 0,
  },
  container: {
    flex: 1,
    flexDirection: 'column',
  },
  image: {
    height: 250,
    width: '100%',
    resizeMode: 'cover',
    marginBottom: 16,
  },
});
