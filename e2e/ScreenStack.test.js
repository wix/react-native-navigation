const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');
const Android = require('./AndroidUtils');

const { elementByLabel, elementById, sleep } = Utils;

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

  it(':android: push and pop screen without animation', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await expect(elementById(testIDs.OPTIONS_SCREEN_HEADER)).toBeVisible();
    Android.pressBack();
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

  it('push stack with multiple children', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await elementById(testIDs.MODAL_WITH_STACK_BUTTON).tap();
    await expect(elementByLabel('Screen 2')).toBeVisible();
    await elementById(testIDs.POP_BUTTON).tap();
    await expect(elementByLabel('Screen 1')).toBeVisible();
  });

  it('push external component with options', async () => {
    await elementById(testIDs.PUSH_EXTERNAL_COMPONENT_BUTTON).tap();
    await expect(elementByLabel('This is an external component')).toBeVisible();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
  });
});
