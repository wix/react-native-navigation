import TestIDs from '../src/testIDs';
import Utils from './Utils';

const { elementById, elementByLabel } = Utils;

describe('sheet detents', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.SHEET_DETENTS_BTN).tap();
  });

  it('shows sheet modal with pageSheet presentation', async () => {
    await expect(elementById(TestIDs.SHEET_DETENT_EXPAND_LARGE_BTN)).toBeVisible();
    await expect(elementByLabel('Sheet Detents')).toBeVisible();
    await expect(elementById(TestIDs.SHEET_DETENT_STATUS)).toHaveText('medium');
  });

  it('updates selected detent via mergeOptions without crashing', async () => {
    await elementById(TestIDs.SHEET_DETENT_EXPAND_LARGE_BTN).tap();
    await expect(elementById(TestIDs.SHEET_DETENT_STATUS)).toHaveText('large');

    await elementById(TestIDs.SHEET_DETENT_MEDIUM_BTN).tap();
    await expect(elementById(TestIDs.SHEET_DETENT_STATUS)).toHaveText('medium');

    await elementById(TestIDs.SHEET_DETENT_COMPACT_BTN).tap();
    await expect(elementById(TestIDs.SHEET_DETENT_STATUS)).toHaveText('compact');
  });

  it.e2e(':android: keeps bottom tabs visible under pageSheet', async () => {
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it('dismisses sheet modal', async () => {
    await elementById(TestIDs.SHEET_DETENT_DISMISS_BTN).tap();
    await expect(elementById(TestIDs.SHEET_DETENT_STATUS)).not.toBeVisible();
    await expect(elementById(TestIDs.SHEET_DETENTS_BTN)).toBeVisible();
  });
});
