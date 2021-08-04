import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const { elementByLabel, elementById } = Utils;

describe('static lifecycle events', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.SHOW_STATIC_EVENTS_SCREEN).tap();
    await elementById(TestIDs.STATIC_EVENTS_OVERLAY_BTN).tap();
    await expect(elementByLabel('Static Lifecycle Events Overlay')).toBeVisible();
    await expect(elementByLabel('componentWillAppear | EventsOverlay | Component')).toBeVisible();
    await expect(elementByLabel('componentDidAppear | EventsOverlay | Component')).toBeVisible();
    await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
  });

  it('willAppear didAppear didDisappear', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('componentWillAppear | Pushed | Component')).toBeVisible();
    await expect(elementByLabel('componentDidAppear | Pushed | Component')).toBeVisible();
    await expect(elementByLabel('componentDidDisappear | EventsScreen | Component')).toBeVisible();
  });

  it('pushing and popping screen dispatch static event', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('push')).toBeVisible();
    await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementByLabel('pop')).toBeVisible();
  });

  it('showModal and dismissModal dispatch static event', async () => {
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('showModal')).toBeVisible();
    await expect(elementByLabel('componentWillAppear | Modal | Component')).toBeVisible();
    await expect(elementByLabel('componentDidAppear | Modal | Component')).toBeVisible();
    await expect(elementByLabel('componentDidDisappear | EventsScreen | Component')).toBeVisible();
    await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementByLabel('dismissModal')).toBeVisible();
    await expect(elementByLabel('componentWillAppear | EventsScreen | Component')).toBeVisible();
    await expect(elementByLabel('componentDidAppear | EventsScreen | Component')).toBeVisible();
    await expect(elementByLabel('componentDidDisappear | Modal | Component')).toBeVisible();
  });

  it('unmounts when dismissed', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.DISMISS_BTN).tap();
    await expect(elementByLabel('Overlay Unmounted')).toBeVisible();
    await elementByLabel('OK').tap();
  });

  it('top bar buttons willAppear didAppear didDisappear', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
    await elementById(TestIDs.GOTO_BUTTONS_SCREEN).tap();
    await expect(
      elementByLabel('componentWillAppear | CustomRoundedButton | TopBarButton')
    ).toBeVisible();
    await expect(
      elementByLabel('componentDidAppear | CustomRoundedButton | TopBarButton')
    ).toBeVisible();
    await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
    await elementById(TestIDs.RESET_BUTTONS).tap();
    await expect(
      elementByLabel('componentDidDisappear | CustomRoundedButton | TopBarButton')
    ).toBeVisible();
  });

  it('top bar title willAppear didAppear didDisappear', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
    await elementById(TestIDs.SET_REACT_TITLE_VIEW).tap();
    await expect(
      elementByLabel('componentWillAppear | ReactTitleView | TopBarTitle')
    ).toBeVisible();
    await expect(elementByLabel('componentDidAppear | ReactTitleView | TopBarTitle')).toBeVisible();
    await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(
      elementByLabel('componentDidDisappear | ReactTitleView | TopBarTitle')
    ).toBeVisible();
  });

  it('unmounts previous root before resolving setRoot promise', async () => {
    await elementById(TestIDs.SET_ROOT_BTN).tap();
    await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
    await elementById(TestIDs.SET_ROOT_BTN).tap();

    await expect(elementByLabel('setRoot complete - previous root is unmounted')).toBeVisible();
  });

  it('top bar custom button willAppear didAppear after pop, on a root screen', async () => {
    await elementById(TestIDs.SHOW_RIGHT_BUTTON).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.CLEAR_OVERLAY_EVENTS_BTN).tap();
    await elementById(TestIDs.BACK_BUTTON).tap();
    await expect(
      elementByLabel('componentWillAppear | CustomRoundedButton | TopBarButton')
    ).toBeVisible();
    await expect(
      elementByLabel('componentDidAppear | CustomRoundedButton | TopBarButton')
    ).toBeVisible();
  });
});
