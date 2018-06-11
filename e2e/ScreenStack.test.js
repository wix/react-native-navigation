const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');
const Android = require('./AndroidUtils');

const { elementByLabel, elementById, sleep } = Utils;

describe('screen stack', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test('push and pop screen', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementById(testIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test(':android: push and pop screen without animation', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await expect(elementById(testIDs.OPTIONS_SCREEN_HEADER)).toBeVisible();
    Android.pressBack();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('pop screen deep in the stack', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementById(testIDs.POP_PREVIOUS_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('pop to specific id', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 3')).toBeVisible();
    await elementById(testIDs.POP_STACK_POSITION_ONE_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
  });

  test('pop to root', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.POP_TO_ROOT).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('switch to tab', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
    await elementById(testIDs.SWITCH_SECOND_TAB_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeNotVisible();
    await expect(elementByLabel('This is tab 2')).toBeVisible();
  });

  test('switch to tab by cotnainerId', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
    await elementById(testIDs.SWITCH_SECOND_TAB_BUTTON).tap();
    await expect(elementByLabel('This is tab 2')).toBeVisible();
    await elementById(testIDs.SWITCH_FIRST_TAB_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
  });

  test('push stack with multiple children', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await elementById(testIDs.MODAL_WITH_STACK_BUTTON).tap();
    await expect(elementByLabel('Screen 2')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementByLabel('Screen 1')).toBeVisible();
  });

  test('push external component with options', async () => {
    await elementById(testIDs.PUSH_EXTERNAL_COMPONENT_BUTTON).tap();
    await expect(elementByLabel('This is an external component')).toBeVisible();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
  });

  test('push into a stack from BottomTabs', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
  });

  test(':ios: set stack root component should be first in stack', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    await elementById(testIDs.SET_STACK_ROOT_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
  });

  test('push to stack with static id from SideMenu', async () => {
    await elementById(testIDs.TAB_BASED_APP_SIDE_BUTTON).tap();
    await elementById(testIDs.SHOW_LEFT_SIDE_MENU_BUTTON).tap();
    await elementById(testIDs.LEFT_SIDE_MENU_PUSH_BUTTON).tap();
    await expect(elementByLabel('Text Screen')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementByLabel('This is a side menu center screen tab 1')).toBeVisible();
  });
});
