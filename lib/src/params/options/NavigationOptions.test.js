const NavigationOptions = require('./NavigationOptions');
const BottomTabs = require('./BottomTabs');
const TopBar = require('./TopBar');
const BottomTab = require('./BottomTab');
const TopTabs = require('./TopTabs');

const TAB_BAR = {};
const TOP_BAR = {};
const TAB_ITEM = {};
const TOP_TABS = {
  selectedTabColor: 'blue',
  unselectedTabColor: 'red'
};
const NAVIGATION_OPTIONS = {
  topBar: TOP_BAR,
  bottomTabs: TAB_BAR,
  bottomTab: TAB_ITEM,
  orientation: 'portrait',
  topTabs: TOP_TABS
};

describe('NavigationOptions', () => {
  it('parses navigationOptions correctly', () => {
    const uut = new NavigationOptions(NAVIGATION_OPTIONS);
    expect(uut.bottomTabs).toBeInstanceOf(BottomTabs);
    expect(uut.topBar).toBeInstanceOf(TopBar);
    expect(uut.bottomTab).toBeInstanceOf(BottomTab);
    expect(uut.orientation).toEqual('portrait');
    expect(uut.topTabs).toBeInstanceOf(TopTabs);
  });
});
