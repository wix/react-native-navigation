import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Store } from '../components/Store';
import { Options, OptionsModalPresentationStyle } from '../interfaces/Options';
import { mock, instance, when, anyNumber, anyString, verify } from 'ts-mockito';
import { AssetService } from '../adapters/AssetResolver';
import { ColorService } from '../adapters/ColorService';

describe(OptionsProcessor.name, () => {
  let optionsProcessor: OptionsProcessor;

  const mockedAssetService = mock(AssetService);
  when(mockedAssetService.resolveFromRequire(anyNumber())).thenReturn('lol');
  const assetService = instance(mockedAssetService);

  const mockedColorService = mock(ColorService);
  when(mockedColorService.toNativeColor(anyString())).thenReturn(666);
  const colorService = instance(mockedColorService);

  const mockedStore = mock(Store);
  const store = instance(mockedStore);

  beforeEach(() => {
    optionsProcessor = new OptionsProcessor(
      store,
      new UniqueIdProvider(),
      assetService,
      colorService,
    );
  });

  it('keeps original values if values were not processed', () => {
    const options: Options = {
      blurOnUnmount: false,
      popGesture: false,
      modalPresentationStyle: OptionsModalPresentationStyle.fullScreen,
      animations: { dismissModal: { alpha: { from: 0, to: 1 } } },
    };
    expect(optionsProcessor.processOptions(options)).toEqual({
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
    expect(optionsProcessor.processOptions(options)).toEqual({
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
    expect(optionsProcessor.processOptions(options)).toEqual({
      backgroundImage: 'lol',
      rootBackgroundImage: 'lol',
      bottomTab: { icon: 'lol', selectedIcon: 'lol' },
    });
  });

  it('calls store when buttons have props', () => {
    const passProps = { prop: 'prop' };
    const options: Options = {
      topBar: {
        rightButtons: [{ component: { passProps, name: 'loool' }, id: '1' }],
      },
    };

    optionsProcessor.processOptions(options);

    verify(mockedStore.setPropsForId('1', passProps)).called();
  });

  it('remove passProps if buttons have passProps', () => {
    const options: Options = {
      topBar: {
        rightButtons: [{ component: { passProps: { prop: 'prop' }, name: 'loool' }, id: '1' }],
      },
    };

    expect(optionsProcessor.processOptions(options)).toEqual({
      topBar: {
        rightButtons: [{ component: { name: 'loool' }, id: '1' }],
      },
    });
  });

  it('keeps button components untouched if they do not have passProps', () => {
    const options: Options = {
      topBar: { rightButtons: [{ component: { name: 'loool' }, id: '1' }] },
    };

    expect(optionsProcessor.processOptions(options)).toEqual({
      topBar: { rightButtons: [{ component: { name: 'loool' }, id: '1' }] },
    });
  });

  it('generates component id for component without id', () => {
    const options: Options = {
      topBar: { title: { component: { name: 'a' } } },
    };

    expect(optionsProcessor.processOptions(options)).toEqual({
      topBar: { title: { component: { name: 'a', componentId: 'CustomComponent1' } } },
    });
  });
});
