const _ = require('lodash');
const { processColor } = require('react-native');
const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource');

const colorRegexp = /color$/i;
const iconRegexp = /(icon|image)$/i;

class OptionsProcessor {
  static processOptions(navigationOptions) {
    _.forEach(navigationOptions, (value, key) => {
      if (colorRegexp.test(key)) {
        navigationOptions[key] = processColor(value);
      } else if (iconRegexp.test(key)) {
        navigationOptions[key] = resolveAssetSource(navigationOptions[key]);
      } else if (_.isPlainObject(value) || _.isArray(value)) {
        OptionsProcessor.processOptions(value);
      }
    });
  }
}

module.exports = OptionsProcessor;
