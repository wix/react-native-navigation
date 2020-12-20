module.exports = function (wallaby) {
  return {
    env: {
      type: 'node',
      runner: 'node',
    },

    testFramework: 'jest',

    files: [
      'package.json',
      'lib/src/**/*.js',
      'lib/src/**/*.ts',
      'lib/src/**/*.tsx',
      '!lib/src/**/*.test.tsx',
      '!lib/src/**/*.test.js',
      '!lib/src/**/*.test.ts',
      'integration/**/*.js',
      '!scripts/**/*.test.js',
      'scripts/**/*.js',
    ],

    tests: [
      'lib/src/**/*.test.js',
      'lib/src/**/*.test.ts',
      'lib/src/**/*.test.tsx',
      'integration/**/*.test.js',
      'scripts/**/*.test.js',
    ],

    setup: (w) => {
      w.testFramework.configure(require('./package.json').jest);
    },
  };
};
