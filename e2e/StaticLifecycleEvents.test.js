const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');
const { elementByLabel, elementById } = Utils;

describe('static lifecycle events', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('didAppear didDisappear', async () => {
    await elementById(testIDs.PUSH_STATIC_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('Static Lifecycle Events Overlay')).toBeVisible();
    await expect(elementByLabel('componentDidAppear | navigation.playground.StaticLifecycleOverlay')).toBeVisible();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('componentDidAppear | navigation.playground.PushedScreen')).toBeVisible();
    await expect(elementByLabel('componentDidDisappear | navigation.playground.WelcomeScreen')).toBeVisible();
  });
});
