import * as _ from 'lodash';
import { processColor } from 'react-native';
import * as resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource';

export class OptionsProcessor {
  constructor(public store) { }

  public processOptions(options) {
    _.forEach(options, (value, key) => {
      if (!value) { return; }

      this.processColor(key, value, options);
      this.processImage(key, value, options);
      this.processButtonsPassProps(key, value);

      if (_.isObject(value) || _.isArray(value)) {
        this.processOptions(value);
      }
    });
  }

  private processColor(key, value, options) {
    if (_.isEqual(key, 'color') || _.endsWith(key, 'Color')) {
      options[key] = processColor(value);
    }
  }

  private processImage(key, value, options) {
    if (_.isEqual(key, 'icon') || _.isEqual(key, 'image') || _.endsWith(key, 'Icon') || _.endsWith(key, 'Image')) {
      options[key] = resolveAssetSource(value);
    }
  }

  private processButtonsPassProps(key, value) {
    if (_.endsWith(key, 'Buttons')) {
      _.forEach(value, (button) => {
        if (button.passProps && button.id) {
          this.store.setPropsForId(button.id, button.passProps);
        }
      });
    }
  }
}
