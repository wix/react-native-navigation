import * as _ from 'lodash';
import { processColor } from 'react-native';
import * as resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource';

export class OptionsProcessor {
  static processOptions(options, store) {
    _.forEach(options, (value, key) => {
      if (!value) { return; }

      if (_.isEqual(key, 'color') || _.endsWith(key, 'Color')) {
        options[key] = processColor(value);
      }
      if (_.isEqual(key, 'icon') || _.isEqual(key, 'image') || _.endsWith(key, 'Icon') || _.endsWith(key, 'Image')) {
        options[key] = resolveAssetSource(options[key]);
      }
      if (_.isObject(value) || _.isArray(value)) {
        OptionsProcessor.processOptions(value, store);
        OptionsProcessor.processArrayOptions(key, value, store);
      }
    });
  }

  static processArrayOptions(key, array, store) {
    _.forEach(array, (value) => {
      if (_.endsWith(key, 'Buttons') && value.passProps) {
        store.setPropsForComponentId(value.id, value.passProps);
      }
    });
  }
}
