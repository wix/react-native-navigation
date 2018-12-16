import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Store } from '../components/Store';
import { Options } from '../interfaces/Options';
import { mock, instance, when, anyNumber, anyString } from 'ts-mockito';
import { AssetResolver } from '../adapters/AssetResolver';
import { ColorService } from '../adapters/ColorService';

describe('navigation options', () => {
  let uut: OptionsProcessor;
  let store: Store;
  const mockedAssetResolver = mock(AssetResolver);
  when(mockedAssetResolver.resolveFromRequire(anyNumber())).thenReturn('lol');
  const assetResolver = instance(mockedAssetResolver);
  const mockedColorService = mock(ColorService);
  when(mockedColorService.toNativeColor(anyString())).thenReturn(666);
  const colorService = instance(mockedColorService);

  beforeEach(() => {
    store = new Store();
    uut = new OptionsProcessor(store, new UniqueIdProvider(), assetResolver, colorService);
  });

  it('empty options', () => {
    const options: Options = {};
    expect(uut.processOptions(options)).toEqual({});
  });

  it('processes colors into numeric AARRGGBB', () => {
    const options: Options = {
      statusBar: { backgroundColor: 'red' },
      topBar: { background: { color: 'blue' } },
    };
    expect(uut.processOptions(options)).toEqual({
      statusBar: { backgroundColor: 666 },
      topBar: { background: { color: 666 } },
    });
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

  it('passProps for Buttons options', () => {
    const passProps = { prop: 'prop' };
    const options: Options = {
      topBar: {
        rightButtons: [{ component: { passProps, name: 'loool' }, id: '1' }],
      },
    };

    uut.processOptions(options);

    expect(store.getPropsForId('1')).toEqual(passProps);
  });

  // it('passProps for custom component', () => {
  //   const passProps = { color: '#ff0000', some: 'thing' };
  //   const options: Options = {
  //     component: { passProps, name: 'a' }
  //   };

  //   uut.processOptions({ o: options });

  //   expect(store.getPropsForId(options.component.componentId)).toEqual(passProps);
  //   expect(Object.keys(options.component)).not.toContain('passProps');
  // });

  // it('generate component id for component in options', () => {
  //   options.component = { name: 'a' };

  //   uut.processOptions({ o: options });

  //   expect(options.component.componentId).toBeDefined();
  // });

  it('passProps from options are not processed', () => {
    const options: Options = {
      topBar: {
        title: {
          component: { passProps: { color: '#ff0000', some: 'thing' }, name: 'a', id: '123' },
        },
      },
    };

    expect(uut.processOptions(options)).toEqual({
      topBar: { title: { component: { name: 'a', componentId: '123', id: '123' } } },
    });
  });

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
