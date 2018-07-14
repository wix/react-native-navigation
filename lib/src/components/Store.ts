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

  setOriginalComponentClassForName(componentName: string, ComponentClass) {
    _.set(this.componentsByName, componentName, ComponentClass);
  }

  getOriginalComponentClassForName(componentName: string) {
    return _.get(this.componentsByName, componentName);
  }

  cleanId(id: string) {
    _.unset(this.propsById, id);
  }
}
