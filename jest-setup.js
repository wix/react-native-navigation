const { mockDetox } = require('detox-testing-library-rnn-adapter');

mockDetox(() => require('./playground/index'));

beforeEach(() => {
  const { mockNativeComponents } = require('react-native-navigation/Mock');
  setTimeout = (func) => {
    func();
  };
  mockNativeComponents();
  mockUILib();
});

const mockUILib = () => {
  const NativeModules = require('react-native').NativeModules;
  NativeModules.KeyboardTrackingViewTempManager = {};
  NativeModules.StatusBarManager = {
    getHeight: () => 40,
  };
};
