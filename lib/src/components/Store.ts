import * as _ from 'lodash';

export class Store {
  private propsByComponentId: {};
  private componentsByName: {};
  private refsById: {};

  constructor() {
    this.propsByComponentId = {};
    this.componentsByName = {};
    this.refsById = {};
  }

  setPropsForComponentId(componentId, props) {
    _.set(this.propsByComponentId, componentId, props);
  }

  getPropsForComponentId(componentId) {
    return _.get(this.propsByComponentId, componentId, {});
  }

  setOriginalComponentClassForName(componentName, ComponentClass) {
    _.set(this.componentsByName, componentName, ComponentClass);
  }

  getOriginalComponentClassForName(componentName) {
    return _.get(this.componentsByName, componentName);
  }

  setRefForComponentId(id, ref) {
    _.set(this.refsById, id, ref);
  }

  getRefForComponentId(id) {
    return _.get(this.refsById, id);
  }

  cleanId(id) {
    _.unset(this.refsById, id);
    _.unset(this.propsByComponentId, id);
  }
}
