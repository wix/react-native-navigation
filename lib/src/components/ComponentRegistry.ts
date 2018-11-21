import { AppRegistry, ComponentProvider } from 'react-native';
import { ComponentWrapper } from './ComponentWrapper';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';

export class ComponentRegistry {
  constructor(private readonly store: Store, private readonly componentEventsObserver: ComponentEventsObserver, private readonly ComponentWrapperClass: typeof ComponentWrapper) { }

  registerComponent(componentName: string, getComponentClassFunc: ComponentProvider, ReduxProvider?: any, reduxStore?: any): ComponentProvider {
    const NavigationComponent = () => {
      return this.ComponentWrapperClass.wrap(componentName, getComponentClassFunc, this.store, this.componentEventsObserver, ReduxProvider, reduxStore)
    };
    this.store.setComponentClassForName(componentName, NavigationComponent);
    AppRegistry.registerComponent(componentName, NavigationComponent);
    return NavigationComponent;
  }
}
