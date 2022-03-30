import { LayoutType } from './LayoutType';
import { LayoutTreeCrawler } from './LayoutTreeCrawler';
import { Store } from '../components/Store';
import { mock, instance, verify, deepEqual } from 'ts-mockito';
import { OptionsProcessor } from './OptionsProcessor';
import { CommandName } from '../interfaces/CommandName';

describe('LayoutTreeCrawler', () => {
  let uut: LayoutTreeCrawler;
  let mockedStore: Store;
  let mockedOptionsProcessor: OptionsProcessor;
  beforeEach(() => {
    mockedStore = mock(Store);
    mockedOptionsProcessor = mock(OptionsProcessor);

    uut = new LayoutTreeCrawler(instance(mockedStore), instance(mockedOptionsProcessor));
  });

  it('saves passProps into store for Component nodes', () => {
    const node = {
      id: 'testId',
      type: LayoutType.BottomTabs,
      children: [
        {
          id: 'testId',
          type: LayoutType.Component,
          data: { name: 'the name', passProps: { myProp: 123 } },
          children: [],
        },
      ],
      data: {},
    };
    uut.crawl(node, CommandName.SetRoot);
    verify(mockedStore.setPendingProps('testId', deepEqual({ myProp: 123 }))).called();
  });

  it('Components: must contain data name', () => {
    const node = { type: LayoutType.Component, data: {}, children: [], id: 'testId' };
    expect(() => uut.crawl(node, CommandName.SetRoot)).toThrowError('Missing component data.name');
  });

  it('Components: omits passProps after processing so they are not passed over the bridge', () => {
    const node = {
      id: 'testId',
      type: LayoutType.Component,
      data: {
        name: 'compName',
        passProps: { someProp: 'here' },
      },
      children: [],
    };
    uut.crawl(node, CommandName.SetRoot);
    expect(node.data.passProps).toBeUndefined();
  });
});
