const path = require('path');
const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');

const root = path.resolve(__dirname);

const config = {
  projectRoot: root,
  watchFolders: [root],
  resolver: {
    nodeModulesPaths: [path.resolve(root, 'node_modules')],
    extraNodeModules: new Proxy(
      {},
      {
        get: (target, name) => {
          if (name === 'react-native-navigation') {
            return root;
          }
          return path.join(root, 'node_modules', name);
        },
      }
    ),
  },
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
