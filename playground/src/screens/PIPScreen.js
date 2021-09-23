const React = require('react');
const Button = require('../components/Button');
const Root = require('../components/Root');
const Navigation = require('./../services/Navigation');
const { Dimensions, Image, Text } = require('react-native');

class PIPScreen extends React.Component {
  render() {
    return (
      <Root style={styles.root} nativeID="rootContainer">
        <Image style={styles.image} source={require('../../img/2048.jpeg')} nativeID="pipImage" />
        <Text nativeID="description">Keyboard e2e kjdbfdjkfsjkdf kjdfhdjkfhsdjkfhsj</Text>
        <Button label="Switch To PIP" onPress={this.switchToPIP} nativeID="switchButton" />
      </Root>
    );
  }

  onPIPStateChanged(prevState, nextState) {
    console.log({ prevState, nextState });
  }

  onPIPButtonPressed(nextState) {
    console.log({ nextState });
  }

  switchToPIP = () => Navigation.switchToPIP(this.props.componentId);
}

module.exports = PIPScreen;
let screenWidth = Dimensions.get('window').width;
const styles = {
  root: {
    backgroundColor: 'red',
  },
  text: {
    fontSize: 14,
    textAlign: 'center',
    marginBottom: 8,
  },
  image: {
    height: 250,
    width: screenWidth,
    resizeMode: 'cover',
  },
};
