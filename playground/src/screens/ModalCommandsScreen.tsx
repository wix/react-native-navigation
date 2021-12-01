import React from 'react';
import { Text } from 'react-native';
import { NavigationComponent, NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

const {
  MODAL_BTN,
  SHOW_MODAL_PROMISE_RESULT,
  MODAL_DISMISSED_LISTENER_RESULT,
  DISMISS_MODAL_PROMISE_RESULT,
  SHOW_SIDE_MENU_MODAL,
} = testIDs;

interface State {
  showModalPromiseResult?: string;
  modalDismissedListenerResult?: string;
  dismissModalPromiseResult?: string;
}

export default class ModalScreen extends NavigationComponent<NavigationComponentProps, State> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Modal Commands',
        },
      },
    };
  }

  constructor(props: NavigationComponentProps) {
    super(props);
    Navigation.events().registerModalDismissedListener(({ componentId }) => {
      this.setState({
        modalDismissedListenerResult: `modalDismissed listener called with with: ${componentId}`,
      });
    });
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Text testID={SHOW_MODAL_PROMISE_RESULT}>{this.state?.showModalPromiseResult || ''}</Text>
        <Text testID={MODAL_DISMISSED_LISTENER_RESULT}>
          {this.state?.modalDismissedListenerResult || ''}
        </Text>
        <Text testID={DISMISS_MODAL_PROMISE_RESULT}>
          {this.state?.dismissModalPromiseResult || ''}
        </Text>
        <Button label="Show Modal" testID={MODAL_BTN} onPress={this.showModal} />
        <Button
          label="Show Side Menu Modal"
          testID={SHOW_SIDE_MENU_MODAL}
          onPress={this.showSideMenuModal}
        />
      </Root>
    );
  }

  showModal = async () => {
    return Navigation.showModal({
      stack: {
        id: 'UniqueStackId',
        children: [
          {
            component: {
              id: 'ChildId',
              name: Screens.Modal,
            },
          },
        ],
      },
    })
      .then(
        (showModalId) =>
          new Promise<string>((resolve) => setTimeout(() => resolve(showModalId), 100))
      )
      .then((showModalId) => {
        this.setState({
          showModalPromiseResult: `showModal promise resolved with: ${showModalId}`,
        });
        return Navigation.dismissModal('ChildId');
      })
      .then((dismissModalId) => {
        this.setState({
          dismissModalPromiseResult: `dismissModal promise resolved with: ${dismissModalId}`,
        });
      });
  };

  showSideMenuModal = () => Navigation.showModal(Screens.SystemUi);
}
