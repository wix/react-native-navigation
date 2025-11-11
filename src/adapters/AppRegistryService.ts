import type { ComponentProvider } from 'react-native';
import { AppRegistry } from 'react-native';

export class AppRegistryService {
  registerComponent(appKey: string, getComponentFunc: ComponentProvider) {
    AppRegistry.registerComponent(appKey, getComponentFunc);
  }
}
