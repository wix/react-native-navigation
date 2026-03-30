const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');

const config = {
  projectRoot: `${__dirname}`,
  resolver: {
    enableGlobalPackages: true,
  },
  watchFolders: [__dirname],
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
