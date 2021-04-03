import React, { useState } from 'react';
import { NavigationFunctionComponent } from 'react-native-navigation';
import { Text } from 'react-native';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const { CLOSE_RIGHT_SIDE_MENU_BTN, SIDE_MENU_RIGHT_DRAWER_WIDTH_TEXT } = testIDs;

interface Props {
  marginTop?: number;
}

const SideMenuRightScreen: NavigationFunctionComponent<Props> = ({ componentId, marginTop }) => {
  const close = () =>
    Navigation.mergeOptions(componentId, {
      sideMenu: {
        right: { visible: false },
      },
    });

  const [width, setWidth] = useState(0);
  return (
    <Root
      componentId={componentId}
      style={{ marginTop: marginTop || 0 }}
      onLayout={(event) => {
        setWidth(event.nativeEvent.layout.width);
      }}
    >
      <Button label="Close" testID={CLOSE_RIGHT_SIDE_MENU_BTN} onPress={close} />
      <Text testID={SIDE_MENU_RIGHT_DRAWER_WIDTH_TEXT}>{`right drawer width: ${Math.round(
        width
      )}`}</Text>
    </Root>
  );
};

export default SideMenuRightScreen;
