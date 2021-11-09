const detox = require('detox');
const config = require('../package.json').detox;
require('detox-testing-library-rnn-adapter').extendDetox();

jest.setTimeout(300000);

beforeAll(async () => {
  await detox.init(config, { launchApp: false });
});

afterAll(async () => {
  await detox.cleanup();
});
