import * as React from 'react';

import { Store } from '../components/Store';
import { mock, instance, when } from 'ts-mockito';
import { Options } from '../interfaces/Options';
import { OptionsCrawler } from './OptionsCrawler';
import { Layout } from 'react-native-navigation/interfaces/Layout';

describe('OptionsCrawler', () => {
  let uut: OptionsCrawler;
  let mockedStore: Store;

  beforeEach(() => {
    mockedStore = mock(Store);
    uut = new OptionsCrawler(instance(mockedStore));
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
