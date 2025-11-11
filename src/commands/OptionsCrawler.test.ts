import * as React from 'react';

import { Store } from '../components/Store';
import { mock, instance, when, anything } from 'ts-mockito';
import { Options } from '../interfaces/Options';
import { OptionsCrawler } from './OptionsCrawler';
import { Layout } from '../interfaces/Layout';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';

describe('OptionsCrawler', () => {
  let uut: OptionsCrawler;
  let mockedStore: Store;
  let mockedUniqueIdProvider: UniqueIdProvider;

  beforeEach(() => {
    mockedStore = mock(Store);
    mockedUniqueIdProvider = mock(UniqueIdProvider);
    when(mockedUniqueIdProvider.generate(anything())).thenCall((prefix) => `${prefix}+UNIQUE_ID`);
    const uniqueIdProvider = instance(mockedUniqueIdProvider);
    uut = new OptionsCrawler(instance(mockedStore), uniqueIdProvider);
  });

  it('Components: injects options object', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options = { popGesture: true };
        }
    );
    const layout: Layout = {
      component: {
        id: 'testId',
        name: 'theComponentName',
      },
    };

    uut.crawl(layout);
    expect(layout.component!.options).toEqual({ popGesture: true });
  });

  it('Components: injects options from original component class static property', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(): Options {
            return { popGesture: true };
          }
        }
    );
    const layout: Layout = {
      component: {
        id: 'testId',
        name: 'theComponentName',
      },
    };

    uut.crawl(layout);
    expect(layout.component!.options).toEqual({ popGesture: true });
  });

  it('ExternalComponent: does nothing as there is no React component for external component', () => {
    const layout: Layout = {
      externalComponent: {
        id: 'testId',
        name: 'theComponentName',
      },
    };

    uut.crawl(layout);
    expect(layout.externalComponent!.options).toEqual(undefined);
  });

  it('ExternalComponent: merge options with passed options', () => {
    const layout: Layout = {
      externalComponent: {
        id: 'testId',
        name: 'theComponentName',
        options: {
          popGesture: false,
        },
      },
    };

    uut.crawl(layout);
    expect(layout.externalComponent!.options).toEqual({ popGesture: false });
  });

  it('Stack: injects options from original component class static property', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(): Options {
            return { popGesture: true };
          }
        }
    );
    const layout: Layout = {
      stack: {
        children: [
          {
            component: {
              id: 'testId',
              name: 'theComponentName',
            },
          },
        ],
      },
    };

    uut.crawl(layout);
    expect(layout.stack!.children![0].component!.options).toEqual({ popGesture: true });
  });

  it('SideMenu: injects options from original component class static property', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(): Options {
            return { popGesture: true };
          }
        }
    );
    const layout: Layout = {
      sideMenu: {
        left: {
          component: {
            id: 'testId',
            name: 'theComponentName',
          },
        },
        center: {
          component: {
            id: 'testId',
            name: 'theComponentName',
          },
        },
        right: {
          component: {
            id: 'testId',
            name: 'theComponentName',
          },
        },
      },
    };

    uut.crawl(layout);
    expect(layout.sideMenu!.center!.component!.options).toEqual({ popGesture: true });
    expect(layout.sideMenu!.left!.component!.options).toEqual({ popGesture: true });
    expect(layout.sideMenu!.right!.component!.options).toEqual({ popGesture: true });
  });

  it('SplitView: injects options from original component class static property', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(): Options {
            return { popGesture: true };
          }
        }
    );
    const layout: Layout = {
      splitView: {
        master: {
          component: {
            id: 'testId',
            name: 'theComponentName',
          },
        },
        detail: {
          component: {
            id: 'testId',
            name: 'theComponentName',
          },
        },
      },
    };

    uut.crawl(layout);
    expect(layout.splitView!.master!.component!.options).toEqual({ popGesture: true });
    expect(layout.splitView!.detail!.component!.options).toEqual({ popGesture: true });
  });

  it('BottomTabs: injects options from original component class static property', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(): Options {
            return { popGesture: true };
          }
        }
    );
    const layout: Layout = {
      bottomTabs: {
        children: [
          {
            component: {
              id: 'testId',
              name: 'theComponentName',
            },
          },
          {
            component: {
              id: 'testId',
              name: 'theComponentName',
            },
          },
        ],
      },
    };

    uut.crawl(layout);
    expect(layout.bottomTabs!.children![0].component!.options).toEqual({ popGesture: true });
    expect(layout.bottomTabs!.children![1].component!.options).toEqual({ popGesture: true });
  });

  it('TopTabs: injects options from original component class static property', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(): Options {
            return { popGesture: true };
          }
        }
    );
    const layout: Layout = {
      topTabs: {
        children: [
          {
            component: {
              id: 'testId',
              name: 'theComponentName',
            },
          },
          {
            component: {
              id: 'testId',
              name: 'theComponentName',
            },
          },
        ],
      },
    };

    uut.crawl(layout);
    expect(layout.topTabs!.children![0].component!.options).toEqual({ popGesture: true });
    expect(layout.topTabs!.children![1].component!.options).toEqual({ popGesture: true });
  });

  it('Components: merges options from component class static property with passed options, favoring passed options', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(): Options {
            return {
              topBar: {
                title: { text: 'this gets overriden' },
                subtitle: { text: 'exists only in static' },
              },
            };
          }
        }
    );

    const node = {
      component: {
        id: 'testId',
        name: 'theComponentName',
        options: {
          topBar: {
            title: {
              text: 'exists only in passed',
            },
          },
        },
      },
    };

    uut.crawl(node);

    expect(node.component.options).toEqual({
      topBar: {
        title: {
          text: 'exists only in passed',
        },
        subtitle: {
          text: 'exists only in static',
        },
      },
    });
  });

  it('Components: options default obj', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () => class extends React.Component {}
    );

    const node = {
      component: { name: 'theComponentName', options: {}, id: 'testId' },
      children: [],
    };
    uut.crawl(node);
    expect(node.component.options).toEqual({});
  });

  it('Components: should generate component id', () => {
    let componentIdInProps: String = '';
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(props: any) {
            componentIdInProps = props.componentId;
          }
        }
    );

    const node = {
      component: { name: 'theComponentName', options: {}, id: undefined },
      children: [],
    };
    uut.crawl(node);
    expect(componentIdInProps).toEqual('Component+UNIQUE_ID');
  });

  it('componentId is included in props passed to options generator', () => {
    let componentIdInProps: String = '';

    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(props: any) {
            componentIdInProps = props.componentId;
            return {};
          }
        }
    );
    const node = {
      component: {
        id: 'testId',
        name: 'theComponentName',
        passProps: { someProp: 'here' },
      },
    };
    uut.crawl(node);
    expect(componentIdInProps).toEqual('testId');
  });

  it('componentId does not override componentId in passProps', () => {
    let componentIdInProps: String = '';

    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(props: any) {
            componentIdInProps = props.componentId;
            return {};
          }
        }
    );
    const node = {
      component: {
        id: 'testId',
        name: 'theComponentName',
        passProps: {
          someProp: 'here',
          componentId: 'compIdFromPassProps',
        },
      },
    };
    uut.crawl(node);
    expect(componentIdInProps).toEqual('compIdFromPassProps');
  });
});
