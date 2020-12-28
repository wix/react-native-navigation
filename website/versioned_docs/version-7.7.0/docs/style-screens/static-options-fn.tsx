import { View } from 'react-native';
import { NavigationComponentProps, NavigationFunctionComponent } from 'react-native-navigation';

const MyFunctionalScreen: NavigationFunctionComponent = (props: NavigationComponentProps) => {
  return <View />;
};

MyFunctionalScreen.options = {
  topBar: {
    title: {
      text: 'My Screen',
    },
  },
};
