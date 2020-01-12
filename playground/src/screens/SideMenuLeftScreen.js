const React = require('react');
const { useEffect } = require('react');
const Root = require('../components/Root');
const Button = require('../components/Button')
const Navigation = require('../services/Navigation');
const Screens = require('./Screens');
const {
  LEFT_SIDE_MENU_PUSH_BTN,
  CLOSE_LEFT_SIDE_MENU_BTN,
  LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN
} = require('../testIDs');

function SideMenuLeftScreen(props) {
  useEffect(() => {
    const componentDisappearListener = Navigation.events().registerComponentDidDisappearListener(
      ({ componentId }) => {
        if (componentId === props.componentId) {
          console.log('RNN', `componentDisappearListener ${componentId}/${JSON.stringify(props)}`);
        }
      },
    );
    return () => {
      componentDisappearListener.remove();
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

    return (
      <Root componentId={props.componentId} style={{ marginTop: props.marginTop || 0 }}>
        <Button label='Push' testID={LEFT_SIDE_MENU_PUSH_BTN} onPress={push} />
        <Button label='Push and Close' testID={LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN} onPress={pushAndClose} />
        <Button label='Close' testID={CLOSE_LEFT_SIDE_MENU_BTN} onPress={close} />
      </Root>
    );
}

module.exports = SideMenuLeftScreen;
