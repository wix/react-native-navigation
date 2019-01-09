import { ComponentProvider } from 'react-native';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { ComponentWrapper } from './ComponentWrapper';
import { AppRegistryService } from '../adapters/AppRegistryService';
import { Service, Inject } from 'typedi';

@Service('ComponentRegistry')
export class ComponentRegistry {
  constructor(
    @Inject('Store') private store: Store,
    @Inject('ComponentEventsObserver') private componentEventsObserver: ComponentEventsObserver,
    @Inject('ComponentWrapper') private componentWrapper: ComponentWrapper,
    @Inject('AppRegistryService') private appRegistryService: AppRegistryService,
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
