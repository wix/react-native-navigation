import concat from 'lodash/concat';
import React from 'react';
import { BackHandler, StyleSheet, View } from 'react-native';
import {
  NavigationButtonPressedEvent,
  NavigationComponent,
  NavigationComponentProps,
  Options,
} from 'react-native-navigation';
import Button from '../components/Button';
import Root from '../components/Root';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';
import Screens from './Screens';

const {
  PUSHED_SCREEN_HEADER,
  TOP_BAR_BTN,
  PUSH_BTN,
  POP_BTN,
  PUSH_NO_ANIM_BTN,
  POP_USING_STACK_ID_BTN,
  POP_USING_PREVIOUS_SCREEN_ID_BTN,
  POP_TO_FIRST_SCREEN_BTN,
  POP_TO_ROOT_BTN,
  ADD_BACK_HANDLER,
  REMOVE_BACK_HANDLER,
  SET_STACK_ROOT_BUTTON,
  PUSH_OPTIONS_BUTTON,
  HIDE_PREVIOUS_SCREEN_TOP_BAR,
  BACK_BUTTON,
} = testIDs;

interface Props extends NavigationComponentProps {
  previousScreenIds: string[];
  stackPosition: number;
}

export default class PushedScreen extends NavigationComponent<Props> {
  static options(): Options {
    return {
      topBar: {
        testID: PUSHED_SCREEN_HEADER,
        title: {
          text: 'Pushed Screen',
        },
        rightButtons: [
          {
            id: 'singleBtn',
            text: 'single',
            testID: TOP_BAR_BTN,
          },
        ],
        backButton: {
          testID: BACK_BUTTON,
          enableMenu: false,
        },
      },
    };
  }

  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  navigationButtonPressed({ buttonId }: NavigationButtonPressedEvent) {
    if (buttonId === 'backPress') alert('back button clicked');
  }

  render() {
    const stackPosition = this.getStackPosition();
    return (
      <Root componentId={this.props.componentId} footer={`Stack Position: ${stackPosition}`}>
        <View style={styles.container}>
          <Button label="Push" testID={PUSH_BTN} onPress={this.push} marginH-5 />
          <Button label="Pop" testID={POP_BTN} onPress={this.pop} marginH-5 />
        </View>
        <Button
          label="Push Without Animation"
          testID={PUSH_NO_ANIM_BTN}
          onPress={this.pushWithoutAnimations}
        />
        <Button
          label="Pop Using Stack ID"
          testID={POP_USING_STACK_ID_BTN}
          onPress={this.popUsingStackId}
        />
        {stackPosition > 1 && (
          <Button
            label="Pop Using Previous Screen ID"
            testID={POP_USING_PREVIOUS_SCREEN_ID_BTN}
            onPress={this.popUsingPreviousScreenId}
          />
        )}
        {stackPosition > 2 && (
          <Button
            label="Pop to First Screen"
            testID={POP_TO_FIRST_SCREEN_BTN}
            onPress={this.popToFirstScreen}
          />
        )}
        <Button label="Pop to Root" testID={POP_TO_ROOT_BTN} onPress={this.popToRoot} />
        <Button label="Add BackHandler" testID={ADD_BACK_HANDLER} onPress={this.addBackHandler} />
        <Button
          label="Remove BackHandler"
          testID={REMOVE_BACK_HANDLER}
          onPress={this.removeBackHandler}
        />
        <Button label="Set Stack Root" testID={SET_STACK_ROOT_BUTTON} onPress={this.setStackRoot} />
        <Button
          label="Push Options Screen"
          testID={PUSH_OPTIONS_BUTTON}
          onPress={this.pushOptionsScreen}
        />
        <Button
          label="Hide previous screen top bar"
          testID={HIDE_PREVIOUS_SCREEN_TOP_BAR}
          onPress={this.hidePreviousScreenTopBar}
        />
      </Root>
    );
  }

  push = () =>
    Navigation.push<Props>(this, {
      component: {
        name: Screens.Pushed,
        passProps: this.createPassProps(),
        options: {
          topBar: {
            title: {
              text: `Pushed ${this.getStackPosition() + 1}`,
            },
          },
        },
      },
    });

  pop = () => Navigation.pop(this);

  popUsingStackId = () => Navigation.pop('StackId');

  popUsingPreviousScreenId = () =>
    Navigation.pop(this.props.previousScreenIds[this.getStackPosition() - 2]);

  pushWithoutAnimations = () =>
    Navigation.push(this, {
      component: {
        name: Screens.Pushed,
        passProps: this.createPassProps(),
        options: {
          animations: {
            push: { enabled: false },
            pop: { enabled: false },
          },
        },
      },
    });

  pushOptionsScreen = () =>
    Navigation.push(this, {
      component: {
        name: Screens.Options,
      },
    });

  popToFirstScreen = () => Navigation.popTo(this.props.previousScreenIds[0]);

  popToRoot = () => Navigation.popToRoot(this);

  hidePreviousScreenTopBar = () =>
    Navigation.mergeOptions(this.props.previousScreenIds[this.getStackPosition() - 1], {
      topBar: {
        visible: false,
      },
    });

  setStackRoot = () =>
    Navigation.setStackRoot(this, [
      {
        component: {
          name: Screens.Pushed,
          passProps: {
            stackPosition: this.getStackPosition() + 1,
            previousScreenIds: concat(
              [],
              this.props.previousScreenIds || [],
              this.props.componentId
            ),
          },
          options: {
            topBar: {
              title: {
                text: `Pushed ${this.getStackPosition() + 1} a`,
              },
            },
          },
        },
      },
      {
        component: {
          name: Screens.Pushed,
          passProps: {
            stackPosition: this.getStackPosition() + 1,
            previousScreenIds: concat(
              [],
              this.props.previousScreenIds || [],
              this.props.componentId
            ),
          },
          options: {
            topBar: {
              title: {
                text: `Pushed ${this.getStackPosition() + 1} b`,
              },
            },
          },
        },
      },
    ]);

  addBackHandler = () => BackHandler.addEventListener('hardwareBackPress', this.backHandler);

  removeBackHandler = () => BackHandler.removeEventListener('hardwareBackPress', this.backHandler);

  backHandler = () => {
    this.setState({
      backPress: 'Back button pressed!',
    });
    return true;
  };

  createPassProps = () => {
    return {
      stackPosition: this.getStackPosition() + 1,
      previousScreenIds: concat([], this.props.previousScreenIds || [], this.props.componentId),
    } as Props;
  };
  getStackPosition = () => this.props.stackPosition || 1;
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
});
