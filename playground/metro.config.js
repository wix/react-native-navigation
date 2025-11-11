const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');
const path = require('path');

const config = {
  projectRoot: `${__dirname}`,
  resolver: {
    enableGlobalPackages: true,
  },
  watchFolders: [__dirname, path.resolve(__dirname, '..')],
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
