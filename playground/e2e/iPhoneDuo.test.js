// Folds and unfolds the iPhone Duo while screens are shown. Runs only on an iPhone Duo simulator
// (`ios.sim.release.scene.duo`); on other devices every test returns early.
import Utils from './Utils';
import TestIDs from '../src/testIDs';
import duo from './duo';

const { elementById, sleep } = Utils;

// Outer display, inner display, and back.
const POSES = ['closed', 'open', 'closed'];
// Each pose change takes 5-10 seconds.
const TIMEOUT = 180000;

async function setPose(pose) {
  await duo.setPose(pose);
  await sleep(1000);
}

describe(':ios: iPhone Duo', () => {
  let onDuo;

  beforeAll(() => {
    onDuo = duo.isDuo();
  });

  afterAll(async () => {
    if (onDuo) await duo.setPose('closed');
  }, TIMEOUT);

  beforeEach(async () => {
    if (!onDuo) return;
    await setPose('closed');
    await device.launchApp({ newInstance: true });
  }, TIMEOUT);

  it('keeps bottom tabs visible and selectable in both displays', async () => {
    if (!onDuo) return;
    for (const pose of POSES) {
      await setPose(pose);
      await expect(elementById(TestIDs.LAYOUTS_TAB)).toBeVisible();
      await elementById(TestIDs.NAVIGATION_TAB).tap();
      await expect(elementById(TestIDs.NAVIGATION_SCREEN)).toBeVisible();
      await elementById(TestIDs.LAYOUTS_TAB).tap();
      await expect(elementById(TestIDs.STACK_BTN)).toBeVisible();
    }
  }, TIMEOUT);

  it('keeps the custom tab row usable in both displays', async () => {
    if (!onDuo) return;
    await elementById(TestIDs.BOTTOM_TABS_CUSTOM_COMPONENT_BTN).tap();
    const tabs = [
      { item: TestIDs.CUSTOM_BOTTOM_TAB_ITEM_1, label: 'Search content' },
      { item: TestIDs.CUSTOM_BOTTOM_TAB_ITEM_0, label: 'Home content' },
    ];
    for (const pose of POSES) {
      await setPose(pose);
      for (const { item, label } of tabs) {
        await expect(elementById(item)).toBeVisible();
        await elementById(item).tap();
        await waitFor(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL))
          .toHaveText(label)
          .withTimeout(3000);
      }
    }
  }, TIMEOUT);

  it('keeps a modal presented across fold and unfold', async () => {
    if (!onDuo) return;
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.MODAL_BTN).tap();
    for (const pose of POSES) {
      await setPose(pose);
      await expect(elementById(TestIDs.MODAL_SCREEN_HEADER)).toBeVisible();
    }
    await elementById(TestIDs.DISMISS_MODAL_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  }, TIMEOUT);

  it('keeps an overlay shown across fold and unfold', async () => {
    if (!onDuo) return;
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.OVERLAY_BTN).tap();
    await elementById(TestIDs.SHOW_OVERLAY_BTN).tap();
    for (const pose of POSES) {
      await setPose(pose);
      await expect(elementById(TestIDs.OVERLAY_ALERT_HEADER)).toBeVisible();
    }
  }, TIMEOUT);

  it('opens and closes the side menu in both displays', async () => {
    if (!onDuo) return;
    await elementById(TestIDs.SIDE_MENU_BTN).tap();
    for (const pose of POSES) {
      await setPose(pose);
      await elementById(TestIDs.OPEN_LEFT_SIDE_MENU_BTN).tap();
      await expect(elementById(TestIDs.LEFT_SIDE_MENU_PUSH_BTN)).toBeVisible();
      await elementById(TestIDs.CLOSE_LEFT_SIDE_MENU_BTN).tap();
      await expect(elementById(TestIDs.CENTER_SCREEN_HEADER)).toBeVisible();
    }
  }, TIMEOUT);
});
