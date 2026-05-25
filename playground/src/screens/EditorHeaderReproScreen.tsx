import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import {
  Navigation as Nav,
  NavigationButtonPressedEvent,
  NavigationComponent,
  NavigationProps,
  Options,
} from 'react-native-navigation';
import Root from '../components/Root';
import Navigation from '../services/Navigation';

const EDITOR_TOP_BAR_COLOR = '#0B0D17';

/** Mirrors member-view screenPicker (no 44pt wrapper, chevron marginTop). */
function EditorScreenPickerRepro() {
  return (
    <View style={pickerStyles.row} testID="EDITOR_REPRO_SCREEN_PICKER">
      <Text style={pickerStyles.prefix}>Screens</Text>
      <Text style={pickerStyles.value}>Home</Text>
      <Text style={pickerStyles.chevron}>▼</Text>
    </View>
  );
}

/** Mirrors topBarPublishButton (link label, no 44pt wrapper). */
function EditorPublishRepro() {
  return (
    <View style={publishStyles.container} testID="EDITOR_REPRO_PUBLISH">
      <Text style={publishStyles.label}>Publish</Text>
    </View>
  );
}

Nav.registerComponent('EditorScreenPickerRepro', () => EditorScreenPickerRepro);
Nav.registerComponent('EditorPublishRepro', () => EditorPublishRepro);

const pickerStyles = StyleSheet.create({
  row: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  prefix: {
    color: '#868AA5',
    fontSize: 14,
    marginRight: 4,
  },
  value: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '600',
  },
  chevron: {
    color: '#FFFFFF',
    fontSize: 10,
    marginLeft: 8,
    marginTop: 4,
  },
});

const publishStyles = StyleSheet.create({
  container: {
    justifyContent: 'center',
  },
  label: {
    color: '#116DFF',
    fontSize: 14,
    fontWeight: '600',
  },
});

interface Props extends NavigationProps {}

export default class EditorHeaderReproScreen extends NavigationComponent<Props> {
  static options(): Options {
    return {
      topBar: {
        visible: true,
        background: {
          color: EDITOR_TOP_BAR_COLOR,
        },
        borderHeight: 0,
        noBorder: true,
        elevation: 0,
        leftButtons: [
          {
            id: 'back',
            icon: require('../../img/clear.png'),
            color: '#FFFFFF',
          },
          {
            id: 'tabPicker',
            component: {
              id: 'EditorScreenPickerRepro',
              name: 'EditorScreenPickerRepro',
            },
          },
        ],
        rightButtons: [
          {
            id: 'publish',
            component: {
              id: 'EditorPublishRepro',
              name: 'EditorPublishRepro',
            },
          },
          {
            id: 'more',
            icon: require('../../img/star.png'),
            color: '#FFFFFF',
          },
        ],
      },
    };
  }

  constructor(props: Props) {
    super(props);
    Nav.events().bindComponent(this);
  }

  navigationButtonPressed({ buttonId }: NavigationButtonPressedEvent) {
    if (buttonId === 'back') {
      Navigation.pop(this);
    }
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <View style={styles.body}>
          <Text style={styles.hint}>
            Editor header repro: Home picker (left) vs Publish (right). Compare vertical alignment in
            the top bar.
          </Text>
        </View>
      </Root>
    );
  }
}

const styles = StyleSheet.create({
  body: {
    flex: 1,
    padding: 16,
    backgroundColor: '#FFFFFF',
  },
  hint: {
    fontSize: 14,
    color: '#333333',
  },
});
