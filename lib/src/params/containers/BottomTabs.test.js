const BottomTabs = require('./BottomTabs');

const TAB1 = {
  container: {
    name: 'navigation.playground.TextScreen',
    passProps: {
      text: 'This is a side menu center screen tab 2'
    }
  }
};
const TAB2 = {
  container: {
    name: 'navigation.playground.TextScreen',
    passProps: {
      text: 'This is a side menu center screen tab 1'
    }
  }
};
const TABS = [TAB1, TAB2];

describe('ContainerRegistry', () => {
  it('parses tabs correctly', () => {
    const uut = new BottomTabs(TABS);
    expect(uut.tabs.length).toBe(2);
  });

  it('throws if tabs are undefined', () => {
    expect(() => new BottomTabs()).toThrowError('BottomTabs undefined');
  });

  it('throws if zero tabs', () => {
    expect(() => new BottomTabs([])).toThrowError('BottomTabs undefined');
  });
});
