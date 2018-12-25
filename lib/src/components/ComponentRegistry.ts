import { AppRegistry, ComponentProvider } from 'react-native';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { ComponentWrapper } from './ComponentWrapper';

export class ComponentRegistry {
  constructor(
    private store: Store,
    private componentEventsObserver: ComponentEventsObserver,
    private componentWrapper: ComponentWrapper
  ) {}

  registerComponent(
    componentName: string | number,
    componentProvider: ComponentProvider,
    concreteComponentProvider?: ComponentProvider,
    ReduxProvider?: any,
    reduxStore?: any
  ): ComponentProvider {
    const NavigationComponent = () => {
      return this.componentWrapper.wrap(
        componentName.toString(),
        componentProvider,
        this.store,
        this.componentEventsObserver,
        concreteComponentProvider,
        ReduxProvider,
        reduxStore
      );
    };
    this.store.setComponentClassForName(componentName.toString(), NavigationComponent);
    AppRegistry.registerComponent(componentName.toString(), NavigationComponent);
    return NavigationComponent;
  }
}
