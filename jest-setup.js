const { mockDetox, mockUILib, mockJest } = require('./Utils');
const { Navigation } = require('react-native-navigation');
mockJest();

setTimeout = (func) => {
  func();
};

beforeEach(() => {
  Navigation.mockNativeComponents();
  mockDetox(() => require('./playground/index'));
  mockUILib();
});
