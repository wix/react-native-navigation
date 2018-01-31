
const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');

const elementById = Utils.elementById;

describe('custom transition', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('sanity', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.CUSTOM_TRANSITION_BUTTON).tap();
    await expect(element(by.id('shared_image1'))).toExist();
    await element(by.id('shared_image1')).tap();
    await expect(element(by.id('shared_image2'))).toExist();
    await element(by.id('shared_image2')).tap();
    await expect(element(by.id('shared_image1'))).toExist();
  });
});
