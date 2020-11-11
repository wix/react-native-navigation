import React from 'react';
import { StyleSheet, View, ViewStyle } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';

interface TopBarBackgroundProps extends NavigationComponentProps {
  color: string;
}

const TopBarBackground = ({ color }: TopBarBackgroundProps) => {
  return (
    <View style={styles.container}>
      {new Array(55).fill('').map((_, i) => (
        <View key={'dot' + i} style={[styles.dot, { backgroundColor: color || '#d3d3d3' }]} />
      ))}
    </View>
  );
};

type Style = {
  container: ViewStyle;
  dot: ViewStyle;
};

const styles = StyleSheet.create<Style>({
  container: {
    flex: 1,
    flexDirection: 'row',
    backgroundColor: '#EDEDED',
    flexWrap: 'wrap',
  },
  dot: {
    height: 16,
    width: 16,
    borderRadius: 8,
    margin: 4,
  },
});

export default TopBarBackground;
