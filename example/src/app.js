import { Navigation } from 'react-native-navigation';
import registerScreens from './screens';

// screen related book keeping
registerScreens();

const tabs = [{
    label: 'Navigation',
    screen: 'example.Types',
    icon: require('../img/list.png'),
    title: 'Navigation Types',
}, {
    label: 'Actions',
    screen: 'example.Actions',
    icon: require('../img/list.png'),
    title: 'Navigation Actions',
}, {
    label: 'Transitions',
    screen: 'example.Transitions',
    icon: require('../img/list.png'),
    title: 'Navigation Transitions',
}];

// this will start our app
Navigation.startTabBasedApp({
    tabs,
    appStyle: {
        tabBarBackgroundColor: '#003a66',
        navBarButtonColor: '#ffffff',
        tabBarButtonColor: '#ffffff',
        navBarTextColor: '#ffffff',
        tabBarSelectedButtonColor: '#ff505c',
        navigationBarColor: '#003a66',
        navBarBackgroundColor: '#003a66',
        statusBarColor: '#002b4c',
        tabFontFamily: 'BioRhyme-Bold',
    },
    drawer: {
        left: {
            screen: 'example.Types.Drawer'
        }
    }
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
