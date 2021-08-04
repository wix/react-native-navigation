import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';
import Colors from '../commons/Colors';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const { TOAST_OK_BTN_INNER, TOAST_OK_BTN_OUTER } = testIDs;

export default function Toast({ componentId }: NavigationComponentProps) {
  const dismiss = (txt: string) => {
    alert(txt);
    Navigation.dismissOverlay(componentId);
  };

  return (
    <View style={styles.root}>
      <TouchableOpacity
        style={styles.outerTouchable}
        testID={TOAST_OK_BTN_OUTER}
        onPress={() => dismiss('Outer button clicked')}
      >
        <View style={styles.toast}>
          <Text style={styles.text}>This a very important message!</Text>
          <TouchableOpacity
            testID={TOAST_OK_BTN_INNER}
            style={styles.button}
            onPress={() => dismiss('Inner button clicked')}
          >
            <Text style={styles.buttonText}>OK</Text>
          </TouchableOpacity>
        </View>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    flexDirection: 'column-reverse',
    backgroundColor: '#3e434aa1',
  },
  outerTouchable: {
    margin: 16,
  },
  toast: {
    elevation: 2,
    flexDirection: 'row',
    height: 40,
    borderRadius: 20,
    backgroundColor: Colors.accent.light,
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  text: {
    color: 'white',
    fontSize: 16,
    marginLeft: 16,
  },
  button: {
    marginRight: 16,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

Toast.options = {
  statusBar: {
    drawBehind: true,
    backgroundColor: 0x3e434aa1,
    style: 'light',
  },
  layout: {
    componentBackgroundColor: 'transparent',
  },
  overlay: {
    interceptTouchOutside: false,
  },
};
