require('babel-polyfill'); // eslint-disable-line import/no-extraneous-dependencies
const detox = require('detox');
const config = require('../package.json').detox;

before(async () => {
  await detox.init(config, { launchApp: false });
});

after(async () => {
  await detox.cleanup();
});
