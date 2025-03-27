import React, { useEffect, useState } from 'react';
import { Text } from 'react-native';
import { NavigationFunctionComponent } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

const {
  LEFT_SIDE_MENU_PUSH_BTN,
  CLOSE_LEFT_SIDE_MENU_BTN,
  LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN,
  SIDE_MENU_LEFT_DRAWER_HEIGHT_TEXT,
  SIDE_MENU_LEFT_DRAWER_WIDTH_TEXT,
} = testIDs;

interface Props {
  marginTop?: number;
}

const SideMenuLeftScreen: NavigationFunctionComponent<Props> = ({ componentId, marginTop }) => {
  useEffect(() => {
    const unsubscribe = Navigation.events().registerComponentListener(
      {
        componentDidAppear: () => {
          console.log('RNN', `componentDidAppear`);
        },
        componentDidDisappear: () => {
          console.log('RNN', `componentDidDisappear`);
        },
      },
      componentId
    );
    return () => {
      unsubscribe.remove();
    };
  }, [componentId]);

  const push = () => Navigation.push('SideMenuCenter', Screens.Pushed);

  const pushAndClose = () =>
    Navigation.push('SideMenuCenter', {
      component: {
        name: Screens.Pushed,
        options: {
          sideMenu: {
            left: {
              visible: false,
            },
          },
        },
      },
    });

  const close = () =>
    Navigation.mergeOptions(componentId, {
      sideMenu: {
        left: { visible: false },
      },
    });

  const [height, setHeight] = useState(0);
  const [width, setWidth] = useState(0);
  return (
    <Root
      componentId={componentId}
      style={{ marginTop: marginTop || 0 }}
      onLayout={(event) => {
        setHeight(event.nativeEvent.layout.height);
        setWidth(event.nativeEvent.layout.width);
      }}
    >
      <Button label="Push" testID={LEFT_SIDE_MENU_PUSH_BTN} onPress={push} />
      <Button
        label="Push and Close"
        testID={LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN}
        onPress={pushAndClose}
      />
      <Button label="Close" testID={CLOSE_LEFT_SIDE_MENU_BTN} onPress={close} />

      <Text testID={SIDE_MENU_LEFT_DRAWER_HEIGHT_TEXT}>{`left drawer height: ${height}`}</Text>
      <Text testID={SIDE_MENU_LEFT_DRAWER_WIDTH_TEXT}>{`left drawer width: ${Math.round(
        width
      )}`}</Text>
    </Root>
  );
};
export default SideMenuLeftScreen;
