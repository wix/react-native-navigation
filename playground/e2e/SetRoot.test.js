import Utils from './Utils';
import TestIDs from '../src/testIDs';

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

  it(':ios: set root with two children and second tab selected hides bottomTabs', async () => {
    await elementById(TestIDs.SET_ROOT_WITH_TWO_CHILDREN_HIDES_BOTTOM_TABS_BTN).tap();
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

it.e2e(':ios: set root with left and right side menus - menu visibility', async () => {
    await elementById(TestIDs.SET_ROOT_WITH_MENUS).tap();
    await elementById(TestIDs.TOGGLE_SIDE_MENU_OPEN_MODE_BTN).tap();
    await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
    await elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN).tap();
    await expect(elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN)).toBeNotVisible();
    await elementById(TestIDs.OPEN_RIGHT_SIDE_MENU_BTN).tap();
    await elementById(TestIDs.CLOSE_RIGHT_SIDE_MENU_BTN).tap();
    await expect(elementById(TestIDs.CLOSE_RIGHT_SIDE_MENU_BTN)).toBeNotVisible();
});
