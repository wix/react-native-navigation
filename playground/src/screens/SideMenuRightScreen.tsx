import React from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Colors from '../commons/Colors';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const { CLOSE_RIGHT_SIDE_MENU_BTN } = testIDs;

export default class SideMenuRightScreen extends React.Component<NavigationComponentProps> {
  render() {
    return (
      <Root componentId={this.props.componentId} style={{ backgroundColor: Colors.background }}>
        <Button label="Close" testID={CLOSE_RIGHT_SIDE_MENU_BTN} onPress={this.close} />
      </Root>
    );
  }

  close = () =>
    Navigation.mergeOptions(this, {
      sideMenu: {
        right: { visible: false },
      },
    });
}
