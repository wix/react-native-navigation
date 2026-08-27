import { LayoutProcessorsStore } from './LayoutProcessorsStore';

describe('Layout processors Store', () => {
  let uut: LayoutProcessorsStore;
  beforeEach(() => {
    uut = new LayoutProcessorsStore();
  });

  it('should register processor to store', () => {
    const processor = (value: any, _commandName: string) => value;
    uut.addProcessor(processor);
    expect(uut.getProcessors()).toEqual([processor]);
  });

  it('should register multiple processors', () => {
    const processor = (value: any, _commandName: string) => value;
    const secondProcessor = (value: any, _commandName: string) => value;
    uut.addProcessor(processor);
    uut.addProcessor(secondProcessor);
    expect(uut.getProcessors()).toEqual([processor, secondProcessor]);
  });

  it('should unregister processor', () => {
    const processor = (value: any, _commandName: string) => value;
    const { remove } = uut.addProcessor(processor);
    expect(uut.getProcessors()).toEqual([processor]);
    remove();
    expect(uut.getProcessors()).toEqual([]);
  });

  it('removes only the selected registration, including duplicate callbacks', () => {
    const first = (value: any) => value;
    const middle = (value: any) => value;
    uut.addProcessor(first);
    const middleSubscription = uut.addProcessor(middle);
    const duplicateSubscription = uut.addProcessor(first);
    duplicateSubscription.remove();
    duplicateSubscription.remove();
    expect(uut.getProcessors()).toEqual([first, middle]);
    uut.addProcessor(first);
    middleSubscription.remove();
    middleSubscription.remove();
    expect(uut.getProcessors()).toEqual([first, first]);
  });
});
