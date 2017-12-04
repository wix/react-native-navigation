const _ = require('lodash');
const LayoutTypes = require('./LayoutTypes');

class LayoutTreeParser {
  /**
   * returns correct layout tree of {type, children, data?}
   */
  parseFromSimpleJSON(simpleJsonApi) {
    if (simpleJsonApi.sideMenu) {
      return this._createSideMenu(simpleJsonApi);
    }
    if (simpleJsonApi.bottomTabs) {
      return this._createTabs(simpleJsonApi.bottomTabs);
    }
    return this._createContainerStackWithContainerData(simpleJsonApi.container);
  }

  createContainer(data) {
    return {
      type: LayoutTypes.Container,
      data,
      children: []
    };
  }

  createDialogContainer(data) {
    return {
      type: LayoutTypes.CustomDialog,
      data,
      children: []
    };
  }

  _createTabs(tabs) {
    return {
      type: LayoutTypes.BottomTabs,
      children: _.map(tabs, (t) => this._createContainerStackWithContainerData(t.container))
    };
  }

  _createContainerStackWithContainerData(containerData) {
    return {
      type: LayoutTypes.ContainerStack,
      children: [this.createContainer(containerData)]
    };
  }

  _createSideMenu(layout) {
    return {
      type: LayoutTypes.SideMenuRoot,
      children: this._createSideMenuChildren(layout)
    };
  }

  _createSideMenuChildren(layout) {
    const children = [];
    if (layout.sideMenu.left) {
      children.push({
        type: LayoutTypes.SideMenuLeft,
        children: [this.createContainer(layout.sideMenu.left.container)]
      });
    }
    children.push({
      type: LayoutTypes.SideMenuCenter,
      children: [
        layout.bottomTabs ? this._createTabs(layout.bottomTabs) : this._createContainerStackWithContainerData(layout.container)
      ]
    });
    if (layout.sideMenu.right) {
      children.push({
        type: LayoutTypes.SideMenuRight,
        children: [this.createContainer(layout.sideMenu.right.container)]
      });
    }
    return children;
  }
}

module.exports = LayoutTreeParser;
