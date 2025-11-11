import { ComponentProvider } from 'react-native';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { ComponentWrapper } from './ComponentWrapper';
import { AppRegistryService } from '../adapters/AppRegistryService';
export declare class ComponentRegistry {
    private store;
    private componentEventsObserver;
    private componentWrapper;
    private appRegistryService;
    constructor(store: Store, componentEventsObserver: ComponentEventsObserver, componentWrapper: ComponentWrapper, appRegistryService: AppRegistryService);
    registerComponent(componentName: string | number, componentProvider: ComponentProvider, concreteComponentProvider?: ComponentProvider, ReduxProvider?: any, reduxStore?: any): ComponentProvider;
}
