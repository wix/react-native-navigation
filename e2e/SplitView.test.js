const Utils = require('./Utils');
const TestIDs = require('../playground/src/testIDs');
const cocktailsList = require('../playground/src/assets/cocktails').default;
const { elementByLabel, elementById } = Utils;

describe(':ios: SplitView', () => {
  beforeEach(async () => {
    await device.relaunchApp();
    await elementById(TestIDs.SPLIT_VIEW_BUTTON).tap();
  });

  it('master screen updates details screen', async () => {
    const secondCocktail = cocktailsList[1];
    await elementById(secondCocktail.id).tap();
    await expect(elementByLabel(secondCocktail.description)).toBeVisible();
  });
});
