const _ = require('lodash');
const LayoutTypes = require('./LayoutTypes');

class LayoutTreeParser {
  constructor() {
    this.parse = this.parse.bind(this);
  }

  /**
   * returns correct layout tree of nodes which are { type, data, children }
   */
  parse(api) {
    if (api.topTabs) {
      return this._topTabs(api.topTabs);
    } else if (api.sideMenu) {
      return this._sideMenu(api.sideMenu);
    } else if (api.bottomTabs) {
      return this._bottomTabs(api.bottomTabs);
    } else if (api.stack) {
      return this._stack(api.stack);
    } else if (api.component) {
      return this._component(api.component);
    }
    throw new Error(`unknown LayoutType "${_.keys(api)}"`);
  }

  _topTabs(api) {
    return {
      type: LayoutTypes.TopTabs,
      data: { options: api.options },
      children: _.map(api.children, this.parse)
    };
  }

  _sideMenu(api) {
    return {
      type: LayoutTypes.SideMenuRoot,
      data: { options: api.options },
      children: this._sideMenuChildren(api)
    };
  }

  _sideMenuChildren(api) {
    if (!api.center) {
      throw new Error(`sideMenu.center is required`);
    }
    const children = [];
    if (api.left) {
      children.push({
        type: LayoutTypes.SideMenuLeft,
        data: {},
        children: [this.parse(api.left)]
      });
    }
    children.push({
      type: LayoutTypes.SideMenuCenter,
      data: {},
      children: [this.parse(api.center)]
    });
    if (api.right) {
      children.push({
        type: LayoutTypes.SideMenuRight,
        data: {},
        children: [this.parse(api.right)]
      });
    }
    return children;
  }

  _bottomTabs(api) {
    return {
      type: LayoutTypes.BottomTabs,
      data: { options: api.options },
      children: _.map(api.children, this.parse)
    };
  }

  _stack(api) {
    return {
      type: LayoutTypes.Stack,
      data: { name: api.name, options: api.options },
      children: _.map(api.children, this.parse)
    };
  }

  _component(api) {
    return {
      type: LayoutTypes.Component,
      data: { name: api.name, options: api.options, passProps: api.passProps },
      children: []
    };
  }
}

module.exports = LayoutTreeParser;
