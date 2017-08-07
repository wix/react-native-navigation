const { AppRegistry } = require('react-native');
const ContainerWrapper = require('./ContainerWrapper');

class ContainerRegistry {
  constructor(store) {
    this.store = store;
  }

  registerContainer(containerName, getContainerFunc) {
    const OriginalContainer = getContainerFunc();
    const NavigationContainer = ContainerWrapper.wrap(containerName, OriginalContainer, this.store);
    this.store.setOriginalContainerClassForName(containerName, OriginalContainer);
    AppRegistry.registerComponent(containerName, () => NavigationContainer);
  }
}

module.exports = ContainerRegistry;
