const _ = require('lodash');
const { processColor } = require('react-native');
const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource');

class OptionsProcessor {
  static processOptions(navigationOptions) {
    _.forEach(navigationOptions, (value, key) => {
      if (value) {
        if (_.endsWith(key, 'Color')) {
          navigationOptions[key] = processColor(value);
        }
        if (_.isEqual(key, 'icon') || _.endsWith(key, 'Icon') || _.endsWith(key, 'Image')) {
          navigationOptions[key] = resolveAssetSource(navigationOptions[key]);
        }
        if (_.isObject(value) || _.isArray(value)) {
          OptionsProcessor.processOptions(value);
        }
      }
    });
  }
}

module.exports = OptionsProcessor;
