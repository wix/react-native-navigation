const LayoutTypes = require('./LayoutTypes');
const LayoutTreeCrawler = require('./LayoutTreeCrawler');
const Store = require('../components/Store');
const UniqueIdProvider = require('../adapters/UniqueIdProvider.mock');

describe('LayoutTreeCrawler', () => {
  let uut;
  let store;

  beforeEach(() => {
    store = new Store();
    uut = new LayoutTreeCrawler(new UniqueIdProvider(), store);
  });

  it('crawls a layout tree and adds unique id to each node', () => {
    const node = { type: LayoutTypes.Stack, children: [{ type: LayoutTypes.BottomTabs }] };
    uut.crawl(node);
    expect(node.id).toEqual('Stack+UNIQUE_ID');
    expect(node.children[0].id).toEqual('BottomTabs+UNIQUE_ID');
  });

  it('crawls a layout tree and ensures data exists', () => {
    const node = { type: LayoutTypes.Stack, children: [{ type: LayoutTypes.BottomTabs }] };
    uut.crawl(node);
    expect(node.data).toEqual({});
    expect(node.children[0].data).toEqual({});
  });

  it('crawls a layout tree and ensures children exists', () => {
    const node = { type: LayoutTypes.Stack, children: [{ type: LayoutTypes.BottomTabs }] };
    uut.crawl(node);
    expect(node.children[0].children).toEqual([]);
  });

  it('crawls a layout tree and asserts known layout type', () => {
    const node = { type: LayoutTypes.Stack, children: [{ type: 'Bob' }] };
    expect(() => uut.crawl(node)).toThrow(new Error('Unknown layout type Bob'));
  });

  it('saves passProps into store for Component nodes', () => {
    const node = {
      type: LayoutTypes.BottomTabs, children: [
        { type: LayoutTypes.Component, data: { name: 'the name', passProps: { myProp: 123 } } }]
    };
    expect(store.getPropsForComponentId('Component+UNIQUE_ID')).toEqual({});
    uut.crawl(node);
    expect(store.getPropsForComponentId('Component+UNIQUE_ID')).toEqual({ myProp: 123 });
  });

  it('Components: injects options from original component class static property', () => {
    const theStyle = {};
    const MyComponent = class {
      static get options() {
        return theStyle;
      }
    };

    const node = { type: LayoutTypes.Component, data: { name: 'theComponentName' } };
    store.setOriginalComponentClassForName('theComponentName', MyComponent);
    uut.crawl(node);
    expect(node.data.options).toEqual(theStyle);
  });

  it('Components: merges options from component class static property with passed options, favoring passed options', () => {
    const theStyle = {
      bazz: 123,
      inner: {
        foo: 'bar'
      },
      opt: 'exists only in static'
    };
    const MyComponent = class {
      static get options() {
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

    const node = { type: LayoutTypes.Component, data: { name: 'theComponentName', options: passedOptions } };
    store.setOriginalComponentClassForName('theComponentName', MyComponent);

    uut.crawl(node);

    expect(node.data.options).toEqual({
      aaa: 'exists only in passed',
      bazz: 789,
      inner: {
        foo: 'this is overriden'
      },
      opt: 'exists only in static'
    });
  });

  it('Component: deepClones options', () => {
    const theStyle = {};
    const MyComponent = class {
      static get options() {
        return theStyle;
      }
    };

    const node = { type: LayoutTypes.Component, data: { name: 'theComponentName' } };
    store.setOriginalComponentClassForName('theComponentName', MyComponent);
    uut.crawl(node);
    expect(node.data.options).not.toBe(theStyle);
  });

  it('Components: must contain data name', () => {
    const node = { type: LayoutTypes.Component, data: {} };
    expect(() => uut.crawl(node)).toThrow(new Error('Missing component data.name'));
  });

  it('Components: options default obj', () => {
    const MyComponent = class { };

    const node = { type: LayoutTypes.Component, data: { name: 'theComponentName' } };
    store.setOriginalComponentClassForName('theComponentName', MyComponent);
    uut.crawl(node);
    expect(node.data.options).toEqual({});
  });

  describe('navigation options', () => {
    let options;
    let node;

    beforeEach(() => {
      options = {};
      node = { type: LayoutTypes.Component, data: { name: 'theComponentName', options } };
    });

    it('processes colors into numeric AARRGGBB', () => {
      options.someKeyColor = 'red';
      uut.crawl(node);
      expect(node.data.options.someKeyColor).toEqual(0xffff0000);
    });

    it('processes colors into numeric AARRGGBB', () => {
      options.someKeyColor = 'yellow';
      uut.crawl(node);
      expect(node.data.options.someKeyColor).toEqual(0xffffff00);
    });

    it('processes numeric colors', () => {
      options.someKeyColor = '#123456';
      uut.crawl(node);
      expect(node.data.options.someKeyColor).toEqual(0xff123456);
    });

    it('processes numeric colors with rrggbbAA', () => {
      options.someKeyColor = 0x123456ff; // wut
      uut.crawl(node);
      expect(node.data.options.someKeyColor).toEqual(0xff123456);
    });

    it('process colors with rgb functions', () => {
      options.someKeyColor = 'rgb(255, 0, 255)';
      uut.crawl(node);
      expect(node.data.options.someKeyColor).toEqual(0xffff00ff);
    });

    it('process colors with special words', () => {
      options.someKeyColor = 'fuchsia';
      uut.crawl(node);
      expect(node.data.options.someKeyColor).toEqual(0xffff00ff);
    });

    it('process colors with hsla functions', () => {
      options.someKeyColor = 'hsla(360, 100%, 100%, 1.0)';
      uut.crawl(node);
      expect(node.data.options.someKeyColor).toEqual(0xffffffff);
    });

    it('unknown colors return undefined', () => {
      options.someKeyColor = 'wut';
      uut.crawl(node);
      expect(node.data.options.someKeyColor).toEqual(undefined);
    });

    it('any keys ending with Color', () => {
      options.otherKeyColor = 'red';
      options.yetAnotherColor = 'blue';
      options.andAnotherColor = 'rgb(0, 255, 0)';
      uut.crawl(node);
      expect(node.data.options.otherKeyColor).toEqual(0xffff0000);
      expect(node.data.options.yetAnotherColor).toEqual(0xff0000ff);
      expect(node.data.options.andAnotherColor).toEqual(0xff00ff00);
    });

    it('keys ending with Color case sensitive', () => {
      options.otherKey_color = 'red'; // eslint-disable-line camelcase
      uut.crawl(node);
      expect(node.data.options.otherKey_color).toEqual('red');
    });

    it('any nested recursive keys ending with Color', () => {
      options.innerObj = { theKeyColor: 'red' };
      options.innerObj.innerMostObj = { anotherColor: 'yellow' };
      uut.crawl(node);
      expect(node.data.options.innerObj.theKeyColor).toEqual(0xffff0000);
      expect(node.data.options.innerObj.innerMostObj.anotherColor).toEqual(0xffffff00);
    });
  });
});
