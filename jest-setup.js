const { mockDetox } = require('detox-testing-library-rnn-adapter');

mockDetox(() => require('./playground/index'));

beforeEach(() => {
  const { Navigation } = require('react-native-navigation');
  setTimeout = (func) => {
    func();
  };
  Navigation.mockNativeComponents();
  mockUILib();
});

const mockUILib = () => {
  const NativeModules = require('react-native').NativeModules;
  NativeModules.KeyboardTrackingViewTempManager = {};
  NativeModules.StatusBarManager = {
    getHeight: () => 40,
  };
};
