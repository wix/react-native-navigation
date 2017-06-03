import { NativeModules } from 'react-native';
import NativeEventsReceiver from './NativeEventsReceiver';

describe('NativeEventsReceiver', () => {
  let uut;
  const eventEmitterMock = { addListener: jest.fn() };

  beforeEach(() => {
    NativeModules.RNNEventEmitter = {};
    uut = new NativeEventsReceiver();
    uut.emitter = eventEmitterMock;
  });

  it('register for appLaunched', () => {
    const callback = jest.fn();
    uut.appLaunched(callback);
    expect(callback).not.toHaveBeenCalled();
    expect(eventEmitterMock.addListener).toHaveBeenCalledTimes(1);
    expect(eventEmitterMock.addListener).toHaveBeenCalledWith('RNN.appLaunched', callback);
  });

  it('containerStart', () => {
    const callback = jest.fn();
    uut.containerStart(callback);
    expect(callback).not.toHaveBeenCalled();
    expect(eventEmitterMock.addListener).toHaveBeenCalledTimes(1);
    expect(eventEmitterMock.addListener).toHaveBeenCalledWith('RNN.containerStart', callback);
  });

  it('containerStop', () => {
    const callback = jest.fn();
    uut.containerStop(callback);
    expect(callback).not.toHaveBeenCalled();
    expect(eventEmitterMock.addListener).toHaveBeenCalledTimes(1);
    expect(eventEmitterMock.addListener).toHaveBeenCalledWith('RNN.containerStop', callback);
  });
});
