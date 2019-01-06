import { ComponentProvider, AppRegistry } from 'react-native';
import { Service } from 'typedi';

@Service()
export class AppRegistryService {
  registerComponent(appKey: string, getComponentFunc: ComponentProvider) {
    AppRegistry.registerComponent(appKey, getComponentFunc);
  }
}
