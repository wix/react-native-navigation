import { OptionProcessorsStore } from './OptionProcessorsStore';

describe('Option processors Store', () => {
  let uut: OptionProcessorsStore;
  beforeEach(() => {
    uut = new OptionProcessorsStore();
  });

  it('should register processor to store', () => {
    const processor = (value: any, _commandName: string) => value;
    uut.addProcessor('topBar', processor);
    expect(uut.getProcessors('topBar')).toEqual([processor]);
  });

  it('should register multiple processors with the same object path', () => {
    const processor = (value: any, _commandName: string) => value;
    const secondProcessor = (value: any, _commandName: string) => value;
    uut.addProcessor('topBar', processor);
    uut.addProcessor('topBar', secondProcessor);
    expect(uut.getProcessors('topBar')).toEqual([processor, secondProcessor]);
  });

  it('should unregister processor', () => {
    const processor = (value: any, _commandName: string) => value;
    const { remove } = uut.addProcessor('topBar', processor);
    expect(uut.getProcessors('topBar')).toEqual([processor]);
    remove();
    expect(uut.getProcessors('topBar')).toEqual([]);
  });

  it('removes only the selected registration, including duplicate callbacks', () => {
    const first = (value: any) => value;
    const middle = (value: any) => value;
    uut.addProcessor('topBar', first);
    const middleSubscription = uut.addProcessor('topBar', middle);
    const duplicateSubscription = uut.addProcessor('topBar', first);
    duplicateSubscription.remove();
    duplicateSubscription.remove();
    expect(uut.getProcessors('topBar')).toEqual([first, middle]);
    uut.addProcessor('topBar', first);
    middleSubscription.remove();
    middleSubscription.remove();
    expect(uut.getProcessors('topBar')).toEqual([first, first]);
  });
});
