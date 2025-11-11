import * as React from 'react';
import { ComponentProvider } from 'react-native';
import { IWrappedComponent } from './ComponentWrapper';
export declare class Store {
    private componentsByName;
    private propsById;
    private pendingPropsById;
    private componentsInstancesById;
    private wrappedComponents;
    private lazyRegistratorFn;
    updateProps(componentId: string, props: any, callback?: () => void): void;
    setPendingProps(componentId: string, newProps: any): void;
    getPropsForId(componentId: string): any;
    consumePendingProps(componentId: string): void;
    mergeNewPropsForId(componentId: string, newProps: any): void;
    clearComponent(componentId: string): void;
    setComponentClassForName(componentName: string | number, ComponentClass: ComponentProvider): void;
    getComponentClassForName(componentName: string | number): ComponentProvider | undefined;
    ensureClassForName(componentName: string | number): void;
    setComponentInstance(id: string, component: IWrappedComponent): void;
    getComponentInstance(id: string): IWrappedComponent | undefined;
    setWrappedComponent(componentName: string | number, wrappedComponent: React.ComponentClass<any>): void;
    hasRegisteredWrappedComponent(componentName: string | number): boolean;
    getWrappedComponent(componentName: string | number): React.ComponentClass<any>;
    setLazyComponentRegistrator(lazyRegistratorFn: (lazyComponentRequest: string | number) => void): void;
}
