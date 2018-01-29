const babelOptions = require('./package.json').babel.env.test;

module.exports = function (wallaby) {
  return {
    env: {
      type: 'node',
      runner: 'node'
    },

    testFramework: 'jest',

    files: [
      'package.json',
      'lib/src/**/*.js',
      'lib/src/**/*.ts',
      '!lib/src/**/*.test.js',
      '!lib/src/**/*.test.ts',
      'integration/**/*.js',
      '!integration/**/*.test.js'
    ],

    tests: [
      'lib/src/**/*.test.js',
      'lib/src/**/*.test.ts',
      'integration/**/*.test.js'
    ],

    compilers: {
      '**/*.js': wallaby.compilers.babel(babelOptions)
    },

    setup: (w) => {
      w.testFramework.configure(require('./package.json').jest);
    }
  };
};
