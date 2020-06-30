import React from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Screens from './Screens';
import Navigation from '../services/Navigation';
import { stack } from '../commons/Layouts';
import testIDs from '../testIDs';

const { PUSH_BTN, MODAL_BTN, REGISTER_MODAL_DISMISS_EVENT_BTN } = testIDs;

export default class ExternalComponentScreen extends React.Component<NavigationComponentProps> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'External Component',
        },
      },
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Push" testID={PUSH_BTN} onPress={this.push} />
        <Button label="Show Modal" testID={MODAL_BTN} onPress={this.modal} />
        <Button
          label="Register modal dismiss event"
          testID={REGISTER_MODAL_DISMISS_EVENT_BTN}
          onPress={this.registerModalDismissEvent}
          platform="ios"
        />
      </Root>
    );
  }

  registerModalDismissEvent = () => Navigation.events().registerModalDismissedListener(() => {});

  push = () =>
    Navigation.push(this, {
      externalComponent: {
        name: Screens.NativeScreen,
        passProps: {
          text: 'This is an external component',
        },
      },
    });
  modal = () =>
    Navigation.showModal(
      stack([
        Screens.Pushed,
        {
          externalComponent: {
            name: Screens.NativeScreen,
            passProps: {
              text: 'External component in deep stack',
            },
            options: {
              topBar: {
                title: {
                  text: 'External Component',
                },
              },
            },
          },
        },
      ])
    );
}
