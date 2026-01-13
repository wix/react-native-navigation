import Utils from './Utils';
import TestIDs from '../src/testIDs';
import jestExpect from 'expect';

const {elementByLabel, elementById, expectImagesToBeEqual} = Utils;

describe('SideMenu', () => {
    beforeEach(async () => {
        await device.launchApp({newInstance : true});
        await elementById(TestIDs.SIDE_MENU_BTN).tap();
    });

    describe.each(['aboveContent', 'pushContent'])('Open mode %s', (openMode) => {
        beforeEach(async () => {
            if (openMode === 'pushContent') {
                await elementById(TestIDs.TOGGLE_SIDE_MENU_OPEN_MODE_BTN).tap();
            }
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

        it.e2e('should rotate', async () => {
            await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
            await device.setOrientation('landscape');
            await expect(elementById(TestIDs.LEFT_SIDE_MENU_PUSH_BTN)).toBeVisible();
        });

        it.e2e(':ios: rotation should update drawer height', async () => {
            await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
            await expect(elementByLabel('left drawer height: 869')).toBeVisible();
            await device.setOrientation('landscape');
            await expect(elementByLabel('left drawer height: 428')).toBeVisible();
            await device.setOrientation('portrait');
            await expect(elementByLabel('left drawer height: 869')).toBeVisible();
        });

        it.e2e('should set left drawer width', async () => {
            await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
            await expect(elementById(TestIDs.SIDE_MENU_LEFT_DRAWER_HEIGHT_TEXT)).toBeVisible();
            await expect(elementByLabel('left drawer width: 250')).toBeVisible();
        });

        it.e2e('should change left drawer width', async () => {
            await elementById(TestIDs.CHANGE_LEFT_SIDE_MENU_WIDTH_BTN).tap();
            await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
            await expect(elementByLabel('left drawer width: 100')).toBeVisible();
        });

        it.e2e('should set right drawer width', async () => {
            await elementById(TestIDs.OPEN_RIGHT_SIDE_MENU_BTN).tap();
            await expect(elementByLabel('right drawer width: 250')).toBeVisible();
        });

        it.e2e('should change right drawer width', async () => {
            await elementById(TestIDs.CHANGE_RIGHT_SIDE_MENU_WIDTH_BTN).tap();
            await elementById(TestIDs.OPEN_RIGHT_SIDE_MENU_BTN).tap();
            await expect(elementByLabel('right drawer width: 50')).toBeVisible();
        });

        it.e2e(':ios: should render side menu correctly', async () => {
            await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
            const snapshottedImagePath = `./e2e/assets/side_menu.${openMode}.png`;
            const actual =
                await elementById('SideMenuContainer').takeScreenshot(`side_menu_${openMode}`);
            expectImagesToBeEqual(actual, snapshottedImagePath);
        });

        it.e2e(':ios: center container should reset to full width after push and close', async () => {
            await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
            await elementById(TestIDs.LEFT_SIDE_MENU_PUSH_AND_CLOSE_BTN).tap();
            await elementById(TestIDs.POP_BTN).tap();
            const containerAttrs = await elementById('SideMenuContainer').getAttributes();
            const containerWidth = containerAttrs.frame.width;
            const centerAttrs = await elementById(TestIDs.SIDE_MENU_CENTER_SCREEN_CONTAINER).getAttributes();
            const centerWidth = centerAttrs.frame.width;
            jestExpect(centerWidth).toBe(containerWidth);
        });
    });

    it.e2e(':ios: should open above-content by default', async () => {
        await elementById(TestIDs.TOGGLE_SIDE_MENU_OPEN_MODE_BTN).tap(); // aboveContent --> pushContent
        await elementById(TestIDs.TOGGLE_SIDE_MENU_OPEN_MODE_BTN).tap(); // pushContent --> undefined
        await expect(elementByLabel('Open mode: undefined')).toBeVisible();

        await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();

        const snapshottedImagePath = `./e2e/assets/side_menu.undefined.png`;
        const actual =
          await elementById('SideMenuContainer').takeScreenshot(`side_menu_undefined`);
        expectImagesToBeEqual(actual, snapshottedImagePath);
    });
});
