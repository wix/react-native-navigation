module.exports = {
  projectRoot: __dirname,
  watchFolders: [__dirname],
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false,
      },
    }),
  },
};
