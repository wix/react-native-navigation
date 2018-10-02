const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');

const { elementByLabel, elementById } = Utils;

describe('complex layout', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test('shows external component in stack in modal', async () => {
    await elementById(testIDs.COMPLEX_LAYOUT_BUTTON).tap();
    await elementById(testIDs.EXTERNAL_COMPONENT_IN_STACK).tap();
    await expect(elementByLabel('External component in stack')).toBeVisible();
  });

  test('shows external component in deep stack in modal', async () => {
    await elementById(testIDs.COMPLEX_LAYOUT_BUTTON).tap();
    await elementById(testIDs.EXTERNAL_COMPONENT_IN_DEEP_STACK).tap();
    await expect(elementByLabel('External component in deep stack')).toBeVisible();
  });

  test('merge options correctly in SideMenu inside BottomTabs layout', async () => {
    await elementById(testIDs.COMPLEX_LAYOUT_BUTTON).tap();
    await elementById(testIDs.SIDE_MENU_LAYOUT_INSIDE_BOTTOM_TAB).tap();
    await elementById(testIDs.SECOND_TAB_BAR_BUTTON).tap();
    await elementById(testIDs.OPEN_SIDE_MENU).tap();
    await expect(elementByLabel('This is a left side menu screen')).toBeVisible();

    await elementById(testIDs.HIDE_LEFT_SIDE_MENU_BUTTON).tap();
    await expect(elementByLabel('This is a left side menu screen')).toBeNotVisible();
  });
});
