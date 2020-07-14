import React from 'react';
import { View, Button, StyleSheet, ViewStyle } from 'react-native';
import { Navigation, NavigationComponentProps } from 'react-native-navigation';

interface Props extends NavigationComponentProps {}

export default class BottomTabSideMenuScreen extends React.Component<Props> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'test',
        },
      },
    };
  }

  onOpenSideMenuPress = () => {
    Navigation.mergeOptions(this.props.componentId, {
      sideMenu: {
        left: {
          visible: true,
        },
      },
    });
  };

  render() {
    return (
      <View style={styles.root}>
        <Button title="Open SideMenu" color="blue" onPress={this.onOpenSideMenuPress} />
      </View>
    );
  }
}

type Style = {
  root: ViewStyle;
};

const styles = StyleSheet.create<Style>({
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff',
  },
});
