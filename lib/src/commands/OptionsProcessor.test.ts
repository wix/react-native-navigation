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

  it('keeps original value if value was not processed', () => {
    const options: Options = { blurOnUnmount: false };
    expect(uut.processOptions(options)).toEqual({ blurOnUnmount: false });
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

  it('passProps for Buttons are remove', () => {
    const options: Options = {
      topBar: {
        rightButtons: [{ component: { passProps: { prop: 'prop' }, name: 'loool' }, id: '1' }],
      },
    };

    expect(uut.processOptions(options)).toEqual({
      topBar: {
        rightButtons: [{ component: { passProps: undefined, name: 'loool' }, id: '1' }],
      },
    });
  });

  it('generates component id for component without id', () => {
    const options: Options = {
      topBar: { title: { component: { name: 'a' } } },
    };

    expect(uut.processOptions(options)).toEqual({
      topBar: { title: { component: { name: 'a', componentId: 'CustomComponent1' } } },
    });
  });

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

  it('pass supplied componentId for component in options', () => {
    const options: Options = {
      topBar: { title: { component: { name: 'a', id: 'Component1' } } },
    };

    expect(uut.processOptions(options)).toEqual({
      topBar: { title: { component: { name: 'a', id: 'Component1', componentId: 'Component1' } } },
    });
  });
});
