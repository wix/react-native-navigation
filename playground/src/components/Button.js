const React = require('react');
const {Button} = require('react-native-ui-lib');
module.exports = (props) =>
  <Button
    {...props}
    style={{
      marginBottom: 8,
    }}
  />;
