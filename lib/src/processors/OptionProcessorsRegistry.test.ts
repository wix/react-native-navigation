import { OptionProcessorsRegistry } from './OptionProcessorsRegistry';

describe('Option processors Store', () => {
  let uut: OptionProcessorsRegistry;
  beforeEach(() => {
    uut = new OptionProcessorsRegistry();
  });

  it('should register processor to store', () => {
    const processor = () => {};
    uut.registerProcessor('topBar', processor);
    expect(uut.getProcessor('topBar')).toEqual(processor);
  });

  it('should not allow multiple processors with the same object path', () => {
    const processor = () => {};
    uut.registerProcessor('topBar', processor);
    expect(() => uut.registerProcessor('topBar', () => {})).toThrowError(
      'Registering multiple option processors are not allowed'
    );
  });

  it('should unregister processor', () => {
    const processor = () => {};
    uut.registerProcessor('topBar', processor);
    expect(uut.getProcessor('topBar')).toEqual(processor);
    uut.unregisterProcessor('topBar');
    expect(uut.getProcessor('topBar')).toEqual(undefined);
  });
});
