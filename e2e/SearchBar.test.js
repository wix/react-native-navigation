import TestIDs from '../playground/src/testIDs';
import Utils from './Utils';

const { elementById, elementByTraits } = Utils;

describe.e2e(':ios: SearchBar', () => {
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

describe.e2e(':ios: SearchBar Modal', () => {
  beforeAll(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.OPTIONS_TAB).tap();
    await elementById(TestIDs.GOTO_SEARCHBAR_MODAL).tap();
  });

  it('show and hide search bar', async () => {
    await elementById(TestIDs.SHOW_SEARCH_BAR_BTN).tap();
    await expect(elementByTraits(['searchField'])).toBeVisible();
    await elementById(TestIDs.HIDE_SEARCH_BAR_BTN).tap();
    await expect(elementByTraits(['searchField'])).toBeNotVisible();
  });

  it('searching then exiting works', async () => {
    await elementById(TestIDs.SHOW_SEARCH_BAR_BTN).tap();
    await elementByTraits(['searchField']).replaceText('foo');
    await elementById(TestIDs.DISMISS_MODAL_TOPBAR_BTN).tap();
    await expect(elementById(TestIDs.OPTIONS_TAB)).toBeVisible();
  });
});
