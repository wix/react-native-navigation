import * as _ from 'lodash';
import { LayoutTreeParser } from './LayoutTreeParser';
import { LayoutTreeCrawler } from './LayoutTreeCrawler';
import { Store } from '../components/Store';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider.mock';
import { NativeCommandsSender } from '../adapters/NativeCommandsSender.mock';
import { Commands } from './Commands';
import { CommandsObserver } from '../events/CommandsObserver';

describe('Commands', () => {
  let uut: Commands;
  let mockCommandsSender;
  let store;
  let commandsObserver: CommandsObserver;

  beforeEach(() => {
    mockCommandsSender = new NativeCommandsSender();
    store = new Store();
    commandsObserver = new CommandsObserver();

    uut = new Commands(
      mockCommandsSender,
      new LayoutTreeParser(),
      new LayoutTreeCrawler(new UniqueIdProvider(), store),
      commandsObserver,
      new UniqueIdProvider()
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
      expect(mockCommandsSender.setRoot).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.setRoot).toHaveBeenCalledWith('setRoot+UNIQUE_ID', {
        root: {
          type: 'Component',
          id: 'Component+UNIQUE_ID',
          children: [],
          data: {
            name: 'com.example.MyScreen',
            options: {}
          }
        },
        modals: [],
        overlays: []
      });
    });

    it('deep clones input to avoid mutation errors', () => {
      const obj = {};
      uut.setRoot({ root: { component: { name: 'bla', inner: obj } } });
      expect(mockCommandsSender.setRoot.mock.calls[0][1].root.data.inner).not.toBe(obj);
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
      mockCommandsSender.setRoot.mockReturnValue(Promise.resolve('the resolved layout'));
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
      expect(mockCommandsSender.setRoot).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.setRoot).toHaveBeenCalledWith('setRoot+UNIQUE_ID', {
        root:
          {
            type: 'Component',
            id: 'Component+UNIQUE_ID',
            children: [],
            data: {
              name: 'com.example.MyScreen',
              options: {}
            }
          },
        modals: [
          {
            type: 'Component',
            id: 'Component+UNIQUE_ID',
            children: [],
            data: {
              name: 'com.example.MyModal',
              options: {}
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
              options: {}
            }
          }
        ]
      });
    });
  });

  describe('mergeOptions', () => {
    it('deep clones input to avoid mutation errors', () => {
      const obj = { title: 'test' };
      uut.mergeOptions('theComponentId', obj);
      expect(mockCommandsSender.mergeOptions.mock.calls[0][1]).not.toBe(obj);
    });

    it('passes options for component', () => {
      uut.mergeOptions('theComponentId', { title: '1' });
      expect(mockCommandsSender.mergeOptions).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.mergeOptions).toHaveBeenCalledWith('theComponentId', { title: '1' });
    });
  });

  describe('setDefaultOptions', () => {
    it('deep clones input to avoid mutation errors', () => {
      const obj = { title: 'test' };
      uut.setDefaultOptions(obj);
      expect(mockCommandsSender.setDefaultOptions.mock.calls[0][0]).not.toBe(obj);
    });
  });

  describe('showModal', () => {
    it('sends command to native after parsing into a correct layout tree', () => {
      uut.showModal({
        component: {
          name: 'com.example.MyScreen'
        }
      });
      expect(mockCommandsSender.showModal).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.showModal).toHaveBeenCalledWith('showModal+UNIQUE_ID', {
        type: 'Component',
        id: 'Component+UNIQUE_ID',
        data: {
          name: 'com.example.MyScreen',
          options: {}
        },
        children: []
      });
    });

    it('deep clones input to avoid mutation errors', () => {
      const obj = {};
      uut.showModal({ component: { name: 'name', inner: obj } });
      expect(mockCommandsSender.showModal.mock.calls[0][1].data.inner).not.toBe(obj);
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
      mockCommandsSender.showModal.mockReturnValue(Promise.resolve('the resolved layout'));
      const result = await uut.showModal({ component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('the resolved layout');
    });
  });

  describe('dismissModal', () => {
    it('sends command to native', () => {
      uut.dismissModal('myUniqueId');
      expect(mockCommandsSender.dismissModal).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.dismissModal).toHaveBeenCalledWith('dismissModal+UNIQUE_ID', 'myUniqueId');
    });

    it('returns a promise with the id', async () => {
      mockCommandsSender.dismissModal.mockReturnValue(Promise.resolve('the id'));
      const result = await uut.dismissModal('myUniqueId');
      expect(result).toEqual('the id');
    });
  });

  describe('dismissAllModals', () => {
    it('sends command to native', () => {
      uut.dismissAllModals();
      expect(mockCommandsSender.dismissAllModals).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.dismissAllModals).toHaveBeenCalledWith('dismissAllModals+UNIQUE_ID');
    });

    it('returns a promise with the id', async () => {
      mockCommandsSender.dismissAllModals.mockReturnValue(Promise.resolve('the id'));
      const result = await uut.dismissAllModals();
      expect(result).toEqual('the id');
    });
  });

  describe('push', () => {
    it('deep clones input to avoid mutation errors', () => {
      const obj = {};
      uut.push('theComponentId', { component: { name: 'name', passProps: { foo: obj } } });
      expect(mockCommandsSender.push.mock.calls[0][2].data.passProps.foo).not.toBe(obj);
    });

    it('resolves with the parsed layout', async () => {
      mockCommandsSender.push.mockReturnValue(Promise.resolve('the resolved layout'));
      const result = await uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('the resolved layout');
    });

    it('parses into correct layout node and sends to native', () => {
      uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
      expect(mockCommandsSender.push).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.push).toHaveBeenCalledWith('push+UNIQUE_ID', 'theComponentId', {
        type: 'Component',
        id: 'Component+UNIQUE_ID',
        data: {
          name: 'com.example.MyScreen',
          options: {}
        },
        children: []
      });
    });
  });

  describe('pop', () => {
    it('pops a component, passing componentId', () => {
      uut.pop('theComponentId', {});
      expect(mockCommandsSender.pop).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.pop).toHaveBeenCalledWith('pop+UNIQUE_ID', 'theComponentId', {});
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
      uut.pop('theComponentId', options);
      expect(mockCommandsSender.pop).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.pop).toHaveBeenCalledWith('pop+UNIQUE_ID', 'theComponentId', options);
    });

    it('pop returns a promise that resolves to componentId', async () => {
      mockCommandsSender.pop.mockReturnValue(Promise.resolve('theComponentId'));
      const result = await uut.pop('theComponentId', {});
      expect(result).toEqual('theComponentId');
    });
  });

  describe('popTo', () => {
    it('pops all components until the passed Id is top', () => {
      uut.popTo('theComponentId');
      expect(mockCommandsSender.popTo).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.popTo).toHaveBeenCalledWith('popTo+UNIQUE_ID', 'theComponentId');
    });

    it('returns a promise that resolves to targetId', async () => {
      mockCommandsSender.popTo.mockReturnValue(Promise.resolve('theComponentId'));
      const result = await uut.popTo('theComponentId');
      expect(result).toEqual('theComponentId');
    });
  });

  describe('popToRoot', () => {
    it('pops all components to root', () => {
      uut.popToRoot('theComponentId');
      expect(mockCommandsSender.popToRoot).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.popToRoot).toHaveBeenCalledWith('popToRoot+UNIQUE_ID', 'theComponentId');
    });

    it('returns a promise that resolves to targetId', async () => {
      mockCommandsSender.popToRoot.mockReturnValue(Promise.resolve('theComponentId'));
      const result = await uut.popToRoot('theComponentId');
      expect(result).toEqual('theComponentId');
    });
  });

  describe('setStackRoot', () => {
    it('parses into correct layout node and sends to native', () => {
      uut.setStackRoot('theComponentId', { component: { name: 'com.example.MyScreen' } });
      expect(mockCommandsSender.setStackRoot).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.setStackRoot).toHaveBeenCalledWith('setStackRoot+UNIQUE_ID', 'theComponentId', {
        type: 'Component',
        id: 'Component+UNIQUE_ID',
        data: {
          name: 'com.example.MyScreen',
          options: {}
        },
        children: []
      });
    });
  });

  describe('showOverlay', () => {
    it('sends command to native after parsing into a correct layout tree', () => {
      uut.showOverlay({
        component: {
          name: 'com.example.MyScreen'
        }
      });
      expect(mockCommandsSender.showOverlay).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.showOverlay).toHaveBeenCalledWith('showOverlay+UNIQUE_ID', {
        type: 'Component',
        id: 'Component+UNIQUE_ID',
        data: {
          name: 'com.example.MyScreen',
          options: {}
        },
        children: []
      });
    });

    it('deep clones input to avoid mutation errors', () => {
      const obj = {};
      uut.showOverlay({ component: { name: 'name', inner: obj } });
      expect(mockCommandsSender.showOverlay.mock.calls[0][1].data.inner).not.toBe(obj);
    });

    it('resolves with the component id', async () => {
      mockCommandsSender.showOverlay.mockReturnValue(Promise.resolve('Component1'));
      const result = await uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('Component1');
    });
  });

  describe('dismissOverlay', () => {
    it('check promise returns true', async () => {
      mockCommandsSender.dismissOverlay.mockReturnValue(Promise.resolve(true));
      const result = await uut.dismissOverlay('Component1');
      expect(mockCommandsSender.dismissOverlay).toHaveBeenCalledTimes(1);
      expect(result).toEqual(true);
    });

    it('send command to native with componentId', () => {
      uut.dismissOverlay('Component1');
      expect(mockCommandsSender.dismissOverlay).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.dismissOverlay).toHaveBeenCalledWith('dismissOverlay+UNIQUE_ID', 'Component1');
    });
  });

  describe('notifies commandsObserver', () => {
    let cb;

    beforeEach(() => {
      cb = jest.fn();
      const mockParser = { parse: () => 'parsed' };
      const mockCrawler = { crawl: (x) => x, processOptions: (x) => x };
      commandsObserver.register(cb);
      uut = new Commands(mockCommandsSender, mockParser, mockCrawler, commandsObserver, new UniqueIdProvider());
    });

    function getAllMethodsOfUut() {
      const uutFns = Object.getOwnPropertyNames(Commands.prototype);
      const methods = _.filter(uutFns, (fn) => fn !== 'constructor');
      expect(methods.length).toBeGreaterThan(1);
      return methods;
    }

    function getAllMethodsOfNativeCommandsSender() {
      const nativeCommandsSenderFns = _.functions(mockCommandsSender);
      expect(nativeCommandsSenderFns.length).toBeGreaterThan(1);
      return nativeCommandsSenderFns;
    }

    it('always call last, when nativeCommand fails, dont notify listeners', () => {
      // throw when calling any native commands sender
      _.forEach(getAllMethodsOfNativeCommandsSender(), (fn) => {
        mockCommandsSender[fn].mockImplementation(() => {
          throw new Error(`throwing from mockNativeCommandsSender`);
        });
      });

      expect(getAllMethodsOfUut().sort()).toEqual(getAllMethodsOfNativeCommandsSender().sort());

      // call all commands on uut, all should throw, no commandObservers called
      _.forEach(getAllMethodsOfUut(), (m) => {
        expect(() => uut[m]()).toThrow();
        expect(cb).not.toHaveBeenCalled();
      });
    });

    it('notify on all commands', () => {
      _.forEach(getAllMethodsOfUut(), (m) => {
        uut[m]({});
      });
      expect(cb).toHaveBeenCalledTimes(getAllMethodsOfUut().length);
    });

    describe('passes correct params', () => {
      const argsForMethodName = {
        setRoot: [{}],
        setDefaultOptions: [{}],
        mergeOptions: ['id', {}],
        showModal: [{}],
        dismissModal: ['id'],
        dismissAllModals: [],
        push: ['id', {}],
        pop: ['id', {}],
        popTo: ['id'],
        popToRoot: ['id'],
        setStackRoot: ['id', {}],
        showOverlay: [{}],
        dismissOverlay: ['id'],
      };
      const paramsForMethodName = {
        setRoot: { commandId: 'setRoot+UNIQUE_ID', layout: { root: 'parsed', modals: [], overlays: [] } },
        setDefaultOptions: { options: {} },
        mergeOptions: { componentId: 'id', options: {} },
        showModal: { commandId: 'showModal+UNIQUE_ID', layout: 'parsed' },
        dismissModal: { commandId: 'dismissModal+UNIQUE_ID', componentId: 'id' },
        dismissAllModals: { commandId: 'dismissAllModals+UNIQUE_ID' },
        push: { commandId: 'push+UNIQUE_ID', componentId: 'id', layout: 'parsed' },
        pop: { commandId: 'pop+UNIQUE_ID', componentId: 'id', options: {} },
        popTo: { commandId: 'popTo+UNIQUE_ID', componentId: 'id' },
        popToRoot: { commandId: 'popToRoot+UNIQUE_ID', componentId: 'id' },
        setStackRoot: { commandId: 'setStackRoot+UNIQUE_ID', componentId: 'id', layout: 'parsed' },
        showOverlay: { commandId: 'showOverlay+UNIQUE_ID', layout: 'parsed' },
        dismissOverlay: { commandId: 'dismissOverlay+UNIQUE_ID', componentId: 'id' },
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
