import { DeviceEventEmitter } from 'react-native';
import { NativeEventsReceiver } from './NativeEventsReceiver';

jest.mock('./NativeRNNTurboEventEmitter', () => ({
  addListener: jest.fn(),
  removeListeners: jest.fn(),
}));

describe('NativeEventsReceiver', () => {
  it('forwards the native payload and removes the listener when unsubscribed', () => {
    const receiver = new NativeEventsReceiver();
    const callback = jest.fn();
    const payload = { componentId: 'screen', buttonId: 'button' };
    const subscription = receiver.registerNavigationButtonPressedListener(callback);

    DeviceEventEmitter.emit('RNN.NavigationButtonPressed', payload);
    expect(callback).toHaveBeenCalledWith(payload);

    subscription.remove();
    DeviceEventEmitter.emit('RNN.NavigationButtonPressed', payload);
    expect(callback).toHaveBeenCalledTimes(1);
  });

  it('supports events without a payload', () => {
    const receiver = new NativeEventsReceiver();
    const callback = jest.fn();
    const subscription = receiver.registerAppLaunchedListener(callback);

    DeviceEventEmitter.emit('RNN.AppLaunched');
    expect(callback).toHaveBeenCalledTimes(1);
    expect(callback).toHaveBeenCalledWith();
    subscription.remove();
  });
});
