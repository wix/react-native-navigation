import { ComponentProvider, AppRegistry } from 'react-native';
import { Service } from 'typedi';

@Service('AppRegistryService')
export class AppRegistryService {
  registerComponent(appKey: string, getComponentFunc: ComponentProvider) {
    AppRegistry.registerComponent(appKey, getComponentFunc);
  }
}
