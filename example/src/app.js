import {Navigation} from 'react-native-navigation';

// screen related book keeping
import {registerScreens} from './screens';
registerScreens();

// this will start our app
Navigation.startTabBasedApp({
  tabs: [
    {
      label: 'One',
      screen: 'example.FirstTabScreen',
      icon: require('../img/one.png'),
      selectedIcon: require('../img/one_selected.png'),
      title: 'Screen One'
    },
    {
      label: 'Two',
      screen: 'example.SecondTabScreen',
      icon: require('../img/two.png'),
      selectedIcon: require('../img/two_selected.png'),
      title: 'Screen Two',
      navigatorStyle: {
        tabBarBackgroundColor: '#4dbce9',
      }
    }
  ],
  drawer: {
    left: {
      screen: 'example.SideMenu'
    }
  },
  portraitOnlyMode: true
});
//Navigation.startSingleScreenApp({
//  screen: {
//    screen: 'example.FirstTabScreen',
//    title: 'Navigation',
//    navigatorStyle: {
//      navBarBackgroundColor: '#4dbce9',
//      navBarTextColor: '#ffff00',
//      navBarSubtitleTextColor: '#ff0000',
//      navBarButtonColor: '#ffffff',
//      statusBarTextColorScheme: 'light'
//    }
//  },
//  drawer: {
//    left: {
//      screen: 'example.SideMenu'
//    }
//  }
//});
