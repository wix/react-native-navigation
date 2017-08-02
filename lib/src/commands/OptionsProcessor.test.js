import optionsProcessor from './OptionsProcessor';
 
describe('navigation options', () => {
  let navigationOptions;

  beforeEach(() => {
    navigationOptions = {};
  });

  it('processes colors into numeric AARRGGBB', () => {
    navigationOptions.someKeyColor = 'red';
    optionsProcessor(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xffff0000);

    navigationOptions.someKeyColor = 'yellow';
    optionsProcessor(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xffffff00);
  });

  it('processes numeric colors', () => {
    navigationOptions.someKeyColor = '#123456';
    optionsProcessor(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xff123456);

    navigationOptions.someKeyColor = 0x123456ff; // wut
    optionsProcessor(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xff123456);
  });

  it('process colors with rgb functions', () => {
    navigationOptions.someKeyColor = 'rgb(255, 0, 255)';
    optionsProcessor(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xffff00ff);
  });

  it('process colors with special words', () => {
    navigationOptions.someKeyColor = 'fuchsia';
    optionsProcessor(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(0xffff00ff);
  });

  it('process colors with hsla functions', () => {
    navigationOptions.someKeyColor = 'hsla(360, 100%, 100%, 1.0)';
    optionsProcessor(navigationOptions);

    expect(navigationOptions.someKeyColor).toEqual(0xffffffff);
  });

  it('unknown colors return undefined', () => {
    navigationOptions.someKeyColor = 'wut';
    optionsProcessor(navigationOptions);
    expect(navigationOptions.someKeyColor).toEqual(undefined);
  });

  it('any keys ending with Color', () => {
    navigationOptions.otherKeyColor = 'red';
    navigationOptions.yetAnotherColor = 'blue';
    navigationOptions.andAnotherColor = 'rgb(0, 255, 0)';
    optionsProcessor(navigationOptions);
    expect(navigationOptions.otherKeyColor).toEqual(0xffff0000);
    expect(navigationOptions.yetAnotherColor).toEqual(0xff0000ff);
    expect(navigationOptions.andAnotherColor).toEqual(0xff00ff00);
  });

  it('keys ending with Color case sensitive', () => {
    navigationOptions.otherKey_color = 'red';
    optionsProcessor(navigationOptions);
    expect(navigationOptions.otherKey_color).toEqual('red');
  });

  it('any nested recursive keys ending with Color', () => {
    navigationOptions.innerObj = { theKeyColor: 'red' };
    navigationOptions.innerObj.innerMostObj = { anotherColor: 'yellow' };
    optionsProcessor(navigationOptions);
    expect(navigationOptions.innerObj.theKeyColor).toEqual(0xffff0000);
    expect(navigationOptions.innerObj.innerMostObj.anotherColor).toEqual(0xffffff00);
  });
});
