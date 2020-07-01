import React from 'react';
import {NavigationComponentProps} from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

class LazilyRegisteredScreen extends React.Component<NavigationComponentProps> {
  static options() {
    return {
      topBar: {
        testID: testIDs.LAZILY_REGISTERED_SCREEN_HEADER,
        title: {
          text: 'Lazily Registered Screen'
        }
      }
    };
  }

  constructor(props: NavigationComponentProps) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label='Pop' testID={testIDs.POP_BTN} onPress={this.pop} />
      </Root>
    );
  }

  pop = () => Navigation.pop(this);
}

export default LazilyRegisteredScreen;
