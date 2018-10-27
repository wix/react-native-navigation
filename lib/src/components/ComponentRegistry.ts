import { AppRegistry, ComponentProvider } from 'react-native';
import { ComponentWrapper } from './ComponentWrapper';
import { ComponentType } from 'react';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { Provider } from 'react-redux';
import { Store as ReduxStore } from 'redux';

export class ComponentRegistry {
  constructor(private readonly store: Store, private readonly componentEventsObserver: ComponentEventsObserver) { }

  registerComponent(componentName: string, getComponentClassFunc: ComponentProvider, ReduxProvider?: typeof Provider, reduxStore?: ReduxStore<any>): ComponentType<any> {
    const OriginalComponentClass = getComponentClassFunc();
    const NavigationComponent = ComponentWrapper.wrap(componentName, OriginalComponentClass, this.store, this.componentEventsObserver, ReduxProvider, reduxStore);
    this.store.setOriginalComponentClassForName(componentName, OriginalComponentClass);
    AppRegistry.registerComponent(componentName, () => NavigationComponent);
    return NavigationComponent;
  }
}
