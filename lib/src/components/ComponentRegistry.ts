import { AppRegistry, ComponentProvider } from 'react-native';
import { ComponentWrapper } from './ComponentWrapper';
import { ComponentType } from 'react';

export class ComponentRegistry {
  private store;

  constructor(store) {
    this.store = store;
  }

  registerComponent(componentName: string, getComponentClassFunc: ComponentProvider): ComponentType<any> {
    const OriginalComponentClass = getComponentClassFunc();
    const NavigationComponent = ComponentWrapper.wrap(componentName, OriginalComponentClass, this.store);
    this.store.setOriginalComponentClassForName(componentName, OriginalComponentClass);
    AppRegistry.registerComponent(componentName, () => NavigationComponent);
    return NavigationComponent;
  }
}
