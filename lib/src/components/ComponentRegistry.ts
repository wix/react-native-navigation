import { AppRegistry, ComponentProvider } from 'react-native';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { ComponentWrapper } from './ComponentWrapper';

export class ComponentRegistry {
  constructor(private readonly store: Store, private readonly componentEventsObserver: ComponentEventsObserver) { }

  registerComponent(componentName: string | number,
                    getComponentClassFunc: ComponentProvider,
                    componentWrapper: ComponentWrapper,
                    ComponentClass?: React.Component,
                    ReduxProvider?: any,
                    reduxStore?: any): ComponentProvider {
    const NavigationComponent = () => {
      return componentWrapper.wrap(componentName.toString(), getComponentClassFunc, this.store, this.componentEventsObserver, ComponentClass, ReduxProvider, reduxStore);
    };
    this.store.setComponentClassForName(componentName.toString(), NavigationComponent);
    AppRegistry.registerComponent(componentName.toString(), NavigationComponent);
    return NavigationComponent;
  }
}
