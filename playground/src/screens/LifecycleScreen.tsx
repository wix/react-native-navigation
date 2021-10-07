import React from 'react';
import { NavigationComponentProps, NavigationButtonPressedEvent } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

const { PUSH_TO_TEST_DID_DISAPPEAR_BTN, DISMISS_MODAL_BTN, SCREEN_POPPED_BTN, POP_BTN } = testIDs;

interface Props extends NavigationComponentProps {
  isModal?: boolean;
}

interface State {
  text: string;
}

export default class LifecycleScreen extends React.Component<Props, State> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Lifecycle Screen',
        },
      },
    };
  }
  state = {
    text: 'nothing yet',
  };

  showUnmountAndDisappearAlerts = true;

  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  componentDidAppear() {
    this.setState({ text: 'didAppear' });
  }

  componentDidDisappear() {
    this.showUnmountAndDisappearAlerts && alert('didDisappear'); // eslint-disable-line no-alert
  }

  componentWillUnmount() {
    setTimeout(() => {
      this.showUnmountAndDisappearAlerts && alert('componentWillUnmount'); // eslint-disable-line no-alert
    }, 100);
  }

  navigationButtonPressed(id: NavigationButtonPressedEvent) {
    alert(`navigationButtonPressed: ${id}`); // eslint-disable-line no-alert
  }

  render() {
    return (
      <Root componentId={this.props.componentId} footer={this.state.text}>
        <Button
          label="Push to test didDisappear"
          testID={PUSH_TO_TEST_DID_DISAPPEAR_BTN}
          onPress={this.push}
        />
        {!this.props.isModal && (
          <Button
            label="Screen popped events"
            testID={SCREEN_POPPED_BTN}
            onPress={this.screenPoppedEvent}
          />
        )}
        {this.renderCloseButton()}
      </Root>
    );
  }

  renderCloseButton = () =>
    this.props.isModal ? (
      <Button label="Dismiss" testID={DISMISS_MODAL_BTN} onPress={this.dismiss} />
    ) : (
      <Button label="Pop" testID={POP_BTN} onPress={this.pop} />
    );

  push = () => Navigation.push(this, Screens.Pushed);
  screenPoppedEvent = async () => {
    this.showUnmountAndDisappearAlerts = false;
    const promise = new Promise<void>((resolve) => {
      const unregister = Navigation.events().registerScreenPoppedListener(() => {
        alert('Screen popped event');
        unregister.remove();
        resolve();
      });
    });
    await Promise.all([Navigation.pop(this), promise]);
  };
  pop = () => Navigation.pop(this);
  dismiss = () => Navigation.dismissModal(this);
}
