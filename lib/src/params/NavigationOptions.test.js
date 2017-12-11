const NavigationOptions = require('./NavigationOptions');
const TabBar = require('./TabBar');
const TopBar = require('./TopBar');

const TAB_BAR = {};
const TOP_BAR = {};
const NAVIGATION_OPTIONS = {
  topBar: TOP_BAR,
  tabBar: TAB_BAR
};

describe('NavigationOptions', () => {
  it('parses navigationOptions correctly', () => {
    const uut = new NavigationOptions(NAVIGATION_OPTIONS);
    expect(uut.tabBar).toBeInstanceOf(TabBar);
    expect(uut.topBar).toBeInstanceOf(TopBar);
  });
});
