const babelOptions = require('./babel.config')().env.test;

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
      '!lib/src/**/*.test.ts'
    ],

    tests: [
      'lib/src/**/*.test.js',
      'lib/src/**/*.test.ts',
      'lib/src/**/*.test.tsx'
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
