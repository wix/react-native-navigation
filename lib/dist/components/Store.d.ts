import { ComponentProvider } from 'react-native';
export declare class Store {
    private componentsByName;
    private propsById;
    setPropsForId(componentId: string, props: any): void;
    getPropsForId(componentId: string): any;
    cleanId(componentId: string): void;
    setComponentClassForName(componentName: string | number, ComponentClass: ComponentProvider): void;
    getComponentClassForName(componentName: string | number): ComponentProvider | undefined;
}
