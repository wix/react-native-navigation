const React = require('react');
const { Text, Button, View } = require('react-native');
const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

function Alert({ componentId, title, message }) {
  const onCLickOk = () => Navigation.dismissOverlay(componentId);

  return (
    <View style={styles.root} key={'overlay'}>
      <View style={styles.alert}>
        <Text style={styles.title} testID={testIDs.DIALOG_HEADER}>{title}</Text>
        <Text style={styles.message}>{message}</Text>
        <Button title='OK' testID={testIDs.OK_BUTTON} onPress={onCLickOk} />
      </View>
    </View>
  );
}

const styles = {
  root: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#00000050',
  },
  alert: {
    alignItems: 'center',
    backgroundColor: 'whitesmoke',
    width: 250,
    elevation: 4,
    padding: 16
  },
  title: {
    fontSize: 18,
    alignSelf: 'flex-start'
  },
  message: {
    marginVertical: 8
  }
};

Alert.options = (props) => {
  return ({
    layout: {
      componentBackgroundColor: 'transparent'
    },
    overlay: {
      interceptTouchOutside: true
    }
  });
}

module.exports = Alert;