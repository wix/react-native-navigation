import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Store } from '../components/Store';
import * as _ from 'lodash';
import { Options, OptionsModalPresentationStyle } from '../interfaces/Options';
import { mock, when, anyString, instance, anyNumber, verify } from 'ts-mockito';
import { ColorService } from '../adapters/ColorService';
import { AssetService } from '../adapters/AssetResolver';

describe('navigation options', () => {
  let uut: OptionsProcessor;
  let optionsRemoveThis: Record<string, any>;
  const mockedStore = mock(Store);
  const store = instance(mockedStore);
  beforeEach(() => {
    optionsRemoveThis = {};
    const mockedAssetService = mock(AssetService);
    when(mockedAssetService.resolveFromRequire(anyNumber())).thenReturn('lol');
    const assetService = instance(mockedAssetService);

    const mockedColorService = mock(ColorService);
    when(mockedColorService.toNativeColor(anyString())).thenReturn(666);
    const colorService = instance(mockedColorService);

    uut = new OptionsProcessor(store, new UniqueIdProvider(), colorService, assetService);
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
      backgroundImage: 'lol',
      rootBackgroundImage: 'lol',
      bottomTab: { icon: 'lol', selectedIcon: 'lol' },
    });
  });

  it('passProps for custom component', () => {
    const passProps = { some: 'thing' };
    optionsRemoveThis.component = { passProps, name: 'a' };

    uut.processOptions(optionsRemoveThis);

    verify(mockedStore.setPropsForId('CustomComponent1', passProps)).called();
    expect(Object.keys(optionsRemoveThis.component)).not.toContain('passProps');
  });

  it('generate component id for component in options', () => {
    optionsRemoveThis.component = { name: 'a' };

    uut.processOptions(optionsRemoveThis);

    expect(optionsRemoveThis.component.componentId).toBeDefined();
  });

  it('passProps from options are not processed', () => {
    const passProps = { some: 'thing' };
    optionsRemoveThis.component = { passProps, name: 'a' };

    uut.processOptions(optionsRemoveThis);
    verify(mockedStore.setPropsForId('CustomComponent1', passProps)).called();
  });

  it('pass supplied componentId for component in options', () => {
    optionsRemoveThis.component = { name: 'a', id: 'Component1' };

    uut.processOptions(optionsRemoveThis);

    expect(optionsRemoveThis.component.componentId).toEqual('Component1');
  });

  it('calls store when button has passProps and id', () => {
    const passProps = { prop: 'prop' };
    optionsRemoveThis.rightButtons = [{ passProps, id: '1' }];

    uut.processOptions(optionsRemoveThis);

    verify(mockedStore.setPropsForId('1', passProps)).called();
  });

  it('omits passProps when processing buttons or components', () => {
    const options = {
      topBar: {
        rightButtons: [
          {
            passProps: {},
            id: 'btn1'
          },
        ],
        leftButtons: [
          {
            passProps: {},
            id: 'btn2'
          }
        ],
        title: {
          component: {
            name: 'helloThere1',
            passProps: {}
          }
        },
        background: {
          component: {
            name: 'helloThere2',
            passProps: {}
          }
        }
      }
    };
    uut.processOptions(options);
    expect(options.topBar.rightButtons[0].passProps).toBeUndefined();
    expect(options.topBar.leftButtons[0].passProps).toBeUndefined();
    expect(options.topBar.title.component.passProps).toBeUndefined();
    expect(options.topBar.background.component.passProps).toBeUndefined();
  });
});
