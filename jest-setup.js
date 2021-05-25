const { mockDetox, mockUILib, mockJest } = require('./lib/src/NavigationMock/Utils');

mockJest();

setTimeout = (func) => {
  func();
};

beforeEach(() => {
  mockDetox(() => require('./playground/index'));
  mockUILib();
});
