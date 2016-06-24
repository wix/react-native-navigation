import { ToastAndroid } from 'react-native';

/**
 * Generic alert interface for Android and iOS.
 * Title is currently ignored on Android since toasts don't have titles.
 */
function show(title, message) {
  ToastAndroid.show(message, ToastAndroid.SHORT);
}

export default { show };
