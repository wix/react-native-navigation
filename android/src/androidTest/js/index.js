
/**
 * Minimal entry point for Android instrumented tests
 * This registers a no-op component to satisfy React Native's initialization.
 * Used only in Android instrumented application class.
 * Regenerate bundle when upgrading React Native:
 * npx react-native bundle --platform android --dev false \
 *   --entry-file android/src/androidTest/js/index.js \
 *   --bundle-output android/src/androidTest/assets/index.android.bundle \
 *   --minify true
 */
import { AppRegistry } from 'react-native';
AppRegistry.registerComponent('RNNTest', () => () => null);

