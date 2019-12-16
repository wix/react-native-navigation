import * as React from 'react';
import { ComponentProvider } from 'react-native';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
export interface IWrappedComponent extends React.Component {
    setProps(newProps: Record<string, any>): void;
}
export declare class ComponentWrapper {
    wrap(componentName: string | number, OriginalComponentGenerator: ComponentProvider, store: Store, componentEventsObserver: ComponentEventsObserver, concreteComponentProvider?: ComponentProvider, ReduxProvider?: any, reduxStore?: any): React.ComponentClass<any>;
    wrapWithRedux(WrappedComponent: React.ComponentClass<any>, ReduxProvider: any, reduxStore: any): React.ComponentClass<any>;
}
