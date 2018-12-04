module.exports = {
  projectRoots: [__dirname, `${__dirname}/playground`],
  transformer: {
    babelTransformerPath: require.resolve('react-native-typescript-transformer'),
  },
};