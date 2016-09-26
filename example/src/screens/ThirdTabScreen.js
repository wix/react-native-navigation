import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet
} from 'react-native';

export default class ThirdTabScreen extends Component {
  static navigatorStyle = {
    drawUnderTabBar: true
  };
  constructor(props) {
    super(props);
    this.state = {
      navBarVisability: 'shown'
    }
  }
  render() {
    return (
      <View style={styles.container}>

        <TouchableOpacity onPress={ this.onPushPress.bind(this) }>
          <Text style={styles.button}>Push Plain Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPushStyledPress.bind(this) }>
          <Text style={styles.button}>Push Styled Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPushStyled2Press.bind(this) }>
          <Text style={styles.button}>Push Styled Screen 2</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onModalPress.bind(this) }>
          <Text style={styles.button}>Show Modal Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onToggleNavBarPressed.bind(this) }>
          <Text style={styles.button}>Toggle Navigation Bar</Text>
        </TouchableOpacity>

      </View>
    );
  }
  onPushPress() {
    this.props.navigator.push({
      title: "More",
      screen: "example.PushedScreen"
    });
  }
  onPushStyledPress() {
    this.props.navigator.push({
      title: "Styled",
      screen: "example.StyledScreen"
    });
  }
  onPushStyled2Press () {
    this.props.navigator.push({
      title: "Styled",
      titleImage: require('../../img/two.png'),
      screen: "example.StyledScreen"
    });
  }
  onModalPress() {
    this.props.navigator.showModal({
      title: "Modal",
      screen: "example.ModalScreen"
    });
  }

  onToggleNavBarPressed() {
    this.state.navBarVisability = (this.state.navBarVisability === 'shown') ? 'hidden' : 'shown';
    this.props.navigator.toggleNavBar({
      to: this.state.navBarVisability,
      animated: true  // true is default
    });
  }

  componentDidUpdate() {
    console.error('this is an error: ' + Math.random());
    this.state.navBarState = 'shown';
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: 'white'
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10,
    color: 'blue'
  }
});
