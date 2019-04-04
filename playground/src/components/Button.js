const React = require('react');
const { Button } = require('react-native-ui-lib');
class RnnButton extends React.PureComponent {
  render() {
    return (
      <Button
        {...this.props}
        style={{
          marginBottom: 8,
        }}
      />
    )
  }
}

module.exports = RnnButton