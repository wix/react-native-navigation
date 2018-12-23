import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Store } from '../components/Store';
import * as _ from 'lodash';
import { Options, OptionsModalPresentationStyle } from '../interfaces/Options';
import { mock, when, anyString, instance, anyNumber } from 'ts-mockito';
import { ColorService } from '../adapters/ColorService';
import { AssetService } from '../adapters/AssetResolver';

describe('navigation options', () => {
  let uut: OptionsProcessor;
  let optionsRemoveThis: Record<string, any>;
  let store: Store;
  beforeEach(() => {
    optionsRemoveThis = {};
    const mockedAssetService = mock(AssetService);
    when(mockedAssetService.resolveFromRequire(anyNumber())).thenReturn('lol');
    const assetService = instance(mockedAssetService);

    const mockedColorService = mock(ColorService);
    when(mockedColorService.toNativeColor(anyString())).thenReturn(666);
    const colorService = instance(mockedColorService);

    store = new Store();
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

  it('process colors with rgb functions', () => {
    optionsRemoveThis.someKeyColor = 'rgb(255, 0, 255)';
    uut.processOptions(optionsRemoveThis);
    expect(optionsRemoveThis.someKeyColor).toEqual(666);
  });

  it('process colors with special words', () => {
    optionsRemoveThis.someKeyColor = 'fuchsia';
    uut.processOptions(optionsRemoveThis);
    expect(optionsRemoveThis.someKeyColor).toEqual(666);
  });

  it('process colors with hsla functions', () => {
    optionsRemoveThis.someKeyColor = 'hsla(360, 100%, 100%, 1.0)';
    uut.processOptions(optionsRemoveThis);

    expect(optionsRemoveThis.someKeyColor).toEqual(666);
  });

  it('any keys ending with Color', () => {
    optionsRemoveThis.otherKeyColor = 'red';
    optionsRemoveThis.yetAnotherColor = 'blue';
    optionsRemoveThis.andAnotherColor = 'rgb(0, 255, 0)';
    uut.processOptions(optionsRemoveThis);
    expect(optionsRemoveThis.otherKeyColor).toEqual(666);
    expect(optionsRemoveThis.yetAnotherColor).toEqual(666);
    expect(optionsRemoveThis.andAnotherColor).toEqual(666);
  });

  it('keys ending with Color case sensitive', () => {
    optionsRemoveThis.otherKey_color = 'red'; // eslint-disable-line camelcase
    uut.processOptions(optionsRemoveThis);
    expect(optionsRemoveThis.otherKey_color).toEqual('red');
  });

  it('any nested recursive keys ending with Color', () => {
    optionsRemoveThis.topBar = { textColor: 'red' };
    optionsRemoveThis.topBar.innerMostObj = { anotherColor: 'yellow' };
    uut.processOptions(optionsRemoveThis);
    expect(optionsRemoveThis.topBar.textColor).toEqual(666);
    expect(optionsRemoveThis.topBar.innerMostObj.anotherColor).toEqual(666);
  });

  it('passProps for Buttons options', () => {
    const passProps = { prop: 'prop' };
    optionsRemoveThis.rightButtons = [{ passProps, id: '1' }];

    uut.processOptions({ o: optionsRemoveThis });

    expect(store.getPropsForId('1')).toEqual(passProps);
  });

  it('passProps for custom component', () => {
    const passProps = { color: '#ff0000', some: 'thing' };
    optionsRemoveThis.component = { passProps, name: 'a' };

    uut.processOptions({ o: optionsRemoveThis });

    expect(store.getPropsForId(optionsRemoveThis.component.componentId)).toEqual(passProps);
    expect(Object.keys(optionsRemoveThis.component)).not.toContain('passProps');
  });

  it('generate component id for component in options', () => {
    optionsRemoveThis.component = { name: 'a' };

    uut.processOptions({ o: optionsRemoveThis });

    expect(optionsRemoveThis.component.componentId).toBeDefined();
  });

  it('passProps from options are not processed', () => {
    const passProps = { color: '#ff0000', some: 'thing' };
    const clonedProps = _.cloneDeep(passProps);
    optionsRemoveThis.component = { passProps, name: 'a' };

    uut.processOptions(optionsRemoveThis);
    expect(store.getPropsForId(optionsRemoveThis.component.componentId)).toEqual(clonedProps);
  });

  it('pass supplied componentId for component in options', () => {
    optionsRemoveThis.component = { name: 'a', id: 'Component1' };

    uut.processOptions({ o: optionsRemoveThis });

    expect(optionsRemoveThis.component.componentId).toEqual('Component1');
  });

  it('passProps must be with id next to it', () => {
    const passProps = { prop: 'prop' };
    optionsRemoveThis.rightButtons = [{ passProps }];

    uut.processOptions({ o: optionsRemoveThis });

    expect(store.getPropsForId('1')).toEqual({});
  });

  it('processes Options object', () => {
    optionsRemoveThis.someKeyColor = 'rgb(255, 0, 255)';
    optionsRemoveThis.topBar = { textColor: 'red' };
    optionsRemoveThis.topBar.innerMostObj = { anotherColor: 'yellow' };

    uut.processOptions({ o: optionsRemoveThis });

    expect(optionsRemoveThis.topBar.textColor).toEqual(666);
  });

  it('undefined value return undefined ', () => {
    optionsRemoveThis.someImage = undefined;
    uut.processOptions(optionsRemoveThis);

    expect(optionsRemoveThis.someImage).toEqual(undefined);
  });

  it('omits passProps when processing options', () => {
    const passProps = {
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
            passProps: {}
          }
        },
        background: {
          component: {
            passProps: {}
          }
        }
      }
    };
    uut.processOptions(passProps);
    expect(passProps.topBar.rightButtons[0].passProps).toBeUndefined();
    expect(passProps.topBar.leftButtons[0].passProps).toBeUndefined();
    expect(passProps.topBar.title.component.passProps).toBeUndefined();
    expect(passProps.topBar.background.component.passProps).toBeUndefined();
  });
});
