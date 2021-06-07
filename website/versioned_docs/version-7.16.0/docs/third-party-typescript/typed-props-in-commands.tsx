import { Navigation } from 'react-native-navigation';

interface Props {
  name: string;
}

Navigation.push<Props>(componentId, {
  component: {
    name: 'MyComponent',
    passProps: {
      name: 'Bob',
      // @ts-expect-error
      color: 'red', // Compilation error! color isn't declared in Props
    },
  },
});
