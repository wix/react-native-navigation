require('babel-polyfill'); // eslint-disable-line import/no-extraneous-dependencies
const detox = require('detox');

before(async () => {
  const detoxConfig = {
    configurations: {
      ios: {
        binaryPath: process.env.detoxAppBuildPath,
        type: 'ios.simulator',
        name: 'iPhone 7'
      }
    }
  };
  await detox.init(detoxConfig, { launchApp: false });
});

after(async () => {
  await detox.cleanup();
});
