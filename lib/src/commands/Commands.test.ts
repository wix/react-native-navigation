import * as _ from 'lodash';
import { mock, verify, instance, deepEqual, when, anything, anyString } from 'ts-mockito';

import { LayoutTreeParser } from './LayoutTreeParser';
import { LayoutTreeCrawler } from './LayoutTreeCrawler';
import { Store } from '../components/Store';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider.mock';
import { Commands } from './Commands';
import { CommandsObserver } from '../events/CommandsObserver';
import { NativeCommandsSender } from '../adapters/NativeCommandsSender';
import { OptionsProcessor } from './OptionsProcessor';

describe('Commands', () => {
  let uut: Commands;
  let mockedNativeCommandsSender: NativeCommandsSender;
  let nativeCommandsSender: NativeCommandsSender;
  let store: Store;
  let commandsObserver: CommandsObserver;

  beforeEach(() => {
    store = new Store();
    commandsObserver = new CommandsObserver();
    mockedNativeCommandsSender = mock(NativeCommandsSender);
    nativeCommandsSender = instance(mockedNativeCommandsSender);

    const mockedOptionsProcessor = mock(OptionsProcessor);
    const optionsProcessor = instance(mockedOptionsProcessor);

    uut = new Commands(
      nativeCommandsSender,
      new LayoutTreeParser(),
      new LayoutTreeCrawler(new UniqueIdProvider(), store, optionsProcessor),
      commandsObserver,
      new UniqueIdProvider(),
      optionsProcessor
    );
  });

  describe('setRoot', () => {
    it('sends setRoot to native after parsing into a correct layout tree', () => {
      uut.setRoot({
        root: {
          component: {
            name: 'com.example.MyScreen'
          }
        }
      });
      verify(mockedNativeCommandsSender.setRoot('setRoot+UNIQUE_ID', deepEqual({
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
        modals: [],
        overlays: []
      }))).called();
    });

    it('passProps into components', () => {
      const passProps = {
        fn: () => 'Hello'
      };
      expect(store.getPropsForId('Component+UNIQUE_ID')).toEqual({});
      uut.setRoot({ root: { component: { name: 'asd', passProps } } });
      expect(store.getPropsForId('Component+UNIQUE_ID')).toEqual(passProps);
      expect(store.getPropsForId('Component+UNIQUE_ID').fn()).toEqual('Hello');
    });

    it('returns a promise with the resolved layout', async () => {
      when(mockedNativeCommandsSender.setRoot(anything(), anything())).thenResolve('the resolved layout' as any);
      const result = await uut.setRoot({ root: { component: { name: 'com.example.MyScreen' } } });
      expect(result).toEqual('the resolved layout');
    });

    it('inputs modals and overlays', () => {
      uut.setRoot({
        root: {
          component: {
            name: 'com.example.MyScreen'
          }
        },
        modals: [
          {
            component: {
              name: 'com.example.MyModal'
            }
          }
        ],
        overlays: [
          {
            component: {
              name: 'com.example.MyOverlay'
            }
          }
        ]
      });
      verify(mockedNativeCommandsSender.setRoot('setRoot+UNIQUE_ID', deepEqual({
        root:
          {
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
      }))).called();
    });
  });

  describe('mergeOptions', () => {
    it('passes options for component', () => {
      uut.mergeOptions('theComponentId', { title: '1' } as any);
      verify(mockedNativeCommandsSender.mergeOptions('theComponentId', deepEqual({title: '1'}))).called();
    });
  });

  describe('showModal', () => {
    it('sends command to native after parsing into a correct layout tree', () => {
      uut.showModal({
        component: {
          name: 'com.example.MyScreen'
        }
      });
      verify(mockedNativeCommandsSender.showModal('showModal+UNIQUE_ID', deepEqual({
        type: 'Component',
        id: 'Component+UNIQUE_ID',
        data: {
          name: 'com.example.MyScreen',
          options: {},
          passProps: undefined
        },
        children: []
      }))).called();
    });

    it('passProps into components', () => {
      const passProps = {};
      expect(store.getPropsForId('Component+UNIQUE_ID')).toEqual({});
      uut.showModal({
        component: {
          name: 'com.example.MyScreen',
          passProps
        }
      });
      expect(store.getPropsForId('Component+UNIQUE_ID')).toEqual(passProps);
    });

    it('returns a promise with the resolved layout', async () => {
      when(mockedNativeCommandsSender.showModal(anything(), anything())).thenResolve('the resolved layout' as any);
      const result = await uut.showModal({ component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('the resolved layout');
    });
  });

  describe('dismissModal', () => {
    it('sends command to native', () => {
      uut.dismissModal('myUniqueId', {});
      verify(mockedNativeCommandsSender.dismissModal('dismissModal+UNIQUE_ID', 'myUniqueId', deepEqual({}))).called();
    });

    it('returns a promise with the id', async () => {
      when(mockedNativeCommandsSender.dismissModal(anyString(), anything(), anything())).thenResolve('the id' as any);
      const result = await uut.dismissModal('myUniqueId');
      expect(result).toEqual('the id');
    });
  });

  describe('dismissAllModals', () => {
    it('sends command to native', () => {
      uut.dismissAllModals({});
      verify(mockedNativeCommandsSender.dismissAllModals('dismissAllModals+UNIQUE_ID', deepEqual({}))).called();
    });

    it('returns a promise with the id', async () => {
      when(mockedNativeCommandsSender.dismissAllModals(anyString(), anything())).thenResolve('the id' as any);
      const result = await uut.dismissAllModals();
      expect(result).toEqual('the id');
    });
  });

  describe('push', () => {
    it('resolves with the parsed layout', async () => {
      when(mockedNativeCommandsSender.push(anyString(), anyString(), anything())).thenResolve('the resolved layout' as any);
      const result = await uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('the resolved layout');
    });

    it('parses into correct layout node and sends to native', () => {
      uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
      verify(mockedNativeCommandsSender.push('push+UNIQUE_ID', 'theComponentId', deepEqual({
        type: 'Component',
        id: 'Component+UNIQUE_ID',
        data: {
          name: 'com.example.MyScreen',
          options: {},
          passProps: undefined
        },
        children: []
      }))).called();
    });

    it('calls component generator once', async () => {
      const generator = jest.fn(() => {
        return {};
      });
      store.setComponentClassForName('theComponentName', generator);
      await uut.push('theComponentId', { component: { name: 'theComponentName' } });
      expect(generator).toHaveBeenCalledTimes(1);
    });
  });

  describe('pop', () => {
    it('pops a component, passing componentId', () => {
      uut.pop('theComponentId', {});
      verify(mockedNativeCommandsSender.pop('pop+UNIQUE_ID', 'theComponentId', deepEqual({}))).called();
    });
    it('pops a component, passing componentId and options', () => {
      const options = {
        customTransition: {
          animations: [
            { type: 'sharedElement', fromId: 'title2', toId: 'title1', startDelay: 0, springVelocity: 0.2, duration: 0.5 }
          ],
          duration: 0.8
        }
      };
      uut.pop('theComponentId', options as any);
      verify(mockedNativeCommandsSender.pop('pop+UNIQUE_ID', 'theComponentId', options)).called();
    });

    it('pop returns a promise that resolves to componentId', async () => {
      when(mockedNativeCommandsSender.pop(anyString(), anyString(), anything())).thenResolve('theComponentId' as any);
      const result = await uut.pop('theComponentId', {});
      expect(result).toEqual('theComponentId');
    });
  });

  describe('popTo', () => {
    it('pops all components until the passed Id is top', () => {
      uut.popTo('theComponentId', {});
      verify(mockedNativeCommandsSender.popTo('popTo+UNIQUE_ID', 'theComponentId', deepEqual({}))).called();
    });

    it('returns a promise that resolves to targetId', async () => {
      when(mockedNativeCommandsSender.popTo(anyString(), anyString(), anything())).thenResolve('theComponentId' as any);
      const result = await uut.popTo('theComponentId');
      expect(result).toEqual('theComponentId');
    });
  });

  describe('popToRoot', () => {
    it('pops all components to root', () => {
      uut.popToRoot('theComponentId', {});
      verify(mockedNativeCommandsSender.popToRoot('popToRoot+UNIQUE_ID', 'theComponentId', deepEqual({}))).called();
    });

    it('returns a promise that resolves to targetId', async () => {
      when(mockedNativeCommandsSender.popToRoot(anyString(), anyString(), anything())).thenResolve('theComponentId' as any);
      const result = await uut.popToRoot('theComponentId');
      expect(result).toEqual('theComponentId');
    });
  });

  describe('setStackRoot', () => {
    it('parses into correct layout node and sends to native', () => {
      uut.setStackRoot('theComponentId', { component: { name: 'com.example.MyScreen' } });
      verify(mockedNativeCommandsSender.setStackRoot('setStackRoot+UNIQUE_ID', 'theComponentId', deepEqual({
        type: 'Component',
        id: 'Component+UNIQUE_ID',
        data: {
          name: 'com.example.MyScreen',
          options: {},
          passProps: undefined
        },
        children: []
      }))).called();
    });
  });

  describe('showOverlay', () => {
    it('sends command to native after parsing into a correct layout tree', () => {
      uut.showOverlay({
        component: {
          name: 'com.example.MyScreen'
        }
      });
      verify(mockedNativeCommandsSender.showOverlay('showOverlay+UNIQUE_ID', deepEqual({
        type: 'Component',
        id: 'Component+UNIQUE_ID',
        data: {
          name: 'com.example.MyScreen',
          options: {},
          passProps: undefined
        },
        children: []
      }))).called();
    });

    it('resolves with the component id', async () => {
      when(mockedNativeCommandsSender.showOverlay(anyString(), anything())).thenResolve('Component1' as any);
      const result = await uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('Component1');
    });
  });

  describe('dismissOverlay', () => {
    it('check promise returns true', async () => {
      when(mockedNativeCommandsSender.dismissOverlay(anyString(), anyString())).thenResolve(true as any);
      const result = await uut.dismissOverlay('Component1');
      verify(mockedNativeCommandsSender.dismissOverlay(anyString(), anyString())).called();
      expect(result).toEqual(true);
    });

    it('send command to native with componentId', () => {
      uut.dismissOverlay('Component1');
      verify(mockedNativeCommandsSender.dismissOverlay('dismissOverlay+UNIQUE_ID', 'Component1')).called();
    });
  });

  describe('notifies commandsObserver', () => {
    let cb: any;

    beforeEach(() => {
      cb = jest.fn();
      const mockParser = { parse: () => 'parsed' };
      const mockCrawler = { crawl: (x: any) => x, processOptions: (x: any) => x };
      commandsObserver.register(cb);
      const mockedOptionsProcessor = mock(OptionsProcessor);
      const optionsProcessor = instance(mockedOptionsProcessor);
      uut = new Commands(
        mockedNativeCommandsSender,
        mockParser as any,
        mockCrawler as any,
        commandsObserver,
        new UniqueIdProvider(),
        optionsProcessor
      );
    });

    function getAllMethodsOfUut() {
      const uutFns = Object.getOwnPropertyNames(Commands.prototype);
      const methods = _.filter(uutFns, (fn) => fn !== 'constructor');
      expect(methods.length).toBeGreaterThan(1);
      return methods;
    }

    // function getAllMethodsOfNativeCommandsSender() {
    //   const nativeCommandsSenderFns = _.functions(mockedNativeCommandsSender);
    //   expect(nativeCommandsSenderFns.length).toBeGreaterThan(1);
    //   return nativeCommandsSenderFns;
    // }

    // it('always call last, when nativeCommand fails, dont notify listeners', () => {
    //   // expect(getAllMethodsOfUut().sort()).toEqual(getAllMethodsOfNativeCommandsSender().sort());

    //   // call all commands on uut, all should throw, no commandObservers called
    //   _.forEach(getAllMethodsOfUut(), (m) => {
    //     expect(() => uut[m]()).toThrow();
    //     expect(cb).not.toHaveBeenCalled();
    //   });
    // });

    // it('notify on all commands', () => {
    //   _.forEach(getAllMethodsOfUut(), (m) => {
    //     uut[m]({});
    //   });
    //   expect(cb).toHaveBeenCalledTimes(getAllMethodsOfUut().length);
    // });

    describe('passes correct params', () => {
      const argsForMethodName: Record<string, any[]> = {
        setRoot: [{}],
        setDefaultOptions: [{}],
        mergeOptions: ['id', {}],
        showModal: [{}],
        dismissModal: ['id', {}],
        dismissAllModals: [{}],
        push: ['id', {}],
        pop: ['id', {}],
        popTo: ['id', {}],
        popToRoot: ['id', {}],
        setStackRoot: ['id', {}],
        showOverlay: [{}],
        dismissOverlay: ['id'],
        getLaunchArgs: ['id']
      };
      const paramsForMethodName: Record<string, object> = {
        setRoot: { commandId: 'setRoot+UNIQUE_ID', layout: { root: 'parsed', modals: [], overlays: [] } },
        setDefaultOptions: { options: {} },
        mergeOptions: { componentId: 'id', options: {} },
        showModal: { commandId: 'showModal+UNIQUE_ID', layout: 'parsed' },
        dismissModal: { commandId: 'dismissModal+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
        dismissAllModals: { commandId: 'dismissAllModals+UNIQUE_ID', mergeOptions: {} },
        push: { commandId: 'push+UNIQUE_ID', componentId: 'id', layout: 'parsed' },
        pop: { commandId: 'pop+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
        popTo: { commandId: 'popTo+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
        popToRoot: { commandId: 'popToRoot+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
        setStackRoot: { commandId: 'setStackRoot+UNIQUE_ID', componentId: 'id', layout: 'parsed' },
        showOverlay: { commandId: 'showOverlay+UNIQUE_ID', layout: 'parsed' },
        dismissOverlay: { commandId: 'dismissOverlay+UNIQUE_ID', componentId: 'id' },
        getLaunchArgs: { commandId: 'getLaunchArgs+UNIQUE_ID' },
      };
      _.forEach(getAllMethodsOfUut(), (m) => {
        it(`for ${m}`, () => {
          expect(argsForMethodName).toHaveProperty(m);
          expect(paramsForMethodName).toHaveProperty(m);
          _.invoke(uut, m, ...argsForMethodName[m]);
          expect(cb).toHaveBeenCalledTimes(1);
          expect(cb).toHaveBeenCalledWith(m, paramsForMethodName[m]);
        });
      });
    });
  });
});
