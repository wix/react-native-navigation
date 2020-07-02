import React from 'react';
import { NavigationComponentProps, Options } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';
import Screens from './Screens';

class LazilyRegisteredScreen extends React.Component<NavigationComponentProps> {
  static options(): Options {
    return {
      topBar: {
        testID: testIDs.LAZILY_REGISTERED_SCREEN_HEADER,
        title: {
          component: {
            name: Screens.LazyTitleView,
            alignment: 'center',
            passProps: {
              text: 'Lazily registered top bar!',
            },
          },
        },
      },
    };
  }

  constructor(props: NavigationComponentProps) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Pop" testID={testIDs.POP_BTN} onPress={this.pop} />
      </Root>
    );
  }

  pop = () => Navigation.pop(this);
}

export default LazilyRegisteredScreen;
