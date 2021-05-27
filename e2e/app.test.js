import React from 'react';
import { fireEvent, render, within } from '@testing-library/react-native';
import TestIDs from '../playground/src/TestIDs';
const { Application } = require('../lib/src/NavigationMock');

describe('testing that the environment is working properly', () => {
  let App;
  beforeEach(() => {
    const NativeModules = require('react-native').NativeModules;
    NativeModules.KeyboardTrackingViewTempManager = {};
    NativeModules.StatusBarManager = {
      getHeight: () => 40,
    };

    const origExpect = expect;
    expect = (e) => {
      const match = origExpect(e);
      match.toBeNotVisible = () => {
        return match.toBe(null);
      };
      match.toBeVisible = () => match.toBeTruthy();
      match.toExist = match.toBeVisible;
      return match;
    };

    setTimeout = (func) => {
      func();
    };

    App = render(<Application entryPoint={() => require('../playground/index')} />);

    jest.spyOn(global, 'alert');
  });

  function elementById(id) {
    let element = null;
    if (within(App.getByTestId(Application.VISIBLE_SCREEN_TEST_ID)).queryByTestId(id)) {
      element = within(App.getByTestId(Application.VISIBLE_SCREEN_TEST_ID)).getByTestId(id);
    } else if (within(App.getByTestId(Application.VISIBLE_OVERLAY_TEST_ID)).queryByTestId(id)) {
      element = within(App.getByTestId(Application.VISIBLE_OVERLAY_TEST_ID)).getByTestId(id);
    }

    if (element)
      element.tap = () => {
        fireEvent.press(element);
      };

    return element;
  }

  function elementByLabel(label) {
    let element = null;
    if (within(App.getByTestId(Application.VISIBLE_SCREEN_TEST_ID)).queryByText(label)) {
      element = within(App.getByTestId(Application.VISIBLE_SCREEN_TEST_ID)).getByText(label);
    } else if (within(App.getByTestId(Application.VISIBLE_OVERLAY_TEST_ID)).queryByText(label)) {
      element = within(App.getByTestId(Application.VISIBLE_OVERLAY_TEST_ID)).getByText(label);
    }

    if (element)
      element.tap = () => {
        fireEvent.press(element);
      };

    return element;
  }

  describe('Stack', () => {
    beforeEach(async () => {
      await elementById(TestIDs.STACK_BTN).tap();
    });

    it('push and pop screen', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
    });

    it('push and pop screen without animation', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.PUSH_NO_ANIM_BTN).tap();
      await expect(elementByLabel('Stack Position: 2')).toBeVisible();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    });

    it('pop using stack id', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
      await elementById(TestIDs.POP_USING_STACK_ID_BTN).tap();
      await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
    });

    it('pop using previous screen id', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementByLabel('Stack Position: 2')).toBeVisible();
      await elementById(TestIDs.POP_USING_PREVIOUS_SCREEN_ID_BTN).tap();
      await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    });

    it('pop to specific id', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementByLabel('Stack Position: 3')).toBeVisible();
      await elementById(TestIDs.POP_TO_FIRST_SCREEN_BTN).tap();
      await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    });

    it('pop to root', async () => {
      await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.POP_TO_ROOT_BTN).tap();
      await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
    });

    it('pop component should not detach component if can`t pop', async () => {
      await elementById(TestIDs.POP_NONE_EXISTENT_SCREEN_BTN).tap();
      await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
    });

    it('push title with subtitle', async () => {
      await elementById(TestIDs.PUSH_TITLE_WITH_SUBTITLE).tap();
      await expect(elementByLabel('Title')).toBeVisible();
      await expect(elementByLabel('Subtitle')).toBeVisible();
    });

    // it.only('does not crash when setting the stack root to an existing component id', async () => {
    //   await elementById(TestIDs.SET_STACK_ROOT_WITH_ID_BTN).tap();
    //   await elementById(TestIDs.SET_STACK_ROOT_WITH_ID_BTN).tap();
    // });

    // it(':ios: set stack root component should be first in stack', async () => {
    //   await elementById(TestIDs.PUSH_BTN).tap();
    //   await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    //   await elementById(TestIDs.SET_STACK_ROOT_BUTTON).tap();
    //   await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    //   await elementById(TestIDs.POP_BTN).tap();
    //   await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    // });

    it('push promise is resolved with pushed ViewController id', async () => {
      await elementById(TestIDs.STACK_COMMANDS_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementByLabel('push promise resolved with: ChildId')).toBeVisible();
      await expect(elementByLabel('pop promise resolved with: ChildId')).toBeVisible();
    });

    it('screen lifecycle', async () => {
      await elementById(TestIDs.PUSH_LIFECYCLE_BTN).tap();
      await expect(elementByLabel('didAppear')).toBeVisible();

      await elementById(TestIDs.PUSH_TO_TEST_DID_DISAPPEAR_BTN).tap();
      await expect(global.alert).toHaveBeenCalledWith('didDisappear');
    });
  });

  describe('setRoot', () => {
    it('componentDidAppear', async () => {
      await expect(elementByLabel('componentDidAppear')).toBeVisible();
    });

    // it('set title', async () => {
    //   await elementById(TestIDs.CHANGE_TITLE_BTN).tap();
    //   await expect(elementByLabel('Title Changed')).toBeVisible();
    // });
  });

  describe('Modals', () => {
    beforeEach(async () => {
      await elementById(TestIDs.NAVIGATION_TAB).tap();
      await elementById(TestIDs.MODAL_BTN).tap();
    });

    it('show modal', async () => {
      await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();
    });

    it('dismiss modal', async () => {
      await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();
      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
    });

    it('unmount modal when dismissed', async () => {
      await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();
      await elementById(TestIDs.MODAL_LIFECYCLE_BTN).tap();
      await expect(elementByLabel('didAppear')).toBeVisible();
      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(global.alert).toHaveBeenCalledWith('componentWillUnmount');
      await expect(global.alert).toHaveBeenCalledWith('didDisappear');
    });

    it('show multiple modals', async () => {
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
    });

    it('dismiss unknown screen id', async () => {
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
      await elementById(TestIDs.DISMISS_UNKNOWN_MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
    });

    it('dismiss modal by id which is not the top most', async () => {
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
      await elementById(TestIDs.DISMISS_PREVIOUS_MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
    });

    it('dismiss all previous modals by id when they are below top presented modal', async () => {
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

      await elementById(TestIDs.DISMISS_ALL_PREVIOUS_MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
    });

    it('dismiss some modal by id deep in the stack', async () => {
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

      await elementById(TestIDs.DISMISS_FIRST_MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();

      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
    });

    it('dismissAllModals', async () => {
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
      await elementById(TestIDs.DISMISS_ALL_MODALS_BTN).tap();
      await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
    });

    it('push into modal', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    });

    it('present modal multiple times', async () => {
      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    });

    it('setRoot dismisses modals', async () => {
      await elementById(TestIDs.SET_ROOT).tap();
      await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeNotVisible();
      await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    });

    it('dismissModal promise is resolved with root ViewController id', async () => {
      await elementById(TestIDs.MODAL_COMMANDS_BTN).tap();
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('showModal promise resolved with: UniqueStackId')).toBeVisible();
      await expect(
        elementByLabel('modalDismissed listener called with with: UniqueStackId')
      ).toBeVisible();
      await expect(
        elementByLabel('dismissModal promise resolved with: UniqueStackId')
      ).toBeVisible();
    });
  });

  describe('BottomTabs', () => {
    beforeEach(async () => {
      await elementById(TestIDs.BOTTOM_TABS_BTN).tap();
      await expect(elementByLabel('First Tab')).toBeVisible();
    });

    it('switch to tab by index', async () => {
      await elementById(TestIDs.SWITCH_TAB_BY_INDEX_BTN).tap();
      await expect(elementByLabel('First Tab')).toBeNotVisible();
      await expect(elementByLabel('Second Tab')).toBeVisible();
    });

    it('switch to tab by componentId', async () => {
      await elementById(TestIDs.SWITCH_TAB_BY_COMPONENT_ID_BTN).tap();
      await expect(elementByLabel('First Tab')).toBeNotVisible();
      await expect(elementByLabel('Second Tab')).toBeVisible();
    });

    it('push bottom tabs', async () => {
      await elementById(TestIDs.SWITCH_TAB_BY_INDEX_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementById(TestIDs.PUSHED_BOTTOM_TABS)).toBeVisible();
    });

    // it('set Tab Bar badge on current Tab', async () => {
    //   await elementById(TestIDs.SET_BADGE_BTN).tap();
    //   await expect(elementByLabel('NEW')).toBeVisible();
    // });

    // it('set empty string badge on a current Tab should clear badge', async () => {
    //   await elementById(TestIDs.SET_BADGE_BTN).tap();
    //   await expect(elementByLabel('NEW')).toBeVisible();
    //   await elementById(TestIDs.CLEAR_BADGE_BTN).tap();
    //   await expect(elementByLabel('NEW')).toBeNotVisible();
    // });

    xit('merge options correctly in SideMenu inside BottomTabs layout', async () => {
      await elementById(TestIDs.SWITCH_TAB_BY_INDEX_BTN).tap();
      await elementById(TestIDs.SIDE_MENU_INSIDE_BOTTOM_TABS_BTN).tap();
      await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();

      await elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN).tap();
      await expect(elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN)).toBeNotVisible();
    });

    it(':android: hide Tab Bar', async () => {
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
      await elementById(TestIDs.HIDE_TABS_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
    });

    it(':android: show Tab Bar', async () => {
      await elementById(TestIDs.HIDE_TABS_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
      await elementById(TestIDs.SHOW_TABS_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
    });

    it('hide Tab Bar on push', async () => {
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
      await elementById(TestIDs.HIDE_TABS_PUSH_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
    });

    it('hide Tab Bar on push from second bottomTabs screen', async () => {
      await elementById(TestIDs.SWITCH_TAB_BY_INDEX_BTN).tap();
      await elementById(TestIDs.HIDE_TABS_PUSH_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
    });

    it('hide Tab Bar on push from second bottomTabs screen - deep stack', async () => {
      await elementById(TestIDs.SWITCH_TAB_BY_INDEX_BTN).tap();
      await elementById(TestIDs.HIDE_TABS_PUSH_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
    });

    it('hide Tab Bar on second tab after pressing the tab', async () => {
      await elementById(TestIDs.SECOND_TAB_BAR_BTN).tap();
      await elementById(TestIDs.HIDE_TABS_PUSH_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
    });
  });

  describe('Overlay', () => {
    beforeEach(async () => {
      await elementById(TestIDs.NAVIGATION_TAB).tap();
      await elementById(TestIDs.OVERLAY_BTN).tap();
    });

    it('show and dismiss overlay', async () => {
      await elementById(TestIDs.SHOW_OVERLAY_BTN).tap();
      await expect(elementById(TestIDs.OVERLAY_ALERT_HEADER)).toBeVisible();
      await elementById(TestIDs.DISMISS_BTN).tap();
      await expect(elementById(TestIDs.OVERLAY_ALERT_HEADER)).toBeNotVisible();
    });

    it('overlay pass touches - true', async () => {
      await elementById(TestIDs.SHOW_TOUCH_THROUGH_OVERLAY_BTN).tap();
      await expect(elementById(TestIDs.SHOW_OVERLAY_BTN)).toBeVisible();
      await elementById(TestIDs.ALERT_BUTTON).tap();
      await expect(elementByLabel('Alert displayed')).toBeVisible();
    });

    it('setRoot should not remove overlay', async () => {
      await elementById(TestIDs.SHOW_TOUCH_THROUGH_OVERLAY_BTN).tap();
      await elementById(TestIDs.SET_ROOT_BTN).tap();
      await expect(elementById(TestIDs.OVERLAY_ALERT_HEADER)).toBeVisible();
    });

    it('nested touchables work as expected', async () => {
      await elementById(TestIDs.TOAST_BTN).tap();
      await elementById(TestIDs.TOAST_OK_BTN_INNER).tap();
      await expect(elementByLabel('Inner button clicked')).toBeVisible();
      await elementById(TestIDs.OK_BUTTON).tap();

      await elementById(TestIDs.TOAST_BTN).tap();
      await elementById(TestIDs.TOAST_OK_BTN_OUTER).tap();
      await expect(elementByLabel('Outer button clicked')).toBeVisible();
    });

    xtest('overlay pass touches - false', async () => {
      await elementById(TestIDs.SHOW_OVERLAY_BUTTON).tap();
      await expect(elementById(TestIDs.SHOW_OVERLAY_BUTTON)).toBeVisible();
      await expect(elementById(TestIDs.TOP_BAR_ELEMENT)).toBeVisible();
      await elementById(TestIDs.HIDE_TOP_BAR_BUTTON).tap();
      await expect(elementById(TestIDs.TOP_BAR_ELEMENT)).toBeVisible();
    });
  });

  describe('Overlay Dismiss all', () => {
    beforeEach(async () => {
      await elementById(TestIDs.NAVIGATION_TAB).tap();
      await elementById(TestIDs.OVERLAY_BTN).tap();
    });

    // it('dismissAllOverlays should dismiss all opened overlays', async () => {
    //   await elementById(TestIDs.SHOW_FULLSCREEN_OVERLAY_BTN).tap();
    //   await elementById(TestIDs.SHOW_OVERLAY_BTN).tap();
    //   await elementById(TestIDs.DISMISS_ALL_OVERLAYS_BUTTON).tap();
    //   await expect(elementById(TestIDs.OVERLAY_DISMISSED_COUNT)).toHaveText('2');
    // });

    // it('dismissAllOverlays should be able to dismiss only one overlay', async () => {
    //   await elementById(TestIDs.SHOW_OVERLAY_BTN).tap();
    //   await elementById(TestIDs.DISMISS_ALL_OVERLAYS_BUTTON).tap();
    //   await expect(elementById(TestIDs.OVERLAY_DISMISSED_COUNT)).toHaveText('1');
    // })
  });

  describe('Buttons', () => {
    beforeEach(async () => {
      await elementById(TestIDs.OPTIONS_TAB).tap();
      await elementById(TestIDs.GOTO_BUTTONS_SCREEN).tap();
    });

    it(':android: should not effect left buttons when hiding back button', async () => {
      await elementById(TestIDs.TOGGLE_BACK).tap();
      await expect(elementById(TestIDs.LEFT_BUTTON)).toBeVisible();
      await expect(elementById(TestIDs.TEXTUAL_LEFT_BUTTON)).toBeVisible();
      await expect(elementById(TestIDs.BACK_BUTTON)).toBeVisible();

      await elementById(TestIDs.TOGGLE_BACK).tap();
      await expect(elementById(TestIDs.LEFT_BUTTON)).toBeVisible();
      await expect(elementById(TestIDs.TEXTUAL_LEFT_BUTTON)).toBeVisible();
    });

    it('sets right buttons', async () => {
      await expect(elementById(TestIDs.BUTTON_ONE)).toBeVisible();
      await expect(elementById(TestIDs.ROUND_BUTTON)).toBeVisible();
    });

    it('set left buttons', async () => {
      await expect(elementById(TestIDs.LEFT_BUTTON)).toBeVisible();
    });

    it('pass props to custom button component', async () => {
      await expect(elementByLabel('Two')).toBeVisible();
    });

    it('pass props to custom button component should exist after push pop', async () => {
      await expect(elementByLabel('Two')).toBeVisible();
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementByLabel('Two')).toBeVisible();
    });

    it('pop using default back button', async () => {
      await expect(elementByLabel('Two')).toBeVisible();
      await elementById('RNN.back').tap();
      await expect(elementByLabel('Two')).toBeNotVisible();
    });

    it('custom button is clickable', async () => {
      await elementByLabel('Two').tap();
      await expect(elementByLabel('Times created: 1')).toExist();
    });

    it('Resetting buttons should unmount button react view', async () => {
      await elementById(TestIDs.SHOW_LIFECYCLE_BTN).tap();
      await elementById(TestIDs.RESET_BUTTONS).tap();
      await expect(elementByLabel('Button component unmounted')).toBeVisible();
    });

    it('change button props without rendering all buttons', async () => {
      await elementById(TestIDs.CHANGE_BUTTON_PROPS).tap();
      await expect(elementByLabel('Three')).toBeVisible();
    });

    it('pop using back button', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.BACK_BUTTON).tap();
      await expect(elementByLabel('Buttons')).toBeVisible();
    });

    it('resizes title component when a button is added with mergeOptions', async () => {
      await elementById(TestIDs.RESET_BUTTONS).tap();
      await elementById(TestIDs.SET_RIGHT_BUTTONS).tap();
      await elementById(TestIDs.BUTTON_THREE).tap();
    });

    it('Button component is not recreated if it has a predefined componentId', async () => {
      await elementById(TestIDs.SET_RIGHT_BUTTONS).tap();
      await elementById(TestIDs.ROUND_BUTTON).tap();
      await expect(elementByLabel('Times created: 1')).toBeVisible();
      await elementById(TestIDs.OK_BUTTON).tap();

      await elementById(TestIDs.SET_RIGHT_BUTTONS).tap();
      await elementById(TestIDs.ROUND_BUTTON).tap();
      await expect(elementByLabel('Times created: 1')).toBeVisible();
      await elementById(TestIDs.OK_BUTTON).tap();

      await elementById(TestIDs.SET_RIGHT_BUTTONS).tap();
      await elementById(TestIDs.ROUND_BUTTON).tap();
      await expect(elementByLabel('Times created: 1')).toBeVisible();
    });

    it('Accepts textual left button', async () => {
      await expect(elementById(TestIDs.TEXTUAL_LEFT_BUTTON)).toBeVisible();
    });

    it('Updates left button', async () => {
      await elementById(TestIDs.ADD_COMPONENT_BUTTON).tap();
      await expect(elementById('leftButton0')).toBeVisible();

      await elementById(TestIDs.ADD_COMPONENT_BUTTON).tap();
      await expect(elementById('leftButton1')).toBeVisible();
    });
  });

  describe('SetRoot', () => {
    beforeEach(async () => {
      await elementById(TestIDs.NAVIGATION_TAB).tap();
      await elementById(TestIDs.SET_ROOT_BTN).tap();
    });

    it('set root multiple times with the same componentId', async () => {
      await elementById(TestIDs.SET_MULTIPLE_ROOTS_BTN).tap();
      await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    });

    it('set root hides bottomTabs', async () => {
      await elementById(TestIDs.SET_ROOT_HIDES_BOTTOM_TABS_BTN).tap();
      await expect(elementById(TestIDs.LAYOUTS_TAB)).toBeNotVisible();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementById(TestIDs.LAYOUTS_TAB)).toBeVisible();
    });

    it('set root with deep stack hides bottomTabs', async () => {
      await elementById(TestIDs.SET_ROOT_WITH_STACK_HIDES_BOTTOM_TABS_BTN).tap();
      await expect(elementById(TestIDs.LAYOUTS_TAB)).toBeNotVisible();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementById(TestIDs.LAYOUTS_TAB)).toBeVisible();
    });

    it('set root without stack hides bottomTabs', async () => {
      await elementById(TestIDs.SET_ROOT_WITHOUT_STACK_HIDES_BOTTOM_TABS_BTN).tap();
      await expect(elementById(TestIDs.LAYOUTS_TAB)).toBeNotVisible();
    });
  });

  describe('Options', () => {
    beforeEach(async () => {
      await elementById(TestIDs.OPTIONS_TAB).tap();
    });

    it('declare options on a component', async () => {
      await expect(elementByLabel('Styling Options')).toBeVisible();
    });

    it('change title on component component', async () => {
      await expect(elementByLabel('Styling Options')).toBeVisible();
      await elementById(TestIDs.CHANGE_TITLE_BTN).tap();
      await expect(elementByLabel('Title Changed')).toBeVisible();
    });

    it('hides TopBar when pressing on Hide TopBar and shows it when pressing on Show TopBar', async () => {
      await elementById(TestIDs.HIDE_TOP_BAR_BTN).tap();
      await expect(elementById(TestIDs.TOP_BAR)).toBeNotVisible();
      await elementById(TestIDs.SHOW_TOP_BAR_BTN).tap();
      await expect(elementById(TestIDs.TOP_BAR)).toBeVisible();
    });

    it('default options should apply to all screens in stack', async () => {
      await elementById(TestIDs.HIDE_TOPBAR_DEFAULT_OPTIONS).tap();
      await expect(elementById(TestIDs.TOP_BAR)).toBeVisible();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeNotVisible();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeNotVisible();
    });

    it('default options should not override static options', async () => {
      await elementById(TestIDs.HIDE_TOPBAR_DEFAULT_OPTIONS).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementById(TestIDs.TOP_BAR)).toBeVisible();
      await expect(elementByLabel('Styling Options')).toBeVisible();
    });

    it('set title component', async () => {
      await elementById(TestIDs.SET_REACT_TITLE_VIEW).tap();
      await expect(elementByLabel('Press Me')).toBeVisible();
    });

    it('set title after setting react component', async () => {
      // await elementById(TestIDs.SET_REACT_TITLE_VIEW).tap();
      // await expect(elementByLabel('Press Me')).toBeVisible();
      await elementById(TestIDs.CHANGE_TITLE_BTN).tap();
      await expect(elementByLabel('Title Changed')).toBeVisible();
    });

    it('Popping screen with yellow box should not crash', async () => {
      await elementById(TestIDs.SHOW_YELLOW_BOX_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementByLabel('Styling Options')).toBeVisible();
    });

    it('Merging options to invisible component in stack should not affect current component', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.HIDE_PREVIOUS_SCREEN_TOP_BAR).tap();
      await expect(elementByLabel('Pushed Screen')).toBeVisible();
    });

    it('Merging options to invisible component should affect the invisible component', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.HIDE_PREVIOUS_SCREEN_TOP_BAR).tap();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementByLabel('Styling Options')).toBeNotVisible();
    });

    // xit('hides topBar onScroll down and shows it on scroll up', async () => {
    //   await elementById(TestIDs.PUSH_OPTIONS_BUTTON).tap();
    //   await elementById(TestIDs.SCROLLVIEW_SCREEN_BUTTON).tap();
    //   await elementById(TestIDs.TOGGLE_TOP_BAR_HIDE_ON_SCROLL).tap();
    //   await expect(elementById(TestIDs.TOP_BAR)).toBeVisible();
    //   await element(by.id(TestIDs.SCROLLVIEW_ELEMENT)).swipe('up', 'slow');
    //   await expect(elementById(TestIDs.TOP_BAR)).toBeNotVisible();
    //   await element(by.id(TestIDs.SCROLLVIEW_ELEMENT)).swipe('down', 'fast');
    //   await expect(elementById(TestIDs.TOP_BAR)).toBeVisible();
    // });
  });

  describe('static lifecycle events', () => {
    beforeEach(async () => {
      await elementById(TestIDs.NAVIGATION_TAB).tap();
      await elementById(TestIDs.SHOW_STATIC_EVENTS_SCREEN).tap();
      await elementById(TestIDs.STATIC_EVENTS_OVERLAY_BTN).tap();
      await expect(elementByLabel('Static Lifecycle Events Overlay')).toBeVisible();
      await expect(elementByLabel('componentWillAppear | EventsOverlay | Component')).toBeVisible();
      await expect(elementByLabel('componentDidAppear | EventsOverlay | Component')).toBeVisible();
      await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
    });

    it('willAppear didAppear didDisappear', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementByLabel('componentWillAppear | Pushed | Component')).toBeVisible();
      await expect(elementByLabel('componentDidAppear | Pushed | Component')).toBeVisible();
      await expect(
        elementByLabel('componentDidDisappear | EventsScreen | Component')
      ).toBeVisible();
    });

    it('pushing and popping screen dispatch static event', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementByLabel('push')).toBeVisible();
      await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementByLabel('pop')).toBeVisible();
    });

    it('showModal and dismissModal dispatch static event', async () => {
      await elementById(TestIDs.MODAL_BTN).tap();
      await expect(elementByLabel('showModal')).toBeVisible();
      await expect(elementByLabel('componentWillAppear | Modal | Component')).toBeVisible();
      await expect(elementByLabel('componentDidAppear | Modal | Component')).toBeVisible();
      await expect(
        elementByLabel('componentDidDisappear | EventsScreen | Component')
      ).toBeVisible();
      await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
      await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
      await expect(elementByLabel('dismissModal')).toBeVisible();
      await expect(elementByLabel('componentWillAppear | EventsScreen | Component')).toBeVisible();
      await expect(elementByLabel('componentDidAppear | EventsScreen | Component')).toBeVisible();
      await expect(elementByLabel('componentDidDisappear | Modal | Component')).toBeVisible();
    });

    it('unmounts when dismissed', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.DISMISS_BTN).tap();
      await expect(elementByLabel('Overlay Unmounted')).toBeVisible();
      await elementByLabel('OK').tap();
    });

    it('top bar buttons willAppear didAppear didDisappear', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.PUSH_OPTIONS_BUTTON).tap();
      await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
      await elementById(TestIDs.GOTO_BUTTONS_SCREEN).tap();
      await expect(
        elementByLabel('componentWillAppear | CustomRoundedButton | TopBarButton')
      ).toBeVisible();
      await expect(
        elementByLabel('componentDidAppear | CustomRoundedButton | TopBarButton')
      ).toBeVisible();
      await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
      await elementById(TestIDs.RESET_BUTTONS).tap();
      await expect(
        elementByLabel('componentDidDisappear | CustomRoundedButton | TopBarButton')
      ).toBeVisible();
    });

    it('top bar title willAppear didAppear didDisappear', async () => {
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.PUSH_OPTIONS_BUTTON).tap();
      await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
      await elementById(TestIDs.SET_REACT_TITLE_VIEW).tap();
      await expect(
        elementByLabel('componentWillAppear | ReactTitleView | TopBarTitle')
      ).toBeVisible();
      await expect(
        elementByLabel('componentDidAppear | ReactTitleView | TopBarTitle')
      ).toBeVisible();
      await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(
        elementByLabel('componentDidDisappear | ReactTitleView | TopBarTitle')
      ).toBeVisible();
    });

    xit('unmounts previous root before resolving setRoot promise', async () => {
      await elementById(TestIDs.SET_ROOT_BTN).tap();
      await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
      await elementById(TestIDs.SET_ROOT_BTN).tap();
      await expect(elementByLabel('setRoot complete - previous root is unmounted')).toBeVisible();
    });

    it('top bar custom button willAppear didAppear after pop, on a root screen', async () => {
      await elementById(TestIDs.SHOW_RIGHT_BUTTON).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
      await elementById(TestIDs.BACK_BUTTON).tap();
      await expect(
        elementByLabel('componentWillAppear | CustomRoundedButton | TopBarButton')
      ).toBeVisible();
      await expect(
        elementByLabel('componentDidAppear | CustomRoundedButton | TopBarButton')
      ).toBeVisible();
    });
  });

  /// Remove below
  it('hides TopBar when pressing on Hide TopBar and shows it when pressing on Show TopBar', async () => {
    await elementById(TestIDs.OPTIONS_TAB).tap();
    await elementById(TestIDs.HIDE_TOP_BAR_BTN).tap();
    await expect(elementById(TestIDs.TOP_BAR)).toBeNotVisible();
    await elementById(TestIDs.SHOW_TOP_BAR_BTN).tap();
    await expect(elementById(TestIDs.TOP_BAR)).toBeVisible();
  });
  it('show overlay', async () => {
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.SHOW_STATIC_EVENTS_SCREEN).tap();
    await elementById(TestIDs.STATIC_EVENTS_OVERLAY_BTN).tap();
    await App.findByTestId('lifecycleOverlay');
  });

  it('send viewDidAppear', async () => {
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.SHOW_STATIC_EVENTS_SCREEN).tap();
    await elementById(TestIDs.STATIC_EVENTS_OVERLAY_BTN).tap();
    await App.findByTestId('lifecycleOverlay');

    await elementById(TestIDs.PUSH_BTN).tap();
    await App.findByText('componentDidAppear | Pushed | Component');
  });
});
