import * as _ from 'lodash';

import { Store } from '../components/Store';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Options, OptionsTopBarButton } from '../interfaces/Options';
import { AssetService } from '../adapters/AssetResolver';
import { ColorService } from '../adapters/ColorService';

export class OptionsProcessor {
  constructor(
    public store: Store,
    public uniqueIdProvider: UniqueIdProvider,
    private assetService: AssetService,
    private colorService: ColorService,
  ) {}

  public processOptions(options: Options): Options {
    return this.processObject(options);
  }

  private processObject(object: { [key: string]: any }): any {
    return _.mapValues(object, (value, key) => {
      if (key === 'component') {
        return this.processComponent(value);
      } else if (key === 'color' || _.endsWith(key, 'Color')) {
        return this.colorService.toNativeColor(value);
      } else if (key === 'icon' || _.endsWith(key, 'Icon') || _.endsWith(key, 'Image')) {
        return this.assetService.resolveFromRequire(value);
      } else if (_.endsWith(key, 'Buttons') && Array.isArray(value)) {
        return value.map(this.processButtonsPassProps);
      } else if (_.isObject(value)) {
        return this.processObject(value);
      }

      return value;
    });
  }

  private processButtonsPassProps = (button: OptionsTopBarButton) => {
    if (button.component && button.component.passProps && button.id) {
      this.store.setPropsForId(button.id, button.component.passProps);
      button.component.passProps = undefined;
    }
    return button;
  }

  private processComponent(value: { [key: string]: any }) {
    const { passProps, ...rest } = value;
    const componentId = rest.id || this.uniqueIdProvider.generate('CustomComponent');
    if (passProps) {
      this.store.setPropsForId(componentId, passProps);
    }
    return { ...rest, componentId };
  }
}
