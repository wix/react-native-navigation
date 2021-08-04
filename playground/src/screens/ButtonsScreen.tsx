/* eslint-disable prettier/prettier */
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
  TOGGLE_BACK,
  BACK_BUTTON,
  TOP_BAR,
  ROUND_BUTTON,
  BUTTON_ONE,
  BUTTON_THREE,
  SET_RIGHT_BUTTONS,
  ADD_BUTTON_RIGHT,
  ADD_BUTTON_ROUND,
  ADD_COMPONENT_BUTTON,
  LEFT_BUTTON,
  TEXTUAL_LEFT_BUTTON,
  SHOW_LIFECYCLE_BTN,
  RESET_BUTTONS,
  CHANGE_BUTTON_PROPS,
  CHANGE_LEFT_RIGHT_COLORS,
} = testIDs;

export default class ButtonOptions extends NavigationComponent {

  backButtonVisibile = false

  static options(): Options {
    return {
      fab: {
        id: 'fab',
        icon: require('../../img/navicon_add.png'),
        backgroundColor: Colors.accent,
        rippleColor: Colors.primary,
        clickColor: Colors.secondary,
        iconColor: Colors.iconTint,
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
        ],
        leftButtons: [
          {
            id: 'LEFT',
            testID: LEFT_BUTTON,
            icon: require('../../img/clear.png'),
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
        <Button
          testID={ADD_BUTTON_RIGHT}
          label="Add End (Right) button"
          onPress={this.addRightButton}
        />
        <Button
          testID={SET_RIGHT_BUTTONS}
          label="Set Right buttons"
          onPress={this.setRightButtons}
        />
        <Button
          testID={ADD_BUTTON_ROUND}
          label="Add End (Right) Round button"
          onPress={this.addRoundButton}
        />
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
        <Button
          label="Toggle back"
          testID={TOGGLE_BACK}
          onPress={this.toggleBack}
        />
      </Root>
    );
  }

  toggleBack= ()=> {
    this.backButtonVisibile = !this.backButtonVisibile;
    Navigation.mergeOptions(this.props.componentId,{
      topBar:{
        backButton:{
          testID:BACK_BUTTON,
          visible:this.backButtonVisibile
        }
      }
    })
  }

  setRightButtons = () =>
    Navigation.mergeOptions(this, {
      topBar: {
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
              },
            },
          },
          {
            id: 'Three',
            text: 'Three',
            testID: BUTTON_THREE,
            color:Colors.buttonColor,
          },
        ],
      },
    });

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
      enabled: currentCount % 2 === 0,
    });
    Navigation.mergeOptions(this, {
      topBar: {
        rightButtons: this.rightButtons,
      },
    });
  };

  addRoundButton = () => {
    this.rightButtons = [];
    this.rightButtons.push({
      id: `ROUND`,
      testID: ROUND_BUTTON,
      component: {
        name: Screens.RoundButton,
        passProps: {
          title: 'Two',
          timesCreated: 1,
        },
      },
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
                timesCreated: 1,
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
