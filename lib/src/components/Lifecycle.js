class Lifecycle {
  constructor(store) {
    this.store = store;
    this.componentDidAppear = this.componentDidAppear.bind(this);
    this.componentDidDisappear = this.componentDidDisappear.bind(this);
    this.onNavigationButtonPressed = this.onNavigationButtonPressed.bind(this);
  }

  componentDidAppear(id) {
    const ref = this.store.getRefForComponentId(id);
    if (ref && ref.didAppear) {
      ref.didAppear();
    }
  }

  componentDidDisappear(id) {
    const ref = this.store.getRefForComponentId(id);
    if (ref && ref.didDisappear) {
      ref.didDisappear();
    }
  }

  onNavigationButtonPressed(params) {
    const ref = this.store.getRefForComponentId(params.componentId);
    if (ref && ref.onNavigationButtonPressed) {
      ref.onNavigationButtonPressed(params.buttonId);
    }
  }
}

module.exports = Lifecycle;
