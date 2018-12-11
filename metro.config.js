module.exports = {
  projectRoot: `${__dirname}/playground`,
  watchFolders: [__dirname],
  transformer: {
    babelTransformerPath: require.resolve('react-native-typescript-transformer'),
  },
  resolver: {
    extraNodeModules: [__dirname]
  }
};

