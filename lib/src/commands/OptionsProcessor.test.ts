import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Store } from '../components/Store';
import { Options } from '../interfaces/Options';
import { mock, instance, when, anyNumber } from 'ts-mockito';
import { AssetResolver } from '../adapters/AssetResolver';

describe('navigation options', () => {
  let uut: OptionsProcessor;
  let store: Store;
  const mockedAssetResolver = mock(AssetResolver);
  when(mockedAssetResolver.resolveFromRequire(anyNumber())).thenReturn('lol');
  const assetResolver = instance(mockedAssetResolver);

  beforeEach(() => {
    store = new Store();
    uut = new OptionsProcessor(store, new UniqueIdProvider(), assetResolver);
  });

  it('processes colors into numeric AARRGGBB', () => {
    const options: Options = {
      statusBar: { backgroundColor: 'red' },
      topBar: { background: { color: 'blue' } },
    };
    const transformed: any = uut.processOptions(options);
    expect(transformed.statusBar.backgroundColor).toEqual(0xffff0000);
    expect(transformed.topBar.background.color).toEqual(0xff0000ff);
  });

  it('processes numeric colors', () => {
    const options: Options = {
      statusBar: { backgroundColor: '#123456' },
    };
    const transformed: any = uut.processOptions(options);
    expect(transformed.statusBar.backgroundColor).toEqual(0xff123456);
  });

  it('process colors with rgb functions', () => {
    const options: Options = {
      statusBar: { backgroundColor: 'rgb(255, 0, 255)' },
    };
    const transformed: any = uut.processOptions(options);
    expect(transformed.statusBar.backgroundColor).toEqual(0xffff00ff);
  });

  it('process colors with special words', () => {
    const options: Options = {
      statusBar: { backgroundColor: 'fuchsia' },
    };
    const transformed: any = uut.processOptions(options);
    expect(transformed.statusBar.backgroundColor).toEqual(0xffff00ff);
  });

  it('process colors with hsla functions', () => {
    const options: Options = {
      statusBar: { backgroundColor: 'hsla(360, 100%, 100%, 1.0)' },
    };
    const transformed: any = uut.processOptions(options);
    expect(transformed.statusBar.backgroundColor).toEqual(0xffffffff);
  });

  it('unknown colors return undefined', () => {
    const options: Options = {
      statusBar: { backgroundColor: 'wut' },
    };
    const transformed: any = uut.processOptions(options);
    expect(transformed.statusBar.backgroundColor).toEqual(undefined);
  });

  it('resolve image sources with name/ending with icon', () => {
    const options: Options = {
      backgroundImage: 123,
      rootBackgroundImage: 234,
      bottomTab: { icon: 345 },
    };
    expect(uut.processOptions(options)).toEqual({
      backgroundImage: 'lol',
      rootBackgroundImage: 'lol',
      bottomTab: { icon: 'lol' },
    });
  });

  // it('passProps for Buttons options', () => {
  //   const passProps = { prop: 'prop' };
  //   options.rightButtons = [{ passProps, id: '1' }];

  //   uut.processOptions({ o: options });

  //   expect(store.getPropsForId('1')).toEqual(passProps);
  // });

  // it('passProps for custom component', () => {
  //   const passProps = { color: '#ff0000', some: 'thing' };
  //   options.component = { passProps, name: 'a' };

  //   uut.processOptions({ o: options });

  //   expect(store.getPropsForId(options.component.componentId)).toEqual(passProps);
  //   expect(Object.keys(options.component)).not.toContain('passProps');
  // });

  // it('generate component id for component in options', () => {
  //   options.component = { name: 'a' };

  //   uut.processOptions({ o: options });

  //   expect(options.component.componentId).toBeDefined();
  // });

  // it('passProps from options are not processed', () => {
  //   const passProps = { color: '#ff0000', some: 'thing' };
  //   const clonedProps = _.cloneDeep(passProps);
  //   options.component = { passProps, name: 'a' };

  //   uut.processOptions(options);
  //   expect(store.getPropsForId(options.component.componentId)).toEqual(clonedProps);
  // });

  // it('pass supplied componentId for component in options', () => {
  //   options.component = { name: 'a', id: 'Component1' };

  //   uut.processOptions({ o: options });

  //   expect(options.component.componentId).toEqual('Component1');
  // });

  // it('passProps must be with id next to it', () => {
  //   const passProps = { prop: 'prop' };
  //   options.rightButtons = [{ passProps }];

  //   uut.processOptions({ o: options });

  //   expect(store.getPropsForId('1')).toEqual({});
  // });

  // it('processes Options object', () => {
  //   options.someKeyColor = 'rgb(255, 0, 255)';
  //   options.topBar = { textColor: 'red' };
  //   options.topBar.innerMostObj = { anotherColor: 'yellow' };

  //   uut.processOptions({ o: options });

  //   expect(options.topBar.textColor).toEqual(0xffff0000);
  // });

  // it('undefined value return undefined ', () => {
  //   options.someImage = undefined;
  //   uut.processOptions(options);

  //   expect(options.someImage).toEqual(undefined);
  // });

  // it('omits passProps when processing options', () => {
  //   const passProps = {
  //     topBar: {
  //       rightButtons: [
  //         {
  //           passProps: {},
  //           id: 'btn1'
  //         },
  //       ],
  //       leftButtons: [
  //         {
  //           passProps: {},
  //           id: 'btn2'
  //         }
  //       ],
  //       title: {
  //         component: {
  //           passProps: {}
  //         }
  //       },
  //       background: {
  //         component: {
  //           passProps: {}
  //         }
  //       }
  //     }
  //   };
  //   uut.processOptions(passProps);
  //   expect(passProps.topBar.rightButtons[0].passProps).toBeUndefined();
  //   expect(passProps.topBar.leftButtons[0].passProps).toBeUndefined();
  //   expect(passProps.topBar.title.component.passProps).toBeUndefined();
  //   expect(passProps.topBar.background.component.passProps).toBeUndefined();
  // });
});
