import React from 'react';
import { Platform, PlatformOSType } from 'react-native';
import { Button } from 'react-native-ui-lib';

type RnnButtonProps = {
  platform?: PlatformOSType;

  /**
   * react-native-ui-lib does not expose the prop type for the components.
   * So used props are manually typed as a workaround.
   */
  label?: string;
  onPress?: () => void;
  testID?: string;
};

export default class RnnButton extends React.Component<RnnButtonProps> {
  render() {
    const { platform, testID, ...props } = this.props;
    // If the platform prop is provided, only render if provided platform matches the current platform.
    if (platform && platform !== Platform.OS) {
      return null;
    }

    return (
      <Button
        {...props}
        testID={testID}
        backgroundColor={testID ? undefined : '#65C888'}
        marginB-8
      />
    );
  }
}
