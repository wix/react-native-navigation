import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const { elementByLabel, elementById } = Utils;

describe.e2e('SideMenu', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.SIDE_MENU_BTN).tap();
  });

  it('close SideMenu and push to stack with static id', async () => {
    await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
    await elementById(TestIDs.LEFT_SIDE_MENU_PUSH_BTN).tap();
    await elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN).tap();
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.CENTER_SCREEN_HEADER)).toBeVisible();
  });

  it('Push to stack with static id and close SideMenu with screen options', async () => {
    await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
    await elementById(TestIDs.LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN).tap();
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.CENTER_SCREEN_HEADER)).toBeVisible();
  });

  it('side menu visibility - left', async () => {
    await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
    await elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN).tap();
    await expect(elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN)).toBeNotVisible();
  });

  it('side menu visibility - right', async () => {
    await elementById(TestIDs.OPEN_RIGHT_SIDE_MENU_BTN).tap();
    await elementById(TestIDs.CLOSE_RIGHT_SIDE_MENU_BTN).tap();
    await expect(elementById(TestIDs.CLOSE_RIGHT_SIDE_MENU_BTN)).toBeNotVisible();
  });

  it('should rotate', async () => {
    await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
    await device.setOrientation('landscape');
    await expect(elementById(TestIDs.LEFT_SIDE_MENU_PUSH_BTN)).toBeVisible();
  });

  it(':ios: rotation should update drawer height', async () => {
    await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
    await expect(elementByLabel('left drawer height: 838')).toBeVisible();
    await device.setOrientation('landscape');
    await expect(elementByLabel('left drawer height: 414')).toBeVisible();
    await device.setOrientation('portrait');
    await expect(elementByLabel('left drawer height: 838')).toBeVisible();
  });

  it('should set left drawer width', async () => {
    await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
    await expect(elementByLabel('left drawer width: 250')).toBeVisible();
  });

  it('should change left drawer width', async () => {
    await elementById(TestIDs.CHANGE_LEFT_SIDE_MENU_WIDTH_BTN).tap();
    await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
    await expect(elementByLabel('left drawer width: 50')).toBeVisible();
  });

  it('should set right drawer width', async () => {
    await elementById(TestIDs.OPEN_RIGHT_SIDE_MENU_BTN).tap();
    await expect(elementByLabel('right drawer width: 250')).toBeVisible();
  });

  it('should change right drawer width', async () => {
    await elementById(TestIDs.CHANGE_RIGHT_SIDE_MENU_WIDTH_BTN).tap();
    await elementById(TestIDs.OPEN_RIGHT_SIDE_MENU_BTN).tap();
    await expect(elementByLabel('right drawer width: 50')).toBeVisible();
  });
});
