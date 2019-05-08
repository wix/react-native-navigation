module.exports = {
  projectRoot: `${__dirname}/playground`,
  watchFolders: [
    __dirname
  ],
  resolver: {
    sourceExts: ['ts', 'tsx', 'js']
  },
  transformer: {
    babelTransformerPath: require.resolve('react-native-typescript-transformer'),
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false,
      },
    })
  }
};
