const Utils = require('./Utils');

const elementByLabel = Utils.elementByLabel;

describe('top level api', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('shows welcome screen', async () => {
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  it('switch to tab based app, passProps and functions', async () => {
    await elementByLabel('Switch to tab based app').tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
    await expect(elementByLabel('Hello from a function!')).toBeVisible();
  });

  it('switch to tabs with side menus', async () => {
    await elementByLabel('Switch to app with side menus').tap();
    await elementByLabel('This is a side menu center screen tab 1').swipe('right');
    await expect(elementByLabel('This is a left side menu screen')).toBeVisible();
  });

  it('screen lifecycle', async () => {
    await elementByLabel('Push Lifecycle Screen').tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await elementByLabel('Push to test didDisappear').tap();
    await expect(elementByLabel('Alert')).toBeVisible();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });

  it('unmount is called on pop', async () => {
    await elementByLabel('Push Lifecycle Screen').tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await Utils.tapBackIos();
    await expect(elementByLabel('componentWillUnmount')).toBeVisible();
    await element(by.traits(['button']).and(by.label('OK'))).atIndex(0).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });
});

describe('reload app', async () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('push a screen to ensure its not there after reload', async () => {
    await elementByLabel('Push').tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
    await device.reloadReactNative();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });
});
