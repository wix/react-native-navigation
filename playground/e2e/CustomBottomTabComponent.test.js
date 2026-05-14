import Utils from './Utils';
import TestIDs from '../src/testIDs';

const { elementById } = Utils;

describe.e2e('Custom BottomTab Component', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.BOTTOM_TABS_CUSTOM_COMPONENT_BTN).tap();
    await new Promise((r) => setTimeout(r, 800));
    await device.takeScreenshot('after_open_modal');
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_0)).toBeVisible();
  });

  it('renders a custom React component for every tab', async () => {
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_0)).toBeVisible();
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_1)).toBeVisible();
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_2)).toBeVisible();
  });

  it('mounts the first tab content by default', async () => {
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL)).toHaveText('Home content');
  });

  it('switches to the second tab when its custom item is tapped', async () => {
    await elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_1).tap();
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL)).toHaveText(
      'Search content'
    );
  });

  it('switches to the third tab when its custom item is tapped', async () => {
    await elementById(TestIDs.CUSTOM_BOTTOM_TAB_ITEM_2).tap();
    await expect(elementById(TestIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL)).toHaveText(
      'Profile content'
    );
  });
});
