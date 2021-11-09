import React from 'react';
import { StyleSheet, Image, View } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';

let translucent = true;
let darkStatusBarScheme = false;
let isBlackStatusBar = true;
let drawBehind = true;
export default class StatusBarOptions extends React.Component<NavigationComponentProps> {
  static options() {
    return {
      statusBar: {
        translucent: translucent,
        drawBehind: drawBehind,
        backgroundColor: isBlackStatusBar ? '#000000' : 'red',
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
          {/*<Button label="Full Screen Modal" onPress={this.fullScreenModal} />*/}
          {/*<Button label="Push" onPress={this.push} />*/}
          <Button label="Toggle color" onPress={this.toggleStatusBarColor} />
          <Button label="Toggle color scheme" onPress={this.toggleStatusBarColorScheme} />
          <Button label="Toggle Translucent" onPress={this.toggleTranslucent} />
          <Button label="Hide Status Bar" onPress={this.hideStatusBar} />
          <Button label="Show Status Bar" onPress={this.showStatusBar} />
          <Button label="BottomTabs" onPress={this.bottomTabs} />
          <Button label="Open Left" onPress={() => this.open('left')} />
          <Button label="Open Right" onPress={() => this.open('right')} />
        </Root>
      </View>
    );
  }

  toggleStatusBarColor = () => {
    isBlackStatusBar = !isBlackStatusBar;
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        backgroundColor: isBlackStatusBar ? '#000000' : 'red',
      },
    });
  };
  toggleTranslucent = () => {
    translucent = !translucent;
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        translucent: translucent,
      },
    });
  };
  toggleStatusBarColorScheme = () => {
    darkStatusBarScheme = !darkStatusBarScheme;
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        style: darkStatusBarScheme ? 'dark' : 'light',
      },
    });
  };
  hideStatusBar = () =>
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        visible: false,
      },
    });

  showStatusBar = () =>
    Navigation.mergeOptions(this.props.componentId, {
      statusBar: {
        visible: true,
      },
    });
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
