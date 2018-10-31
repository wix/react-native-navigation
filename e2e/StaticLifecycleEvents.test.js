const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');
const { elementByLabel, elementById } = Utils;

describe('static lifecycle events', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test('didAppear didDisappear', async () => {
    await elementById(testIDs.PUSH_STATIC_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('Static Lifecycle Events Overlay')).toBeVisible();
    await expect(elementByLabel('componentDidAppear | navigation.playground.StaticLifecycleOverlay')).toBeVisible();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('componentDidAppear | navigation.playground.PushedScreen')).toBeVisible();
    await expect(elementByLabel('componentDidDisappear | navigation.playground.WelcomeScreen')).toBeVisible();
  });

  test(':ios: pushing and popping screen dispatch static event', async () => {
    await elementById(testIDs.PUSH_STATIC_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('Static Lifecycle Events Overlay')).toBeVisible();
    await expect(elementByLabel('componentDidAppear | navigation.playground.StaticLifecycleOverlay')).toBeVisible();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('push')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementByLabel('pop')).toBeVisible();
  });

  test(':ios: showModal and dismissModal dispatch static event', async () => {
    await elementById(testIDs.PUSH_STATIC_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('Static Lifecycle Events Overlay')).toBeVisible();
    await expect(elementByLabel('componentDidAppear | navigation.playground.StaticLifecycleOverlay')).toBeVisible();
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('showModal')).toBeVisible();
    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementByLabel('dismissModal')).toBeVisible();
  });

  test(':ios: unmounts when dismissed', async () => {
    await elementById(testIDs.PUSH_STATIC_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('Static Lifecycle Events Overlay')).toBeVisible();
    await elementById(testIDs.DISMISS_BUTTON).tap();
    await expect(elementByLabel('Overlay Unmounted')).toBeVisible();
    await elementByLabel('OK').tap();
  });
});
