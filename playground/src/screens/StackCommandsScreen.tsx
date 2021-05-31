import React from 'react';
import { Text } from 'react-native';
import { NavigationComponent, NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

const { PUSH_BTN, PUSH_PROMISE_RESULT, POP_PROMISE_RESULT } = testIDs;

interface State {
  pushPromiseResult?: string;
  popPromiseResult?: string;
}

export default class StackCommandsScreen extends NavigationComponent<
  NavigationComponentProps,
  State
> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Stack Commands',
        },
      },
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Text testID={PUSH_PROMISE_RESULT}>{this.state?.pushPromiseResult || ''}</Text>
        <Text testID={POP_PROMISE_RESULT}>{this.state?.popPromiseResult || ''}</Text>
        <Button label="Push" testID={PUSH_BTN} onPress={this.push} />
      </Root>
    );
  }

  push = async () => {
    return Navigation.push(this.props.componentId, {
      component: {
        id: 'ChildId',
        name: Screens.Pushed,
      },
    })
      .then(
        (pushId) => new Promise<string>((resolve) => setTimeout(() => resolve(pushId), 100))
      )
      .then((pushId) => {
        this.setState({
          pushPromiseResult: `push promise resolved with: ${pushId}`,
        });
        return Navigation.pop('ChildId');
      })
      .then((popId) => {
        this.setState({
          popPromiseResult: `pop promise resolved with: ${popId}`,
        });
      });
  };
}
