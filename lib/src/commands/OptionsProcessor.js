const _ = require('lodash');
const { processColor } = require('react-native');

class OptionsProcessor {
  static processOptions(navigationOptions) {
    _.forEach(navigationOptions, (value, key) => {
      if (_.endsWith(key, 'Color')) {
        navigationOptions[key] = processColor(value);
      }
      if (_.isPlainObject(value)) {
        OptionsProcessor.processOptions(value);
      }
    });
  }
}

module.exports = OptionsProcessor;
