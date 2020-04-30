import { ComponentProvider } from 'react-native';
import { IWrappedComponent } from './ComponentWrapper';
export declare class Store {
    private componentsByName;
    private propsById;
    private componentsInstancesById;
    updateProps(componentId: string, props: any): void;
    getPropsForId(componentId: string): any;
    clearComponent(componentId: string): void;
    setComponentClassForName(componentName: string | number, ComponentClass: ComponentProvider): void;
    getComponentClassForName(componentName: string | number): ComponentProvider | undefined;
    setComponentInstance(id: string, component: IWrappedComponent): void;
    getComponentInstance(id: string): IWrappedComponent;
}
