import React from 'react';
import { StyleSheet, View, TouchableOpacity, Text, Image } from 'react-native';
import { Navigation, NavigationComponent, NavigationComponentProps } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  title?: string;
  timesCreated?: number;
  dismissTooltip: () => Promise<string>;
}

export default class Tooltip extends NavigationComponent<Props> {
  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  componentDidAppear() {
    console.log('Tooltip: componentDidAppear:', this.props.componentId);
  }

  componentDidDisappear() {
    console.log('Tooltip: componentDidDisappear:', this.props.componentId);
  }
  render() {
    return (
      <View style={styles.container}>
        <Image
          style={styles.tinyLogo}
          source={{
            uri: 'https://wix.github.io/react-native-navigation/img/logo.png',
          }}
        />
        <View style={styles.button}>
          <Text style={styles.text}>{'Hey, This is some text that belongs to a tooltip!!!'}</Text>
          <TouchableOpacity
            // @ts-ignore
            onPress={async () => {
              await Navigation.dismissOverlay(this.props.componentId);
            }}
          >
            <Text style={styles.text}>{'OK'}</Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    width: '70%',
    backgroundColor: 'white',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 4,
  },
  button: {
    borderRadius: 12,
    justifyContent: 'center',
  },
  tinyLogo: {
    justifyContent: 'center',
    width: 64,
    height: 64,
  },
  text: {
    color: 'black',
    alignSelf: 'flex-end',
  },
});
