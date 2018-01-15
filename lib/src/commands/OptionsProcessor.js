const _ = require('lodash');
const { processColor } = require('react-native');
const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource');

class OptionsProcessor {
  static processOptions(options) {
    _.forEach(options, (value, key) => {
      if (value) {
        if (_.endsWith(key, 'Color')) {
          options[key] = processColor(value);
        }
        if (_.isEqual(key, 'icon') || _.endsWith(key, 'Icon') || _.endsWith(key, 'Image')) {
          options[key] = resolveAssetSource(options[key]);
        }
        if (_.isObject(value) || _.isArray(value)) {
          OptionsProcessor.processOptions(value);
        }
      }
    });
  }
}

module.exports = OptionsProcessor;
