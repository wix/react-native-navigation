const detox = require('detox');
const config = require('../package.json').detox;
// require('detox-testing-library-rnn-adapter').extendDetox();
const isDetox = () => !!process.env.DETOX_START_TIMESTAMP;

it.e2e = (name, fn) => {
  if (isDetox()) {
    it(name, fn);
  }
};

jest.setTimeout(300000);

beforeAll(async () => {
  await detox.init(config, { launchApp: false });
});

afterAll(async () => {
  await detox.cleanup();
});
