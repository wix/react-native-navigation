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
});
