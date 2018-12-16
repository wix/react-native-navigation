import * as _ from 'lodash';

import { Store } from '../components/Store';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Options } from '../interfaces/Options';
import { AssetResolver } from '../adapters/AssetResolver';
import { ColorService } from '../adapters/ColorService';

export class OptionsProcessor {
  constructor(
    public store: Store,
    public uniqueIdProvider: UniqueIdProvider,
    private assetResolver: AssetResolver,
    private colorService: ColorService,
  ) {}

  public processOptions(options: Options): Options {
    return this.processObjectOrArray(options);
  }

  private processObjectOrArray(objectOrArray: object | any[]): any {
    return _.mapValues(objectOrArray, (value, key) => {
      if (!value) {
        return;
      }
      if (key === 'component') {
        return this.processComponent(value);
      }
      if (key === 'color' || _.endsWith(key, 'Color')) {
        return this.colorService.toNativeColor(value);
      }
      if (['icon', 'image'].includes(key) || _.endsWith(key, 'Icon') || _.endsWith(key, 'Image')) {
        return this.assetResolver.resolveFromRequire(value);
      }
      if (_.endsWith(key, 'Buttons') && Array.isArray(value)) {
        return this.processButtonsPassProps(value);
      }

      if (_.isObject(value) || Array.isArray(value)) {
        return this.processObjectOrArray(value);
      }

      return value;
    });
  }

  private processButtonsPassProps(value: any[]) {
    return value
      .filter((button: any) => button.component && button.component.passProps && button.id)
      .forEach((button: any) => {
        this.store.setPropsForId(button.id, button.component.passProps);
        button.component.passProps = undefined;
      });
  }

  private processComponent(value: any) {
    const { passProps, ...rest } = value;
    const componentId = rest.id || this.uniqueIdProvider.generate('CustomComponent');
    if (passProps) {
      this.store.setPropsForId(componentId, passProps);
    }
    return { ...rest, componentId };
  }
}
