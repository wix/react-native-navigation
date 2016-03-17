import React, {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet
} from 'react-native';

// important imports, the magic is here
import { Screen } from 'react-native-navigation';

// instead of React.Component, we extend Screen (imported above)
export default class PushedScreen extends Screen {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <View style={{flex: 1, padding: 20}}>

        <TouchableOpacity onPress={ this.onPushPress.bind(this) }>
          <Text style={styles.button}>Push Plain Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPushStyledPress.bind(this) }>
          <Text style={styles.button}>Push Styled Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPopPress.bind(this) }>
          <Text style={styles.button}>Pop Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPopToRootPress.bind(this) }>
          <Text style={styles.button}>Pop To Root</Text>
        </TouchableOpacity>

      </View>
    );
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
  onPopToRootPress() {
    this.navigator.popToRoot();
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