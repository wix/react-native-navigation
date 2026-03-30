import { View } from 'react-native';
import { NavigationFunctionComponent } from 'react-native-navigation';

interface Props {
  name: string;
}

const MyFunctionalComponent: NavigationFunctionComponent<Props> = ({ componentId, name }) => {
  return <View />;
};

// Static options are also supported!
MyFunctionalComponent.options = {
  topBar: {
    title: {
      text: 'Hello functional component',
    },
  },
};
export default MyFunctionalComponent;
