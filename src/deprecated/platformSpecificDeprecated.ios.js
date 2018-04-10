/*eslint-disable*/
import {Component} from 'react';
import {findNodeHandle} from 'react-native';
import Navigation from './../Navigation';
import Controllers, {Modal, Notification, ScreenUtils} from './controllers';
const React = Controllers.hijackReact();
const {
  ControllerRegistry,
  TabBarControllerIOS,
  NavigationControllerIOS,
  DrawerControllerIOS
} = React;
import _ from 'lodash';

import PropRegistry from '../PropRegistry';

async function startTabBasedApp(params) {
  if (!params.tabs) {
    console.error('startTabBasedApp(params): params.tabs is required');
    return;
  }

  const controllerID = _.uniqueId('controllerID');
  params.tabs.map(function (tab, index) {
    const navigatorID = controllerID + '_nav' + index;
    const screenInstanceID = _.uniqueId('screenInstanceID');

    const components = tab.components;
    if (!tab.screen && !components) {
      console.error('startTabBasedApp(params): every tab must include a screen property, take a look at tab#' + (index + 1));
      return;
    }

    if (components) {
      params.tabs[index].components = components;
      Object.assign(tab, components[0]);
      components.shift();

      components.forEach(component => {
        const screenInstanceID = _.uniqueId('screenInstanceID');
  
        const {
          navigatorStyle,
          navigatorButtons,
          navigatorEventID
        } = _mergeScreenSpecificSettings(component.screen, screenInstanceID, params);
        _saveNavigatorButtonsProps(navigatorButtons);
        _saveNavBarComponentProps(navigatorStyle);
        const passProps = Object.assign({}, component.passProps);
        passProps.navigatorID = navigatorID;
        passProps.screenInstanceID = screenInstanceID;
        passProps.navigatorEventID = navigatorEventID;
  
  
        component.navigationParams = {
          screenInstanceID,
          navigatorStyle,
          navigatorButtons,
          navigatorEventID,
          navigatorID: navigatorID,
          passProps
        };
  
        component.subtitle = params.subtitle;
        component.passProps = passProps;
        component.navigatorStyle = navigatorStyle;

        savePassProps(component);
  
      });
        
    }

    const {
      navigatorStyle,
      navigatorButtons,
      navigatorEventID
    } = _mergeScreenSpecificSettings(tab.screen, screenInstanceID, tab);
    _saveNavigatorButtonsProps(navigatorButtons);
    _saveNavBarComponentProps(navigatorStyle);
    tab.navigationParams = {
      screenInstanceID,
      navigatorStyle,
      navigatorButtons,
      navigatorEventID,
      navigatorID
    };
  });

  const Controller = Controllers.createClass({
    render: function () {
      if (!params.drawer || (!params.drawer.left && !params.drawer.right)) {
        return this.renderBody();
      } else {
        const navigatorID = controllerID + '_drawer';

        const leftScreenId = _.uniqueId('screenInstanceID');
        const rightScreenId = _.uniqueId('screenInstanceID')

        const {navigatorStyle: leftNavigatorStyle} = params.drawer.left
          ? _mergeScreenSpecificSettings(params.drawer.left.screen, leftScreenId, params.drawer.left)
          : {};

        const {navigatorStyle: rightNavigatorStyle} = params.drawer.right
          ? _mergeScreenSpecificSettings(params.drawer.right.screen, rightScreenId, params.drawer.right)
          : {};

        return (
          <DrawerControllerIOS id={navigatorID}
            componentLeft={params.drawer.left ? params.drawer.left.screen : undefined}
            styleLeft={leftNavigatorStyle}
            passPropsLeft={{navigatorID: navigatorID}}
            componentRight={params.drawer.right ? params.drawer.right.screen : undefined}
            styleRight={rightNavigatorStyle}
            passPropsRight={{navigatorID: navigatorID}}
            disableOpenGesture={params.drawer.disableOpenGesture}
            type={params.drawer.type ? params.drawer.type : 'MMDrawer'}
            animationType={params.drawer.animationType ? params.drawer.animationType : 'slide'}
            style={params.drawer.style}
            appStyle={params.appStyle}
          >
            {this.renderBody()}
          </DrawerControllerIOS>
        );
      }
    },
    renderBody: function () {
      return (
        <TabBarControllerIOS
          id={controllerID + '_tabs'}
          style={params.tabsStyle}
          appStyle={params.appStyle}
          initialTabIndex={params.initialTabIndex}>
          {
            params.tabs.map(function (tab, index) {
              return (
                <TabBarControllerIOS.Item {...tab} title={tab.label}>
                  <NavigationControllerIOS
                    id={tab.navigationParams.navigatorID}
                    title={tab.title}
                    subtitle={tab.subtitle}
                    titleImage={tab.titleImage}
                    component={tab.screen}
                    components={tab.components}
                    passProps={{
                      navigatorID: tab.navigationParams.navigatorID,
                      screenInstanceID: tab.navigationParams.screenInstanceID,
                      navigatorEventID: tab.navigationParams.navigatorEventID,
                    }}
                    style={tab.navigationParams.navigatorStyle}
                    leftButtons={tab.navigationParams.navigatorButtons.leftButtons}
                    rightButtons={tab.navigationParams.navigatorButtons.rightButtons}
                  />
                </TabBarControllerIOS.Item>
              );
            })
          }
        </TabBarControllerIOS>
      );
    }
  });
  savePassProps(params);
  _.set(params, 'passProps.timestamp', Date.now());

  ControllerRegistry.registerController(controllerID, () => Controller);
  return await ControllerRegistry.setRootController(controllerID, params.animationType, params.passProps || {});
}

async function startSingleScreenApp(params) {
  const components = params.components;
  let screen = params.screen;
  if (!screen && !components) {
    console.error('startSingleScreenApp(params): params.screen is required');
    return;
  }

  const controllerID = _.uniqueId('controllerID');
  const navigatorID = controllerID + '_nav';

  if (components) {
    screen = components[0];
    components.shift();

    components.forEach(component => {
      const screenInstanceID = _.uniqueId('screenInstanceID');

      const {
        navigatorStyle,
        navigatorButtons,
        navigatorEventID
      } = _mergeScreenSpecificSettings(component.screen, screenInstanceID, params);
      _saveNavigatorButtonsProps(navigatorButtons);
      _saveNavBarComponentProps(navigatorStyle);
      const passProps = Object.assign({}, params.passProps);
      passProps.navigatorID = navigatorID;
      passProps.screenInstanceID = screenInstanceID;
      passProps.navigatorEventID = navigatorEventID;


      component.navigationParams = {
        screenInstanceID,
        navigatorStyle,
        navigatorButtons,
        navigatorEventID,
        navigatorID: navigatorID,
        passProps
      };

      component.subtitle = params.subtitle;
      component.passProps = passProps;

      savePassProps(component);

    });
  }

  if (!screen.screen) {
    console.error('startSingleScreenApp(params): screen must include a screen property');
    return;
  }


  const screenInstanceID = _.uniqueId('screenInstanceID');
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(screen.screen, screenInstanceID, screen);
  _saveNavigatorButtonsProps(navigatorButtons);
  _saveNavBarComponentProps(navigatorStyle);
  params.navigationParams = {
    screenInstanceID,
    navigatorStyle,
    navigatorButtons,
    navigatorEventID,
    navigatorID
  };

  const passProps = {
    navigatorID: navigatorID,
    screenInstanceID: screenInstanceID,
    navigatorEventID: navigatorEventID,
    ...screen.passProps
  };

  const Controller = Controllers.createClass({
    render: function () {
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
            type={params.drawer.type ? params.drawer.type : 'MMDrawer'}
            animationType={params.drawer.animationType ? params.drawer.animationType : 'slide'}
            style={params.drawer.style}
            appStyle={params.appStyle}
          >
            {this.renderBody()}
          </DrawerControllerIOS>
        );
      }
    },
    renderBody: function () {
      return (
        <NavigationControllerIOS
          id={navigatorID}
          title={screen.title}
          subtitle={params.subtitle}
          titleImage={screen.titleImage}
          component={screen.screen}
          components={components}
          passProps={passProps}
          style={navigatorStyle}
          leftButtons={navigatorButtons.leftButtons}
          rightButtons={navigatorButtons.rightButtons}
          appStyle={params.appStyle}
        />
      );
    }
  });
  savePassProps(params);

  ControllerRegistry.registerController(controllerID, () => Controller);
  return await ControllerRegistry.setRootController(controllerID, params.animationType, params.passProps || {});
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

  let navigatorEventID = screenInstanceID + '_events';
  let navigatorButtons = _.cloneDeep(screenClass.navigatorButtons);
  if (params.navigatorButtons) {
    navigatorButtons = _.cloneDeep(params.navigatorButtons);
  }
  if (navigatorButtons.leftButtons) {
    for (let i = 0; i < navigatorButtons.leftButtons.length; i++) {
      navigatorButtons.leftButtons[i].onPress = navigatorEventID;
    }
  }
  if (navigatorButtons.rightButtons) {
    for (let i = 0; i < navigatorButtons.rightButtons.length; i++) {
      navigatorButtons.rightButtons[i].onPress = navigatorEventID;
    }
  }
  return {navigatorStyle, navigatorButtons, navigatorEventID};
}

function navigatorPush(navigator, params) {
  if (!params.screen) {
    console.error('Navigator.push(params): params.screen is required');
    return;
  }
  let previewViewID;
  const screenInstanceID = _.uniqueId('screenInstanceID');
  if (params.previewView instanceof Component) {
    previewViewID = findNodeHandle(params.previewView)
  } else if (typeof params.previewView === 'number') {
    previewViewID = params.previewView;
  } else if (params.previewView) {
    console.error('Navigator.push(params): params.previewView is not a valid react view');
  }
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
  _saveNavigatorButtonsProps(navigatorButtons);
  _saveNavBarComponentProps(navigatorStyle);
  const passProps = Object.assign({}, params.passProps);
  passProps.navigatorID = navigator.navigatorID;
  passProps.screenInstanceID = screenInstanceID;
  passProps.navigatorEventID = navigatorEventID;
  passProps.previewViewID = previewViewID;
  passProps.isPreview = !!previewViewID;

  params.navigationParams = {
    screenInstanceID,
    navigatorStyle,
    navigatorButtons,
    navigatorEventID,
    navigatorID: navigator.navigatorID
  };

  savePassProps(params);

  Controllers.NavigationControllerIOS(navigator.navigatorID).push({
    title: params.title,
    subtitle: params.subtitle,
    titleImage: params.titleImage,
    component: params.screen,
    animated: params.animated,
    animationType: params.animationType,
    passProps: passProps,
    style: navigatorStyle,
    backButtonTitle: params.backButtonTitle,
    backButtonHidden: params.backButtonHidden,
    leftButtons: navigatorButtons.leftButtons,
    rightButtons: navigatorButtons.rightButtons,
    previewViewID: previewViewID,
    previewActions: params.previewActions,
    previewHeight: params.previewHeight,
    previewCommit: params.previewCommit,
    timestamp: Date.now()
  });
}

function navigatorPop(navigator, params) {
  Controllers.NavigationControllerIOS(navigator.navigatorID).pop({
    animated: params.animated,
    animationType: params.animationType,
    timestamp: Date.now()
  });
}

function navigatorPopToRoot(navigator, params) {
  Controllers.NavigationControllerIOS(navigator.navigatorID).popToRoot({
    animated: params.animated,
    animationType: params.animationType
  });
}

function navigatorResetTo(navigator, params) {
  if (!params.screen) {
    console.error('Navigator.resetTo(params): params.screen is required');
    return;
  }
  const screenInstanceID = _.uniqueId('screenInstanceID');
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
  _saveNavigatorButtonsProps(navigatorButtons);
  _saveNavBarComponentProps(navigatorStyle);
  const passProps = Object.assign({}, params.passProps);
  passProps.navigatorID = navigator.navigatorID;
  passProps.screenInstanceID = screenInstanceID;
  passProps.navigatorEventID = navigatorEventID;

  params.navigationParams = {
    screenInstanceID,
    navigatorStyle,
    navigatorButtons,
    navigatorEventID,
    navigatorID: navigator.navigatorID
  };

  savePassProps(params);

  Controllers.NavigationControllerIOS(navigator.navigatorID).resetTo({
    title: params.title,
    subtitle: params.subtitle,
    titleImage: params.titleImage,
    component: params.screen,
    animated: params.animated,
    animationType: params.animationType,
    passProps: passProps,
    style: navigatorStyle,
    leftButtons: navigatorButtons.leftButtons,
    rightButtons: navigatorButtons.rightButtons
  });
}

function navigatorSetDrawerEnabled(navigator, params) {
  const controllerID = navigator.navigatorID.split('_')[0];
  Controllers.NavigationControllerIOS(controllerID + '_drawer').setDrawerEnabled(params)
}

function navigatorSetTitle(navigator, params) {
  Controllers.NavigationControllerIOS(navigator.navigatorID).setTitle({
    title: params.title,
    subtitle: params.subtitle,
    titleImage: params.titleImage,
    style: params.navigatorStyle,
    isSetSubtitle: false
  });
}

function navigatorSetSubtitle(navigator, params) {
  Controllers.NavigationControllerIOS(navigator.navigatorID).setTitle({
    title: params.title,
    subtitle: params.subtitle,
    titleImage: params.titleImage,
    style: params.navigatorStyle,
    isSetSubtitle: true
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

function navigatorSetStyle(navigator, params) {
  _saveNavBarComponentProps(params);
  Controllers.NavigationControllerIOS(navigator.navigatorID).setStyle(params)
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
      badge: params.badge,
      badgeColor: params.badgeColor
    });
  } else {
    Controllers.TabBarControllerIOS(controllerID + '_tabs').setBadge({
      contentId: navigator.navigatorID,
      contentType: 'NavigationControllerIOS',
      badge: params.badge
    });
  }
}

function navigatorSetTabButton(navigator, params) {
  const controllerID = navigator.navigatorID.split('_')[0];
  if (params.tabIndex || params.tabIndex === 0) {
    Controllers.TabBarControllerIOS(controllerID + '_tabs').setTabButton({
      tabIndex: params.tabIndex,
      icon: params.icon,
      selectedIcon: params.selectedIcon,
      label: params.label,
    });
  } else {
    Controllers.TabBarControllerIOS(controllerID + '_tabs').setTabButton({
      contentId: navigator.navigatorID,
      contentType: 'NavigationControllerIOS',
      icon: params.icon,
      selectedIcon: params.selectedIcon,
      label: params.label,
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
  _saveNavigatorButtonsProps(params);
  if (params.leftButtons) {
    const buttons = params.leftButtons.slice(); // clone
    for (let i = 0; i < buttons.length; i++) {
      buttons[i].onPress = navigatorEventID;
    }
    Controllers.NavigationControllerIOS(navigator.navigatorID).setLeftButtons(buttons, params.animated);
  }
  if (params.rightButtons) {
    const buttons = params.rightButtons.slice(); // clone
    for (let i = 0; i < buttons.length; i++) {
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
  const controllerID = _.uniqueId('controllerID');
  const navigatorID = controllerID + '_nav';
  const screenInstanceID = _.uniqueId('screenInstanceID');
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
  _saveNavigatorButtonsProps(navigatorButtons);
  _saveNavBarComponentProps(navigatorStyle);
  const passProps = Object.assign({}, params.passProps);
  passProps.navigatorID = navigatorID;
  passProps.screenInstanceID = screenInstanceID;
  passProps.navigatorEventID = navigatorEventID;
  passProps.timestamp = Date.now();

  params.navigationParams = {
    screenInstanceID,
    navigatorStyle,
    navigatorButtons,
    navigatorEventID,
    navigatorID: navigator.navigatorID
  };

  const Controller = Controllers.createClass({
    render: function () {
      return (
        <NavigationControllerIOS
          id={navigatorID}
          title={params.title}
          subtitle={params.subtitle}
          titleImage={params.titleImage}
          component={params.screen}
          passProps={passProps}
          style={navigatorStyle}
          leftButtons={navigatorButtons.leftButtons}
          rightButtons={navigatorButtons.rightButtons} />
      );
    }
  });

  savePassProps(params);

  ControllerRegistry.registerController(controllerID, () => Controller);
  Modal.showController(controllerID, params.animationType);
}

async function dismissModal(params) {
  return await Modal.dismissController(params.animationType);
}

async function dismissAllModals(params) {
  return await Modal.dismissAllControllers(params.animationType);
}

function showLightBox(params) {
  if (!params.screen) {
    console.error('showLightBox(params): params.screen is required');
    return;
  }
  const controllerID = _.uniqueId('controllerID');
  const navigatorID = controllerID + '_nav';
  const screenInstanceID = _.uniqueId('screenInstanceID');
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
  const passProps = Object.assign({}, params.passProps);
  passProps.navigatorID = navigatorID;
  passProps.screenInstanceID = screenInstanceID;
  passProps.navigatorEventID = navigatorEventID;

  params.navigationParams = {
    screenInstanceID,
    navigatorStyle,
    navigatorButtons,
    navigatorEventID,
    navigatorID
  };

  savePassProps(params);

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

  const controllerID = _.uniqueId('controllerID');
  const navigatorID = controllerID + '_nav';
  const screenInstanceID = _.uniqueId('screenInstanceID');
  const {
    navigatorStyle,
    navigatorButtons,
    navigatorEventID
  } = _mergeScreenSpecificSettings(params.screen, screenInstanceID, params);
  const passProps = Object.assign({}, params.passProps);
  passProps.navigatorID = navigatorID;
  passProps.screenInstanceID = screenInstanceID;
  passProps.navigatorEventID = navigatorEventID;

  params.navigationParams = {
    screenInstanceID,
    navigatorStyle,
    navigatorButtons,
    navigatorEventID,
    navigatorID
  };

  savePassProps(params);

  let args = {
    component: params.screen,
    passProps: passProps,
    style: params.style,
    animation: params.animation || Notification.AnimationPresets.default,
    position: params.position,
    shadowRadius: params.shadowRadius,
    dismissWithSwipe: params.dismissWithSwipe || true,
    autoDismissTimerSec: params.autoDismissTimerSec || 5
  };
  if (params.autoDismiss === false) delete args.autoDismissTimerSec;
  Notification.show(args);
}

function dismissInAppNotification(params) {
  Notification.dismiss(params);
}

function savePassProps(params) {
  //TODO this needs to be handled in a common place,
  //TODO also, all global passProps should be handled differently
  if (params.navigationParams && params.passProps) {
    PropRegistry.save(params.navigationParams.screenInstanceID, params.passProps);
  }

  if (params.screen && params.screen.passProps) {
    PropRegistry.save(params.screen.navigationParams.screenInstanceID, params.screen.passProps);
  }

  if (_.get(params, 'screen.topTabs')) {
    _.forEach(params.screen.topTabs, (tab) => savePassProps(tab));
  }

  if (params.tabs) {
    _.forEach(params.tabs, (tab) => {
      if (!tab.passProps) {
        tab.passProps = params.passProps;
      }
      savePassProps(tab);
    });
  }
}

function showContextualMenu() {
  // Android only
}

function dismissContextualMenu() {
  // Android only
}

async function getCurrentlyVisibleScreenId() {
  return await ScreenUtils.getCurrentlyVisibleScreenId();
}

function _saveNavBarComponentProps(navigatorStyle) {
  if (navigatorStyle.navBarCustomViewInitialProps) {
    const passPropsKey = _.uniqueId('navBarComponent');
    PropRegistry.save(passPropsKey, navigatorStyle.navBarCustomViewInitialProps);
    navigatorStyle.navBarCustomViewInitialProps = {passPropsKey};
  }
}

function _saveNavigatorButtonsProps({rightButtons, leftButtons}) {
  _saveNavigatorButtonsPassProps(rightButtons);
  _saveNavigatorButtonsPassProps(leftButtons);
}

function _saveNavigatorButtonsPassProps(buttons = []) {
  buttons.forEach((button) => {
    if (button.component) {
      const passPropsKey = _.uniqueId('customButtonComponent');
      PropRegistry.save(passPropsKey, button.passProps);
      button.passProps = {passPropsKey};
    }
  })
}

async function getLaunchArgs() {
  return await ControllerRegistry.getLaunchArgs();
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
  navigatorSetDrawerEnabled,
  navigatorSetTitle,
  navigatorSetSubtitle,
  navigatorSetStyle,
  navigatorSetTitleImage,
  navigatorToggleDrawer,
  navigatorToggleTabs,
  navigatorSetTabBadge,
  navigatorSetTabButton,
  navigatorSwitchToTab,
  navigatorToggleNavBar,
  showContextualMenu,
  dismissContextualMenu,
  getCurrentlyVisibleScreenId,
  getLaunchArgs
};
