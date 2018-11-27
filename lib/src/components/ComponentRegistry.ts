import { AppRegistry, ComponentProvider } from 'react-native';
import { ComponentWrapper } from './ComponentWrapper';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';

export class ComponentRegistry {
  constructor(private readonly store: Store, private readonly componentEventsObserver: ComponentEventsObserver, private readonly ComponentWrapperClass: typeof ComponentWrapper) { }

  registerComponent(componentName: string | number, getComponentClassFunc: ComponentProvider, ReduxProvider?: any, reduxStore?: any): ComponentProvider {
    const NavigationComponent = () => {
      return this.ComponentWrapperClass.wrap(componentName.toString(), getComponentClassFunc, this.store, this.componentEventsObserver, ReduxProvider, reduxStore)
    };
    this.store.setComponentClassForName(componentName.toString(), NavigationComponent);
    AppRegistry.registerComponent(componentName.toString(), NavigationComponent);
    return NavigationComponent;
  }
}
