import { ComponentProvider } from 'react-native';

export class Store {
  private componentsByName: Record<string, ComponentProvider> = {};
  private propsById: Record<string, any> = {};

  setPropsForId(componentId: string, props: any) {
    this.propsById[componentId] = props;
  }

  getPropsForId(componentId: string) {
    return this.propsById[componentId] || {};
  }

  cleanId(componentId: string) {
    delete this.propsById[componentId];
  }

  setComponentClassForName(componentName: string | number, ComponentClass: ComponentProvider) {
    this.componentsByName[componentName.toString()] = ComponentClass;
  }

  getComponentClassForName(componentName: string | number): ComponentProvider | undefined {
    return this.componentsByName[componentName.toString()];
  }
}
