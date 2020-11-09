import React from 'react';
import {
  View,
  Text,
  Button,
  LayoutChangeEvent,
  StyleSheet,
  ViewStyle,
  TextStyle,
} from 'react-native';
import {
  Navigation,
  NavigationComponentProps,
  LayoutOrientation,
  NavigationComponent,
  Options,
} from 'react-native-navigation';
import TestIDs from '../testIDs';

interface Props extends NavigationComponentProps {
  orientation: LayoutOrientation[]; // LayoutOrientation type is not exposed.
}

interface State {
  horizontal: boolean;
}

export default class OrientationDetectScreen extends NavigationComponent<Props, State> {
  static options = (props: Props): Options => {
    return {
      layout: {
        orientation: props.orientation,
      },
    };
  };

  state: State = {
    horizontal: false,
  };

  constructor(props: Props) {
    super(props);
    this.detectHorizontal = this.detectHorizontal.bind(this);
  }

  render() {
    return (
      <View style={styles.root} onLayout={this.detectHorizontal}>
        <Text style={styles.h1}>{`Orientation Screen`}</Text>
        <Button
          title="Dismiss"
          testID={TestIDs.DISMISS_BTN}
          onPress={() => Navigation.dismissModal(this.props.componentId)}
        />
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
        {this.state.horizontal ? (
          <Text style={styles.footer} testID={TestIDs.LANDSCAPE_ELEMENT}>
            Landscape
          </Text>
        ) : (
          <Text style={styles.footer} testID={TestIDs.PORTRAIT_ELEMENT}>
            Portrait
          </Text>
        )}
      </View>
    );
  }

  detectHorizontal({
    nativeEvent: {
      layout: { width, height },
    },
  }: LayoutChangeEvent) {
    this.setState({
      horizontal: width > height,
    });
  }
}

type Style = {
  root: ViewStyle;
  h1: TextStyle;
  footer: TextStyle;
};

const styles = StyleSheet.create<Style>({
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10,
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10,
  },
});
