const React = require('react');
const { useEffect, useState } = require('react');
const { Text } = require('react-native');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('../services/Navigation');
const Screens = require('./Screens');
const {
  LEFT_SIDE_MENU_PUSH_BTN,
  CLOSE_LEFT_SIDE_MENU_BTN,
  LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN,
  SIDE_MENU_LEFT_DRAWER_HEIGHT_TEXT
} = require('../testIDs');

function SideMenuLeftScreen(props) {
  useEffect(() => {
    const unsubscribe = Navigation.events().bindComponent({
      componentDidAppear: () => {
        console.log('RNN', `componentDidAppear`);
      },
      componentDidDisappear: () => {
        console.log('RNN', `componentDidDisappear`);
      }
    }, props.componentId);
    return () => {
      unsubscribe.remove();
    };
  }, []);

  const push = () => Navigation.push('SideMenuCenter', Screens.Pushed);

  const pushAndClose = () => Navigation.push('SideMenuCenter', {
    component: {
      name: Screens.Pushed,
      options: {
        sideMenu: {
          left: {
            visible: false
          }
        }
      }
    }
  });

  const close = () => Navigation.mergeOptions(props.componentId, {
    sideMenu: {
      left: { visible: false }
    }
  });

  const [height, setHeight] = useState(0);
    return (
      <Root componentId={props.componentId} style={{ marginTop: props.marginTop || 0 }}
        onLayout={(event) => {
          setHeight(event.nativeEvent.layout.height);
        }}>
        <Button label='Push' testID={LEFT_SIDE_MENU_PUSH_BTN} onPress={push} />
        <Button label='Push and Close' testID={LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN} onPress={pushAndClose} />
        <Button label='Close' testID={CLOSE_LEFT_SIDE_MENU_BTN} onPress={close} />

        <Text testID={SIDE_MENU_LEFT_DRAWER_HEIGHT_TEXT}>{`left drawer height: ${height}`}</Text>
      </Root>
    );
}

module.exports = SideMenuLeftScreen;
