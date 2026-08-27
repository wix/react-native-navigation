import React from 'react';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import hoistNonReactStatics from 'hoist-non-react-statics';

const rootStyle = { flex: 1 };

// Gesture Handler 3 removes gestureHandlerRootHOC. Keep the same root layout
// and screen statics (including RNN options) on both Gesture Handler 2 and 3.
export default function withGestureHandlerRoot<P extends object>(Screen: React.ComponentType<P>) {
  function GestureRoot(props: P) {
    return (
      <GestureHandlerRootView style={rootStyle}>
        <Screen {...props} />
      </GestureHandlerRootView>
    );
  }
  return hoistNonReactStatics(GestureRoot, Screen);
}
