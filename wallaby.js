/*eslint-disable*/
'use strict';
process.env.BABEL_ENV = 'test';
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
      '!lib/src/**/*.test.js',
      'integration/**/*.js',
      '!integration/**/*.test.js'
    ],

    tests: [
      'lib/src/**/*.test.js',
      'integration/**/*.test.js'
    ],

    compilers: {
      '**/*.js': wallaby.compilers.babel(babelOptions)
    },

    setup: function (w) {
      require('babel-polyfill');
      w.testFramework.configure(require('./package.json').jest);
    }
  };
};
