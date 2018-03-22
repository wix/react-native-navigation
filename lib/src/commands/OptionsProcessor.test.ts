import { OptionsProcessor } from './OptionsProcessor';
import { Store } from '../components/Store';

describe('navigation options', () => {
  let uut: OptionsProcessor;
  let options;
  let store: Store;
  beforeEach(() => {
    options = {};
    store = new Store();
    uut = new OptionsProcessor(store);
  });

  it('processes colors into numeric AARRGGBB', () => {
    options.someKeyColor = 'red';
    options.color = 'blue';
    uut.processOptions(options);
    expect(options.someKeyColor).toEqual(0xffff0000);
    expect(options.color).toEqual(0xff0000ff);

    options.someKeyColor = 'yellow';
    uut.processOptions(options);
    expect(options.someKeyColor).toEqual(0xffffff00);
  });

  it('processes numeric colors', () => {
    options.someKeyColor = '#123456';
    uut.processOptions(options);
    expect(options.someKeyColor).toEqual(0xff123456);

    options.someKeyColor = 0x123456ff; // wut
    uut.processOptions(options);
    expect(options.someKeyColor).toEqual(0xff123456);
  });

  it('process colors with rgb functions', () => {
    options.someKeyColor = 'rgb(255, 0, 255)';
    uut.processOptions(options);
    expect(options.someKeyColor).toEqual(0xffff00ff);
  });

  it('process colors with special words', () => {
    options.someKeyColor = 'fuchsia';
    uut.processOptions(options);
    expect(options.someKeyColor).toEqual(0xffff00ff);
  });

  it('process colors with hsla functions', () => {
    options.someKeyColor = 'hsla(360, 100%, 100%, 1.0)';
    uut.processOptions(options);

    expect(options.someKeyColor).toEqual(0xffffffff);
  });

  it('unknown colors return undefined', () => {
    options.someKeyColor = 'wut';
    uut.processOptions(options);
    expect(options.someKeyColor).toEqual(undefined);
  });

  it('any keys ending with Color', () => {
    options.otherKeyColor = 'red';
    options.yetAnotherColor = 'blue';
    options.andAnotherColor = 'rgb(0, 255, 0)';
    uut.processOptions(options);
    expect(options.otherKeyColor).toEqual(0xffff0000);
    expect(options.yetAnotherColor).toEqual(0xff0000ff);
    expect(options.andAnotherColor).toEqual(0xff00ff00);
  });

  it('keys ending with Color case sensitive', () => {
    options.otherKey_color = 'red'; // eslint-disable-line camelcase
    uut.processOptions(options);
    expect(options.otherKey_color).toEqual('red');
  });

  it('any nested recursive keys ending with Color', () => {
    options.topBar = { textColor: 'red' };
    options.topBar.innerMostObj = { anotherColor: 'yellow' };
    uut.processOptions(options);
    expect(options.topBar.textColor).toEqual(0xffff0000);
    expect(options.topBar.innerMostObj.anotherColor).toEqual(0xffffff00);
  });

  it('resolve image sources with name/ending with icon', () => {
    options.icon = 'require("https://wix.github.io/react-native-navigation/_images/logo.png");';
    options.image = 'require("https://wix.github.io/react-native-navigation/_images/logo.png");';
    options.myImage = 'require("https://wix.github.io/react-native-navigation/_images/logo.png");';
    options.topBar = {
      myIcon: 'require("https://wix.github.io/react-native-navigation/_images/logo.png");',
      myOtherValue: 'value'
    };
    uut.processOptions(options);

    // As we can't import external images and we don't want to add an image here
    // I assign the icons to strings (what the require would generally look like)
    // and expect the value to be resovled, in this case it doesn't find anything and returns null
    expect(options.icon).toEqual(null);
    expect(options.topBar.myIcon).toEqual(null);
    expect(options.image).toEqual(null);
    expect(options.myImage).toEqual(null);
    expect(options.topBar.myOtherValue).toEqual('value');
  });

  it('passProps for Buttons options', () => {
    const passProps = { prop: 'prop' };
    options.rightButtons = [{ passProps, id: '1' }];

    uut.processOptions({ o: options });

    expect(store.getPropsForId('1')).toEqual(passProps);
  });

  it('passProps must be with id next to it', () => {
    const passProps = { prop: 'prop' };
    options.rightButtons = [{ passProps }];

    uut.processOptions({ o: options });

    expect(store.getPropsForId('1')).toEqual({});
  });

  it('processes Options object', () => {
    options.someKeyColor = 'rgb(255, 0, 255)';
    options.topBar = { textColor: 'red' };
    options.topBar.innerMostObj = { anotherColor: 'yellow' };

    uut.processOptions({ o: options });

    expect(options.topBar.textColor).toEqual(0xffff0000);
  });

  it('undefined value return undefined ', () => {
    options.someImage = undefined;
    uut.processOptions(options);

    expect(options.someImage).toEqual(undefined);
  });
});
