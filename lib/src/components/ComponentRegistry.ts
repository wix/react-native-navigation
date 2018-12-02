import { AppRegistry, ComponentProvider } from 'react-native';
import { ComponentWrapper } from './ComponentWrapper';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';

export class ComponentRegistry {
  constructor(private readonly store: Store, private readonly componentEventsObserver: ComponentEventsObserver) { }

  registerComponent(componentName: string | number,
                    getComponentClassFunc: ComponentProvider,
                    componentWrapper: ComponentWrapper): ComponentProvider {
    const NavigationComponent = () => {
      return componentWrapper.wrap(componentName.toString(), getComponentClassFunc, this.store, this.componentEventsObserver);
    };
    this.store.setComponentClassForName(componentName.toString(), NavigationComponent);
    AppRegistry.registerComponent(componentName.toString(), NavigationComponent);
    return NavigationComponent;
  }
}
