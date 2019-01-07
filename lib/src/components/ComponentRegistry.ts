import { ComponentProvider } from 'react-native';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { ComponentWrapper } from './ComponentWrapper';
import { AppRegistryService } from '../adapters/AppRegistryService';
import { Service } from 'typedi';

@Service('ComponentRegistry')
export class ComponentRegistry {
  constructor(
    public store: Store,
    public componentEventsObserver: ComponentEventsObserver,
    public componentWrapper: ComponentWrapper,
    public appRegistryService: AppRegistryService,
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
    this.appRegistryService.registerComponent(componentName.toString(), NavigationComponent);
    return NavigationComponent;
  }
}
