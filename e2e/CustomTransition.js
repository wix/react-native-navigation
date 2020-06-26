import Utils from './Utils';
import testIDs from '../playground/src/testIDs';

const { elementById } = Utils;

describe(':ios: custom transition', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
  });

  test('sanity', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.CUSTOM_TRANSITION_BUTTON).tap();
    await expect(element(by.id('shared_image1'))).toExist();
    await element(by.id('shared_image1')).tap();
    await expect(element(by.id('shared_image2'))).toExist();
    await element(by.id('shared_image2')).tap();
    await expect(element(by.id('shared_image1'))).toExist();
  });
});
