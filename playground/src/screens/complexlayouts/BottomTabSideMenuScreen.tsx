import React from 'react';
import { View, Button } from 'react-native';
import { Navigation, NavigationComponentProps } from 'react-native-navigation';
import testIDs from '../../testIDs';

interface Props extends NavigationComponentProps {}

class BottomTabSideMenuScreen extends React.Component<Props> {
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
        <Button
          title="Open SideMenu"
          color="blue"
          onPress={this.onOpenSideMenuPress}
          testID={testIDs.OPEN_SIDE_MENU}
        />
      </View>
    );
  }
}

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff',
  },
};

export default BottomTabSideMenuScreen;
