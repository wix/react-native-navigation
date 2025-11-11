import { ComponentProvider } from 'react-native';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { ComponentWrapper } from './ComponentWrapper';
import { AppRegistryService } from '../adapters/AppRegistryService';

export class ComponentRegistry {
  constructor(
    private store: Store,
    private componentEventsObserver: ComponentEventsObserver,
    private componentWrapper: ComponentWrapper,
    private appRegistryService: AppRegistryService
  ) {}

  registerComponent(
    componentName: string | number,
    componentProvider: ComponentProvider,
    concreteComponentProvider?: ComponentProvider,
    ReduxProvider?: any,
    reduxStore?: any
  ): ComponentProvider {
    const NavigationComponent = () => {
      if (this.store.hasRegisteredWrappedComponent(componentName)) {
        return this.store.getWrappedComponent(componentName);
      } else {
        const wrappedComponent = this.componentWrapper.wrap(
          componentName.toString(),
          componentProvider,
          this.store,
          this.componentEventsObserver,
          concreteComponentProvider,
          ReduxProvider,
          reduxStore
        );
        this.store.setWrappedComponent(componentName, wrappedComponent);
        return wrappedComponent;
      }
    };
    this.store.setComponentClassForName(componentName.toString(), NavigationComponent);
    this.appRegistryService.registerComponent(componentName.toString(), NavigationComponent);
    return NavigationComponent;
  }
}
