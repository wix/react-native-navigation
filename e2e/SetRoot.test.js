import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const { elementById, elementByLabel } = Utils;

describe('SetRoot', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
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

  it('set root should not override props for component with identical id', async () => {
    await expect(elementByLabel('Two')).toBeVisible();
    await elementById(TestIDs.ROUND_BUTTON).tap();
    await expect(elementByLabel('Times created: 1')).toBeVisible();
    await elementById(TestIDs.OK_BUTTON).tap();
    await elementById(TestIDs.SET_ROOT_WITH_BUTTONS).tap();
    await expect(elementByLabel('Two')).toBeVisible();
    await elementById(TestIDs.ROUND_BUTTON).tap();
    await expect(elementByLabel('Times created: 1')).toBeVisible();
    await elementById(TestIDs.OK_BUTTON).tap();
  });
});
