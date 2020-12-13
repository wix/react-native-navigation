import React from 'react';
import { Platform, PlatformOSType } from 'react-native';
import { Button, ButtonPropTypes } from 'react-native-ui-lib';

type RnnButtonProps = {
  platform?: PlatformOSType;
} & ButtonPropTypes;

export default class RnnButton extends React.Component<RnnButtonProps> {
  render() {
    const { platform, ...props } = this.props;
    // If the platform prop is provided, only render if provided platform matches the current platform.
    if (platform && platform !== Platform.OS) {
      return null;
    }

    return <Button {...props} backgroundColor={props.testID ? undefined : '#65C888'} marginB-8 />;
  }
}
