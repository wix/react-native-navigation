const Utils = require('./Utils');
const TestIDs = require('../playground/src/testIDs');
const { elementByLabel, elementById, sleep } = Utils;
const Android = require('./AndroidUtils');

describe('Stack', () => {
  beforeEach(async () => {
    await device.relaunchApp();
    await elementById(TestIDs.STACK_BTN).tap();
  });

  it('push and pop screen', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
  });

  it('push and pop screen without animation', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_NO_ANIM_BTN).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
  });

  it('pop to specific id', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Stack Position: 3')).toBeVisible();
    await elementById(TestIDs.POP_TO_FIRST_SCREEN_BTN).tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
  });

  it('pop to root', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.POP_TO_ROOT_BTN).tap();
    await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
  });

  it('pop component should not detach component if can`t pop', async () => {
    await elementById(TestIDs.POP_NONE_EXISTENT_SCREEN_BTN).tap();
    await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
  });

  it(':android: custom back button', async () => {
    await elementById(TestIDs.PUSH_CUSTOM_BACK_BTN).tap();
    await elementById(TestIDs.CUSTOM_BACK_BTN).tap();
    await expect(elementByLabel('back button clicked')).toBeVisible();
  });

  it('screen lifecycle', async () => {
    await elementById(TestIDs.PUSH_LIFECYCLE_BTN).tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await elementById(TestIDs.PUSH_TO_TEST_DID_DISAPPEAR_BTN).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });

  it('unmount is called on pop', async () => {
    await elementById(TestIDs.PUSH_LIFECYCLE_BTN).tap();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementByLabel('componentWillUnmount')).toBeVisible();
    await elementByLabel('OK').atIndex(0).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });

  it(':android: override hardware back button', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.ADD_BACK_HANDLER).tap();
    Android.pressBack();
    await sleep(100);
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();

    await elementById(TestIDs.REMOVE_BACK_HANDLER).tap();
    Android.pressBack();
    await sleep(100);
    await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
  });

  it(':ios: set stack root component should be first in stack', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.SET_STACK_ROOT_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
  });

  it(':ios: set searchBar and handle onSearchUpdated event', async () => {
    await elementById(TestIDs.SEARCH_BTN).tap();
    await expect(elementByLabel('Start Typing')).toBeVisible();
    await elementByLabel('Start Typing').tap();
    const query = '124';
    await elementByLabel('Start Typing').typeText(query);
    await expect(elementById(TestIDs.SEARCH_RESULT_ITEM)).toHaveText(`Item ${query}`);
  });
});
