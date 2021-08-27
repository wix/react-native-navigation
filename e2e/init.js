const detox = require('detox');
const config = require('../package.json').detox;
const exec = require('shell-utils').exec;
const adapter = require('detox/runners/jest/adapter');
require('detox-testing-library-rnn-adapter').extendDetox();

jest.setTimeout(300000);
jasmine.getEnv().addReporter(adapter);

beforeAll(async () => {
  await detox.init(config, { launchApp: false });
});

afterAll(async () => {
  await adapter.afterAll();
  await detox.cleanup();
});

beforeEach(async () => {
  await adapter.beforeEach();
});
