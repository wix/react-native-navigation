import * as _ from 'lodash';

export class Store {
  private componentsByName = {};
  private propsById = {};

  setPropsForId(componentId: string, props) {
    _.set(this.propsById, componentId, props);
  }

  getPropsForId(componentId: string) {
    return _.get(this.propsById, componentId, {});
  }


  setComponentClassForName(componentName: string | number, ComponentClass) {
    _.set(this.componentsByName, componentName.toString(), ComponentClass);
  }

  getComponentClassForName(componentName: string | number) {
    return _.get(this.componentsByName, componentName.toString());
  }
  
  cleanId(id: string) {
    _.unset(this.propsById, id);
  }
}
