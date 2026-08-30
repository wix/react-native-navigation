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

  it('should unregister only the subscribed processor', () => {
    const firstProcessor = (value: any, _commandName: string) => value;
    const secondProcessor = (value: any, _commandName: string) => value;
    const thirdProcessor = (value: any, _commandName: string) => value;
    uut.addProcessor(firstProcessor);
    const { remove } = uut.addProcessor(secondProcessor);
    uut.addProcessor(thirdProcessor);

    remove();

    expect(uut.getProcessors()).toEqual([firstProcessor, thirdProcessor]);
  });

  it('should ignore repeated subscription removal', () => {
    const processor = (value: any, _commandName: string) => value;
    const firstSubscription = uut.addProcessor(processor);
    const secondSubscription = uut.addProcessor(processor);

    firstSubscription.remove();
    firstSubscription.remove();

    expect(uut.getProcessors()).toEqual([processor]);

    secondSubscription.remove();
    expect(uut.getProcessors()).toEqual([]);
  });
});
