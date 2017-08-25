const OptionsProcessor = require('./OptionsProcessor');

describe('navigation options', () => {
  let navigationOptions;

  beforeEach(() => {
    navigationOptions = {};
  });

  it('processes colors into numeric AARRGGBB', () => {
    navigationOptions.someKeyColor = 'red';
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xffff0000);

    navigationOptions.someKeyColor = 'yellow';
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xffffff00);
  });

  it('processes numeric colors', () => {
    navigationOptions.someKeyColor = '#123456';
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xff123456);

    navigationOptions.someKeyColor = 0x123456ff; // wut
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xff123456);
  });

  it('process colors with rgb functions', () => {
    navigationOptions.someKeyColor = 'rgb(255, 0, 255)';
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xffff00ff);
  });

  it('process colors with special words', () => {
    navigationOptions.someKeyColor = 'fuchsia';
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xffff00ff);
  });

  it('process colors with hsla functions', () => {
    navigationOptions.someKeyColor = 'hsla(360, 100%, 100%, 1.0)';
    OptionsProcessor.processOptions(navigationOptions);

    expect(navigationOptions.someKeyColor).toEqual(0xffffffff);
  });

  it('unknown colors return undefined', () => {
    navigationOptions.someKeyColor = 'wut';
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(undefined);
  });

  it('any keys ending with Color', () => {
    navigationOptions.otherKeyColor = 'red';
    navigationOptions.yetAnotherColor = 'blue';
    navigationOptions.andAnotherColor = 'rgb(0, 255, 0)';
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.otherKeyColor).toEqual(0xffff0000);
    expect(navigationOptions.yetAnotherColor).toEqual(0xff0000ff);
    expect(navigationOptions.andAnotherColor).toEqual(0xff00ff00);
  });

  it('keys ending with Color case sensitive', () => {
    navigationOptions.otherKey_color = 'red'; // eslint-disable-line camelcase
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.otherKey_color).toEqual('red');
  });

  it('any nested recursive keys ending with Color', () => {
    navigationOptions.innerObj = { theKeyColor: 'red' };
    navigationOptions.innerObj.innerMostObj = { anotherColor: 'yellow' };
    OptionsProcessor.processOptions(navigationOptions);
    expect(navigationOptions.innerObj.theKeyColor).toEqual(0xffff0000);
    expect(navigationOptions.innerObj.innerMostObj.anotherColor).toEqual(0xffffff00);
  });

  it('resolve image sources with name/ending with icon', () => {
    navigationOptions.icon = 'require("https://wix.github.io/react-native-navigation/_images/logo.png");';
    navigationOptions.innerObj = {
      myIcon: 'require("https://wix.github.io/react-native-navigation/_images/logo.png");',
      myOtherValue: 'value'
    };
    OptionsProcessor.processOptions(navigationOptions);

    // As we can't import external images and we don't want to add an image here
    // I assign the icons to strings (what the require would generally look like)
    // and expect the value to be resovled, in this case it doesn't find anything and returns null
    expect(navigationOptions.icon).toEqual(null);
    expect(navigationOptions.innerObj.myIcon).toEqual(null);
    expect(navigationOptions.innerObj.myOtherValue).toEqual('value');
  });
});
