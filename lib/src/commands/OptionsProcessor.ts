import clone from 'lodash/clone';
import isEqual from 'lodash/isEqual';
import isObject from 'lodash/isObject';
import isArray from 'lodash/isArray';
import isString from 'lodash/isString';
import endsWith from 'lodash/endsWith';
import forEach from 'lodash/forEach';

import { Store } from '../components/Store';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { ColorService } from '../adapters/ColorService';
import { AssetService } from '../adapters/AssetResolver';
import { Options } from '../interfaces/Options';
import { Deprecations } from './Deprecations';

export class OptionsProcessor {
  constructor(
    private store: Store,
    private uniqueIdProvider: UniqueIdProvider,
    private colorService: ColorService,
    private assetService: AssetService,
    private deprecations: Deprecations
  ) {}

  public processOptions(options: Options) {
    this.processObject(options, clone(options), (key, parentOptions) => {
      this.deprecations.onProcessOptions(key, parentOptions);
    });
  }

  public processDefaultOptions(options: Options) {
    this.processObject(options, clone(options), (key, parentOptions) => {
      this.deprecations.onProcessDefaultOptions(key, parentOptions);
    });
  }

  private processObject(
    objectToProcess: object,
    parentOptions: object,
    onProcess: (key: string, parentOptions: object) => void
  ) {
    forEach(objectToProcess, (value, key) => {
      this.processColor(key, value, objectToProcess);

      if (!value) {
        return;
      }

      this.processComponent(key, value, objectToProcess);
      this.processImage(key, value, objectToProcess);
      this.processButtonsPassProps(key, value);

      onProcess(key, parentOptions);

      if (!isEqual(key, 'passProps') && (isObject(value) || isArray(value))) {
        this.processObject(value, parentOptions, onProcess);
      }
    });
  }

  private processColor(key: string, value: any, options: Record<string, any>) {
    if (isEqual(key, 'color') || endsWith(key, 'Color')) {
      options[key] = value === null ? 'NoColor' : this.colorService.toNativeColor(value);
    }
  }

  private processImage(key: string, value: any, options: Record<string, any>) {
    if (
      isEqual(key, 'icon') ||
      isEqual(key, 'image') ||
      endsWith(key, 'Icon') ||
      endsWith(key, 'Image')
    ) {
      options[key] = isString(value) ? value : this.assetService.resolveFromRequire(value);
    }
  }

  private processButtonsPassProps(key: string, value: any) {
    if (endsWith(key, 'Buttons')) {
      forEach(value, (button) => {
        if (button.passProps && button.id) {
          this.store.updateProps(button.id, button.passProps);
          button.passProps = undefined;
        }
      });
    }
  }

  private processComponent(key: string, value: any, options: Record<string, any>) {
    if (isEqual(key, 'component')) {
      value.componentId = value.id ? value.id : this.uniqueIdProvider.generate('CustomComponent');
      this.store.ensureClassForName(value.name);
      if (value.passProps) {
        this.store.updateProps(value.componentId, value.passProps);
      }
      options[key].passProps = undefined;
    }
  }
}
