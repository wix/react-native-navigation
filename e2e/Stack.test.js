import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';
import Android from './AndroidUtils';

const { elementByLabel, elementById, sleep } = Utils;

describe('Stack', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.STACK_BTN).tap();
  });

  it.e2e('SetStackRoot on a non created tab should work', async () => {
    await elementById(TestIDs.SET_ROOT_NAVIGATION_TAB).tap();
    await elementById(TestIDs.DISMISS_MODAL_TOPBAR_BTN).tap();
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.BACK_BUTTON).tap();
    await expect(elementById(TestIDs.NAVIGATION_SCREEN)).toBeVisible();
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

  it('pop using stack id', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.POP_USING_STACK_ID_BTN).tap();
    await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
  });

  it('pop using previous screen id', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.POP_USING_PREVIOUS_SCREEN_ID_BTN).tap();
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

  it('push title with subtitle', async () => {
    await elementById(TestIDs.PUSH_TITLE_WITH_SUBTITLE).tap();
    await expect(elementByLabel('Title')).toBeVisible();
    await expect(elementByLabel('Subtitle')).toBeVisible();
  });

  it('screen lifecycle', async () => {
    await elementById(TestIDs.PUSH_LIFECYCLE_BTN).tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await elementById(TestIDs.PUSH_TO_TEST_DID_DISAPPEAR_BTN).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });

  it.e2e('Screen popped event', async () => {
    await elementById(TestIDs.PUSH_LIFECYCLE_BTN).tap();
    await elementById(TestIDs.SCREEN_POPPED_BTN).tap();
    await expect(elementByLabel('Screen popped event')).toBeVisible();
  });

  it.e2e('unmount is called on pop', async () => {
    await elementById(TestIDs.PUSH_LIFECYCLE_BTN).tap();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementByLabel('componentWillUnmount')).toBeVisible();
    await elementByLabel('OK').atIndex(0).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
  });

  it.e2e(':android: override hardware back button', async () => {
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

  it('does not crash when setting the stack root to an existing component id', async () => {
    await elementById(TestIDs.SET_STACK_ROOT_WITH_ID_BTN).tap();
    await elementById(TestIDs.SET_STACK_ROOT_WITH_ID_BTN).tap();
  });

  it.e2e(':ios: set stack root component should be first in stack', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.SET_STACK_ROOT_BUTTON).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
  });

  xit(':ios: set searchBar and handle onSearchUpdated event', async () => {
    // Broken on iOS 13
    await elementById(TestIDs.SEARCH_BTN).tap();
    await expect(elementByLabel('Start Typing')).toBeVisible();
    await elementByLabel('Start Typing').tap();
    const query = '124';
    await elementByLabel('Start Typing').typeText(query);
    await expect(elementById(TestIDs.SEARCH_RESULT_ITEM)).toHaveText(`Item ${query}`);
  });

  it('push promise is resolved with pushed ViewController id', async () => {
    await elementById(TestIDs.STACK_COMMANDS_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('push promise resolved with: ChildId')).toBeVisible();
    await expect(elementByLabel('pop promise resolved with: ChildId')).toBeVisible();
  });

  it('pop from root screen should do nothing', async () => {
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
  });
});
