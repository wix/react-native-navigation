import _ from 'lodash';
import { processColor } from 'react-native';

export default function optionsProcessor(navigationOptions) {
  _.forEach(navigationOptions, (value, key) => {
    if (_.endsWith(key, 'Color')) {
      navigationOptions[key] = processColor(value);
    }
    if (_.isPlainObject(value)) {
      optionsProcessor(value);
    }
  });
}
