import * as _ from 'lodash';

export class Store {
  private componentsByName = {};
  private propsById = {};
  private refsById = {};

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

  setRefForId(id: string, ref) {
    _.set(this.refsById, id, ref);
  }

  getRefForId(id: string) {
    return _.get(this.refsById, id);
  }

  cleanId(id: string) {
    _.unset(this.refsById, id);
    _.unset(this.propsById, id);
  }
}
