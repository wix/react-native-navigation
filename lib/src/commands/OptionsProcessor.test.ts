import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Store } from '../components/Store';
import { Options, OptionsModalPresentationStyle } from '../interfaces/Options';
import { mock, when, anyString, instance, anyNumber, verify } from 'ts-mockito';
import { ColorService } from '../adapters/ColorService';
import { AssetService } from '../adapters/AssetResolver';
import { Deprecations } from './Deprecations';

describe('navigation options', () => {
  let uut: OptionsProcessor;
  const mockedStore = mock(Store) as Store;
  const store = instance(mockedStore) as Store;

  beforeEach(() => {
    const mockedAssetService = mock(AssetService) as AssetService;
    when(mockedAssetService.resolveFromRequire(anyNumber())).thenReturn({
      height: 100,
      scale: 1,
      uri: 'lol',
      width: 100,
    });
    const assetService = instance(mockedAssetService);

    const mockedColorService = mock(ColorService) as ColorService;
    when(mockedColorService.toNativeColor(anyString())).thenReturn(666);
    const colorService = instance(mockedColorService);

    uut = new OptionsProcessor(
      store,
      new UniqueIdProvider(),
      colorService,
      assetService,
      new Deprecations()
    );
  });

  it('keeps original values if values were not processed', () => {
    const options: Options = {
      blurOnUnmount: false,
      popGesture: false,
      modalPresentationStyle: OptionsModalPresentationStyle.fullScreen,
      animations: { dismissModal: { alpha: { from: 0, to: 1 } } },
    };
    uut.processOptions(options);
    expect(options).toEqual({
      blurOnUnmount: false,
      popGesture: false,
      modalPresentationStyle: OptionsModalPresentationStyle.fullScreen,
      animations: { dismissModal: { alpha: { from: 0, to: 1 } } },
    });
  });

  it('processes color keys', () => {
    const options: Options = {
      statusBar: { backgroundColor: 'red' },
      topBar: { background: { color: 'blue' } },
    };
    uut.processOptions(options);
    expect(options).toEqual({
      statusBar: { backgroundColor: 666 },
      topBar: { background: { color: 666 } },
    });
  });

  it('processes image keys', () => {
    const options: Options = {
      backgroundImage: 123,
      rootBackgroundImage: 234,
      bottomTab: { icon: 345, selectedIcon: 345 },
    };
    uut.processOptions(options);
    expect(options).toEqual({
      backgroundImage: { height: 100, scale: 1, uri: 'lol', width: 100 },
      rootBackgroundImage: { height: 100, scale: 1, uri: 'lol', width: 100 },
      bottomTab: {
        icon: { height: 100, scale: 1, uri: 'lol', width: 100 },
        selectedIcon: { height: 100, scale: 1, uri: 'lol', width: 100 },
      },
    });
  });

  it('calls store if component has passProps', () => {
    const passProps = { some: 'thing' };
    const options = { topBar: { title: { component: { passProps, name: 'a' } } } };

    uut.processOptions(options);

    verify(mockedStore.updateProps('CustomComponent1', passProps)).called();
  });

  it('generates componentId for component id was not passed', () => {
    const options = { topBar: { title: { component: { name: 'a' } } } };

    uut.processOptions(options);

    expect(options).toEqual({
      topBar: { title: { component: { name: 'a', componentId: 'CustomComponent1' } } },
    });
  });

  it('copies passed id to componentId key', () => {
    const options = { topBar: { title: { component: { name: 'a', id: 'Component1' } } } };

    uut.processOptions(options);

    expect(options).toEqual({
      topBar: { title: { component: { name: 'a', id: 'Component1', componentId: 'Component1' } } },
    });
  });

  it('calls store when button has passProps and id', () => {
    const passProps = { prop: 'prop' };
    const options = { topBar: { rightButtons: [{ passProps, id: '1' }] } };

    uut.processOptions(options);

    verify(mockedStore.updateProps('1', passProps)).called();
  });

  it('do not touch passProps when id for button is missing', () => {
    const passProps = { prop: 'prop' };
    const options = { topBar: { rightButtons: [{ passProps } as any] } };

    uut.processOptions(options);

    expect(options).toEqual({ topBar: { rightButtons: [{ passProps }] } });
  });

  it('omits passProps when processing buttons or components', () => {
    const options = {
      topBar: {
        rightButtons: [{ passProps: {}, id: 'btn1' }],
        leftButtons: [{ passProps: {}, id: 'btn2' }],
        title: { component: { name: 'helloThere1', passProps: {} } },
        background: { component: { name: 'helloThere2', passProps: {} } },
      },
    };
    uut.processOptions(options);
    expect(options.topBar.rightButtons[0].passProps).toBeUndefined();
    expect(options.topBar.leftButtons[0].passProps).toBeUndefined();
    expect(options.topBar.title.component.passProps).toBeUndefined();
    expect(options.topBar.background.component.passProps).toBeUndefined();
  });

  it('Will ensure the store has a chance to lazily load components in options', () => {
    const options = {
      topBar: {
        title: { component: { name: 'helloThere1', passProps: {} } },
        background: { component: { name: 'helloThere2', passProps: {} } },
      },
    };
    uut.processOptions(options);
    verify(mockedStore.ensureClassForName('helloThere1')).called();
    verify(mockedStore.ensureClassForName('helloThere2')).called();
  });
});
