import React from 'react';
import { View, Platform, PlatformOSType } from 'react-native';
import { Button } from 'react-native-ui-lib';

export type RnnButtonProps = {
  platform?: PlatformOSType;

  /**
   * react-native-ui-lib does not expose the prop type for the components.
   * So used props are manually typed as a workaround.
   */
  label?: string;
  onPress?: () => void;
  testID?: string;
};

const RnnButton: React.FC<RnnButtonProps> = ({ platform, testID, ...props }) => {
  // If the platform prop is provided, only render if provided platform matches the current platform.
  if (platform && platform !== Platform.OS) {
    return null;
  }

  return <Button {...props} testID={testID} backgroundColor={testID ? undefined : '#65C888'} marginB-8 />;
};

export default RnnButton;

// Keeping the old behavior to avoid a breaking change.
// This allows importing the component as `const Button = require('./Button')`.
module.exports = RnnButton;
