const BottomTabs = require('./BottomTabs');

const BOTTOM_TABS = {
  currentTabId: 1,
  currentTabIndex: 2,
  hidden: true,
  animateHide: true
};

describe('BottomTabs', () => {
  it('parses BottomTabs options', () => {
    const uut = new BottomTabs(BOTTOM_TABS);
    expect(uut.currentTabId).toEqual(1);
    expect(uut.currentTabIndex).toEqual(2);
    expect(uut.hidden).toEqual(true);
    expect(uut.animateHide).toEqual(true);
  });
});
