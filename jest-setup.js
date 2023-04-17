const { mockDetox } = require('detox-testing-library-rnn-adapter');

jest.mock('react-native-gesture-handler', () => {
  return {
    gestureHandlerRootHOC: jest.fn(),
  };
});

mockDetox(() => require('./playground/index'));

beforeEach(() => {
  const { mockNativeComponents } = require('react-native-navigation/Mock');
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
