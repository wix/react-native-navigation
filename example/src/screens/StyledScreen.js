import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  Image,
  StyleSheet,
  AlertIOS
} from 'react-native';

export default class StyledScreen extends Component {
  static navigatorStyle = {
    drawUnderNavBar: true,
    drawUnderTabBar: true,
    navBarTranslucent: true
  };
  static navigatorButtons = {
    rightButtons: [{
      icon: require('../../img/navicon_edit.png'),
      id: 'compose',
      testID: 'e2e_is_awesome'
    }]
  };
  constructor(props) {
    super(props);
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }
  render() {
    return (
      <ScrollView style={{flex: 1}}>

        <Image style={{width: undefined, height: 100}} source={require('../../img/colors.png')} />

        <View style={{padding: 20}}>

          <TouchableOpacity onPress={ this.onPushPress.bind(this) }>
            <Text style={styles.button}>Push Plain Screen</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onPushStyledPress.bind(this) }>
            <Text style={styles.button}>Push Styled Screen</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onPopPress.bind(this) }>
            <Text style={styles.button}>Pop Screen</Text>
          </TouchableOpacity>
  
          <TouchableOpacity onPress={ this.onSetSubtitlePress.bind(this) }>
            <Text style={styles.button}>Set Subtitle</Text>
          </TouchableOpacity>
  
          <TouchableOpacity onPress={ this.onSetTitleImagePress.bind(this) }>
            <Text style={styles.button}>Set Title Image</Text>
          </TouchableOpacity>

        </View>

      </ScrollView>
    );
  }
  onNavigatorEvent(event) {
    if (event.id == 'compose') {
      AlertIOS.alert('NavBar', 'Compose button pressed');
    }
  }
  onPushPress() {
    this.props.navigator.push({
      title: "More",
      screen: "example.PushedScreen"
    });
  }
  onPushStyledPress() {
    this.props.navigator.push({
      title: "More",
      screen: "example.StyledScreen"
    });
  }
  onPopPress() {
    this.props.navigator.pop();
  }
  
  onSetSubtitlePress() {
    this.props.navigator.setTitle({
      title: 'title',
      subtitle: 'subtitle',
      navigatorStyle: {
        navBarSubtitleTextColor: '#ff00ff',
        navBarTextColor: '#ffff00'
        
      }
    })
  }
  
  onSetTitleImagePress() {
    this.props.navigator.setTitle({
      title: 'title',
      titleImage: require('../../img/one.png'),
    })
  }
  
}

const styles = StyleSheet.create({
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10,
    color: 'blue'
  }
});
