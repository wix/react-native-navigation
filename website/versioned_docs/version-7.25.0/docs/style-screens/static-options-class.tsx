import { NavigationComponent, Options } from 'react-native-navigation';

class MyScreen extends NavigationComponent {
  static options: Options = {
    topBar: {
      title: {
        text: 'My Screen',
      },
    },
  };
}
