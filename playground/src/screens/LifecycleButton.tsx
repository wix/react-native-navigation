import showAlert from '../services/Alert';
import RoundedButton from './RoundedButton';

export default class LifecycleButton extends RoundedButton {
  componentWillUnmount() {
    showAlert('Button component unmounted');
  }
}
