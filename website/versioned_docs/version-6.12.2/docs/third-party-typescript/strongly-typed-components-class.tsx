import { NavigationComponent, NavigationComponentProps } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  age: number;
}

export default class MyComponent extends NavigationComponent<Props> {
  // Options are strongly typed
  static options() {
    return {
      statusBar: {
        // Some options are of union type. We're using `as const` to let the-
        // TS compiler know this value is not a regular string
        style: 'dark' as const,
      },
      topBar: {
        title: {
          text: 'My Screen',
        },
      },
    };
  }

  constructor(props: Props) {
    super(props);
  }
}
