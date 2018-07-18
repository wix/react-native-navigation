const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');

const { elementByLabel, elementById } = Utils;

describe('top level api', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test('shows welcome screen', async () => {
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('switch to tab based app, passProps and functions', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
    await expect(elementByLabel('Hello from a function!')).toBeVisible();
  });

  test(':ios: switch to tabs with side menus', async () => {
    await elementById(testIDs.TAB_BASED_APP_SIDE_BUTTON).tap();
    await elementById(testIDs.CENTERED_TEXT_HEADER).swipe('right');
    await expect(elementById(testIDs.HIDE_LEFT_SIDE_MENU_BUTTON)).toBeVisible();
  });

  test('screen lifecycle', async () => {
    await elementById(testIDs.PUSH_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await elementById(testIDs.PUSH_TO_TEST_DID_DISAPPEAR_BUTTON).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });

  test('unmount is called on pop', async () => {
    await elementById(testIDs.PUSH_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementByLabel('componentWillUnmount')).toBeVisible();
    await elementByLabel('OK').atIndex(0).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });
});
