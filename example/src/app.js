import React, {Component} from 'react';
import {
  AppRegistry,
  View
} from 'react-native';
import {Navigation} from 'react-native-navigation';

// screen related book keeping
import {registerScreens} from './screens';
registerScreens();

// this will start our app
Navigation.startSingleScreenApp({
  screen: {
    screen: 'example.FirstTabScreen',
    title: 'Login',
    navigatorStyle: {
      navBarBackgroundColor: '#4dbce9',
      navBarTextColor: '#ffff00',
      navBarButtonColor: '#ffffff',
      statusBarTextColorScheme: 'light'
    }
  },
  drawer: {
    left: {
      screen: 'example.SideMenu'
    }
  }
  // tabsStyle: {
  //   tabBarButtonColor: '#ffff00',
  //   tabBarSelectedButtonColor: '#ff9900',
  //   tabBarBackgroundColor: '#551A8B'
  // },
});
