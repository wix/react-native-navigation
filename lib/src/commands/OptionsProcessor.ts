import clone from 'lodash/clone';
import isEqual from 'lodash/isEqual';
import isObject from 'lodash/isObject';
import isArray from 'lodash/isArray';
import isString from 'lodash/isString';
import endsWith from 'lodash/endsWith';
import forEach from 'lodash/forEach';
import has from 'lodash/has';

import { Store } from '../components/Store';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { ColorService } from '../adapters/ColorService';
import { AssetService } from '../adapters/AssetResolver';
import {
  AnimationOptions,
  EnterExitAnimationOptions,
  ModalAnimationOptions,
  OldModalAnimationOptions,
  Options,
  OptionsSearchBar,
  OptionsTopBar,
  StackAnimationOptions,
  ViewAnimationOptions,
} from '../interfaces/Options';
import { Deprecations } from './Deprecations';
import { OptionProcessorsStore } from '../processors/OptionProcessorsStore';
import { CommandName } from '../interfaces/CommandName';
import { Platform, DynamicColorIOS, ColorValue } from 'react-native';

export class OptionsProcessor {
  constructor(
    private store: Store,
    private uniqueIdProvider: UniqueIdProvider,
    private optionProcessorsRegistry: OptionProcessorsStore,
    private colorService: ColorService,
    private assetService: AssetService,
    private deprecations: Deprecations
  ) {}

  public processOptions(commandName: CommandName, options?: Options, props?: any) {
    if (options) {
      this.processObject(
        options,
        clone(options),
        (key, parentOptions) => {
          this.deprecations.onProcessOptions(key, parentOptions, commandName);
          this.deprecations.checkForDeprecatedOptions(parentOptions);
        },
        commandName,
        props
      );
    }
  }

  public processDefaultOptions(options: Options, commandName: CommandName) {
    this.processObject(
      options,
      clone(options),
      (key, parentOptions) => {
        this.deprecations.onProcessDefaultOptions(key, parentOptions);
      },
      commandName
    );
  }

  private processObject(
    objectToProcess: Record<string, any>,
    parentOptions: object,
    onProcess: (key: string, parentOptions: object) => void,
    commandName: CommandName,
    props?: any,
    parentPath?: string
  ) {
    forEach(objectToProcess, (value, key) => {
      const objectPath = this.resolveObjectPath(key, parentPath);
      this.processWithRegisteredProcessor(
        key,
        value,
        objectToProcess,
        objectPath,
        commandName,
        props
      );

      this.processColor(key, value, objectToProcess);

      if (!value) {
        return;
      }
      this.processComponent(key, value, objectToProcess);
      this.processImage(key, value, objectToProcess);
      this.processButtonsPassProps(key, value);
      this.processSearchBar(key, value, objectToProcess);
      this.processInterpolation(key, value, objectToProcess);
      this.processAnimation(key, value, objectToProcess);

      onProcess(key, parentOptions);

      const processedValue = objectToProcess[key];
      if (!isEqual(key, 'passProps') && (isObject(processedValue) || isArray(processedValue))) {
        this.processObject(
          processedValue,
          parentOptions,
          onProcess,
          commandName,
          props,
          objectPath
        );
      }
    });
  }

  private resolveObjectPath(key: string, path?: string): string {
    if (!path) path = key;
    else path += `.${key}`;
    return path;
  }

  private processColor(key: string, value: any, options: Record<string, any>) {
    if (isEqual(key, 'color') || endsWith(key, 'Color')) {
      if (Platform.OS === 'ios') this.processColorIOS(key, value, options);
      else this.processColorAndroid(key, value, options);
    }
  }

  private processColorIOS(key: string, value: any, options: Record<string, any>) {
    if (value !== undefined) {
      if (value === null) {
        options[key] = 'NoColor';
      } else if (value instanceof Object) {
        if ('semantic' in value) {
          options[key] = value;
        } else if ('dynamic' in value) {
          options[key] = DynamicColorIOS({
            light: this.colorService.toNativeColor(value.dynamic.light) as ColorValue,
            dark: this.colorService.toNativeColor(value.dynamic.dark) as ColorValue,
          });
        } else {
          options[key] = DynamicColorIOS({
            light: this.colorService.toNativeColor(value.light) as ColorValue,
            dark: this.colorService.toNativeColor(value.dark) as ColorValue,
          });
        }
      } else {
        options[key] = this.colorService.toNativeColor(value);
      }
    }
  }

  private processColorAndroid(key: string, value: any, options: Record<string, any>) {
    if (value !== undefined) {
      const newColorObj: Record<string, any> = { dark: 'NoColor', light: 'NoColor' };
      if (value === null) {
        options[key] = newColorObj;
      } else if (value instanceof Object) {
        if ('semantic' in value || 'resource_paths' in value) {
          options[key] = value;
          return;
        } else {
          for (let keyColor in value) {
            newColorObj[keyColor] = this.colorService.toNativeColor(value[keyColor]);
          }
          options[key] = newColorObj;
        }
      } else {
        let parsedColor = this.colorService.toNativeColor(value);
        newColorObj.light = parsedColor;
        newColorObj.dark = parsedColor;
        options[key] = newColorObj;
      }
    }
  }

  private processWithRegisteredProcessor(
    key: string,
    value: string,
    options: Record<string, any>,
    path: string,
    commandName: CommandName,
    passProps: any
  ) {
    const registeredProcessors = this.optionProcessorsRegistry.getProcessors(path);
    if (registeredProcessors) {
      registeredProcessors.forEach((processor) => {
        options[key] = processor(value, commandName, passProps);
      });
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
          this.store.setPendingProps(button.id, button.passProps);
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
        this.store.setPendingProps(value.componentId, value.passProps);
      }
      options[key].passProps = undefined;
    }
  }

  private processSearchBar(key: string, value: OptionsSearchBar | boolean, options: OptionsTopBar) {
    if (key !== 'searchBar') {
      return;
    }

    const deprecatedSearchBarOptions: OptionsSearchBar = {
      visible: false,
      hideOnScroll: options.searchBarHiddenWhenScrolling ?? false,
      hideTopBarOnFocus: options.hideNavBarOnFocusSearchBar ?? false,
      obscuresBackgroundDuringPresentation: false,
      backgroundColor: options.searchBarBackgroundColor,
      tintColor: options.searchBarTintColor,
      placeholder: options.searchBarPlaceholder ?? '',
    };

    if (typeof value === 'boolean') {
      // Deprecated
      this.deprecations.onProcessOptions(key, options, '');

      options[key] = {
        ...deprecatedSearchBarOptions,
        visible: value,
      };
    } else {
      options[key] = {
        ...deprecatedSearchBarOptions,
        ...value,
      };
    }
  }

  private processInterpolation(key: string, value: any, options: Record<string, any>) {
    if (isEqual(key, 'interpolation')) {
      if (typeof value === 'string') {
        this.deprecations.onProcessOptions(key, options, '');
        options[key] = {
          type: options[key],
        };
      }
    }
  }

  private processAnimation(key: string, value: any, options: Record<string, any>) {
    this.processSetRootAnimation(key, value, options);
    this.processPush(key, value, options);
    this.processPop(key, value, options);
    this.processSetStackRoot(key, value, options);
    this.processShowModal(key, value, options);
    this.processDismissModal(key, value, options);
  }

  private processSetStackRoot(
    key: string,
    animation: ViewAnimationOptions | StackAnimationOptions,
    parentOptions: AnimationOptions
  ) {
    if (key !== 'setStackRoot') return;
    if (this.isNewStackAnimationApi(animation)) return;
    this.convertDeprecatedViewAnimationApiToNewStackAnimationApi(animation, parentOptions);
  }

  private isNewStackAnimationApi(animation: ViewAnimationOptions | StackAnimationOptions) {
    return has(animation, 'content') || has(animation, 'topBar') || has(animation, 'bottomTabs');
  }

  private convertDeprecatedViewAnimationApiToNewStackAnimationApi(
    animation: ViewAnimationOptions | StackAnimationOptions,
    parentOptions: AnimationOptions
  ) {
    if (!has(animation, 'content.enter') && !has(animation, 'content.exit')) {
      parentOptions.setStackRoot = {
        content: {
          enter: animation,
        },
      };
      if (has(animation, 'enabled')) {
        parentOptions.setStackRoot!!.enabled = animation.enabled;
      }
      if (has(animation, 'waitForRender')) {
        parentOptions.setStackRoot!!.waitForRender = animation.waitForRender;
      }
    }
  }

  private processPop(
    key: string,
    animation: StackAnimationOptions,
    parentOptions: AnimationOptions
  ) {
    if (key !== 'pop') return;
    if (animation.content && !has(animation, 'content.enter') && !has(animation, 'content.exit')) {
      parentOptions.pop!!.content = {
        exit: animation.content as ViewAnimationOptions,
      };
    }
    if (animation.topBar && !has(animation, 'topBar.enter') && !has(animation, 'topBar.exit')) {
      parentOptions.pop!!.topBar = {
        exit: animation.topBar as ViewAnimationOptions,
      };
    }
    if (
      animation.bottomTabs &&
      !has(animation, 'bottomTabs.enter') &&
      !has(animation, 'bottomTabs.exit')
    ) {
      parentOptions.pop!!.bottomTabs = {
        exit: animation.bottomTabs as ViewAnimationOptions,
      };
    }
  }

  private processSetRootAnimation(
    key: string,
    animation: ViewAnimationOptions | EnterExitAnimationOptions,
    parentOptions: AnimationOptions
  ) {
    if (key !== 'setRoot') return;
    if (Platform.OS === 'android' && !('enter' in animation)) {
      parentOptions.setRoot = {
        enter: animation,
      } as EnterExitAnimationOptions;
    } else if (Platform.OS === 'ios' && 'enter' in animation) {
      parentOptions.setRoot = animation;
    }
  }

  private processShowModal(
    key: string,
    animation: OldModalAnimationOptions | ModalAnimationOptions,
    parentOptions: AnimationOptions
  ) {
    if (key !== 'showModal') return;
    if (!('enter' in animation)) {
      const elementTransitions = animation.elementTransitions;
      const sharedElementTransitions = animation.sharedElementTransitions;
      const enter = { ...(animation as OldModalAnimationOptions) };
      delete enter.sharedElementTransitions;
      delete enter.elementTransitions;
      parentOptions.showModal = {
        enter,
        sharedElementTransitions,
        elementTransitions,
      };
    }
  }

  private processDismissModal(
    key: string,
    animation: OldModalAnimationOptions | ModalAnimationOptions,
    parentOptions: AnimationOptions
  ) {
    if (key !== 'dismissModal') return;
    if (!('exit' in animation)) {
      const elementTransitions = animation.elementTransitions;
      const sharedElementTransitions = animation.sharedElementTransitions;
      const exit = { ...(animation as OldModalAnimationOptions) };
      delete exit.sharedElementTransitions;
      delete exit.elementTransitions;
      parentOptions.dismissModal = {
        exit,
        sharedElementTransitions,
        elementTransitions,
      };
    }
  }

  private processPush(
    key: string,
    animation: StackAnimationOptions,
    parentOptions: AnimationOptions
  ) {
    if (key !== 'push') return;
    if (animation.content && !has(animation, 'content.enter') && !has(animation, 'content.exit')) {
      parentOptions.push!!.content = {
        enter: animation.content as ViewAnimationOptions,
      };
    }
    if (animation.topBar && !has(animation, 'topBar.enter') && !has(animation, 'topBar.exit')) {
      parentOptions.push!!.topBar = {
        enter: animation.topBar as ViewAnimationOptions,
      };
    }
    if (
      animation.bottomTabs &&
      !has(animation, 'bottomTabs.enter') &&
      !has(animation, 'bottomTabs.exit')
    ) {
      parentOptions.push!!.bottomTabs = {
        enter: animation.bottomTabs as ViewAnimationOptions,
      };
    }
  }
}
