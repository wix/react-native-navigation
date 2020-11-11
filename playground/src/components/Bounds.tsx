import React from 'react';
import { View } from 'react-native';

type BoundsProps = {
  children: React.ReactNode;
};

const Bounds = ({ children }: BoundsProps) => {
  return (
    <View
      style={{
        borderWidth: 0.5,
        borderColor: 'red',
        flex: 1,
      }}
    >
      {children}
    </View>
  );
};

export default Bounds;
