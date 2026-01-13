import Utils from './Utils';
import TestIDs from '../src/testIDs';

const { elementById, elementByLabel, sleep } = Utils;

const LOAD_ORDER_PERMUTATIONS = [
    '0→1→2', '0→2→1', '1→0→2', '1→2→0', '2→0→1', '2→1→0',
];

describe('Tabs with Together flag', () => {
    beforeEach(async () => {
        await device.launchApp({ newInstance: true });
        await elementById(TestIDs.BOTTOM_TABS_BTN).tap();
        await expect(elementByLabel('First Tab')).toBeVisible();
    });

    it('should load all tabs when tabsAttachMode is together', async () => {
        await elementById(TestIDs.TABS_TOGETHER_BTN).tap();

        let found = false;
        for (let i = 0; i < 90 && !found; i++) {
            for (const permutation of LOAD_ORDER_PERMUTATIONS) {
                try {
                    await expect(element(by.text(permutation))).toExist();
                    found = true;
                    break;
                } catch (e) {
                    // Not found yet, continue
                }
            }
            if (!found) {
                await sleep(1000);
            }
        }

        if (!found) {
            throw new Error('All 3 tabs did not load within 90 seconds - load order indicator should show X→Y→Z pattern');
        }

        await elementById(TestIDs.TABS_TOGETHER_DISMISS).tap();
        await expect(elementByLabel('First Tab')).toBeVisible();
    });
});
