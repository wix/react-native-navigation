const Screen = require('./Screen');

const SCREEN = {
  orientation: 'portrait',
  backgroundImage: require('../../img/navicon_add.png'),
  rootBackgroundImage: require('../../img/navicon_add.png'),
  screenBackgroundColor: 'black',
  statusBarHidden: true,
  backButtonTransition: 'custom',
  statusBarBlur: true,
  statusBarHideWithTopBar: false,
  popGesture: true
};

describe('Screen', () => {
  it('Parses Screen', () => {
    const uut = new Screen(SCREEN);
    expect(uut.orientation).toEqual('portrait');
    expect(uut.backgroundImage).toEqual(require('../../img/navicon_add.png'));
    expect(uut.rootBackgroundImage).toEqual(require('../../img/navicon_add.png'));
    expect(uut.screenBackgroundColor).toEqual('black');
    expect(uut.statusBarHidden).toEqual(true);
    expect(uut.backButtonTransition).toEqual('custom');
    expect(uut.statusBarBlur).toEqual(true);
    expect(uut.statusBarHideWithTopBar).toEqual(false);
    expect(uut.popGesture).toEqual(true);
  });
});
