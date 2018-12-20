import * as React from 'react';
import * as _ from 'lodash';

export class Store {
  private componentsByName: Record<string, () => React.ComponentClass<any, any>> = {};
  private propsById: Record<string, any> = {};

  setPropsForId(componentId: string, props: any) {
    _.set(this.propsById, componentId, props);
  }

  getPropsForId(componentId: string) {
    return _.get(this.propsById, componentId, {});
  }

  setComponentClassForName(componentName: string | number, ComponentClass: () => React.ComponentClass<any, any>) {
    _.set(this.componentsByName, componentName.toString(), ComponentClass);
  }

  getComponentClassForName(componentName: string | number) {
    return _.get(this.componentsByName, componentName.toString());
  }

  cleanId(componentId: string) {
    _.unset(this.propsById, componentId);
  }
}
