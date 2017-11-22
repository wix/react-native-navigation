const SimpleLayouts = require('./SimpleLayouts');
const LayoutTreeParser = require('./LayoutTreeParser');
const LayoutTreeCrawler = require('./LayoutTreeCrawler');
const Store = require('../containers/Store');
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
        container: {
          name: 'com.example.MyScreen'
        }
      });
      expect(mockCommandsSender.setRoot).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.setRoot).toHaveBeenCalledWith({
        type: 'ContainerStack',
        id: 'ContainerStack+UNIQUE_ID',
        data: {},
        children: [
          {
            type: 'Container',
            id: 'Container+UNIQUE_ID',
            children: [],
            data: {
              name: 'com.example.MyScreen',
              navigationOptions: {}
            }
          }
        ]
      });
    });

    it('deep clones input to avoid mutation errors', () => {
      const obj = {};
      uut.setRoot({ container: { name: 'bla', inner: obj } });
      expect(mockCommandsSender.setRoot.mock.calls[0][0].children[0].data.inner).not.toBe(obj);
    });

    it('passProps into containers', () => {
      expect(store.getPropsForContainerId('Container+UNIQUE_ID')).toEqual({});
      uut.setRoot(SimpleLayouts.singleScreenWithAditionalParams);
      expect(store.getPropsForContainerId('Container+UNIQUE_ID')).toEqual(SimpleLayouts.passProps);
    });

    it('returns a promise with the resolved layout', async () => {
      mockCommandsSender.setRoot.mockReturnValue(Promise.resolve('the resolved layout'));
      const result = await uut.setRoot({ container: { name: 'com.example.MyScreen' } });
      expect(result).toEqual('the resolved layout');
    });
  });

  describe('setOptions', () => {
    it('deep clones input to avoid mutation errors', () => {
      const obj = { title: 'test' };
      uut.setOptions('theContainerId', obj);
      expect(mockCommandsSender.setOptions.mock.calls[0][1]).not.toBe(obj);
    });

    it('passes options for container', () => {
      uut.setOptions('theContainerId', { title: '1' });
      expect(mockCommandsSender.setOptions).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.setOptions).toHaveBeenCalledWith('theContainerId', { title: '1' });
    });
  });

  describe('showModal', () => {
    it('sends command to native after parsing into a correct layout tree', () => {
      uut.showModal({
        container: {
          name: 'com.example.MyScreen'
        }
      });
      expect(mockCommandsSender.showModal).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.showModal).toHaveBeenCalledWith({
        type: 'ContainerStack',
        id: 'ContainerStack+UNIQUE_ID',
        data: {},
        children: [{
          type: 'Container',
          id: 'Container+UNIQUE_ID',
          data: {
            name: 'com.example.MyScreen',
            navigationOptions: {}
          },
          children: []
        }]
      });
    });

    it('deep clones input to avoid mutation errors', () => {
      const obj = {};
      uut.showModal({ container: { name: 'name', inner: obj } });
      expect(mockCommandsSender.showModal.mock.calls[0][0].data.inner).not.toBe(obj);
    });

    it('passProps into containers', () => {
      expect(store.getPropsForContainerId('Container+UNIQUE_ID')).toEqual({});
      uut.showModal({
        container: {
          name: 'com.example.MyScreen',
          passProps: SimpleLayouts.passProps
        }
      });
      expect(store.getPropsForContainerId('Container+UNIQUE_ID')).toEqual(SimpleLayouts.passProps);
    });

    it('returns a promise with the resolved layout', async () => {
      mockCommandsSender.showModal.mockReturnValue(Promise.resolve('the resolved layout'));
      const result = await uut.showModal({ container: { name: 'com.example.MyScreen' } });
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
      uut.push('theContainerId', { name: 'name', inner: { foo: obj } });
      expect(mockCommandsSender.push.mock.calls[0][1].data.inner.foo).not.toBe(obj);
    });

    it('resolves with the parsed layout', async () => {
      mockCommandsSender.push.mockReturnValue(Promise.resolve('the resolved layout'));
      const result = await uut.push('theContainerId', { name: 'com.example.MyScreen' });
      expect(result).toEqual('the resolved layout');
    });

    it('parses into correct layout node and sends to native', () => {
      uut.push('theContainerId', { name: 'com.example.MyScreen' });
      expect(mockCommandsSender.push).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.push).toHaveBeenCalledWith('theContainerId', {
        type: 'Container',
        id: 'Container+UNIQUE_ID',
        data: {
          name: 'com.example.MyScreen',
          navigationOptions: {}
        },
        children: []
      });
    });
  });

  describe('pop', () => {
    it('pops a container, passing containerId', () => {
      uut.pop('theContainerId');
      expect(mockCommandsSender.pop).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.pop).toHaveBeenCalledWith('theContainerId');
    });

    it('pop returns a promise that resolves to containerId', async () => {
      mockCommandsSender.pop.mockReturnValue(Promise.resolve('theContainerId'));
      const result = await uut.pop('theContainerId');
      expect(result).toEqual('theContainerId');
    });
  });

  describe('popTo', () => {
    it('pops all containers until the passed Id is top', () => {
      uut.popTo('theContainerId');
      expect(mockCommandsSender.popTo).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.popTo).toHaveBeenCalledWith('theContainerId');
    });

    it('returns a promise that resolves to targetId', async () => {
      mockCommandsSender.popTo.mockReturnValue(Promise.resolve('theContainerId'));
      const result = await uut.popTo('theContainerId');
      expect(result).toEqual('theContainerId');
    });
  });

  describe('popToRoot', () => {
    it('pops all containers to root', () => {
      uut.popToRoot('theContainerId');
      expect(mockCommandsSender.popToRoot).toHaveBeenCalledTimes(1);
      expect(mockCommandsSender.popToRoot).toHaveBeenCalledWith('theContainerId');
    });

    it('returns a promise that resolves to targetId', async () => {
      mockCommandsSender.popToRoot.mockReturnValue(Promise.resolve('theContainerId'));
      const result = await uut.popToRoot('theContainerId');
      expect(result).toEqual('theContainerId');
    });
  });

  describe('showOverlay', () => {
    it('deep clones input to avoid mutation errors', () => {
      const obj = { title: 'test' };
      uut.showOverlay('alert', obj);
      expect(mockCommandsSender.showOverlay.mock.calls[0][1]).not.toBe(obj);
    });
  });
});
