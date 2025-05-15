import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const { elementByLabel, elementById } = Utils;

describe.each([true, false])(`SideMenu - ${shouldTestAboveContent ? 'Above Content' : 'Push Screen'}`, (shouldTestAboveContent) => {
  let openLeftMenuTestId;
  let openRightMenuTestId;

  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.SIDE_MENU_BTN).tap();
    openLeftMenuTestId = shouldTestAboveContent ? TestIDs.OPEN_LEFT_SIDE_MENU_ABOVE_CONTENT_BTN : TestIDs.OPEN_LEFT_SIDE_MENU_BTN;
    openRightMenuTestId = shouldTestAboveContent ? TestIDs.OPEN_RIGHT_SIDE_MENU_ABOVE_CONTENT_BTN : TestIDs.OPEN_RIGHT_SIDE_MENU_BTN;
  });

  it('close SideMenu and push to stack with static id', async () => {
    await elementById(openLeftMenuTestId).tap();
    await elementById(TestIDs.LEFT_SIDE_MENU_PUSH_BTN).tap();
    if(shouldTestAboveContent) {
      await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
      await expect(elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN)).toBeNotVisible();
    } else {
      await elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN).tap();
      await elementById(TestIDs.POP_BTN).tap();
      await expect(elementById(TestIDs.CENTER_SCREEN_HEADER)).toBeVisible();
    }
  });

  it('Push to stack with static id and close SideMenu with screen options', async () => {
    await elementById(openLeftMenuTestId).tap();
    await elementById(TestIDs.LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN).tap();
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.CENTER_SCREEN_HEADER)).toBeVisible();
  });

  it('side menu visibility - left', async () => {
    await elementById(openLeftMenuTestId).tap();
    await elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN).tap();
    await expect(elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN)).toBeNotVisible();
  });

  it('side menu visibility - right', async () => {
    await elementById(openRightMenuTestId).tap();
    await elementById(TestIDs.CLOSE_RIGHT_SIDE_MENU_BTN).tap();
    await expect(elementById(TestIDs.CLOSE_RIGHT_SIDE_MENU_BTN)).toBeNotVisible();
  });

  it.e2e('should rotate', async () => {
    await elementById(openLeftMenuTestId).tap();
    await device.setOrientation('landscape');
    await expect(elementById(TestIDs.LEFT_SIDE_MENU_PUSH_BTN)).toBeVisible();
  });

  it.e2e(':ios: rotation should update drawer height', async () => {
    await elementById(openLeftMenuTestId).tap();
    await expect(elementByLabel('left drawer height: 869')).toBeVisible();
    await device.setOrientation('landscape');
    await expect(elementByLabel('left drawer height: 428')).toBeVisible();
    await device.setOrientation('portrait');
    await expect(elementByLabel('left drawer height: 869')).toBeVisible();
  });

  it.e2e('should set left drawer width', async () => {
    await elementById(openLeftMenuTestId).tap();
    await expect(elementById(TestIDs.SIDE_MENU_LEFT_DRAWER_HEIGHT_TEXT)).toBeVisible();
    await expect(elementByLabel('left drawer width: 250')).toBeVisible();
  });

  it.e2e('should change left drawer width', async () => {
    await elementById(TestIDs.CHANGE_LEFT_SIDE_MENU_WIDTH_BTN).tap();
    await elementById(openLeftMenuTestId).tap();
    await expect(elementByLabel('left drawer width: 50')).toBeVisible();
  });

  it.e2e('should set right drawer width', async () => {
    await elementById(openRightMenuTestId).tap();
    await expect(elementByLabel('right drawer width: 250')).toBeVisible();
  });

  it.e2e('should change right drawer width', async () => {
    await elementById(TestIDs.CHANGE_RIGHT_SIDE_MENU_WIDTH_BTN).tap();
    await elementById(openRightMenuTestId).tap();
    await expect(elementByLabel('right drawer width: 50')).toBeVisible();
  });
});
