import React from 'react';
import {
  Platform,
  PlatformOSType,
  StyleSheet,
  Text,
  TouchableNativeFeedbackProps,
  TouchableOpacity,
  TouchableOpacityProps,
  View,
} from 'react-native';

type RnnButtonProps = {
  platform?: PlatformOSType;
  label: string;
} & TouchableNativeFeedbackProps &
  TouchableOpacityProps;

export default class RnnButton extends React.Component<RnnButtonProps> {
  render() {
    const { platform, onPress, ...props } = this.props;
    // If the platform prop is provided, only render if provided platform matches the current platform.
    if (platform && platform !== Platform.OS) {
      return null;
    }

    const color = props.testID ? '#5B49F5' : '#65C888';

    return (
      <TouchableOpacity onPress={onPress}>
        <View style={[styles.buttonContainer, { backgroundColor: color }]}>
          <Text style={styles.buttonText} {...props}>
            {props.label}
          </Text>
        </View>
      </TouchableOpacity>
    );
  }
}

const styles = StyleSheet.create({
  buttonContainer: {
    marginBottom: 8,
    borderRadius: 999,
    padding: 12,
  },
  buttonText: {
    color: 'white',
  },
});
