import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';
import Android from './AndroidUtils';

const { elementByLabel, elementById } = Utils;

describe('BottomTabs', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
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

  it('Badge not cleared after showing/dismissing modal', async () => {
    await elementById(TestIDs.SECOND_TAB_BAR_BTN).tap();
    await elementById(TestIDs.SET_BADGE_BTN).tap();
    await expect(element(by.text('Badge'))).toBeVisible();
    await elementById(TestIDs.MODAL_BTN).tap();
    await elementById(TestIDs.MODAL_BTN).tap();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(element(by.text('Badge'))).toBeVisible();
  });

  it('set empty string badge on a current Tab should clear badge', async () => {
    await elementById(TestIDs.SET_BADGE_BTN).tap();
    await expect(element(by.text('NEW'))).toBeVisible();
    await elementById(TestIDs.CLEAR_BADGE_BTN).tap();
    await expect(element(by.text('NEW'))).toBeNotVisible();
  });

  it.e2e('merge options correctly in SideMenu inside BottomTabs layout', async () => {
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

  it('hide Tab Bar on push from second bottomTabs screen', async () => {
    await elementById(TestIDs.SWITCH_TAB_BY_INDEX_BTN).tap();
    await elementById(TestIDs.HIDE_TABS_PUSH_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
  });

  it('hide Tab Bar on push from second bottomTabs screen - deep stack', async () => {
    await elementById(TestIDs.SWITCH_TAB_BY_INDEX_BTN).tap();
    await elementById(TestIDs.HIDE_TABS_PUSH_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
  });

  it('hide Tab Bar on second tab after pressing the tab', async () => {
    await elementById(TestIDs.SECOND_TAB_BAR_BTN).tap();
    await elementById(TestIDs.HIDE_TABS_PUSH_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeNotVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.BOTTOM_TABS)).toBeVisible();
  });

  it('invoke bottomTabPressed event', async () => {
    await elementById(TestIDs.THIRD_TAB_BAR_BTN).tap();
    await expect(elementByLabel('BottomTabPressed')).toBeVisible();
    await elementByLabel('OK').tap();
    await expect(elementByLabel('First Tab')).toBeVisible();
  });

  it.e2e(':android: hardware back tab selection history', async () => {
    await elementById(TestIDs.SECOND_TAB_BAR_BTN).tap();
    await elementById(TestIDs.FIRST_TAB_BAR_BUTTON).tap();
    await elementById(TestIDs.SECOND_TAB_BAR_BTN).tap();
    await elementById(TestIDs.SECOND_TAB_BAR_BTN).tap();
    await elementById(TestIDs.FIRST_TAB_BAR_BUTTON).tap();

     Android.pressBack();
    await expect(elementByLabel('Second Tab')).toBeVisible();

     Android.pressBack();
    await expect(elementByLabel('First Tab')).toBeVisible();

     Android.pressBack();
    await expect(elementByLabel('Second Tab')).toBeVisible();

     Android.pressBack();
    await expect(elementByLabel('First Tab')).toBeVisible();

     Android.pressBack();
    await expect(elementByLabel('First Tab')).toBeNotVisible();
    await expect(elementByLabel('Second Tab')).toBeNotVisible();
  });
});
