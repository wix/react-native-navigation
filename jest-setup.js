const { mockDetox, mockUILib, mockJest } = require('./Utils');

mockJest();

setTimeout = (func) => {
  func();
};

beforeEach(() => {
  mockDetox(() => require('./playground/index'));
  mockUILib();
});
