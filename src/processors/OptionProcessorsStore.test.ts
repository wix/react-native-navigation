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

  it.each(['toString', '__proto__'])(
    'should register processors for the %s object path',
    (optionPath) => {
      const processor = (value: any, _commandName: string) => value;
      uut.addProcessor(optionPath, processor);
      expect(uut.getProcessors(optionPath)).toEqual([processor]);
    }
  );

  it('should unregister processor', () => {
    const processor = (value: any, _commandName: string) => value;
    const { remove } = uut.addProcessor('topBar', processor);
    expect(uut.getProcessors('topBar')).toEqual([processor]);
    remove();
    expect(uut.getProcessors('topBar')).toEqual([]);
  });

  it('should unregister only the subscribed processor', () => {
    const firstProcessor = (value: any, _commandName: string) => value;
    const secondProcessor = (value: any, _commandName: string) => value;
    const thirdProcessor = (value: any, _commandName: string) => value;
    uut.addProcessor('topBar', firstProcessor);
    const { remove } = uut.addProcessor('topBar', secondProcessor);
    uut.addProcessor('topBar', thirdProcessor);

    remove();

    expect(uut.getProcessors('topBar')).toEqual([firstProcessor, thirdProcessor]);
  });

  it('should ignore repeated subscription removal', () => {
    const processor = (value: any, _commandName: string) => value;
    const firstSubscription = uut.addProcessor('topBar', processor);
    const secondSubscription = uut.addProcessor('topBar', processor);

    firstSubscription.remove();
    firstSubscription.remove();

    expect(uut.getProcessors('topBar')).toEqual([processor]);

    secondSubscription.remove();
    expect(uut.getProcessors('topBar')).toEqual([]);
  });
});
