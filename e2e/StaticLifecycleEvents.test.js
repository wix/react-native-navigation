const Utils = require('./Utils');
const TestIDs = require('../playground/src/testIDs');
const { elementByLabel, elementById } = Utils;

describe('static lifecycle events', () => {
  beforeEach(async () => {
    await device.relaunchApp();
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.SHOW_STATIC_EVENTS_SCREEN).tap();
    await elementById(TestIDs.STATIC_EVENTS_OVERLAY_BTN).tap();
  });

  it('didAppear didDisappear', async () => {
    await expect(elementByLabel('componentDidAppear | EventsOverlay')).toBeVisible();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('componentDidAppear | Pushed')).toBeVisible();
    await expect(elementByLabel('componentDidDisappear | EventsScreen')).toBeVisible();
  });

  it('pushing and popping screen dispatch static event', async () => {
    await expect(elementByLabel('Static Lifecycle Events Overlay')).toBeVisible();
    await expect(elementByLabel('componentDidAppear | EventsOverlay')).toBeVisible();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('push')).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementByLabel('pop')).toBeVisible();
  });

  it('showModal and dismissModal dispatch static event', async () => {
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('showModal')).toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementByLabel('dismissModal')).toBeVisible();
  });

  it('unmounts when dismissed', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Static Lifecycle Events Overlay')).toBeVisible();
    await elementById(TestIDs.DISMISS_BTN).tap();
    await expect(elementByLabel('Overlay Unmounted')).toBeVisible();
    await elementByLabel('OK').tap();
  });
});
