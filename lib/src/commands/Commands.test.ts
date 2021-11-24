import forEach from 'lodash/forEach';
import filter from 'lodash/filter';
import invoke from 'lodash/invoke';
import { mock, verify, instance, deepEqual, when, anything, anyString, capture } from 'ts-mockito';

import { LayoutTreeParser } from './LayoutTreeParser';
import { LayoutTreeCrawler } from './LayoutTreeCrawler';
import { Store } from '../components/Store';
import { Commands } from './Commands';
import { CommandsObserver } from '../events/CommandsObserver';
import { NativeCommandsSender } from '../adapters/NativeCommandsSender';
import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Options } from '../interfaces/Options';
import { LayoutProcessor } from '../processors/LayoutProcessor';
import { LayoutProcessorsStore } from '../processors/LayoutProcessorsStore';
import { CommandName } from '../interfaces/CommandName';
import { OptionsCrawler } from './OptionsCrawler';
import React from 'react';
import { IWrappedComponent } from 'react-native-navigation/components/ComponentWrapper';

describe('Commands', () => {
  let uut: Commands;
  let mockedNativeCommandsSender: NativeCommandsSender;
  let mockedOptionsProcessor: OptionsProcessor;
  let mockedStore: Store;
  let commandsObserver: CommandsObserver;
  let mockedUniqueIdProvider: UniqueIdProvider;
  let layoutProcessor: LayoutProcessor;

  beforeEach(() => {
    mockedNativeCommandsSender = mock(NativeCommandsSender);
    mockedUniqueIdProvider = mock(UniqueIdProvider);
    when(mockedUniqueIdProvider.generate(anything())).thenCall((prefix) => `${prefix}+UNIQUE_ID`);
    const uniqueIdProvider = instance(mockedUniqueIdProvider);
    mockedStore = mock(Store);
    commandsObserver = new CommandsObserver(uniqueIdProvider);
    const layoutProcessorsStore = new LayoutProcessorsStore();

    mockedOptionsProcessor = mock(OptionsProcessor);
    const optionsProcessor = instance(mockedOptionsProcessor) as OptionsProcessor;

    layoutProcessor = new LayoutProcessor(layoutProcessorsStore);
    jest.spyOn(layoutProcessor, 'process');

    uut = new Commands(
      instance(mockedStore),
      instance(mockedNativeCommandsSender),
      new LayoutTreeParser(uniqueIdProvider),
      new LayoutTreeCrawler(instance(mockedStore), optionsProcessor),
      commandsObserver,
      uniqueIdProvider,
      optionsProcessor,
      layoutProcessor,
      new OptionsCrawler(instance(mockedStore), uniqueIdProvider)
    );
  });

  describe('setRoot', () => {
    it('sends setRoot to native after parsing into a correct layout tree', () => {
      uut.setRoot({
        root: { component: { name: 'com.example.MyScreen' } },
      });
      verify(
        mockedNativeCommandsSender.setRoot(
          'setRoot+UNIQUE_ID',
          deepEqual({
            root: {
              type: 'Component',
              id: 'Component+UNIQUE_ID',
              children: [],
              data: { name: 'com.example.MyScreen', options: {}, passProps: undefined },
            },
            modals: [],
            overlays: [],
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
        overlays: [{ component: { name: 'com.example.MyOverlay' } }],
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
                passProps: undefined,
              },
            },
            modals: [
              {
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                children: [],
                data: {
                  name: 'com.example.MyModal',
                  options: {},
                  passProps: undefined,
                },
              },
            ],
            overlays: [
              {
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                children: [],
                data: {
                  name: 'com.example.MyOverlay',
                  options: {},
                  passProps: undefined,
                },
              },
            ],
          })
        )
      ).called();
    });

    it('process layout with layoutProcessor', () => {
      uut.setRoot({
        root: { component: { name: 'com.example.MyScreen' } },
      });
      expect(layoutProcessor.process).toBeCalledWith(
        { component: { name: 'com.example.MyScreen', options: {}, id: 'Component+UNIQUE_ID' } },
        CommandName.SetRoot
      );
    });

    it('pass component static options to layoutProcessor', () => {
      when(mockedStore.getComponentClassForName('com.example.MyScreen')).thenReturn(
        () =>
          class extends React.Component {
            static options(): Options {
              return {
                topBar: {
                  visible: false,
                },
              };
            }
          }
      );

      uut.setRoot({
        root: { component: { name: 'com.example.MyScreen' } },
      });
      expect(layoutProcessor.process).toBeCalledWith(
        {
          component: {
            id: 'Component+UNIQUE_ID',
            name: 'com.example.MyScreen',
            options: { topBar: { visible: false } },
          },
        },
        CommandName.SetRoot
      );
    });

    it('retains passProps properties identity', () => {
      const obj = { some: 'content' };
      uut.setRoot({ root: { component: { name: 'com.example.MyScreen', passProps: { obj } } } });
      const args = capture(mockedStore.setPendingProps).last();
      expect(args[1].obj).toBe(obj);
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

    it('show warning when invoking before componentDidMount', () => {
      jest.spyOn(console, 'warn');
      when(mockedStore.getComponentInstance('component1')).thenReturn({} as IWrappedComponent);
      const componentId = 'component1';
      uut.mergeOptions(componentId, { blurOnUnmount: true });
      expect(console.warn).toBeCalledWith(
        `Navigation.mergeOptions was invoked on component with id: ${componentId} before it is mounted, this can cause UI issues and should be avoided.\n Use static options instead.`
      );
    });

    it('should not show warning for mounted component', () => {
      jest.spyOn(console, 'warn');
      const componentId = 'component1';
      when(mockedStore.getComponentInstance('component1')).thenReturn({
        isMounted: true,
      } as IWrappedComponent);

      uut.mergeOptions('component1', { blurOnUnmount: true });
      expect(console.warn).not.toBeCalledWith(
        `Navigation.mergeOptions was invoked on component with id: ${componentId} before it is mounted, this can cause UI issues and should be avoided.\n Use static options instead.`
      );
    });

    it('should not show warning for component id that does not exist', () => {
      jest.spyOn(console, 'warn');
      const componentId = 'component1';
      when(mockedStore.getComponentInstance('stackId')).thenReturn(undefined);

      uut.mergeOptions('stackId', { blurOnUnmount: true });
      expect(console.warn).not.toBeCalledWith(
        `Navigation.mergeOptions was invoked on component with id: ${componentId} before it is mounted, this can cause UI issues and should be avoided.\n Use static options instead.`
      );
    });
  });

  describe('updateProps', () => {
    it('delegates to store', () => {
      uut.updateProps('theComponentId', { someProp: 'someValue' });
      verify(mockedStore.updateProps('theComponentId', deepEqual({ someProp: 'someValue' })));
    });

    it('notifies commands observer', () => {
      uut.updateProps('theComponentId', { someProp: 'someValue' });
      verify(
        commandsObserver.notify(
          'updateProps',
          deepEqual({ componentId: 'theComponentId', props: { someProp: 'someValue' } })
        )
      );
    });

    it('update props with callback', () => {
      const callback = jest.fn();
      uut.updateProps('theComponentId', { someProp: 'someValue' }, callback);

      const args = capture(mockedStore.updateProps).last();
      expect(args[0]).toEqual('theComponentId');
      expect(args[1]).toEqual({ someProp: 'someValue' });
      expect(args[2]).toEqual(callback);
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
              passProps: undefined,
            },
            children: [],
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

    it('process layout with layoutProcessor', () => {
      uut.showModal({ component: { name: 'com.example.MyScreen' } });
      expect(layoutProcessor.process).toBeCalledWith(
        { component: { id: 'Component+UNIQUE_ID', name: 'com.example.MyScreen', options: {} } },
        CommandName.ShowModal
      );
    });

    it('retains passProps properties identity', () => {
      const obj = { some: 'content' };
      uut.showModal({ component: { name: 'com.example.MyScreen', passProps: { obj } } });
      const args = capture(mockedStore.setPendingProps).last();
      expect(args[1].obj).toBe(obj);
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

    it('processes mergeOptions', async () => {
      const options = {
        animations: {
          dismissModal: {
            enabled: false,
          },
        },
      };

      uut.dismissModal('myUniqueId', options);
      verify(mockedOptionsProcessor.processOptions(CommandName.DismissModal, options)).called();
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

    it('processes mergeOptions', async () => {
      const options: Options = {
        animations: {
          dismissModal: {
            enabled: false,
          },
        },
      };

      uut.dismissAllModals(options);
      verify(mockedOptionsProcessor.processOptions(CommandName.DismissAllModals, options)).called();
    });
  });

  describe('push', () => {
    it('resolves with the parsed layout', async () => {
      when(mockedNativeCommandsSender.push(anyString(), anyString(), anything())).thenResolve(
        'the resolved layout'
      );
      const result = await uut.push('theComponentId', {
        component: { name: 'com.example.MyScreen' },
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
              passProps: undefined,
            },
            children: [],
          })
        )
      ).called();
    });

    it('process layout with layoutProcessor', () => {
      uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
      expect(layoutProcessor.process).toBeCalledWith(
        { component: { id: 'Component+UNIQUE_ID', name: 'com.example.MyScreen', options: {} } },
        CommandName.Push
      );
    });

    it('retains passProps properties identity', () => {
      const obj = { some: 'content' };
      uut.push('theComponentId', {
        component: { name: 'com.example.MyScreen', passProps: { obj } },
      });
      const args = capture(mockedStore.setPendingProps).last();
      expect(args[1].obj).toBe(obj);
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

    it('processes mergeOptions', async () => {
      const options: Options = {
        animations: {
          pop: {
            enabled: false,
          },
        },
      };

      uut.pop('theComponentId', options);
      verify(mockedOptionsProcessor.processOptions(CommandName.Pop, options)).called();
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

    it('processes mergeOptions', async () => {
      const options: Options = {
        animations: {
          pop: {
            enabled: false,
          },
        },
      };

      uut.popTo('theComponentId', options);
      verify(mockedOptionsProcessor.processOptions(CommandName.PopTo, options)).called();
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

    it('processes mergeOptions', async () => {
      const options: Options = {
        animations: {
          pop: {
            enabled: false,
          },
        },
      };

      uut.popToRoot('theComponentId', options);
      verify(mockedOptionsProcessor.processOptions(CommandName.PopToRoot, options)).called();
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
                passProps: undefined,
              },
              children: [],
            },
          ])
        )
      ).called();
    });

    it('process layout with layoutProcessor', () => {
      uut.setStackRoot('theComponentId', [{ component: { name: 'com.example.MyScreen' } }]);
      expect(layoutProcessor.process).toBeCalledWith(
        { component: { id: 'Component+UNIQUE_ID', name: 'com.example.MyScreen', options: {} } },
        CommandName.SetStackRoot
      );
    });

    it('retains passProps properties identity', () => {
      const obj = { some: 'content' };
      uut.setStackRoot('theComponentId', [
        { component: { name: 'com.example.MyScreen', passProps: { obj } } },
      ]);
      const args = capture(mockedStore.setPendingProps).last();
      expect(args[1].obj).toBe(obj);
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
              passProps: undefined,
            },
            children: [],
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

    it('process layout with layoutProcessor', () => {
      uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
      expect(layoutProcessor.process).toBeCalledWith(
        { component: { id: 'Component+UNIQUE_ID', name: 'com.example.MyScreen', options: {} } },
        CommandName.ShowOverlay
      );
    });

    it('retains passProps properties identity', () => {
      const obj = { some: 'content' };
      uut.showOverlay({ component: { name: 'com.example.MyScreen', passProps: { obj } } });
      const args = capture(mockedStore.setPendingProps).last();
      expect(args[1].obj).toBe(obj);
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

    beforeEach(() => {
      cb = jest.fn();
      mockedLayoutTreeParser = mock(LayoutTreeParser);
      mockedLayoutTreeCrawler = mock(LayoutTreeCrawler);
      commandsObserver.register(cb);
      const mockedOptionsProcessor = mock(OptionsProcessor) as OptionsProcessor;

      uut = new Commands(
        mockedStore,
        mockedNativeCommandsSender,
        instance(mockedLayoutTreeParser),
        instance(mockedLayoutTreeCrawler),
        commandsObserver,
        instance(mockedUniqueIdProvider),
        instance(mockedOptionsProcessor),
        new LayoutProcessor(new LayoutProcessorsStore()),
        new OptionsCrawler(instance(mockedStore), mockedUniqueIdProvider)
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
        dismissAllOverlays: [{}],
        getLaunchArgs: ['id'],
      };
      const paramsForMethodName: Record<string, object> = {
        setRoot: {
          commandId: 'setRoot+UNIQUE_ID',
          layout: { root: null, modals: [], overlays: [] },
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
          layout: [null],
        },
        showOverlay: { commandId: 'showOverlay+UNIQUE_ID', layout: null },
        dismissOverlay: { commandId: 'dismissOverlay+UNIQUE_ID', componentId: 'id' },
        dismissAllOverlays: { commandId: 'dismissAllOverlays+UNIQUE_ID' },
        getLaunchArgs: { commandId: 'getLaunchArgs+UNIQUE_ID' },
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
