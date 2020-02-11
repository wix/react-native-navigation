const babelOptions = require('./babel.config')().env.test;

module.exports = function (wallaby) {
  return {
    env: {
      type: 'node',
      runner: 'node'
    },

    testFramework: 'jest',
    setup: (w) => {
      w.testFramework.configure(require('./package.json').jest);
    }
  };
};
