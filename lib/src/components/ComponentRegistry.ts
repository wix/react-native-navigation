import { AppRegistry, ComponentProvider } from 'react-native';
import { ComponentWrapper } from './ComponentWrapper';
import { ComponentType } from 'react';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';

export class ComponentRegistry {
  constructor(private readonly store: Store, private readonly componentEventsObserver: ComponentEventsObserver) { }

  registerComponent(componentName: string, getComponentClassFunc: ComponentProvider, ReduxProvider?: any, userStore?: any): ComponentType<any> {
    const OriginalComponentClass = getComponentClassFunc();
    const NavigationComponent = ComponentWrapper.wrap(componentName, OriginalComponentClass, this.store, this.componentEventsObserver, ReduxProvider, userStore);
    this.store.setOriginalComponentClassForName(componentName, OriginalComponentClass);
    AppRegistry.registerComponent(componentName, () => NavigationComponent);
    return NavigationComponent;
  }
}
