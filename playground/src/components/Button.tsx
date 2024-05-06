import React from 'react';
import { Platform, PlatformOSType, Button, ButtonProps } from 'react-native';

type RnnButtonProps = {
  platform?: PlatformOSType;
} & ButtonProps;

export default class RnnButton extends React.Component<RnnButtonProps> {
  render() {
    const { platform, ...props } = this.props;
    // If the platform prop is provided, only render if provided platform matches the current platform.
    if (platform && platform !== Platform.OS) {
      return null;
    }

    return <Button {...props} title={props.label ?? props.title} backgroundColor={props.testID ? undefined : '#65C888'} marginB-8 />;
  }
}
