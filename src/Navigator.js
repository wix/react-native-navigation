class Navigator {
  constructor() {
    this.screens = {};
    
    this.addScreen = this.addScreen.bind(this);
    this.removeScreen = this.removeScreen.bind(this);

    this.push = this.push.bind(this);
    this.pop = this.pop.bind(this);
    this.showModal = this.showModal.bind(this);
    this.dismissModal = this.dismissModal.bind(this);
  }

  addScreen(containerId, navigator) {
    this.screens[containerId] = navigator;
  }

  removeScreen(containerId) {
    delete this.screens[containerId];
  }

  push(containerId, obj) {
    // this.screens[this.screens.length].navigator
    this.screens[containerId].push(obj);
  }

  pop(containerId, obj) {
    // this.screens[this.screens.length].navigator
    this.screens[containerId].pop(obj);
  }

  showModal(containerId, obj) {
    // this.screens[this.screens.length].navigator
    this.screens[containerId].showModal(obj);
  }

  dismissModal(containerId, obj) {
    // this.screens[this.screens.length].navigator
    this.screens[containerId].dismissModal(obj);
  }

  dismissAllModals(containerId, obj) {
    // this.screens[this.screens.length].navigator
    this.screens[containerId].dismissAllModals(obj);
  }

  getAllScreens() {
    return screens;
  }
}

const singleton = new Navigator();
export default singleton;