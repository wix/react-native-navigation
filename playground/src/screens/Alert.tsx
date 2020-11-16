import React from 'react';
import { StyleSheet, Text, Button, View, ViewStyle, TextStyle } from 'react-native';
import { Navigation, NavigationComponentProps } from 'react-native-navigation';
import testIDs from '../testIDs';

interface Props extends NavigationComponentProps {
  title: string;
  message: string;
}

export default function Alert({ componentId, title, message }: Props) {
  const onCLickOk = () => Navigation.dismissOverlay(componentId);

  return (
    <View style={styles.root} key={'overlay'}>
      <View style={styles.alert}>
        <Text style={styles.title}>{title}</Text>
        <Text style={styles.message}>{message}</Text>
        <Button title="OK" testID={testIDs.OK_BUTTON} onPress={onCLickOk} />
      </View>
    </View>
  );
}

type Style = {
  root: ViewStyle;
  alert: ViewStyle;
  title: TextStyle;
  message: TextStyle;
};

const styles = StyleSheet.create<Style>({
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
    padding: 16,
  },
  title: {
    fontSize: 18,
    alignSelf: 'flex-start',
  },
  message: {
    marginVertical: 8,
  },
});

Alert.options = () => {
  return {
    layout: {
      componentBackgroundColor: 'transparent',
    },
    overlay: {
      interceptTouchOutside: true,
    },
  };
};
