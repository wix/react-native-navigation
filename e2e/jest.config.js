module.exports = {
  ...require('../jest.config.js'),

  // bail: true,
  globalSetup: 'detox/runners/jest/globalSetup',
  globalTeardown: 'detox/runners/jest/globalTeardown',
  reporters: ['detox/runners/jest/reporter'],
  maxWorkers: 1,
  moduleNameMapper: {
    ...require('../jest.config.js').moduleNameMapper,
    '.+\\.(css|styl|less|sass|scss|png|jpg|ttf|woff|woff2)$': 'identity-obj-proxy',
  },
  rootDir: '..',
  setupFilesAfterEnv: ['<rootDir>/e2e/init.js'],
  testEnvironment: 'detox/runners/jest/testEnvironment',
  testMatch: ['<rootDir>/e2e/**.test.{js,ts}'],
  testTimeout: 30000,
  verbose: true,
};
