import { ComponentProvider } from 'react-native';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { ComponentWrapper } from './ComponentWrapper';
import { AppRegistryService } from '../adapters/AppRegistryService';
import { Service, Inject } from 'typedi';

@Service()
export class ComponentRegistry {
  @Inject()
  public store!: Store;
  @Inject()
  public componentEventsObserver!: ComponentEventsObserver;
  @Inject()
  public componentWrapper!: ComponentWrapper;
  @Inject()
  public appRegistryService!: AppRegistryService;

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
    this.appRegistryService.registerComponent(componentName.toString(), NavigationComponent);
    return NavigationComponent;
  }
}
