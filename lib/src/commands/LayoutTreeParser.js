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
    if (simpleJsonApi.topTabs) {
      return this._createTopTabsContainer(simpleJsonApi.topTabs);
    }
    if (simpleJsonApi.name) {
      return this._createContainer(simpleJsonApi);
    }
    return this._createContainerStackWithContainerData(simpleJsonApi.container);
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

  _createTopTabsContainer(topTabs) {
    return {
      type: LayoutTypes.TopTabsContainer,
      children: _.map(topTabs, (t) => this._createTopTab(t.container))
    };
  }

  _createTopTab(data) {
    return {
      type: LayoutTypes.TopTab,
      data,
      children: []
    };
  }

  _createContainerStackWithContainerData(containerData) {
    return {
      type: LayoutTypes.ContainerStack,
      children: [this._createContainer(containerData)]
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
        children: [this._createContainer(layout.sideMenu.left.container)]
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
        children: [this._createContainer(layout.sideMenu.right.container)]
      });
    }
    return children;
  }

  _createContainer(data) {
    return {
      type: LayoutTypes.Container,
      data,
      children: []
    };
  }
}

module.exports = LayoutTreeParser;
