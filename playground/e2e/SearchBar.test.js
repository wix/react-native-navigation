import TestIDs from '../src/testIDs';
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

  it('find magnifying button in integrated placement and tap it (iOS 26+)', async () => {
    try {
      await expect(elementById(TestIDs.TOGGLE_PLACEMENT_BTN)).toExist();
    } catch (e) {
      console.log('Skipping - requires iOS 26+');
      return;
    }
    await elementById(TestIDs.TOGGLE_PLACEMENT_BTN).tap();
    await elementById(TestIDs.SHOW_SEARCH_BAR_BTN).tap();
    const searchButton = element(
      by.type('_UIButtonBarButton').and(by.label('Search')).withAncestor(by.type('UINavigationBar'))
    );
    await expect(searchButton).toBeVisible();
    await searchButton.tap();
    await expect(elementByTraits(['searchField'])).toBeVisible();
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
    try {
      await expect(elementById(TestIDs.TOGGLE_PLACEMENT_BTN)).toExist();
    } catch (e) {
      await elementById(TestIDs.DISMISS_MODAL_TOPBAR_BTN).tap();
      await expect(elementById(TestIDs.OPTIONS_TAB)).toBeVisible();
    }
  });

  it('find magnifying button in integrated placement and tap it (iOS 26+)', async () => {
    try {
      await expect(elementById(TestIDs.TOGGLE_PLACEMENT_BTN)).toExist();
    } catch (e) {
      console.log('Skipping - requires iOS 26+');
      return;
    }
    await elementById(TestIDs.TOGGLE_PLACEMENT_BTN).tap();
    await elementById(TestIDs.SHOW_SEARCH_BAR_BTN).tap();
    const searchButton = element(
      by.type('UISearchBarTextField').withAncestor(by.type('_UIFloatingBarContainerView'))
    );
    await expect(searchButton).toExist();
    await expect(element(by.type('_UISearchBarFieldEditor'))).not.toExist();
    await searchButton.tap();
    await expect(element(by.type('_UISearchBarFieldEditor'))).toExist();
  });
});