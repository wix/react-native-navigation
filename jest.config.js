const { defaults: tsjPreset } = require('ts-jest/presets');

module.exports = {
  ...tsjPreset,
  preset: 'react-native',
  transform: {
    ...tsjPreset.transform,
    '\\.js$': '<rootDir>/node_modules/react-native/jest/preprocessor.js',
  },
  globals: {
    'ts-jest': {
      babelConfig: true,
    },
  },
  cacheDirectory: '.jest/cache',
  roots: ['<rootDir>/node_modules/', '<rootDir>/lib/dist/', '<rootDir>/integration/'],
  collectCoverageFrom: [
    'lib/dist/**/*.js',
    'integration/**/*.js',
    '!lib/dist/index.js',
    '!lib/dist/Navigation.js',
    '!lib/dist/adapters/**/*',
    '!lib/dist/interfaces/**/*',
    '!lib/dist/**/*.test.*',
    '!integration/**/*.test.*',
    '!integration/*.test.*',
  ],
  resetMocks: true,
  resetModules: true,
  coverageReporters: ['json', 'lcov', 'text', 'html'],
};
