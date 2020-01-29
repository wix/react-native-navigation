const React = require('react');
const { Button } = require('react-native-ui-lib');
class RnnButton extends React.PureComponent {
  render() {
    return (
      <Button
        {...this.props}
        style={this.getStyle()}
      />
    )
  }

  getStyle() {
    const style = {marginBottom: 8};
    if (!this.props.testID) {
      style.backgroundColor = '#65C888';
    }
    return style;
  }
}

module.exports = RnnButton