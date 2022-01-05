import React from 'react';
import { StyleSheet, View, TouchableOpacity, Text, Image } from 'react-native';
import { Navigation, NavigationComponent, NavigationComponentProps } from 'react-native-navigation';
import testIDs from '../testIDs';

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
  componentWillAppear() {
    console.log('Tooltip: componentWillAppear:', this.props.componentId);
  }

  componentDidAppear() {
    console.log('Tooltip: componentDidAppear:', this.props.componentId);
  }

  componentDidDisappear() {
    console.log('Tooltip: componentDidDisappear:', this.props.componentId);
  }

  componentDidMount() {
    console.log('Tooltip: componentDidMount:', this.props.componentId);
  }
  componentWillUnmount() {
    console.log('Tooltip: componentWillUnmount:', this.props.componentId);
  }
  render() {
    return (
      <View style={styles.root}>
        <View style={styles.container}>
          <Image
            style={styles.tinyLogo}
            source={{
              uri: 'https://wix.github.io/react-native-navigation/img/logo.png',
            }}
          />
          <View style={styles.button}>
            <Text style={styles.text}>{`Hey, This is some text that belongs to a tooltip!!!`}</Text>
            <Text style={styles.text}>{this.props.componentId}</Text>
            <TouchableOpacity
              testID={testIDs.OK_BUTTON}
              style={styles.buttonClick}
              // @ts-ignore
              onPress={async () => {
                await Navigation.dismissOverlay(this.props.componentId);
              }}
            >
              <Text style={styles.text}>{'Got it!'}</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  root: { alignItems: 'flex-start' },
  container: {
    backgroundColor: 'red',
    alignItems: 'center',
    justifyContent: 'center',
  },
  buttonClick: { padding: 8 },
  button: {
    padding: 8,
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
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
