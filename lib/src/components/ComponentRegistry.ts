import { AppRegistry, ComponentProvider } from 'react-native';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { ComponentWrapperWithProviders } from './ComponentWrapperWithProviders';
import { ComponentWrapper } from './ComponentWrapper';

export class ComponentRegistry {
  constructor(private readonly store: Store, private readonly componentEventsObserver: ComponentEventsObserver) { }

  registerComponent(componentName: string | number,
                    getComponentClassFunc: ComponentProvider,
                    componentWrapper: ComponentWrapper): ComponentProvider {
    const NavigationComponent = () => {
      console.log('guyca', 'Is this even called?!?!');
      return componentWrapper.wrap(componentName.toString(), getComponentClassFunc, this.store, this.componentEventsObserver);
    };
    this.store.setComponentClassForName(componentName.toString(), NavigationComponent);
    AppRegistry.registerComponent(componentName.toString(), NavigationComponent);
    return NavigationComponent;
  }

  registerComponentWithProvider(componentName: string | number,
                                getComponentClassFunc: ComponentProvider,
                                componentWrapper: ComponentWrapperWithProviders): ComponentProvider {
    const NavigationComponent = () => {
      return componentWrapper.wrap(componentName.toString(), getComponentClassFunc, this.store, this.componentEventsObserver);
    };

    componentWrapper.do();
    this.store.setComponentClassForName(componentName.toString(), NavigationComponent);
    AppRegistry.registerComponent(componentName.toString(), NavigationComponent);
    return NavigationComponent;
  }
}
