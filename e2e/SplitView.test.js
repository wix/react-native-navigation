import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const cocktailsList = require('../playground/src/assets/cocktails').default;
const { elementByLabel, elementById } = Utils;

describe.e2e(':ios: SplitView', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.SPLIT_VIEW_BUTTON).tap();
  });

  it('master screen updates details screen', async () => {
    const secondCocktail = cocktailsList[1];
    await elementById(secondCocktail.id).tap();
    await expect(elementByLabel(secondCocktail.description)).toBeVisible();
  });

  it('push screen to master screen', async () => {
    await elementById(TestIDs.PUSH_MASTER_BTN).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });

  it('push screen to detail screen', async () => {
    await elementById(TestIDs.PUSH_DETAILS_BTN).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });
});
