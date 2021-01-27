import React from 'react';
import { NavigationComponent, Options, OptionsTopBarButton } from 'react-native-navigation';
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
  BUTTON_THREE,
  ADD_BUTTON,
  ADD_COMPONENT_BUTTON,
  LEFT_BUTTON,
  TEXTUAL_LEFT_BUTTON,
  SHOW_LIFECYCLE_BTN,
  RESET_BUTTONS,
  CHANGE_BUTTON_PROPS,
  CHANGE_LEFT_RIGHT_COLORS,
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
          component: {
            name: Screens.ReactTitleView,
            alignment: 'center',
            passProps: {
              text: 'Buttons',
              clickable: false,
            },
          },
        },
        rightButtons: [
          {
            id: 'ONE',
            testID: BUTTON_ONE,
            text: 'One',
          },
          {
            id: 'ROUND',
            testID: ROUND_BUTTON,
            component: {
              id: 'ROUND_COMPONENT',
              name: Screens.RoundButton,
              passProps: {
                title: 'Two',
                timesCreated: 1,
              },
            },
          },
          {
            id: 'Three',
            text: 'B3',
            testID: BUTTON_THREE,
            color: Colors.primary,
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
          {
            id: 'TextualLeft',
            testID: TEXTUAL_LEFT_BUTTON,
            text: 'Cancel',
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
        <Button testID={ADD_BUTTON} label="Add End (Right) button" onPress={this.addRightButton} />
        <Button
          testID={ADD_COMPONENT_BUTTON}
          label="Add Start (Left) component button"
          onPress={this.addComponentButtons}
        />
        <Button
          testID={CHANGE_LEFT_RIGHT_COLORS}
          label="Set leftButtons default Color"
          onPress={this.changeButtonsColor}
        />
      </Root>
    );
  }

  leftButtons: OptionsTopBarButton[] = [];
  addComponentButtons = () => {
    this.leftButtons.push({
      id: `leftButton${this.leftButtons.length}`,
      text: `L${this.leftButtons.length}`,
      testID: `leftButton${this.leftButtons.length}`,
      component: {
        name: Screens.RoundButton,
        passProps: {
          title: `L${this.leftButtons.length}`,
        },
      },
    });
    Navigation.mergeOptions(this, {
      topBar: {
        leftButtons: this.leftButtons,
      },
    });
  };
  rightButtons: OptionsTopBarButton[] = ButtonOptions.options().topBar?.rightButtons || [];

  addRightButton = () => {
    const currentCount = this.rightButtons.length;
    this.rightButtons.push({
      id: `rightButton${currentCount}`,
      testID: `rightButton${currentCount}`,
      text: `R${currentCount}`,
      showAsAction: 'ifRoom',
      color: Colors.primary,
      enabled: currentCount % 2 === 0,
    });
    Navigation.mergeOptions(this, {
      topBar: {
        rightButtons: this.rightButtons,
      },
    });
  };

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

  resetButtons = () => {
    this.rightButtons = [];
    this.leftButtons = [];
    Navigation.mergeOptions(this, {
      topBar: {
        rightButtons: [],
        leftButtons: [],
      },
    });
  };

  changeButtonsColor = () => {
    Navigation.mergeOptions(this, {
      topBar: {
        leftButtonColor: 'red',
        rightButtonColor: 'pink',
        leftButtonDisabledColor: 'gray',
        rightButtonDisabledColor: 'black',
      },
    });
  };

  changeButtonProps = () => {
    Navigation.updateProps('ROUND_COMPONENT', {
      title: 'Three',
    });
  };
}
