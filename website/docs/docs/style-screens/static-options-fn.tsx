import { NavigationFunctionalComponent, Options } from 'react-native-navigation';

const MyFunctionalScreen: NavigationFunctionComponent = (props: NavigationComponentProps) => {};

MyFunctionalScreen.options: Options = {
  topBar: {
    title: {
      text: 'My Screen',
    },
  },
};