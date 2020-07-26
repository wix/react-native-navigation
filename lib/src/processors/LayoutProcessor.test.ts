import { LayoutProcessor } from './LayoutProcessor';
import { LayoutProcessorsStore } from '../processors/LayoutProcessorsStore';
import { CommandName } from '../interfaces/CommandName';

describe('Layout processor', () => {
  let uut: LayoutProcessor;
  let layoutProcessorsStore: LayoutProcessorsStore;
  beforeEach(() => {
    layoutProcessorsStore = new LayoutProcessorsStore();
    uut = new LayoutProcessor(layoutProcessorsStore);
  });

  it('do nothing when store is empty', () => {
    const layout = {
      component: {
        name: 'component',
      },
    };

    uut.process(layout, CommandName.SetRoot);
    expect(layout).toEqual({
      component: {
        name: 'component',
      },
    });
  });

  it('manipulates layout with external processor', () => {
    const layout = {
      component: {
        name: 'component',
      },
    };

    layoutProcessorsStore.addProcessor((_layout, commandName) => {
      return {
        component: {
          name: 'component1',
          passProps: {
            commandName,
          },
        },
      };
    });

    const result = uut.process(layout, CommandName.SetRoot);
    expect(result).toEqual({
      component: {
        name: 'component1',
        passProps: {
          commandName: CommandName.SetRoot,
        },
      },
    });
  });
});
