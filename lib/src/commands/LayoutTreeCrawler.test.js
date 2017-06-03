import LayoutTypes from './LayoutTypes';
import LayoutTreeCrawler from './LayoutTreeCrawler';
import Store from '../containers/Store';
import UniqueIdProvider from '../adapters/UniqueIdProvider.mock';

describe('LayoutTreeCrawler', () => {
  let uut;
  let store;

  beforeEach(() => {
    store = new Store();
    uut = new LayoutTreeCrawler(new UniqueIdProvider(), store);
  });

  it('crawls a layout tree and adds unique id to each node', () => {
    const node = { type: LayoutTypes.ContainerStack, children: [{ type: LayoutTypes.BottomTabs }] };
    uut.crawl(node);
    expect(node.id).toEqual('ContainerStack+UNIQUE_ID');
    expect(node.children[0].id).toEqual('BottomTabs+UNIQUE_ID');
  });

  it('crawls a layout tree and ensures data exists', () => {
    const node = { type: LayoutTypes.ContainerStack, children: [{ type: LayoutTypes.BottomTabs }] };
    uut.crawl(node);
    expect(node.data).toEqual({});
    expect(node.children[0].data).toEqual({});
  });

  it('crawls a layout tree and ensures children exists', () => {
    const node = { type: LayoutTypes.ContainerStack, children: [{ type: LayoutTypes.BottomTabs }] };
    uut.crawl(node);
    expect(node.children[0].children).toEqual([]);
  });

  it('crawls a layout tree and asserts known layout type', () => {
    const node = { type: LayoutTypes.ContainerStack, children: [{ type: 'Bob' }] };
    expect(() => uut.crawl(node)).toThrow(new Error('Unknown layout type Bob'));
  });

  it('saves passProps into store for Container nodes', () => {
    const node = {
      type: LayoutTypes.BottomTabs, children: [
        { type: LayoutTypes.Container, data: { name: 'the name', passProps: { myProp: 123 } } }]
    };
    expect(store.getPropsForContainerId('Container+UNIQUE_ID')).toEqual({});
    uut.crawl(node);
    expect(store.getPropsForContainerId('Container+UNIQUE_ID')).toEqual({ myProp: 123 });
  });

  it('Containers: injects navigationOptions from original container class static property', () => {
    const theStyle = {};
    const MyContainer = class {
      static navigationOptions = theStyle;
    };

    const node = { type: LayoutTypes.Container, data: { name: 'theContainerName' } };
    store.setOriginalContainerClassForName('theContainerName', MyContainer);
    uut.crawl(node);
    expect(node.data.navigationOptions).toEqual(theStyle);
  });

  it('Containers: deepClones navigationOptions', () => {
    const theStyle = {};
    const MyContainer = class {
      static navigationOptions = theStyle;
    };

    const node = { type: LayoutTypes.Container, data: { name: 'theContainerName' } };
    store.setOriginalContainerClassForName('theContainerName', MyContainer);
    uut.crawl(node);
    expect(node.data.navigationOptions).not.toBe(theStyle);
  });

  it('Containers: must contain data name', () => {
    const node = { type: LayoutTypes.Container, data: {} };
    expect(() => uut.crawl(node)).toThrow(new Error('Missing container data.name'));
  });

  it('Containers: navigationOptions default obj', () => {
    const MyContainer = class { };

    const node = { type: LayoutTypes.Container, data: { name: 'theContainerName' } };
    store.setOriginalContainerClassForName('theContainerName', MyContainer);
    uut.crawl(node);
    expect(node.data.navigationOptions).toEqual({});
  });
});
