const TopTab = require('./TopTab');

const TOP_TAB = {
  title: 'something',
  titleFontFamily: 'Dosis-Regular'
};

describe('TopBar', () => {
  it('Parses TopBar', () => {
    const uut = new TopTab(TOP_TAB);
    expect(uut.title).toEqual('something');
    expect(uut.titleFontFamily).toEqual('Dosis-Regular');
  });
});
