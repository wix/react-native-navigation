import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const {elementById } = Utils;

describe(':android: AttachedOverlays', () => {
    beforeEach(async () => {
        await device.launchApp({ newInstance: true });
        await elementById(TestIDs.ATTACHED_OVERLAYS_SCREEN).tap();
    });

    it('should show and hide tooltips', async () => {
        await elementById(TestIDs.SHOW_TOOLTIP_MAIN_BTMTABS_TPBAR_HIT).tap();
        await elementById(TestIDs.OK_BUTTON).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeNotVisible();
    });

    it.e2e('should be always in main bottom tabs layer', async () => {
        await elementById(TestIDs.SHOW_TOOLTIP_MAIN_BTMTABS_TPBAR_HIT).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
        await elementById(TestIDs.BACK_BUTTON).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
        await elementById(TestIDs.ATTACHED_OVERLAYS_SCREEN).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
    });


    it.e2e('should be hidden, but still in main bottom tabs layer when modal opened', async () => {
        await elementById(TestIDs.SHOW_TOOLTIP_MAIN_BTMTABS_TPBAR_HIT).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
        await elementById(TestIDs.MODAL_BTN).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeNotVisible();
        await elementById(TestIDs.DISMISS_MODAL_TOPBAR_BTN).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
    });

    it.e2e('should attach tooltip to stack', async () => {
        await elementById(TestIDs.SHOW_TOOLTIP_LAYOUT_STACK_TPBAR_HIT).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
        await elementById(TestIDs.OPTIONS_TAB).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeNotVisible();
        await elementById(TestIDs.LAYOUTS_TAB).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
        await elementById(TestIDs.PUSH_BTN).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
    });

    it.e2e('should keep tooltip on stack when pushing screen with same button id', async () => {
        await elementById(TestIDs.SHOW_TOOLTIP_LAYOUT_STACK_TPBAR_HIT).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
        await elementById(TestIDs.PUSH_BTN).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
    });

    it.e2e('should be hidden, but still in a stack layer when modal opened', async () => {
        await elementById(TestIDs.SHOW_TOOLTIP_LAYOUT_STACK_TPBAR_HIT).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
        await elementById(TestIDs.MODAL_BTN).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeNotVisible();
        await elementById(TestIDs.DISMISS_MODAL_TOPBAR_BTN).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
    });


    it.e2e('should attach anchored tooltip to component', async () => {
        await elementById(TestIDs.SHOW_TOOLTIP_COMPONENT_TPBAR_HIT).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
        await elementById(TestIDs.PUSH_BTN).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeNotVisible();
        await elementById(TestIDs.BACK_BUTTON).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
        await elementById(TestIDs.PUSH_PUSHED_SCREEN).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeNotVisible();
        await elementById(TestIDs.BACK_BUTTON).tap();
        await expect(elementById(TestIDs.OK_BUTTON)).toBeVisible();
    });

    it.e2e('should attach overlay to component', async () => {
        await elementById(TestIDs.SHOW_TOOLTIP_COMPONENT_NO_ANCHOR_TPBAR_HIT).tap();
        await expect(elementById(TestIDs.DISMISS_BTN)).toBeVisible();
        await elementById(TestIDs.PUSH_BTN).tap();
        await expect(elementById(TestIDs.DISMISS_BTN)).toBeNotVisible();
        await elementById(TestIDs.BACK_BUTTON).tap();
        await expect(elementById(TestIDs.DISMISS_BTN)).toBeVisible();
        await elementById(TestIDs.PUSH_PUSHED_SCREEN).tap();
        await expect(elementById(TestIDs.DISMISS_BTN)).toBeNotVisible();
        await elementById(TestIDs.BACK_BUTTON).tap();
        await expect(elementById(TestIDs.DISMISS_BTN)).toBeVisible();
    });

});
