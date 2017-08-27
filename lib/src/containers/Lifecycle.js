class Lifecycle {
  constructor(store) {
    this.store = store;
    this.containerDidAppear = this.containerDidAppear.bind(this);
    this.containerDidDisappear = this.containerDidDisappear.bind(this);
    this.onNavigationButtonPressed = this.onNavigationButtonPressed.bind(this);
  }

  containerDidAppear(id) {
    const ref = this.store.getRefForContainerId(id);
    if (ref && ref.didAppear) {
      ref.didAppear();
    }
  }

  containerDidDisappear(id) {
    const ref = this.store.getRefForContainerId(id);
    if (ref && ref.didDisappear) {
      ref.didDisappear();
    }
  }

  onNavigationButtonPressed(params) {
    const ref = this.store.getRefForContainerId(params.containerId);
    if (ref && ref.onNavigationButtonPressed) {
      ref.onNavigationButtonPressed(params.buttonId);
    }
  }
}

module.exports = Lifecycle;
