const TopBar = require('./TopBar');

const TOP_BAR = {
  title: 'something',
  backgroundColor: 'red',
  textColor: 'green',
  textFontSize: 13,
  textFontFamily: 'guttmanYadBrush',
  hidden: true,
  animateHide: true,
  hideOnScroll: true,
  transparent: true
};

describe('TopBar', () => {
  it('Parses TopBar', () => {
    const uut = new TopBar(TOP_BAR);
    expect(uut.title).toEqual('something');
    expect(uut.backgroundColor).toEqual('red');
    expect(uut.textColor).toEqual('green');
    expect(uut.textFontSize).toEqual(13);
    expect(uut.textFontFamily).toEqual('guttmanYadBrush');
    expect(uut.hidden).toEqual(true);
    expect(uut.animateHide).toEqual(true);
    expect(uut.hideOnScroll).toEqual(true);
    expect(uut.transparent).toEqual(true);
  });
});
