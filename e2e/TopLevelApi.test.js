const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');

const { elementByLabel, elementById } = Utils;

describe('top level api', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('shows welcome screen', async () => {
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  it('switch to tab based app, passProps and functions', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
    await expect(elementByLabel('Hello from a function!')).toBeVisible();
  });

  it('switch to tabs with side menus', async () => {
    await elementById(testIDs.TAB_BASED_APP_SIDE_BUTTON).tap();
    await elementById(testIDs.CENTERED_TEXT_HEADER).swipe('right');
    await expect(elementById(testIDs.HIDE_LEFT_SIDE_MENU_BUTTON)).toBeVisible();
  });

  it('screen lifecycle', async () => {
    await elementById(testIDs.PUSH_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await elementById(testIDs.PUSH_TO_TEST_DID_DISAPPEAR_BUTTON).tap();
    await expect(elementByLabel('Alert')).toBeVisible();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });

  it('unmount is called on pop', async () => {
    await elementById(testIDs.PUSH_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementByLabel('componentWillUnmount')).toBeVisible();
    await elementByLabel('OK').atIndex(0).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });
});

describe('reload app', async () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('push a screen to ensure its not there after reload', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
    await device.reloadReactNative();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });
});
