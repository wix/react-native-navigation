import { AlertIOS } from 'react-native'

/**
 * Generic alert interface for Android and iOS.
 */
function show(title, message) {
  AlertIOS.alert(title, message);
}

export default { show };