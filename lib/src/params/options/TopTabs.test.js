const TopTabs = require('./TopTabs');

const TOP_TABS = {
  selectedTabColor: 'red',
  unselectedTabColor: 'blue'
};

describe('TopTabs', () => {
  it('Parses TopTabs', () => {
    const uut = new TopTabs(TOP_TABS);
    expect(uut.selectedTabColor).toEqual('red');
    expect(uut.unselectedTabColor).toEqual('blue');
  });
});
