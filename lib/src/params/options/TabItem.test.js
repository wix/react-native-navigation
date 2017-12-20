const TabItem = require('./TabItem');

const TAB_ITEM = {
  title: 'title',
  tabBadge: 3,
  visible: true,
  icon: { uri: '' }
};

describe('TabItem', () => {
  it('parses TabItem options', () => {
    const uut = new TabItem(TAB_ITEM);
    expect(uut.title).toEqual('title');
    expect(uut.tabBadge).toEqual(3);
    expect(uut.visible).toEqual(true);
    expect(uut.icon).toEqual({ uri: '' });
  });
});
