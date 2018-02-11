const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');

const { elementById, elementByLabel } = Utils;

describe('screen style', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('declare a options on component component', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await expect(elementByLabel('Static Title')).toBeVisible();
  });

  it('change title on component component', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await expect(elementByLabel('Static Title')).toBeVisible();
    await elementById(testIDs.DYNAMIC_OPTIONS_BUTTON).tap();
    await expect(elementByLabel('Dynamic Title')).toBeVisible();
  });

  it('set dynamic options with valid options will do something and not crash', async () => {
    // we have no way of testing individual styles for the screen
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.DYNAMIC_OPTIONS_BUTTON).tap();
    await expect(elementById(testIDs.OPTIONS_SCREEN_HEADER)).toBeVisible();
  });

  it('hides Tab Bar when pressing on Hide Top Bar and shows it when pressing on Show Top Bar', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.HIDE_TOP_BAR_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeNotVisible();
    await elementById(testIDs.SHOW_TOP_BAR_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
  });

  it('hides topBar onScroll down and shows it on scroll up', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.SCROLLVIEW_SCREEN_BUTTON).tap();
    await elementById(testIDs.TOGGLE_TOP_BAR_HIDE_ON_SCROLL).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
    await element(by.id(testIDs.SCROLLVIEW_ELEMENT)).swipe('up', 'fast');
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeNotVisible();
    await element(by.id(testIDs.SCROLLVIEW_ELEMENT)).swipe('down', 'fast');
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
  });

  it('set Tab Bar badge on a current Tab', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await elementById(testIDs.SET_TAB_BADGE_BUTTON).tap();
    await expect(element(by.text('TeSt'))).toBeVisible();
  });

  it('hide Tab Bar', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await expect(elementById(testIDs.BOTTOM_TABS_ELEMENT)).toBeVisible();
    await elementById(testIDs.HIDE_BOTTOM_TABS_BUTTON).tap();
    await expect(elementById(testIDs.BOTTOM_TABS_ELEMENT)).toBeNotVisible();
  });

  it('show Tab Bar', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await elementById(testIDs.HIDE_BOTTOM_TABS_BUTTON).tap();
    await expect(elementById(testIDs.BOTTOM_TABS_ELEMENT)).toBeNotVisible();
    await elementById(testIDs.SHOW_BOTTOM_TABS_BUTTON).tap();
    await expect(elementById(testIDs.BOTTOM_TABS_ELEMENT)).toBeVisible();
  });

  it('side menu visibility - left', async () => {
    await elementById(testIDs.TAB_BASED_APP_SIDE_BUTTON).tap();
    await elementById(testIDs.SHOW_LEFT_SIDE_MENU_BUTTON).tap();
    await expect(elementById(testIDs.HIDE_LEFT_SIDE_MENU_BUTTON)).toBeVisible();
    await elementById(testIDs.HIDE_LEFT_SIDE_MENU_BUTTON).tap();
    await expect(elementById(testIDs.CENTERED_TEXT_HEADER)).toBeVisible();
  });

  it('side menu visibility - right', async () => {
    await elementById(testIDs.TAB_BASED_APP_SIDE_BUTTON).tap();
    await elementById(testIDs.SHOW_RIGHT_SIDE_MENU_BUTTON).tap();
    await expect(elementById(testIDs.HIDE_RIGHT_SIDE_MENU_BUTTON)).toBeVisible();
    await elementById(testIDs.HIDE_RIGHT_SIDE_MENU_BUTTON).tap();
    await expect(elementById(testIDs.CENTERED_TEXT_HEADER)).toBeVisible();
  });

  it('set right buttons', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await expect(elementById('buttonOne')).toBeVisible();
    await elementById('buttonOne').tap();
    await expect(elementById('buttonTwo')).toBeVisible();
    await elementById('buttonTwo').tap();
    await expect(elementById('buttonOne')).toBeVisible();
  });

  it('set left buttons', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await expect(elementById('buttonLeft')).toBeVisible();
  });

  it('tab bar items visibility', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await expect(elementById(testIDs.FIRST_TAB_BAR_BUTTON)).toBeVisible();
    await expect(elementById(testIDs.SECOND_TAB_BAR_BUTTON)).toBeVisible();
  });

  it('default options should apply to all screens in stack', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.PUSH_DEFAULT_OPTIONS_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeNotVisible();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeNotVisible();
  });

  it('default options should not override static options', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.PUSH_DEFAULT_OPTIONS_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeNotVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
  });
});
