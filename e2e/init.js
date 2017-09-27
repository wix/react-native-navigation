require('babel-polyfill'); // eslint-disable-line import/no-extraneous-dependencies
const detox = require('detox');
const config = require('../package.json').detox;

before(async () => {
<<<<<<< HEAD
  await detox.init(config, { launchApp: false });
=======
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
>>>>>>> upstream merge
});

after(async () => {
  await detox.cleanup();
});
