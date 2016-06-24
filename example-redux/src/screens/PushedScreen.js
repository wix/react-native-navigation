import React, {Component, PropTypes} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  TextInput
} from 'react-native';
import { connect } from 'react-redux';
import * as counterActions from '../reducers/counter/actions';

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

  static propTypes = {
    str: PropTypes.string.isRequired,
    obj: PropTypes.object.isRequired,
    num: PropTypes.number.isRequired
  };

  constructor(props) {
    super(props);
    this.bgColor = this.getRandomColor();
  }

  getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  render() {
    return (
      <View style={{flex: 1, padding: 20, backgroundColor: this.bgColor}}>

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

        <TextInput style={{height: 40, borderColor: 'gray', borderWidth: 1}}/>

        <Text style={{fontWeight: '500'}}>String prop: {this.props.str}</Text>
        <Text style={{fontWeight: '500'}}>Number prop: {this.props.num}</Text>
        <Text style={{fontWeight: '500'}}>Object prop: {this.props.obj.str}</Text>
        <Text style={{fontWeight: '500'}}>Array prop: {this.props.obj.arr[0].str}</Text>

      </View>
    );
  }

  onIncrementPress() {
    this.props.dispatch(counterActions.increment());
  }

  onPushPress() {
    this.props.navigator.push({
      title: "More",
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
}

const styles = StyleSheet.create({
  text: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10,
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
