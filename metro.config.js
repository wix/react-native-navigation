const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');

const config = {
  projectRoot: `${__dirname}`,
  watchFolders: [__dirname],
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
