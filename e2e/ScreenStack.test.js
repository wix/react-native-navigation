const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');

const { elementByLabel, elementById } = Utils;

describe('screen stack', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('push and pop screen', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementById(testIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  it('pop screen deep in the stack', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementById(testIDs.POP_PREVIOUS_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  it('pop to specific id', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 3')).toBeVisible();
    await elementById(testIDs.POP_STACK_POSITION_ONE_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
  });

  it('pop to root', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.POP_TO_ROOT).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  it('switch to tab', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
    await elementById(testIDs.SWITCH_SECOND_TAB_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeNotVisible();
    await expect(elementByLabel('This is tab 2')).toBeVisible();
  });

  it('switch to tab by cotnainerId', async () => {
    await elementById(testIDs.TAB_BASED_APP_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
    await elementById(testIDs.SWITCH_SECOND_TAB_BUTTON).tap();
    await expect(elementByLabel('This is tab 2')).toBeVisible();
    await elementById(testIDs.SWITCH_FIRST_TAB_BUTTON).tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
  });

  it('show and dismiss overlay', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.SHOW_OVERLAY_BUTTON).tap();
    await expect(elementById(testIDs.DIALOG_HEADER)).toBeVisible();
    await elementById(testIDs.OK_BUTTON).tap();
    await expect(elementById(testIDs.DIALOG_HEADER)).toBeNotVisible();
  });

  it('overlay pass touches - true', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.SHOW_TOUCH_THROUGH_OVERLAY_BUTTON).tap();
    await expect(elementById(testIDs.DIALOG_HEADER)).toBeVisible();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
    await elementById(testIDs.HIDE_TOP_BAR_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeNotVisible();
  });

  it('overlay pass touches - false', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.SHOW_OVERLAY_BUTTON).tap();
    await expect(elementById(testIDs.DIALOG_HEADER)).toBeVisible();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
    await elementById(testIDs.HIDE_TOP_BAR_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
  });
});
