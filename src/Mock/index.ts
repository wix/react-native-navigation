export const ApplicationMock = require('./Application').Application;

export * from './constants';

export function mockNativeComponents() {
  const { NativeCommandsSender } = require('./mocks/NativeCommandsSender');
  const { NativeEventsReceiver } = require('./mocks/NativeEventsReceiver');
  const { AppRegistryService } = require('./mocks/AppRegistryService');
  const { Navigation } = require('react-native-navigation');

  Navigation.mockNativeComponents(
    new NativeCommandsSender(),
    new NativeEventsReceiver(),
    new AppRegistryService()
  );
}
