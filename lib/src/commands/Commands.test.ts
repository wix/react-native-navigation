import forEach from 'lodash/forEach'
import filter from 'lodash/filter'
import invoke from 'lodash/invoke'
import { mock, verify, instance, deepEqual, when, anything, anyString } from 'ts-mockito';

import { LayoutTreeParser } from './LayoutTreeParser';
import { LayoutTreeCrawler } from './LayoutTreeCrawler';
import { Store } from '../components/Store';
import { Commands } from './Commands';
import { CommandsObserver } from '../events/CommandsObserver';
import { NativeCommandsSender } from '../adapters/NativeCommandsSender';
import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Options } from '../interfaces/Options';

describe('Commands', () => {
  let uut: Commands;
  let mockedNativeCommandsSender: NativeCommandsSender;
  let mockedStore: Store;
  let commandsObserver: CommandsObserver;
  let mockedUniqueIdProvider: UniqueIdProvider;

  beforeEach(() => {
    mockedNativeCommandsSender = mock(NativeCommandsSender);
    mockedUniqueIdProvider = mock(UniqueIdProvider);
    when(mockedUniqueIdProvider.generate(anything())).thenCall((prefix) => `${prefix}+UNIQUE_ID`);
    const uniqueIdProvider = instance(mockedUniqueIdProvider);
    mockedStore = mock(Store);
    commandsObserver = new CommandsObserver(uniqueIdProvider);

    const mockedOptionsProcessor = mock(OptionsProcessor);
    const optionsProcessor = instance(mockedOptionsProcessor) as OptionsProcessor;

    uut = new Commands(
      mockedStore,
      instance(mockedNativeCommandsSender),
      new LayoutTreeParser(uniqueIdProvider),
      new LayoutTreeCrawler(instance(mockedStore), optionsProcessor),
      commandsObserver,
      uniqueIdProvider,
      optionsProcessor
    );
  });

  describe('setRoot', () => {
    it('sends setRoot to native after parsing into a correct layout tree', () => {
      uut.setRoot({
        root: { component: { name: 'com.example.MyScreen' } }
      });
      verify(
        mockedNativeCommandsSender.setRoot(
          'setRoot+UNIQUE_ID',
          deepEqual({
            root: {
              type: 'Component',
              id: 'Component+UNIQUE_ID',
              children: [],
              data: { name: 'com.example.MyScreen', options: {}, passProps: undefined }
            },
            modals: [],
            overlays: []
          })
        )
      ).called();
    });

    it('returns a promise with the resolved layout', async () => {
      when(mockedNativeCommandsSender.setRoot(anything(), anything())).thenResolve(
        'the resolved layout'
      );
      const result = await uut.setRoot({ root: { component: { name: 'com.example.MyScreen' } } });
      expect(result).toEqual('the resolved layout');
    });

    it('inputs modals and overlays', () => {
      uut.setRoot({
        root: { component: { name: 'com.example.MyScreen' } },
        modals: [{ component: { name: 'com.example.MyModal' } }],
        overlays: [{ component: { name: 'com.example.MyOverlay' } }]
      });
      verify(
        mockedNativeCommandsSender.setRoot(
          'setRoot+UNIQUE_ID',
          deepEqual({
            root: {
              type: 'Component',
              id: 'Component+UNIQUE_ID',
              children: [],
              data: {
                name: 'com.example.MyScreen',
                options: {},
                passProps: undefined
              }
            },
            modals: [
              {
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                children: [],
                data: {
                  name: 'com.example.MyModal',
                  options: {},
                  passProps: undefined
                }
              }
            ],
            overlays: [
              {
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                children: [],
                data: {
                  name: 'com.example.MyOverlay',
                  options: {},
                  passProps: undefined
                }
              }
            ]
          })
        )
      ).called();
    });
  });

  describe('mergeOptions', () => {
    it('passes options for component', () => {
      uut.mergeOptions('theComponentId', { blurOnUnmount: true });
      verify(
        mockedNativeCommandsSender.mergeOptions(
          'theComponentId',
          deepEqual({ blurOnUnmount: true })
        )
      ).called();
    });
  });

  describe('updateProps', () => {
    it('delegates to store', () => {
      uut.updateProps('theComponentId', {someProp: 'someValue'});
      verify(mockedStore.updateProps('theComponentId', deepEqual({someProp: 'someValue'})));
    });

    it('notifies commands observer', () => {
      uut.updateProps('theComponentId', {someProp: 'someValue'});
      verify(commandsObserver.notify('updateProps', deepEqual({componentId: 'theComponentId', props: {someProp: 'someValue'}})));
    });
  });

  describe('showModal', () => {
    it('sends command to native after parsing into a correct layout tree', () => {
      uut.showModal({ component: { name: 'com.example.MyScreen' } });
      verify(
        mockedNativeCommandsSender.showModal(
          'showModal+UNIQUE_ID',
          deepEqual({
            type: 'Component',
            id: 'Component+UNIQUE_ID',
            data: {
              name: 'com.example.MyScreen',
              options: {},
              passProps: undefined
            },
            children: []
          })
        )
      ).called();
    });

    it('returns a promise with the resolved layout', async () => {
      when(mockedNativeCommandsSender.showModal(anything(), anything())).thenResolve(
        'the resolved layout'
      );
      const result = await uut.showModal({ component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('the resolved layout');
    });
  });

  describe('dismissModal', () => {
    it('sends command to native', () => {
      uut.dismissModal('myUniqueId', {});
      verify(
        mockedNativeCommandsSender.dismissModal(
          'dismissModal+UNIQUE_ID',
          'myUniqueId',
          deepEqual({})
        )
      ).called();
    });

    it('returns a promise with the id', async () => {
      when(
        mockedNativeCommandsSender.dismissModal(anyString(), anything(), anything())
      ).thenResolve('the id');
      const result = await uut.dismissModal('myUniqueId');
      expect(result).toEqual('the id');
    });
  });

  describe('dismissAllModals', () => {
    it('sends command to native', () => {
      uut.dismissAllModals({});
      verify(
        mockedNativeCommandsSender.dismissAllModals('dismissAllModals+UNIQUE_ID', deepEqual({}))
      ).called();
    });

    it('returns a promise with the id', async () => {
      when(mockedNativeCommandsSender.dismissAllModals(anyString(), anything())).thenResolve(
        'the id'
      );
      const result = await uut.dismissAllModals();
      expect(result).toEqual('the id');
    });
  });

  describe('push', () => {
    it('resolves with the parsed layout', async () => {
      when(mockedNativeCommandsSender.push(anyString(), anyString(), anything())).thenResolve(
        'the resolved layout'
      );
      const result = await uut.push('theComponentId', {
        component: { name: 'com.example.MyScreen' }
      });
      expect(result).toEqual('the resolved layout');
    });

    it('parses into correct layout node and sends to native', () => {
      uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
      verify(
        mockedNativeCommandsSender.push(
          'push+UNIQUE_ID',
          'theComponentId',
          deepEqual({
            type: 'Component',
            id: 'Component+UNIQUE_ID',
            data: {
              name: 'com.example.MyScreen',
              options: {},
              passProps: undefined
            },
            children: []
          })
        )
      ).called();
    });
  });

  describe('pop', () => {
    it('pops a component, passing componentId', () => {
      uut.pop('theComponentId', {});
      verify(
        mockedNativeCommandsSender.pop('pop+UNIQUE_ID', 'theComponentId', deepEqual({}))
      ).called();
    });
    it('pops a component, passing componentId and options', () => {
      const options: Options = { popGesture: true };
      uut.pop('theComponentId', options);
      verify(mockedNativeCommandsSender.pop('pop+UNIQUE_ID', 'theComponentId', options)).called();
    });

    it('pop returns a promise that resolves to componentId', async () => {
      when(mockedNativeCommandsSender.pop(anyString(), anyString(), anything())).thenResolve(
        'theComponentId'
      );
      const result = await uut.pop('theComponentId', {});
      expect(result).toEqual('theComponentId');
    });
  });

  describe('popTo', () => {
    it('pops all components until the passed Id is top', () => {
      uut.popTo('theComponentId', {});
      verify(
        mockedNativeCommandsSender.popTo('popTo+UNIQUE_ID', 'theComponentId', deepEqual({}))
      ).called();
    });

    it('returns a promise that resolves to targetId', async () => {
      when(mockedNativeCommandsSender.popTo(anyString(), anyString(), anything())).thenResolve(
        'theComponentId'
      );
      const result = await uut.popTo('theComponentId');
      expect(result).toEqual('theComponentId');
    });
  });

  describe('popToRoot', () => {
    it('pops all components to root', () => {
      uut.popToRoot('theComponentId', {});
      verify(
        mockedNativeCommandsSender.popToRoot('popToRoot+UNIQUE_ID', 'theComponentId', deepEqual({}))
      ).called();
    });

    it('returns a promise that resolves to targetId', async () => {
      when(mockedNativeCommandsSender.popToRoot(anyString(), anyString(), anything())).thenResolve(
        'theComponentId'
      );
      const result = await uut.popToRoot('theComponentId');
      expect(result).toEqual('theComponentId');
    });
  });

  describe('setStackRoot', () => {
    it('parses into correct layout node and sends to native', () => {
      uut.setStackRoot('theComponentId', [{ component: { name: 'com.example.MyScreen' } }]);
      verify(
        mockedNativeCommandsSender.setStackRoot(
          'setStackRoot+UNIQUE_ID',
          'theComponentId',
          deepEqual([
            {
              type: 'Component',
              id: 'Component+UNIQUE_ID',
              data: {
                name: 'com.example.MyScreen',
                options: {},
                passProps: undefined
              },
              children: []
            }
          ])
        )
      ).called();
    });
  });

  describe('showOverlay', () => {
    it('sends command to native after parsing into a correct layout tree', () => {
      uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
      verify(
        mockedNativeCommandsSender.showOverlay(
          'showOverlay+UNIQUE_ID',
          deepEqual({
            type: 'Component',
            id: 'Component+UNIQUE_ID',
            data: {
              name: 'com.example.MyScreen',
              options: {},
              passProps: undefined
            },
            children: []
          })
        )
      ).called();
    });

    it('resolves with the component id', async () => {
      when(mockedNativeCommandsSender.showOverlay(anyString(), anything())).thenResolve(
        'Component1'
      );
      const result = await uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('Component1');
    });
  });

  describe('dismissOverlay', () => {
    it('check promise returns true', async () => {
      when(mockedNativeCommandsSender.dismissOverlay(anyString(), anyString())).thenResolve(true);
      const result = await uut.dismissOverlay('Component1');
      verify(mockedNativeCommandsSender.dismissOverlay(anyString(), anyString())).called();
      expect(result).toEqual(true);
    });

    it('send command to native with componentId', () => {
      uut.dismissOverlay('Component1');
      verify(
        mockedNativeCommandsSender.dismissOverlay('dismissOverlay+UNIQUE_ID', 'Component1')
      ).called();
    });
  });

  describe('notifies commandsObserver', () => {
    let cb: any;
    let mockedLayoutTreeParser: LayoutTreeParser;
    let mockedLayoutTreeCrawler: LayoutTreeCrawler;
    let anotherMockedUniqueIdProvider: UniqueIdProvider;

    beforeEach(() => {
      cb = jest.fn();
      mockedLayoutTreeParser = mock(LayoutTreeParser);
      mockedLayoutTreeCrawler = mock(LayoutTreeCrawler);
      commandsObserver.register(cb);
      const mockedOptionsProcessor = mock(OptionsProcessor) as OptionsProcessor;
      anotherMockedUniqueIdProvider = mock(UniqueIdProvider);
      when(anotherMockedUniqueIdProvider.generate(anything())).thenCall(
        (prefix) => `${prefix}+UNIQUE_ID`
      );

      uut = new Commands(
        mockedStore,
        mockedNativeCommandsSender,
        instance(mockedLayoutTreeParser),
        instance(mockedLayoutTreeCrawler),
        commandsObserver,
        instance(anotherMockedUniqueIdProvider),
        instance(mockedOptionsProcessor)
      );
    });

    function getAllMethodsOfUut() {
      const uutFns = Object.getOwnPropertyNames(Commands.prototype);
      const methods = filter(uutFns, (fn) => fn !== 'constructor');
      expect(methods.length).toBeGreaterThan(1);
      return methods;
    }

    describe('passes correct params', () => {
      const argsForMethodName: Record<string, any[]> = {
        setRoot: [{}],
        setDefaultOptions: [{}],
        mergeOptions: ['id', {}],
        updateProps: ['id', {}],
        showModal: [{}],
        dismissModal: ['id', {}],
        dismissAllModals: [{}],
        push: ['id', {}],
        pop: ['id', {}],
        popTo: ['id', {}],
        popToRoot: ['id', {}],
        setStackRoot: ['id', [{}]],
        showOverlay: [{}],
        dismissOverlay: ['id'],
        getLaunchArgs: ['id']
      };
      const paramsForMethodName: Record<string, object> = {
        setRoot: {
          commandId: 'setRoot+UNIQUE_ID',
          layout: { root: null, modals: [], overlays: [] }
        },
        setDefaultOptions: { options: {} },
        mergeOptions: { componentId: 'id', options: {} },
        updateProps: { componentId: 'id', props: {} },
        showModal: { commandId: 'showModal+UNIQUE_ID', layout: null },
        dismissModal: { commandId: 'dismissModal+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
        dismissAllModals: { commandId: 'dismissAllModals+UNIQUE_ID', mergeOptions: {} },
        push: { commandId: 'push+UNIQUE_ID', componentId: 'id', layout: null },
        pop: { commandId: 'pop+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
        popTo: { commandId: 'popTo+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
        popToRoot: { commandId: 'popToRoot+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
        setStackRoot: {
          commandId: 'setStackRoot+UNIQUE_ID',
          componentId: 'id',
          layout: [null]
        },
        showOverlay: { commandId: 'showOverlay+UNIQUE_ID', layout: null },
        dismissOverlay: { commandId: 'dismissOverlay+UNIQUE_ID', componentId: 'id' },
        getLaunchArgs: { commandId: 'getLaunchArgs+UNIQUE_ID' }
      };
      forEach(getAllMethodsOfUut(), (m) => {
        it(`for ${m}`, () => {
          expect(argsForMethodName).toHaveProperty(m);
          expect(paramsForMethodName).toHaveProperty(m);
          invoke(uut, m, ...argsForMethodName[m]);
          expect(cb).toHaveBeenCalledTimes(1);
          expect(cb).toHaveBeenCalledWith(m, paramsForMethodName[m]);
        });
      });
    });
  });
});
