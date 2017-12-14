const LayoutTypes = require('./LayoutTypes');
const LayoutTreeCrawler = require('./LayoutTreeCrawler');
const Store = require('../containers/Store');
const UniqueIdProvider = require('../adapters/UniqueIdProvider.mock');

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
      static get navigationOptions() {
        return theStyle;
      }
    };

    const node = { type: LayoutTypes.Container, data: { name: 'theContainerName' } };
    store.setOriginalContainerClassForName('theContainerName', MyContainer);
    uut.crawl(node);
    expect(node.data.navigationOptions).toEqual(theStyle);
  });

  it('Containers: merges navigationOptions from container class static property with passed options, favoring passed options', () => {
    const theStyle = {
      bazz: 123,
      inner: {
        foo: 'bar'
      },
      opt: 'exists only in static'
    };
    const MyContainer = class {
      static get navigationOptions() {
        return theStyle;
      }
    };

    const passedOptions = {
      aaa: 'exists only in passed',
      bazz: 789,
      inner: {
        foo: 'this is overriden'
      }
    };

    const node = { type: LayoutTypes.Container, data: { name: 'theContainerName', navigationOptions: passedOptions } };
    store.setOriginalContainerClassForName('theContainerName', MyContainer);

    uut.crawl(node);

    expect(node.data.navigationOptions).toEqual({
      aaa: 'exists only in passed',
      bazz: 789,
      inner: {
        foo: 'this is overriden'
      },
      opt: 'exists only in static'
    });
  });

  it('Containers: deepClones navigationOptions', () => {
    const theStyle = {};
    const MyContainer = class {
      static get navigationOptions() {
        return theStyle;
      }
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

  describe('navigation options', () => {
    let navigationOptions;
    let node;

    beforeEach(() => {
      navigationOptions = {};
      node = { type: LayoutTypes.Container, data: { name: 'theContainerName', navigationOptions } };
    });

    it('processes colors into numeric AARRGGBB', () => {
      navigationOptions.someKeyColor = 'red';
      uut.crawl(node);
      expect(node.data.navigationOptions.someKeyColor).toEqual(0xffff0000);
    });

    it('processes colors into numeric AARRGGBB', () => {
      navigationOptions.someKeyColor = 'yellow';
      uut.crawl(node);
      expect(node.data.navigationOptions.someKeyColor).toEqual(0xffffff00);
    });

    it('processes numeric colors', () => {
      navigationOptions.someKeyColor = '#123456';
      uut.crawl(node);
      expect(node.data.navigationOptions.someKeyColor).toEqual(0xff123456);
    });

    it('processes numeric colors with rrggbbAA', () => {
      navigationOptions.someKeyColor = 0x123456ff; // wut
      uut.crawl(node);
      expect(node.data.navigationOptions.someKeyColor).toEqual(0xff123456);
    });

    it('process colors with rgb functions', () => {
      navigationOptions.someKeyColor = 'rgb(255, 0, 255)';
      uut.crawl(node);
      expect(node.data.navigationOptions.someKeyColor).toEqual(0xffff00ff);
    });

    it('process colors with special words', () => {
      navigationOptions.someKeyColor = 'fuchsia';
      uut.crawl(node);
      expect(node.data.navigationOptions.someKeyColor).toEqual(0xffff00ff);
    });

    it('process colors with hsla functions', () => {
      navigationOptions.someKeyColor = 'hsla(360, 100%, 100%, 1.0)';
      uut.crawl(node);
      expect(node.data.navigationOptions.someKeyColor).toEqual(0xffffffff);
    });

    it('unknown colors return undefined', () => {
      navigationOptions.someKeyColor = 'wut';
      uut.crawl(node);
      expect(node.data.navigationOptions.someKeyColor).toEqual(undefined);
    });

    it('any keys ending with Color', () => {
      navigationOptions.otherKeyColor = 'red';
      navigationOptions.yetAnotherColor = 'blue';
      navigationOptions.andAnotherColor = 'rgb(0, 255, 0)';
      uut.crawl(node);
      expect(node.data.navigationOptions.otherKeyColor).toEqual(0xffff0000);
      expect(node.data.navigationOptions.yetAnotherColor).toEqual(0xff0000ff);
      expect(node.data.navigationOptions.andAnotherColor).toEqual(0xff00ff00);
    });

    it('keys ending with Color case sensitive', () => {
      navigationOptions.otherKey_color = 'red'; // eslint-disable-line camelcase
      uut.crawl(node);
      expect(node.data.navigationOptions.otherKey_color).toEqual('red');
    });

    it('any nested recursive keys ending with Color', () => {
      navigationOptions.innerObj = { theKeyColor: 'red' };
      navigationOptions.innerObj.innerMostObj = { anotherColor: 'yellow' };
      uut.crawl(node);
      expect(node.data.navigationOptions.innerObj.theKeyColor).toEqual(0xffff0000);
      expect(node.data.navigationOptions.innerObj.innerMostObj.anotherColor).toEqual(0xffffff00);
    });
  });
});
