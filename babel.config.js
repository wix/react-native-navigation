module.exports = function (api) {
  api && api.cache(false);
  return {
    env: {
      test: {
        presets: [
          "module:metro-react-native-babel-preset"
        ],
        plugins: [
          "@babel/plugin-proposal-class-properties"
        ]
      }
    }
  };
}
