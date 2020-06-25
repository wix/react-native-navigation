import React, { useEffect } from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

const {
  LEFT_SIDE_MENU_PUSH_BTN,
  CLOSE_LEFT_SIDE_MENU_BTN,
  LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN,
} = testIDs;

interface Props extends NavigationComponentProps {
  marginTop?: number;
}

export default function SideMenuLeftScreen({ componentId, marginTop }: Props) {
  useEffect(() => {
    const unsubscribe = Navigation.events().bindComponent(
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

  return (
    <Root componentId={componentId} style={{ marginTop: marginTop || 0 }}>
      <Button label="Push" testID={LEFT_SIDE_MENU_PUSH_BTN} onPress={push} />
      <Button
        label="Push and Close"
        testID={LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN}
        onPress={pushAndClose}
      />
      <Button label="Close" testID={CLOSE_LEFT_SIDE_MENU_BTN} onPress={close} />
    </Root>
  );
}
