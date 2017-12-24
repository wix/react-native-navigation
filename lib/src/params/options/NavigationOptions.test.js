const NavigationOptions = require('./NavigationOptions');
const BottomTabs = require('./BottomTabs');
const TopBar = require('./TopBar');
const BottomTab = require('./BottomTab');

const TAB_BAR = {};
const TOP_BAR = {};
const TAB_ITEM = {};
const RIGHT_BUTTONS = [
  {
    id: 'myBtn',
    testID: 'BTN',
    title: 'My Button',
    buttonColor: 'red'
  }
];
const LEFT_BUTTONS = [
  {
    id: 'myBtn',
    testID: 'BTN',
    title: 'My Button',
    buttonColor: 'red'
  }
];
const NAVIGATION_OPTIONS = {
  topBar: TOP_BAR,
  bottomTabs: TAB_BAR,
  bottomTab: TAB_ITEM,
  orientation: 'portrait',
  rightButtons: RIGHT_BUTTONS,
  leftButtons: LEFT_BUTTONS
};

describe('NavigationOptions', () => {
  it('parses navigationOptions correctly', () => {
    const uut = new NavigationOptions(NAVIGATION_OPTIONS);
    expect(uut.bottomTabs).toBeInstanceOf(BottomTabs);
    expect(uut.topBar).toBeInstanceOf(TopBar);
    expect(uut.bottomTab).toBeInstanceOf(BottomTab);
    expect(uut.orientation).toEqual('portrait');
    expect(uut.rightButtons).toEqual(RIGHT_BUTTONS);
    expect(uut.leftButtons).toEqual(LEFT_BUTTONS);
  });
});
