module.exports = function (api) {
  api && api.cache(false);
  return {
    presets: ['module:@react-native/babel-preset'],
    plugins: [
      '@babel/plugin-proposal-export-namespace-from',
      '@babel/plugin-proposal-export-default-from',
      '@babel/plugin-transform-flow-strip-types',
      'react-native-reanimated/plugin',
    ],
  };
};
