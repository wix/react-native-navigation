export class Lifecycle {
  constructor(private readonly store) {
    this.componentDidAppear = this.componentDidAppear.bind(this);
    this.componentDidDisappear = this.componentDidDisappear.bind(this);
    this.onNavigationButtonPressed = this.onNavigationButtonPressed.bind(this);
  }

  private componentDidAppear(id) {
    const ref = this.store.getRefForComponentId(id);
    if (ref && ref.didAppear) {
      ref.didAppear();
    }
  }

  private componentDidDisappear(id) {
    const ref = this.store.getRefForComponentId(id);
    if (ref && ref.didDisappear) {
      ref.didDisappear();
    }
  }

  private onNavigationButtonPressed(params) {
    const ref = this.store.getRefForComponentId(params.componentId);
    if (ref && ref.onNavigationButtonPressed) {
      ref.onNavigationButtonPressed(params.buttonId);
    }
  }
}
