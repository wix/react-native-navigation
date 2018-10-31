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
      'lib/src/**/*.tsx',
      '!lib/src/**/*.test.tsx',
      '!lib/src/**/*.test.js',
      '!lib/src/**/*.test.ts',
      'integration/**/*.js',
      '!integration/**/*.test.js'
    ],

    tests: [
      'lib/src/**/*.test.js',
      'lib/src/**/*.test.ts',
      'lib/src/**/*.test.tsx',
      'integration/**/*.test.js'
    ],

    compilers: {
      '**/*.js': wallaby.compilers.babel(babelOptions),
      '**/*.ts?(x)': wallaby.compilers.typeScript({
        module: 'commonjs',
        jsx: 'React'
      })
    },

    setup: (w) => {
      w.testFramework.configure(require('./package.json').jest);
    }
  };
};
