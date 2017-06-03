export default class Lifecycle {
  constructor(store) {
    this.store = store;
    this.containerStart = this.containerStart.bind(this);
    this.containerStop = this.containerStop.bind(this);
  }

  containerStart(id) {
    const ref = this.store.getRefForId(id);
    if (ref && ref.onStart) {
      ref.onStart();
    }
  }

  containerStop(id) {
    const ref = this.store.getRefForId(id);
    if (ref && ref.onStop) {
      ref.onStop();
    }
  }
}
