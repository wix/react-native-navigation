import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const { elementById, elementByTraits } = Utils;

describe(':ios: SearchBar', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.OPTIONS_TAB).tap();
    await elementById(TestIDs.GOTO_SEARCHBAR_SCREEN).tap();
  });

  it('show and hide search bar', async () => {
    await elementById(TestIDs.SHOW_SEARCH_BAR_BTN).tap();
    await expect(elementByTraits(['searchField'])).toBeVisible();
    await elementById(TestIDs.HIDE_SEARCH_BAR_BTN).tap();
    await expect(elementByTraits(['searchField'])).toBeNotVisible();
  });
});
