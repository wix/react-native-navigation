const LayoutTreeParser = require('./LayoutTreeParser');
const LayoutTreeCrawler = require('./LayoutTreeCrawler');
const Store = require('../components/Store');
const UniqueIdProvider = require('../adapters/UniqueIdProvider.mock');
const NativeCommandsSender = require('../adapters/NativeCommandsSender.mock');
const Commands = require('./Commands');

describe('Commands', () => {
  let uut;
  let mockCommandsSender;
  let store;

  beforeEach(() => {
    mockCommandsSender = new NativeCommandsSender();
    store = new Store();

    uut = new Commands(mockCommandsSender, new LayoutTreeParser(), new LayoutTreeCrawler(new UniqueIdProvider(), store));
  });

  describe('setRoot', () => {
    it('sends setRoot to native after parsing into a correct layout tree', () => {
      uut.setRoot({
        component: {
          name: 'com.example.MyScreen'
        }
      });
      expect(mockCommandsSender.setRoot).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.setRoot).toHaveBeenCalledWith({
        type: 'Component',
        id: 'Component+UNIQUE_ID',
        children: [],
        data: {
          name: 'com.example.MyScreen',
          options: {}
        }
      });
    });

    it('deep clones input to avoid mutation errors', () => {
      const obj = {};
      uut.setRoot({ component: { name: 'bla', inner: obj } });
      expect(mockCommandsSender.setRoot.mock.calls[0][0].data.inner).not.toBe(obj);
    });

    it('passProps into components', () => {
      const passProps = {
        fn: () => 'Hello'
      };
      expect(store.getPropsForComponentId('Component+UNIQUE_ID')).toEqual({});
      uut.setRoot({ component: { name: 'asd', passProps } });
      expect(store.getPropsForComponentId('Component+UNIQUE_ID')).toEqual(passProps);
      expect(store.getPropsForComponentId('Component+UNIQUE_ID').fn()).toEqual('Hello');
    });

    it('returns a promise with the resolved layout', async () => {
      mockCommandsSender.setRoot.mockReturnValue(Promise.resolve('the resolved layout'));
      const result = await uut.setRoot({ component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('the resolved layout');
    });
  });

  describe('setOptions', () => {
    it('deep clones input to avoid mutation errors', () => {
      const obj = { title: 'test' };
      uut.setOptions('theComponentId', obj);
      expect(mockCommandsSender.setOptions.mock.calls[0][1]).not.toBe(obj);
    });

    it('passes options for component', () => {
      uut.setOptions('theComponentId', { title: '1' });
      expect(mockCommandsSender.setOptions).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.setOptions).toHaveBeenCalledWith('theComponentId', { title: '1' });
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
      expect(mockCommandsSender.showModal).toHaveBeenCalledWith({
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
      expect(mockCommandsSender.showModal.mock.calls[0][0].data.inner).not.toBe(obj);
    });

    it('passProps into components', () => {
      const passProps = {
        fn: () => 'hello'
      };
      expect(store.getPropsForComponentId('Component+UNIQUE_ID')).toEqual({});
      uut.showModal({
        component: {
          name: 'com.example.MyScreen',
          passProps
        }
      });
      expect(store.getPropsForComponentId('Component+UNIQUE_ID')).toEqual(passProps);
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
      expect(mockCommandsSender.dismissModal).toHaveBeenCalledWith('myUniqueId');
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
      expect(mockCommandsSender.dismissAllModals).toHaveBeenCalledWith();
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
      expect(mockCommandsSender.push.mock.calls[0][1].data.passProps.foo).not.toBe(obj);
    });

    it('resolves with the parsed layout', async () => {
      mockCommandsSender.push.mockReturnValue(Promise.resolve('the resolved layout'));
      const result = await uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('the resolved layout');
    });

    it('parses into correct layout node and sends to native', () => {
      uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
      expect(mockCommandsSender.push).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.push).toHaveBeenCalledWith('theComponentId', {
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
      uut.pop('theComponentId');
      expect(mockCommandsSender.pop).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.pop).toHaveBeenCalledWith('theComponentId', undefined);
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
      expect(mockCommandsSender.pop).toHaveBeenCalledWith('theComponentId', options);
    });

    it('pop returns a promise that resolves to componentId', async () => {
      mockCommandsSender.pop.mockReturnValue(Promise.resolve('theComponentId'));
      const result = await uut.pop('theComponentId');
      expect(result).toEqual('theComponentId');
    });
  });

  describe('popTo', () => {
    it('pops all components until the passed Id is top', () => {
      uut.popTo('theComponentId');
      expect(mockCommandsSender.popTo).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.popTo).toHaveBeenCalledWith('theComponentId');
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
      expect(mockCommandsSender.popToRoot).toHaveBeenCalledWith('theComponentId');
    });

    it('returns a promise that resolves to targetId', async () => {
      mockCommandsSender.popToRoot.mockReturnValue(Promise.resolve('theComponentId'));
      const result = await uut.popToRoot('theComponentId');
      expect(result).toEqual('theComponentId');
    });
  });

  xdescribe('showOverlay', () => {
    it('deep clones input to avoid mutation errors', () => {
      const obj = { title: 'test' };
      uut.showOverlay('alert', obj);
      expect(mockCommandsSender.showOverlay.mock.calls[0][1]).not.toBe(obj);
    });

    it('resolves with the parsed layout', async () => {
      mockCommandsSender.showOverlay.mockReturnValue(Promise.resolve('result'));
      const result = await uut.showOverlay('custom', 'com.example.MyScreen');
      expect(result).toEqual('result');
    });
  });

  describe('dismissOverlay', () => {
    it('check promis returns true', () => {
      uut.dismissOverlay();
      expect(mockCommandsSender.dismissOverlay).toHaveBeenCalledTimes(1);
    });
  });
});
