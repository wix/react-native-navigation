import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Image
} from 'react-native';
import {SharedElementTransition} from 'react-native-navigation';

export default class HeroScreen extends Component {
  static navigatorStyle = {
    drawUnderNavBar: false,
  };

  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={{height: 120}}>
          <Image
            sourcee={this.props.header}
            style={styles.header}
          />
        </View>
        <SharedElementTransition
          key={this.props.sharedIconId}
          sharedElementId={this.props.sharedIconId}
          style={styles.iconContainer}
        >
          <Image
            source={this.props.icon}
            style={styles.heroIcon}
          />
        </SharedElementTransition>
      </View>
    );
  }

  onNavigatorEvent(event) {

  }
}
//#90CAF9
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'transparent',
    flexDirection: 'column'
  },
  iconContainer: {
    position: 'absolute',
    top: 90,
    left: 16
  },
  header: {
    height: 120,
    resizeMode: 'cover'
  },
  heroIcon: {
    width: 100,
    height: 100,
    resizeMode: 'contain'
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop: 10,
    color: 'blue'
  }
});
