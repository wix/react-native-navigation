import * as React from 'react';
import { NavigationRoot } from '../Navigation';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { AppRegistryService } from '../adapters/AppRegistryService';
import { NativeScriptCommandsSender } from './NativeScriptCommandsSender';
import { NativeScriptNavigationSurface } from './NativeScriptNavigationSurface';

type NativeScriptNavigationApi = Record<string, any> & {
  registerComponent(...args: any[]): any;
  setRoot(layout: any): Promise<string>;
  setDefaultOptions(options: any): void;
  mergeOptions(componentId: string, options: any): void;
};

export type NativeScriptNavigationInstance = {
  Navigation: NativeScriptNavigationApi;
  NativeScriptNavigationRoot: React.ComponentType<{attachController?: boolean}>;
};

export function createNativeScriptNavigation(
  appRegistryService = new AppRegistryService()
): NativeScriptNavigationInstance {
  const Navigation = new NavigationRoot(
    new NativeScriptCommandsSender(),
    new NativeEventsReceiver(),
    appRegistryService
  );

  const NativeScriptNavigationRoot = function NativeScriptNavigationRoot({
    attachController = true,
  }: {
    attachController?: boolean;
  }) {
    return (
      <NativeScriptNavigationSurface
        attachController={attachController}
        store={Navigation.store}
      />
    );
  };

  return { Navigation, NativeScriptNavigationRoot };
}

export function registerNativeScriptNavigationRoot(appKey = 'ReactNativeNavigation') {
  const appRegistryService = new AppRegistryService();
  const navigation = createNativeScriptNavigation(appRegistryService);
  appRegistryService.registerComponent(appKey, () => navigation.NativeScriptNavigationRoot);
  return navigation;
}
