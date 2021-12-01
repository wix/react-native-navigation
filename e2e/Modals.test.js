import TestIDs from '../playground/src/testIDs';
import Android from './AndroidUtils';
import Utils from './Utils';

const { elementByLabel, elementById, sleep } = Utils;

describe('modal', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.MODAL_BTN).tap();
  });

  it('show modal', async () => {
    await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();
  });

  it('dismiss modal', async () => {
    await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it.e2e('unmount modal when dismissed', async () => {
    await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.MODAL_LIFECYCLE_BTN).tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementByLabel('componentWillUnmount')).toBeVisible();
    await elementByLabel('OK').atIndex(0).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
    await elementByLabel('OK').atIndex(0).tap();
  });

  it('show multiple modals', async () => {
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it('dismiss unknown screen id', async () => {
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.DISMISS_UNKNOWN_MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it('dismiss modal by id which is not the top most', async () => {
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.DISMISS_PREVIOUS_MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it('dismiss all previous modals by id when they are below top presented modal', async () => {
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementById(TestIDs.DISMISS_ALL_PREVIOUS_MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it('dismiss some modal by id deep in the stack', async () => {
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementById(TestIDs.DISMISS_FIRST_MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();

    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it('dismissAllModals', async () => {
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(TestIDs.DISMISS_ALL_MODALS_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it('push into modal', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });

  it.e2e(':android: push into modal and dismiss pushed screen with hardware back', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    Android.pressBack();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });

  it('present modal multiple times', async () => {
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
  });

  it('setRoot dismisses modals', async () => {
    await elementById(TestIDs.SET_ROOT).tap();
    await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeNotVisible();
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
  });

  it.e2e(':android: override hardware back button in modal with stack', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.ADD_BACK_HANDLER).tap();

    // Back is handled in Js
    Android.pressBack();
    await sleep(100);
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();

    // pop
    await elementById(TestIDs.REMOVE_BACK_HANDLER).tap();
    Android.pressBack();
    await sleep(100);
    await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();

    // modal dismissed
    Android.pressBack();
    await sleep(100);
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it('dismissModal promise is resolved with root ViewController id', async () => {
    await elementById(TestIDs.MODAL_COMMANDS_BTN).tap();
    await elementById(TestIDs.MODAL_BTN).tap();

    await expect(elementByLabel('showModal promise resolved with: UniqueStackId')).toBeVisible();
    await expect(
      elementByLabel('modalDismissed listener called with with: UniqueStackId')
    ).toBeVisible();
    await expect(elementByLabel('dismissModal promise resolved with: UniqueStackId')).toBeVisible();
  });

  it.e2e('should show declared modal', async () => {
    await elementById(TestIDs.TOGGLE_REACT_DECLARED_MODAL).tap();
    await expect(elementByLabel('Dismiss declared Modal')).toBeVisible();
    await elementById(TestIDs.DISMISS_REACT_MODAL_BTN).tap();
    await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();
  });

  it.e2e('should show and dismiss multiple modals including declared modal', async () => {
    await elementById(TestIDs.TOGGLE_REACT_DECLARED_MODAL).tap();
    await elementById(TestIDs.SHOW_MODAL_FROM_DECLARED_BUTTON).tap();
    await expect(elementByLabel('Toggle declared modal')).toBeVisible();
    await elementById(TestIDs.TOGGLE_REACT_DECLARED_MODAL).tap();
    await elementById(TestIDs.DISMISS_REACT_MODAL_BTN).tap();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementByLabel('Dismiss declared Modal')).toBeVisible();
    await elementById(TestIDs.DISMISS_REACT_MODAL_BTN).tap();

    await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();
  });

  it.e2e('overlay should be on top of all modals', async () => {
    await elementById(TestIDs.TOGGLE_REACT_DECLARED_MODAL).tap();
    await elementById(TestIDs.OVERLAY_BTN).tap();
    await expect(elementByLabel('Dismiss declared Modal')).toBeVisible();
    await expect(elementById(TestIDs.DISMISS_ALL_OVERLAYS_BUTTON)).toBeVisible();

    await elementById(TestIDs.SHOW_MODAL_FROM_DECLARED_BUTTON).tap();
    await expect(elementByLabel('Modal Lifecycle')).toBeVisible();

    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await elementById(TestIDs.DISMISS_REACT_MODAL_BTN).tap();

    await elementById(TestIDs.DISMISS_ALL_OVERLAYS_BUTTON).tap();
  });

  it.e2e(':android: should handle back properly', async () => {
    await elementById(TestIDs.TOGGLE_REACT_DECLARED_MODAL).tap();
    await elementById(TestIDs.SHOW_MODAL_FROM_DECLARED_BUTTON).tap();
    await expect(elementByLabel('Toggle declared modal')).toBeVisible();

    Android.pressBack();

    await expect(elementByLabel('Dismiss declared Modal')).toBeVisible();

    Android.pressBack();

    await expect(elementByLabel('Toggle declared modal')).toBeVisible();
  });

  it.e2e('dismiss modal with side menu', async () => {
    await elementById(TestIDs.MODAL_COMMANDS_BTN).tap();
    await elementById(TestIDs.SHOW_SIDE_MENU_MODAL).tap();
    await expect(elementByLabel('System UI Options')).toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_TOPBAR_BTN).tap();
    await expect(elementByLabel('System UI Options')).not.toBeVisible();
    await expect(elementByLabel('Modal Commands')).toBeVisible();
  });
});
