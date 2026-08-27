import Navigation from './Navigation';
import Screens from '../screens/Screens';

export default function showAlert(title: string, message?: string) {
  return Navigation.showOverlay({
    component: {
      name: Screens.Alert,
      passProps: { title, message },
    },
  });
}
