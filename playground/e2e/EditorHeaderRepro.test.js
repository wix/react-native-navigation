import Utils from './Utils';
import TestIDs from '../src/testIDs';

const { elementById, elementTopBar } = Utils;

describe('Editor Header Repro', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.OPTIONS_TAB).tap();
    await elementById(TestIDs.GOTO_EDITOR_HEADER_REPRO).tap();
    await elementById('EDITOR_REPRO_SCREEN_PICKER').waitForVisible();
    await elementById('EDITOR_REPRO_PUBLISH').waitForVisible();
  });

  it.e2e('captures editor-like top bar for alignment check', async () => {
    await elementTopBar().takeScreenshot('editor_header_repro_topbar');
    await expect(elementById('EDITOR_REPRO_SCREEN_PICKER')).toBeVisible();
    await expect(elementById('EDITOR_REPRO_PUBLISH')).toBeVisible();
  });
});
