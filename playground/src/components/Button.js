const React = require('react');
const { Button } = require('react-native-ui-lib');
const { Platform } = require('react-native');
class RnnButton extends React.PureComponent {
  render() {
    return (
      (this.props.platform ? this.props.platform === Platform.OS : true) ?
      <Button
        {...this.props}
        style={this.getStyle()}
      /> : null
    )
  }

  getStyle() {
    const style = { marginBottom: 8 };
    if (!this.props.testID) {
      style.backgroundColor = '#65C888';
    }
    return style;
  }
}

module.exports = RnnButton