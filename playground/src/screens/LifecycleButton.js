const RoundedButton = require('./RoundedButton');

class LifecycleButton extends RoundedButton {
  componentWillUnmount() {
    alert('Button component unmounted');
  }
}

module.exports = LifecycleButton;
