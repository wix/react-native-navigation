/* global device, waitFor, by */
import Utils from './Utils';
import TestIDs from '../src/testIDs';

const { elementById, elementByLabel } = Utils;

describe.e2e('Shared elements', () => {
  it(':android: restores nested shared elements after repeated push and pop', async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await waitFor(elementByLabel('Shared Element (Cocktails)'))
      .toBeVisible()
      .whileElement(by.id(`${TestIDs.NAVIGATION_SCREEN}_LIST`))
      .scroll(200, 'down');
    await elementByLabel('Shared Element (Cocktails)').tap();

    // The cocktail image, title and backdrop have different parents and zIndex
    // values. A second transition verifies that native child order was restored.
    for (let iteration = 0; iteration < 2; iteration++) {
      await elementById('1').tap();
      await expect(elementById(TestIDs.COCKTAILS_DETAILS_HEADER)).toBeVisible();
      await device.pressBack();
      await expect(elementById('1')).toBeVisible();
    }
  });
});
