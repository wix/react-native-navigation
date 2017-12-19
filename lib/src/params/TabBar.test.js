const TabBar = require('./TabBar');

const TAB_BAR = {
  currentTabId: 1,
  currentTabIndex: 2,
  tabBadge: 3,
  hidden: true,
  animateHide: true
};

describe('TabBar', () => {
  it('parses TabBar options', () => {
    const uut = new TabBar(TAB_BAR);
    expect(uut.currentTabId).toEqual(1);
    expect(uut.currentTabIndex).toEqual(2);
    expect(uut.tabBadge).toEqual(3);
    expect(uut.hidden).toEqual(true);
    expect(uut.animateHide).toEqual(true);
  });
});
