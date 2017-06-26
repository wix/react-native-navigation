class Navigator {
  constructor() {
    this.screens = {};
    
    this.addScreen = this.addScreen.bind(this);
    this.removeScreen = this.removeScreen.bind(this);
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

  getAllScreens() {
    return screens;
  }
}

const singleton = new Navigator();
export default singleton;