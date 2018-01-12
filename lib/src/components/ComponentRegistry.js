const { AppRegistry } = require('react-native');
const ComponentWrapper = require('./ComponentWrapper');

class ComponentRegistry {
  constructor(store) {
    this.store = store;
  }

  registerComponent(componentName, getComponentClassFunc) {
    const OriginalComponentClass = getComponentClassFunc();
    const NavigationComponent = ComponentWrapper.wrap(componentName, OriginalComponentClass, this.store);
    this.store.setOriginalComponentClassForName(componentName, OriginalComponentClass);
    AppRegistry.registerComponent(componentName, () => NavigationComponent);
  }
}

module.exports = ComponentRegistry;
