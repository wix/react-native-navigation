import React from 'react';
import { NavigationComponent, Options } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import Colors from '../commons/Colors';
import testIDs from '../testIDs';

const {
  PUSH_BTN,
  TOP_BAR,
  ROUND_BUTTON,
  BUTTON_ONE,
  LEFT_BUTTON,
  SHOW_LIFECYCLE_BTN,
  RESET_BUTTONS,
  CHANGE_BUTTON_PROPS,
} = testIDs;

export default class ButtonOptions extends NavigationComponent {
  static options(): Options {
    return {
      fab: {
        id: 'fab',
        icon: require('../../img/navicon_add.png'),
        backgroundColor: Colors.secondary,
      },
      topBar: {
        testID: TOP_BAR,
        title: {
          text: 'Buttons',
        },
        rightButtons: [
          {
            id: 'ONE',
            testID: BUTTON_ONE,
            text: 'One',
            color: Colors.primary,
          },
          {
            id: 'ROUND',
            testID: ROUND_BUTTON,
            component: {
              id: 'ROUND_COMPONENT',
              name: Screens.RoundButton,
              passProps: {
                title: 'Two',
              },
            },
          },
        ],
        leftButtons: [
          {
            id: 'LEFT',
            testID: LEFT_BUTTON,
            icon: require('../../img/clear.png'),
            color: Colors.primary,
            accessibilityLabel: 'Close button',
          },
        ],
      },
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Push" testID={PUSH_BTN} onPress={this.push} />
        <Button
          label="Show Lifecycle button"
          testID={SHOW_LIFECYCLE_BTN}
          onPress={this.showLifecycleButton}
        />
        <Button label="Remove all buttons" testID={RESET_BUTTONS} onPress={this.resetButtons} />
        <Button
          label="Change Button Props"
          testID={CHANGE_BUTTON_PROPS}
          onPress={this.changeButtonProps}
        />
      </Root>
    );
  }

  push = () => Navigation.push(this, Screens.Pushed);

  showLifecycleButton = () =>
    Navigation.mergeOptions(this, {
      topBar: {
        rightButtons: [
          {
            id: 'ROUND',
            testID: ROUND_BUTTON,
            component: {
              name: Screens.LifecycleButton,
              passProps: {
                title: 'Two',
              },
            },
          },
        ],
      },
    });

  resetButtons = () =>
    Navigation.mergeOptions(this, {
      topBar: {
        rightButtons: [],
        leftButtons: [],
      },
    });

  changeButtonProps = () => {
    Navigation.updateProps('ROUND_COMPONENT', {
      title: 'Three',
    });
  };
}
