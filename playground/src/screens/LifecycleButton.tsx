import RoundedButton from './RoundedButton';

export default class LifecycleButton extends RoundedButton {
  componentWillUnmount() {
    alert('Button component unmounted');
  }
}
