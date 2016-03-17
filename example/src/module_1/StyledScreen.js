import React, {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  Image,
  StyleSheet,
  AlertIOS
} from 'react-native';

// important imports, the magic is here
import { Screen } from 'react-native-navigation';

// instead of React.Component, we extend Screen (imported above)
export default class StyledScreen extends Screen {
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
    this.navigator.push({
      title: "More",
      screen: "module_2.PushedScreen"
    });
  }
  onPushStyledPress() {
    this.navigator.push({
      title: "More",
      screen: "module_1.StyledScreen"
    });
  }
  onPopPress() {
    this.navigator.pop();
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