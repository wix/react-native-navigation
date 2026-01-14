import Utils from './Utils';
import TestIDs from '../src/testIDs';

const { elementById, elementByLabel } = Utils;

describe.e2e(':ios: Tabs with Together flag', () => {
    beforeEach(async () => {
        await device.launchApp({ newInstance: true });
        await elementById(TestIDs.BOTTOM_TABS_BTN).tap();
        await expect(elementByLabel('First Tab')).toBeVisible();
    });

    it('should load all tabs when tabsAttachMode is together', async () => {
        await elementById(TestIDs.TABS_TOGETHER_BTN).tap();
        await waitFor(element(by.text(/\d→\d→\d/)))
            .toExist()
            .withTimeout(5000);

        await elementById(TestIDs.TABS_TOGETHER_DISMISS).tap();
        await expect(elementByLabel('First Tab')).toBeVisible();
    });
});
