const BottomTab = require('./BottomTab');

const BOTTOM_TAB = {
  title: 'title',
  badge: 3,
  visible: true,
  icon: { uri: '' }
};

describe('BottomTab', () => {
  it('parses BottomTab options', () => {
    const uut = new BottomTab(BOTTOM_TAB);
    expect(uut.title).toEqual('title');
    expect(uut.badge).toEqual(3);
    expect(uut.visible).toEqual(true);
    expect(uut.icon).toEqual({ uri: '' });
  });
});
