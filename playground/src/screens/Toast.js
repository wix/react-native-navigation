const React = require('react');
const { View, Text, StyleSheet, TouchableOpacity } = require('react-native');
const Colors = require('../commons/Colors');
const Navigation = require('../services/Navigation');

const Toast = function ({componentId}) {
  dismiss = (txt) => {
    console.log('guyca', txt);
    Navigation.dismissOverlay(componentId);
  }

  return (
    <View style={styles.root}>
      <TouchableOpacity onPress={() => dismiss('d1')}>
        <View style={styles.toast}>
          <Text style={styles.text}>This a very important message!</Text>
          <TouchableOpacity style={styles.button} onPress={() => dismiss('d2')}>
            <Text style={styles.buttonText}>OK</Text>
          </TouchableOpacity>
        </View>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    flexDirection: 'column-reverse',
    backgroundColor: 0x3e434aa1
    // backgroundColor: 'red'
  },
  toast: {
    elevation: 2,
    flexDirection: 'row',
    height: 40,
    margin: 16,
    borderRadius: 20,
    backgroundColor: Colors.accent,
    alignItems: 'center',
    justifyContent: 'space-between'
  },
  text: {
    color: 'white',
    fontSize: 16,
    marginLeft: 16
  },
  button: {
    marginRight: 16
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: 'bold'
  }
});

Toast.options = {
  statusBar: {
    drawBehind: true,
    backgroundColor: 0x3e434aa1,
    style: 'light'
  },
  layout: {
    componentBackgroundColor: 'transparent'
  },
  overlay: {
    interceptTouchOutside: false
  }
}

module.exports = Toast;