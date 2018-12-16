import * as _ from 'lodash';
import { processColor } from 'react-native';

import { Store } from '../components/Store';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Options, OptionsTopBarButton } from '../interfaces/Options';
import { AssetResolver } from '../adapters/AssetResolver';

export class OptionsProcessor {
  constructor(
    public store: Store,
    public uniqueIdProvider: UniqueIdProvider,
    private assetResolver: AssetResolver,
  ) {}

  public processOptions(options: Options) {
    const input = options;
    _.forEach(input, (value, key) => {
      if (!value) {
        return;
      }
      this.processComponent(key, value, input);
      this.processColor(key, value, input);
      this.processImage(key, value, input);
      this.processButtonsPassProps(key, value);

      if (!_.isEqual(key, 'passProps') && (_.isObject(value) || _.isArray(value))) {
        this.processOptions(value as any);
      }
    });
    return input;
  }

  private processColor(key: string, value: any, options: Record<string, any>) {
    if (_.isEqual(key, 'color') || _.endsWith(key, 'Color')) {
      options[key] = processColor(value);
    }
  }

  private processImage(key: string, value: any, options: Record<string, any>) {
    if (
      _.isEqual(key, 'icon') ||
      _.isEqual(key, 'image') ||
      _.endsWith(key, 'Icon') ||
      _.endsWith(key, 'Image')
    ) {
      options[key] = this.assetResolver.resolveFromRequire(value);
    }
  }

  private isButton(button: any): button is OptionsTopBarButton {
    return (
      (button as OptionsTopBarButton).component !== undefined &&
      (button as OptionsTopBarButton).id !== undefined
    );
  }

  private processButtonsPassProps(key: string, value: any) {
    if (_.endsWith(key, 'Buttons')) {
      _.forEach(value, (button) => {
        if (this.isButton(button) && button.component) {
          this.store.setPropsForId(button.id, button.component.passProps);
          button.component.passProps = undefined;
        }
      });
    }
  }

  private processComponent(key: string, value: any, options: Record<string, any>) {
    if (_.isEqual(key, 'component')) {
      value.componentId = value.id ? value.id : this.uniqueIdProvider.generate('CustomComponent');
      if (value.passProps) {
        this.store.setPropsForId(value.componentId, value.passProps);
      }
      options[key] = _.omit(value, 'passProps');
    }
  }
}
