// Minimal entry point for Android instrumented tests
// This registers a no-op component to satisfy React Native's initialization
import { AppRegistry } from 'react-native';
AppRegistry.registerComponent('RNNTest', () => () => null);
