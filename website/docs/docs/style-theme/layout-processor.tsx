import { Layout, Navigation } from 'react-native-navigation';

interface ScreenProps {
  theme: 'light' | 'dark';
}

function isScreenProps(obj: unknown): obj is ScreenProps {
  return typeof obj === 'object' && obj !== null && typeof (obj as ScreenProps).theme === 'string';
}

Navigation.addLayoutProcessor((layout: Layout, commandName: string) => {
  layout.stack?.children?.forEach((child) => {
    if (!child.component) {
      return;
    }
    const props = child.component.passProps;
    if (isScreenProps(props) && props.theme === 'dark') {
      child.component.options = {
        topBar: {
          background: {
            color: 'black',
          },
        },
      };
    }
  });
  return layout;
});
