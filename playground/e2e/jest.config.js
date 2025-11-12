module.exports = {
  ...require('../../jest.config.js'),

  // bail: true,
  globalSetup: 'detox/runners/jest/globalSetup',
  globalTeardown: 'detox/runners/jest/globalTeardown',
  reporters: ['detox/runners/jest/reporter'],
  maxWorkers: 1,
  roots: ['<rootDir>/e2e', '<rootDir>/src'],
  moduleNameMapper: {
    ...require('../../jest.config.js').moduleNameMapper,
    '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$': 'identity-obj-proxy',
    '.+\\.(css|styl|less|sass|scss)$': 'identity-obj-proxy',
  },
  rootDir: '..',
  setupFilesAfterEnv: ['<rootDir>/e2e/init.js'],
  testEnvironment: 'detox/runners/jest/testEnvironment',
  testMatch: ['<rootDir>/e2e/**.test.{js,ts}'],
  testTimeout: 30000,
  verbose: true,
  passWithNoTests: true,
};
