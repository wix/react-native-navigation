export const ApplicationMock = require('./Application').Application;

export * from './constants';

export function mockNativeComponents() {
  const { NativeCommandsSender } = require('./mocks/NativeCommandsSender');
  const { NativeEventsReceiver } = require('./mocks/NativeEventsReceiver');
  const { Navigation } = require('react-native-navigation');
  Navigation.mockNativeComponents(new NativeCommandsSender(), new NativeEventsReceiver());
}
