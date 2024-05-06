import React from 'react';
import { Platform, PlatformOSType, ButtonProps, Text, Pressable } from 'react-native';
import colors from '../commons/Colors';

type RnnButtonProps = {
  platform?: PlatformOSType;
  label?: string;
} & ButtonProps;

export default class RnnButton extends React.Component<RnnButtonProps> {
  render() {
    const { platform, ...props } = this.props;
    // If the platform prop is provided, only render if provided platform matches the current platform.
    if (platform && platform !== Platform.OS) {
      return null;
    }

    return (
      <Pressable {...props} style={{ backgroundColor: props.testID ? colors.accent.light : '#65C888', marginBottom: 8, alignItems: 'center', justifyContent: 'center', padding: 8, borderRadius: 8 }}>
        <Text>{props.label ?? props.title}</Text>
      </Pressable>
    );
  }
}
