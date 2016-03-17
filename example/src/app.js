import { Navigation } from 'react-native-navigation';

//import and call the default function from each module
//The default function of a module should register all the pushable screens
import register1 from './module_1';
import register2 from './module_2';
register1();
register2();

Navigation.startTabBasedApp({
  tabs: [
    {
      label: 'One',
      screen: 'module_1.FirstTabScreen',
      icon: require('../img/one.png'),
      selectedIcon: require('../img/one_selected.png'),
      title: 'Screen One'
    },
    {
      label: 'Two',
      screen: 'module_2.SecondTabScreen',
      icon: require('../img/two.png'),
      selectedIcon: require('../img/two_selected.png'),
      title: 'Screen Two'
    },
    {
      label: 'Three',
      screen: 'module_2.ThirdTabScreen',
      icon: require('../img/three.png'),
      selectedIcon: require('../img/three_selected.png'),
      title: 'Screen Three',
      navigatorStyle: {
        navBarBackgroundColor: '#4dbce9',
        navBarTextColor: '#ffff00',
        navBarButtonColor: '#ffffff',
        statusBarTextColorScheme: 'light'
      }
    }
  ],
  drawer: {
    left: {
      screen: 'module_1.SideMenu'
    }
  }
});
