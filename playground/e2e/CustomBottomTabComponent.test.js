import Utils from './Utils';
import TestIDs from '../src/testIDs';

const { elementById, sleep } = Utils;

async function openCustomBottomTabsModal() {
  await elementById(TestIDs.BOTTOM_TABS_CUSTOM_COMPONENT_BTN).tap();
  await waitFor(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_0))
    .toBeVisible()
    .withTimeout(5000);
  await waitFor(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_1))
    .toBeVisible()
    .withTimeout(5000);
  await waitFor(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_2))
    .toBeVisible()
    .withTimeout(5000);
}

describe.e2e('Custom BottomTab Component', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await openCustomBottomTabsModal();
  });

  it('renders a custom React component for every tab', async () => {
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_0)).toBeVisible();
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_1)).toBeVisible();
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_2)).toBeVisible();
  });

  it('mounts the first tab content by default', async () => {
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL)).toHaveText('Home content');
  });

  it('shows the badge on the Search tab item', async () => {
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_BADGE)).toBeVisible();
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_BADGE)).toHaveText('3');
  });

  it('switches to the second tab when its custom item is tapped', async () => {
    await elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_1).tap();
    await waitFor(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL))
      .toHaveText('Search content')
      .withTimeout(3000);
  });

  it('switches to the third tab when its custom item is tapped', async () => {
    await elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_2).tap();
    await waitFor(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL))
      .toHaveText('Profile content')
      .withTimeout(3000);
  });

  it('switches back to the first tab when its custom item is tapped', async () => {
    await elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_2).tap();
    await waitFor(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL))
      .toHaveText('Profile content')
      .withTimeout(3000);
    await elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_0).tap();
    await waitFor(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL))
      .toHaveText('Home content')
      .withTimeout(3000);
  });

  it('cycles through all tabs in order', async () => {
    const tabs = [
      { item: TestIDs.CUSTOM_BOTTOM_TAB_ITEM_1, label: 'Search content' },
      { item: TestIDs.CUSTOM_BOTTOM_TAB_ITEM_2, label: 'Profile content' },
      { item: TestIDs.CUSTOM_BOTTOM_TAB_ITEM_0, label: 'Home content' },
    ];
    for (const { item, label } of tabs) {
      await elementById(item).tap();
      await sleep(300);
      await waitFor(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL))
        .toHaveText(label)
        .withTimeout(3000);
    }
  });
});
