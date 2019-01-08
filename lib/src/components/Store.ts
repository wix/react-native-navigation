import * as _ from 'lodash';
import { ComponentProvider } from 'react-native';

export class Store {
  private componentsByName: Record<string, ComponentProvider> = {};
  private propsById: Record<string, any> = {};

  setPropsForId(componentId: string, props: any) {
    _.set(this.propsById, componentId, props);
  }

  getPropsForId(componentId: string) {
    return _.get(this.propsById, componentId, {});
  }

  cleanId(componentId: string) {
    _.unset(this.propsById, componentId);
  }

  setComponentClassForName(componentName: string | number, ComponentClass: ComponentProvider) {
    _.set(this.componentsByName, componentName.toString(), ComponentClass);
  }

  getComponentClassForName(componentName: string | number): ComponentProvider | undefined {
    return _.get(this.componentsByName, componentName.toString());
  }
}
