import utils from './utils';
import Navigation from './Navigation';
import Controllers, { Modal, Notification } from 'react-native-controllers';
const React = Controllers.hijackReact();
const {
  ControllerRegistry,
  TabBarControllerIOS,
  NavigationControllerIOS,
  DrawerControllerIOS
} = React;

function startTabBasedApp(params) {
  if (!params.tabs) {
    console.error('startTabBasedApp(params): params.tabs is required');
    return;
  }
  const controllerID = utils.getRandomId();
  const Controller = Controllers.createClass({
    render: function() {
      if (!params.drawer || (!params.drawer.left && !params.drawer.right)) {
        return this.renderBody();
      } else {
        const navigatorID = controllerID + '_drawer';
        return (
          <DrawerControllerIOS id={navigatorID}
            componentLeft={params.drawer.left ? params.drawer.left.screen : undefined}
            passPropsLeft={{navigatorID: navigatorID}}
            componentRight={params.drawer.right ? params.drawer.right.screen : undefined}
            passPropsRight={{navigatorID: navigatorID}}
            disableOpenGesture={params.drawer.disableOpenGesture}
            type={params.drawer.type ? params.drawer.type : undefined}
            animationType={params.drawer.animationType ? params.drawer.animationType : undefined}
          >
            {this.renderBody()}
          </DrawerControllerIOS>
        );
      }
    },
    renderBody: function() {
      return (
        <TabBarControllerIOS
          id={controllerID + '_tabs'}
          style={params.tabsStyle}>
          {
          params.tabs.map(function(tab, index) {
            const navigatorID = controllerID + '_nav' + index;
            const screenInstanceID = utils.getRandomId();
            if (!tab.screen) {
              console.error('startTabBasedApp(params): every tab must include a screen property, take a look at tab#' + (index+1));
              return;
            }
            const {
              navigatorStyle,
              navigatorButtons,
              navigatorEventID
            } = _mergeScreenSpecificSettings(tab.screen, screenInstanceID, tab);
            return (
              <TabBarControllerIOS.Item {...tab} title={tab.label}>
                <NavigationControllerIOS
                  id={navigatorID}
                  title={tab.title}
                  titleImage={tab.titleImage}
                  component={tab.screen}
                  passProps={{
                    navigatorID: navigatorID,
                    screenInstanceID: screenInstanceID,
                    navigatorEventID: navigatorEventID
                  }}
                  style={navigatorStyle}
                  leftButtons={navigatorButtons.leftButtons}
                  rightButtons={navigatorButtons.rightButtons}
                />
              </TabBarControllerIOS.Item>
            );
          })
        }
        </TabBarControllerIOS>
      );
    }
  });
  ControllerRegistry.registerController(controllerID, () => Controller);
  ControllerRegistry.setRootController(controllerID, params.animationType, params.passProps || {});
}

function startSingleScreenApp(params) {
  if (!params.screen) {
    console.error('startSingleScreenApp(params): params.screen is required');
    return;
  }
  const controllerID = utils.getRandomId();
  const Controller = Controllers.createClass({
    render: function() {
      if (!params.drawer || (!params.drawer.left && !params.drawer.right)) {
        return this.renderBody();
      } else {
        const navigatorID = controllerID + '_drawer';
        return (
          <DrawerControllerIOS id={navigatorID}
            componentLeft={params.drawer.left ? params.drawer.left.screen : undefined}
            passPropsLeft={{navigatorID: navigatorID}}
            componentRight={params.drawer.right ? params.drawer.right.screen : undefined}
            passPropsRight={{navigatorID: navigatorID}}
            disableOpenGesture={params.drawer.disableOpenGesture}
            type={params.drawer.type ? params.drawer.type : undefined}
            animationType={params.drawer.animationType ? params.drawer.animationType : undefined}>
            {this.renderBody()}
          </DrawerControllerIOS>
        );
      }
    },
    renderBody: function() {
      const screen = params.screen;
      const navigatorID = controllerID + '_nav';
      const screenInstanceID = utils.getRandomId();
      if (!screen.screen) {
        console.error('startSingleScreenApp(params): screen must include a screen property');
        return;
      }
      const {
        navigatorStyle,
        navigatorButtons,
        navigatorEventID
      } = _mergeScreenSpecificSettings(screen.screen, screenInstanceID, screen);
      return (
        <NavigationControllerIOS
          id={navigatorID}
          title={screen.title}
          titleImage={screen.titleImage}
          component={screen.screen}
          passProps={{
            navigatorID: navigatorID,
            screenInstanceID: screenInstanceID,
            navigatorEventID: navigatorEventID
          }}
          style={navigatorStyle}
          leftButtons={navigatorButtons.leftButtons}
          rightButtons={navigatorButtons.rightButtons}
        />
      );
    }
  });
  ControllerRegistry.registerController(controllerID, () => Controller);
  ControllerRegistry.setRootController(controllerID, params.animationType, params.passProps || {});
}

function _mergeScreenSpecificSettings(screenID, screenInstanceID, params) {
  const screenClass = Navigation.getRegisteredScreen(screenID);
  if (!screenClass) {
    console.error('Cannot create screen ' + screenID + '. Are you it was registered with Navigation.registerScreen?');
    return;
  }
  const navigatorStyle = Object.assign({}, screenClass.navigatorStyle);
  if (params.navigatorStyle) {
    Object.assign(navigatorStyle, params.navigatorStyle);
  }

  const navigatorEventID = screenInstanceID + '_events';
  const navigatorButtons = Object.assign({}, screenClass.navigatorButtons);
  if (params.navigatorButtons) {
    Object.assign(navigatorButtons, params.navigatorButtons);
  }
  if (navigatorButtons.leftButtons) {
    for (let i = 0 ; i < navigatorButtons.leftButtons.length ; i++) {
      navigatorButtons.leftButtons[i].onPress = navigatorEventID;
    }
  }
  if (navigatorButtons.rightButtons) {
    for (let i = 0 ; i < navigatorButtons.rightButtons.length ; i++) {
      navigatorButtons.rightButtons[i].onPress = navigatorEventID;
    }
  }
  return { navigatorStyle, navigatorButtons, navigatorEventID };
}

function navigatorPush(navigator, params) {
  if (!params.screen) {
    console.error('Navigator.push(params): params.screen is required');
    return;
  }
  const screenInstanceID = utils.getRandomId();
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
  const passProps = Object.assign({}, params.passProps);
  passProps.navigatorID = navigator.navigatorID;
  passProps.screenInstanceID = screenInstanceID;
  passProps.navigatorEventID = navigatorEventID;
  Controllers.NavigationControllerIOS(navigator.navigatorID).push({
    title: params.title,
    titleImage: params.titleImage,
    component: params.screen,
    animated: params.animated,
    passProps: passProps,
    style: navigatorStyle,
    backButtonTitle: params.backButtonTitle,
    backButtonHidden: params.backButtonHidden,
    leftButtons: navigatorButtons.leftButtons,
    rightButtons: navigatorButtons.rightButtons
  });
}

function navigatorPop(navigator, params) {
  Controllers.NavigationControllerIOS(navigator.navigatorID).pop({
    animated: params.animated
  });
}

function navigatorPopToRoot(navigator, params) {
  Controllers.NavigationControllerIOS(navigator.navigatorID).popToRoot({
    animated: params.animated
  });
}

function navigatorResetTo(navigator, params) {
  if (!params.screen) {
    console.error('Navigator.resetTo(params): params.screen is required');
    return;
  }
  const screenInstanceID = utils.getRandomId();
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
  const passProps = Object.assign({}, params.passProps);
  passProps.navigatorID = navigator.navigatorID;
  passProps.screenInstanceID = screenInstanceID;
  passProps.navigatorEventID = navigatorEventID;
  Controllers.NavigationControllerIOS(navigator.navigatorID).resetTo({
    title: params.title,
    titleImage: params.titleImage,
    component: params.screen,
    animated: params.animated,
    passProps: passProps,
    style: navigatorStyle,
    leftButtons: navigatorButtons.leftButtons,
    rightButtons: navigatorButtons.rightButtons
  });
}

function navigatorSetTitle(navigator, params) {
  Controllers.NavigationControllerIOS(navigator.navigatorID).setTitle({
    title: params.title
  });
}

function navigatorSetTitleImage(navigator, params) {
  Controllers.NavigationControllerIOS(navigator.navigatorID).setTitleImage({
    titleImage: params.titleImage
  });
}

function navigatorToggleNavBar(navigator, params) {
  Controllers.NavigationControllerIOS(navigator.navigatorID).setHidden({
    hidden: ((params.to === 'hidden') ? true : false),
    animated: params.animated
  });
}

function navigatorToggleDrawer(navigator, params) {
  const controllerID = navigator.navigatorID.split('_')[0];
  if (params.to == 'open') {
    Controllers.DrawerControllerIOS(controllerID + '_drawer').open({
      side: params.side,
      animated: params.animated
    });
  } else if (params.to == 'closed') {
    Controllers.DrawerControllerIOS(controllerID + '_drawer').close({
      side: params.side,
      animated: params.animated
    });
  } else {
    Controllers.DrawerControllerIOS(controllerID + '_drawer').toggle({
      side: params.side,
      animated: params.animated
    });
  }
}

function navigatorToggleTabs(navigator, params) {
  const controllerID = navigator.navigatorID.split('_')[0];
  Controllers.TabBarControllerIOS(controllerID + '_tabs').setHidden({
    hidden: params.to == 'hidden',
    animated: !(params.animated === false)
  });
}

function navigatorSetTabBadge(navigator, params) {
  const controllerID = navigator.navigatorID.split('_')[0];
  if (params.tabIndex || params.tabIndex === 0) {
    Controllers.TabBarControllerIOS(controllerID + '_tabs').setBadge({
      tabIndex: params.tabIndex,
      badge: params.badge
    });
  } else {
    Controllers.TabBarControllerIOS(controllerID + '_tabs').setBadge({
      contentId: navigator.navigatorID,
      contentType: 'NavigationControllerIOS',
      badge: params.badge
    });
  }
}

function navigatorSwitchToTab(navigator, params) {
  const controllerID = navigator.navigatorID.split('_')[0];
  if (params.tabIndex || params.tabIndex === 0) {
    Controllers.TabBarControllerIOS(controllerID + '_tabs').switchTo({
      tabIndex: params.tabIndex
    });
  } else {
    Controllers.TabBarControllerIOS(controllerID + '_tabs').switchTo({
      contentId: navigator.navigatorID,
      contentType: 'NavigationControllerIOS'
    });
  }
}

function navigatorSetButtons(navigator, navigatorEventID, params) {
  if (params.leftButtons) {
    const buttons = params.leftButtons.slice(); // clone
    for (let i = 0 ; i < buttons.length ; i++) {
      buttons[i].onPress = navigatorEventID;
    }
    Controllers.NavigationControllerIOS(navigator.navigatorID).setLeftButtons(buttons, params.animated);
  }
  if (params.rightButtons) {
    const buttons = params.rightButtons.slice(); // clone
    for (let i = 0 ; i < buttons.length ; i++) {
      buttons[i].onPress = navigatorEventID;
    }
    Controllers.NavigationControllerIOS(navigator.navigatorID).setRightButtons(buttons, params.animated);
  }
}

function showModal(params) {
  if (!params.screen) {
    console.error('showModal(params): params.screen is required');
    return;
  }
  const controllerID = utils.getRandomId();
  const Controller = Controllers.createClass({
    render: function() {
      const navigatorID = controllerID + '_nav';
      const screenInstanceID = utils.getRandomId();
      const {
        navigatorStyle,
        navigatorButtons,
        navigatorEventID
      } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
      const passProps = Object.assign({}, params.passProps);
      passProps.navigatorID = navigatorID;
      passProps.screenInstanceID = screenInstanceID;
      passProps.navigatorEventID = navigatorEventID;
      return (
        <NavigationControllerIOS
          id={navigatorID}
          title={params.title}
          titleImage={params.titleImage}
          component={params.screen}
          passProps={passProps}
          style={navigatorStyle}
          leftButtons={navigatorButtons.leftButtons}
          rightButtons={navigatorButtons.rightButtons}/>
      );
    }
  });
  ControllerRegistry.registerController(controllerID, () => Controller);
  Modal.showController(controllerID, params.animationType);
}

function dismissModal(params) {
  Modal.dismissController(params.animationType);
}

function dismissAllModals(params) {
  Modal.dismissAllControllers(params.animationType);
}

function showLightBox(params) {
  if (!params.screen) {
    console.error('showLightBox(params): params.screen is required');
    return;
  }
  const controllerID = utils.getRandomId();
  const navigatorID = controllerID + '_nav';
  const screenInstanceID = utils.getRandomId();
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
  const passProps = Object.assign({}, params.passProps);
  passProps.navigatorID = navigatorID;
  passProps.screenInstanceID = screenInstanceID;
  passProps.navigatorEventID = navigatorEventID;
  Modal.showLightBox({
    component: params.screen,
    passProps: passProps,
    style: params.style
  });
}

function dismissLightBox(params) {
  Modal.dismissLightBox();
}

function showInAppNotification(params) {
  if (!params.screen) {
    console.error('showInAppNotification(params): params.screen is required');
    return;
  }

  const controllerID = utils.getRandomId();
  const navigatorID = controllerID + '_nav';
  const screenInstanceID = utils.getRandomId();
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
  const passProps = Object.assign({}, params.passProps);
  passProps.navigatorID = navigatorID;
  passProps.screenInstanceID = screenInstanceID;
  passProps.navigatorEventID = navigatorEventID;

  Notification.show({
    component: params.screen,
    passProps: passProps,
    style: params.style,
    animation: params.animation || Notification.AnimationPresets.default,
    position: params.position,
    shadowRadius: params.shadowRadius,
    dismissWithSwipe: params.dismissWithSwipe || true,
    autoDismissTimerSec: params.autoDismissTimerSec || 5
  });
}

function dismissInAppNotification(params) {
  Notification.dismiss(params);
}

export default {
  startTabBasedApp,
  startSingleScreenApp,
  navigatorPush,
  navigatorPop,
  navigatorPopToRoot,
  navigatorResetTo,
  showModal,
  dismissModal,
  dismissAllModals,
  showLightBox,
  dismissLightBox,
  showInAppNotification,
  dismissInAppNotification,
  navigatorSetButtons,
  navigatorSetTitle,
  navigatorSetTitleImage,
  navigatorToggleDrawer,
  navigatorToggleTabs,
  navigatorSetTabBadge,
  navigatorSwitchToTab,
  navigatorToggleNavBar
}
