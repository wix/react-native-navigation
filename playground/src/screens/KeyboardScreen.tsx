import React from 'react';
import { View, ScrollView, Dimensions, StyleSheet, Image, TextInput, Text } from 'react-native';
import {
  NavigationComponentProps,
  NavigationComponent,
  ComponentDidAppearEvent,
} from 'react-native-navigation';
import Navigation from '../services/Navigation';
import Button from '../components/Button';
import Screens from './Screens';
import testIDs from '../testIDs';
import { stack } from '../commons/Layouts';
const screenWidth = Dimensions.get('window').width;
const KEYBOARD_LABEL = 'Keyboard Demo';
interface Props extends NavigationComponentProps {
  title?: string;
  autoFocus?: boolean;
}

export default class KeyboardScreen extends NavigationComponent<Props> {
  static options() {
    return {
      bottomTabs: {
        drawBehind: true,
      },
      topBar: {
        title: {
          text: 'Keyboard',
        },
        backButton: {
          testID: testIDs.BACK_BUTTON,
          enableMenu: false,
        },
      },
    };
  }
  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  componentDidAppear(_event: ComponentDidAppearEvent) {
    Navigation.mergeOptions(this.props.componentId, {
      topBar: {
        title: {
          text: this.props.title ?? 'Keyboard',
        },
      },
    });
  }

  render() {
    return (
      <View style={styles.root}>
        <ScrollView>
          <Image style={styles.image} source={require('../../img/2048.jpeg')} />
          <View style={{ alignItems: 'center' }}>
            <Button
              style={styles.button}
              label={'Modal Keyboard Screen'}
              testID={testIDs.MODAL_BTN}
              onPress={async () => {
                await this.openModalKeyboard(undefined);
              }}
            />
            <Button
              style={styles.button}
              label={'Push Focused Keyboard Screen'}
              testID={testIDs.PUSH_FOCUSED_KEYBOARD_SCREEN}
              onPress={async () => {
                await this.openPushedKeyboard(undefined, true);
              }}
            />
            <Button
              style={styles.button}
              label={'Show Focused Keyboard Screen Modal'}
              testID={testIDs.MODAL_FOCUSED_KEYBOARD_SCREEN}
              onPress={async () => {
                await this.openModalKeyboard(undefined, true);
              }}
            />
            <TextInput
              style={styles.input}
              autoFocus={this.props.autoFocus}
              testID={testIDs.TEXT_INPUT1}
              placeholderTextColor="rgba(255, 0, 0, 0.5)"
              placeholder="Submit opens modal"
              onSubmitEditing={async (event) => {
                if (event.nativeEvent.text || event.nativeEvent.text.trim().length > 0)
                  await this.openModalKeyboard(event.nativeEvent.text);
              }}
            />
            <TextInput
              style={styles.input}
              testID={testIDs.TEXT_INPUT2}
              placeholderTextColor="rgba(255, 0, 0, 0.5)"
              placeholder="Submit pushes screen"
              onFocus={this.hideTabs}
              onBlur={this.showTabs}
              onSubmitEditing={async (event) => {
                if (event.nativeEvent.text || event.nativeEvent.text.trim().length > 0)
                  await this.openPushedKeyboard(event.nativeEvent.text, true);
              }}
            />
          </View>
        </ScrollView>
        <View style={styles.footer}>
          <Text style={styles.input}> {KEYBOARD_LABEL}</Text>
        </View>
      </View>
    );
  }

  openPushedKeyboard = async (text?: string, autoFocus?: boolean) => {
    await Navigation.push(this.props.componentId, {
      component: {
        name: Screens.KeyboardScreen,
        passProps: {
          title: text,
          autoFocus,
        },
      },
    });
  };

  openModalKeyboard = async (text?: string, autoFocus?: boolean) => {
    await Navigation.showModal(
      stack({
        component: {
          name: Screens.KeyboardScreen,
          passProps: { title: text, autoFocus },
        },
      })
    );
  };

  hideTabs = () => {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        visible: false,
      },
    });
  };

  showTabs = () => {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        visible: true,
      },
    });
  };
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
  },
  input: {
    color: 'red',
  },
  footer: { flex: 1, alignItems: 'center' },
  button: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    margin: 4,
  },
  image: {
    height: 220,
    width: screenWidth,
    resizeMode: 'cover',
  },
});
