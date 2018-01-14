import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  TextInput,
  Alert
} from 'react-native';
import {connect} from 'react-redux';
import * as counterActions from '../reducers/counter/actions';

const leftButtons = ['back', 'cancel', 'accept', 'sideMenu'];
let bottomTabsVisible = true;

// this is a traditional React component connected to the redux store
class PushedScreen extends Component {
  static navigatorStyle = {
    statusBarColor: '#303F9F',
    toolBarColor: '#3F51B5',
    navigationBarColor: '#303F9F',
    tabSelectedTextColor: '#FFA000',
    tabNormalTextColor: '#FFC107',
    tabIndicatorColor: '#FF4081'
  };

  static navigatorButtons = {
    leftButton: {
      id: 'back',
      color: '#00ff00'
    },
    fab: {
      collapsedId: 'home',
      collapsedIcon: require('../../img/ic_home.png'),
      backgroundColor: '#607D8B'
    }
  };

  constructor(props) {
    super(props);
    console.log('PushedScreen', 'constructor');
    this.bgColor = this.getRandomColor();
    console.log(`constructor ${this.bgColor}`);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
    this.currentBackButton = 0;
  }

  componentWillUnmount() {
    console.log('PushedScreen', `componentWillUnmount ${this.bgColor}`);
  }

  componentWillMount() {
    console.log('PushedScreen', 'componentWillMount');
  }

  getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  onNavigatorEvent(event) {
    switch (event.id) {
      case 'cancel':
        Alert.alert('NavBar', 'Cancel button pressed');
        break;

      case 'accept':
        Alert.alert('NavBar', 'Accept button pressed');
        break;

      case 'backPress':
        this.handleBackPress();
        break;

      case 'tabSelected':
        this.onTabSelected();
        break;

      default:
        console.log('PushedScreen', 'Unknown event ' + event.id);
    }
  }

  onTabSelected() {
    console.log('PushedScreen', 'onTabSelected');
    this.props.navigator.setButtons({
      rightButtons: [
        {
          id: 'account',
          icon: require('../../img/ic_add_alert.png')
        }
      ]
    });
  }


  handleBackPress() {
    Alert.alert(
      'Back button press!',
      'Handling back press in JS',
      [
        {text: 'Pop', onPress: () => this.props.navigator.pop()}
      ]
    )
  }

  render() {
    return (
      <ScrollView style={{flex: 1, padding: 20, backgroundColor: this.bgColor}}>

        <Text style={styles.text}>
          <Text style={{fontWeight: '500'}}>Counter: </Text> {this.props.counter.count}
        </Text>

        <TouchableOpacity onPress={ this.onIncrementPress.bind(this) }>
          <Text style={styles.button}>Increment Counter</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPushPress.bind(this) }>
          <Text style={styles.button}>Push Another</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPopPress.bind(this) }>
          <Text style={styles.button}>Pop Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onShowModalPress.bind(this) }>
          <Text style={styles.button}>Modal Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onDismissModal.bind(this) }>
          <Text style={styles.button}>Dismiss modal</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onDismissAllModalsPress.bind(this) }>
          <Text style={styles.button}>Dismiss all modals</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPopToRootPress.bind(this) }>
          <Text style={styles.button}>Pop to root</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onNewStackPress.bind(this) }>
          <Text style={styles.button}>New Stack</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onChangeLeftButtonPress.bind(this) }>
          <Text style={styles.button}>Change Left Button</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onToggleBottomTabsPress.bind(this) }>
          <Text style={styles.button}>Toggle BottomTabs</Text>
        </TouchableOpacity>

        <TextInput style={{height: 40, borderColor: 'gray', borderWidth: 1}}/>

        <Text style={{fontWeight: '500'}}>String prop: {this.props.str}</Text>
        <Text style={{fontWeight: '500'}}>Number prop: {this.props.num}</Text>
        {this.props.obj ? <Text style={{fontWeight: '500'}}>Object prop: {this.props.obj.str}</Text> : false}
        {this.props.obj && this.props.obj.arr ? <Text style={{fontWeight: '500'}}>Array prop: {this.props.obj.arr[0].str}</Text> : false}

      </ScrollView>
    );
  }

  onIncrementPress() {
    this.props.dispatch(counterActions.increment());
  }

  onPushPress() {
    this.props.navigator.push({
      title: "List",
      screen: "example.ListScreen",
      passProps: {
        passed: 'This is a prop passed in \'navigator.push()\'!',
        obj: {
          str: 'This is a prop passed in an object!',
          arr: [
            {
              str: 'This is a prop in an object in an array in an object!'
            }
          ]
        },
        num: 1234
      }
    });
  }

  onPopPress() {
    this.props.navigator.pop();
  }

  onShowModalPress() {
    this.props.navigator.showModal({
      title: "Modal Screen",
      screen: "example.PushedScreen",
      passProps: {
        passed: 'This is a prop passed in \'navigator.showModal()\'!',
        obj: {
          str: 'This is a prop passed in an object!',
          arr: [
            {
              str: 'This is a prop in an object in an array in an object!'
            }
          ]
        },
        num: 1234
      }
    });
  }

  onDismissAllModalsPress() {
    this.props.navigator.dismissAllModals();
  }

  onDismissModal() {
    this.props.navigator.dismissModal();
  }

  onPopToRootPress() {
    this.props.navigator.popToRoot();
  }

  onNewStackPress() {
    this.props.navigator.resetTo({
      title: "NEW STACK",
      screen: "example.PushedScreen",
      passProps: {
        passed: 'This is a prop passed in \'navigator.push()\'!',
        obj: {
          str: 'This is a prop passed in an object!',
          arr: [
            {
              str: 'This is a prop in an object in an array in an object!'
            }
          ]
        },
        num: 1234
      }
    });
  }

  onChangeLeftButtonPress() {
    this.props.navigator.setButtons({
      leftButton: {
        id: leftButtons[this.currentBackButton]
      }
    });
    this.currentBackButton += 1;
    this.currentBackButton = this.currentBackButton % 4;
  }

  onToggleBottomTabsPress() {
    this.props.navigator.toggleTabs({
      to: bottomTabsVisible ? 'shown' : 'hidden',
      animated: true
    });
    bottomTabsVisible = !bottomTabsVisible;
  }
}

const styles = StyleSheet.create({
  text: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop: 10
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop: 10,
    color: 'blue'
  }
});

// which props do we want to inject, given the global state?
function mapStateToProps(state) {
  return {
    counter: state.counter
  };
}

export default connect(mapStateToProps)(PushedScreen);
