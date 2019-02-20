const Utils = require('./Utils');
const TestIDs = require('../playground/src/testIDs');
const Android = require('./AndroidUtils');
const { elementByLabel, elementById, sleep } = Utils;
describe('BottomTabs', () => {
  beforeEach(async () => {
    await device.relaunchApp();
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

  it('set Tab Bar badge on current Tab', async () => {
    await elementById(TestIDs.SET_BADGE_BTN).tap();
    await expect(element(by.text('NEW'))).toBeVisible();
  });

  it(':ios: set Tab Bar badge null on a current Tab should reset badge', async () => {
    await elementById(TestIDs.SET_BADGE_BTN).tap();
    await expect(element(by.text('NEW'))).toBeVisible();
    await elementById(TestIDs.CLEAR_BADGE_BTN).tap();
    await expect(element(by.text('NEW'))).toBeNotVisible();
  });

  it('merge options correctly in SideMenu inside BottomTabs layout', async () => {
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
    await elementById(TestIDs.HIDE_TABS_PUSH_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
  });
});
