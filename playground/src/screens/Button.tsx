import React from 'react';
import {
  Platform,
  StyleSheet,
  TouchableNativeFeedback,
  TouchableOpacity,
  View,
  Text,
  ViewStyle,
  TextStyle,
} from 'react-native';
import { Navigation, NavigationComponentProps } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  title: string;
  accessibilityLabel?: string;
  color: string;
  disabled?: boolean;
  hasTVPreferredFocus?: boolean;
  onPress?: () => void;
  onPressIn?: () => void;
  testID?: string;
}

export default class Button extends React.Component<Props> {
  render() {
    const {
      accessibilityLabel,
      color,
      onPress,
      onPressIn,
      title,
      hasTVPreferredFocus,
      disabled,
      testID,
    } = this.props;
    const buttonStyles = [styles.button];
    const textStyles = [styles.text];

    if (color) {
      if (Platform.OS === 'ios') {
        textStyles.push({ color: color });
      } else {
        buttonStyles.push({ backgroundColor: color });
      }
    }

    const accessibilityTraits = ['button'];
    if (disabled) {
      buttonStyles.push(styles.buttonDisabled);
      textStyles.push(styles.textDisabled);
      accessibilityTraits.push('disabled');
    }

    const formattedTitle = Platform.OS === 'android' ? title.toUpperCase() : title;
    let Touchable: React.ElementType =
      Platform.OS === 'android' ? TouchableNativeFeedback : TouchableOpacity;

    if (typeof onPressIn === 'function') {
      Touchable = Navigation.TouchablePreview;
    }

    return (
      <Touchable
        accessibilityComponentType="button"
        accessibilityLabel={accessibilityLabel}
        accessibilityTraits={accessibilityTraits}
        hasTVPreferredFocus={hasTVPreferredFocus}
        testID={testID}
        disabled={disabled}
        onPress={onPress}
        onPressIn={onPressIn}
      >
        <View style={buttonStyles}>
          <Text style={textStyles}>{formattedTitle}</Text>
        </View>
      </Touchable>
    );
  }
}

type Style = {
  button: ViewStyle;
  text: TextStyle;
  buttonDisabled: ViewStyle;
  textDisabled: TextStyle;
};

const styles = StyleSheet.create<Style>({
  button: Platform.select<ViewStyle>({
    ios: {},
    android: {
      elevation: 4,
      backgroundColor: '#2196F3',
      borderRadius: 2,
    },
  })!,
  text: Platform.select<TextStyle>({
    ios: {
      color: '#007AFF',
      textAlign: 'center',
      padding: 8,
      fontSize: 18,
    },
    android: {
      color: 'white',
      textAlign: 'center',
      padding: 8,
      fontWeight: '500',
    },
  })!,
  buttonDisabled: Platform.select({
    ios: {},
    android: {
      elevation: 0,
      backgroundColor: '#dfdfdf',
    },
  })!,
  textDisabled: Platform.select({
    ios: {
      color: '#cdcdcd',
    },
    android: {
      color: '#a1a1a1',
    },
  })!,
});
