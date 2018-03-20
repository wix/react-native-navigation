import * as _ from 'lodash';
import { LayoutType } from './LayoutType';
import { LayoutNode } from './LayoutTreeCrawler';

export class LayoutTreeParser {
  constructor() {
    this.parse = this.parse.bind(this);
  }

  parse(api): LayoutNode {
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
    } else if (api.externalComponent) {
      return this._externalComponent(api.externalComponent);
    }
    throw new Error(`unknown LayoutType "${_.keys(api)}"`);
  }

  _topTabs(api): LayoutNode {
    return {
      id: api.id,
      type: LayoutType.TopTabs,
      data: { options: api.options },
      children: _.map(api.children, this.parse)
    };
  }

  _sideMenu(api): LayoutNode {
    return {
      id: api.id,
      type: LayoutType.SideMenuRoot,
      data: { options: api.options },
      children: this._sideMenuChildren(api)
    };
  }

  _sideMenuChildren(api): LayoutNode[] {
    if (!api.center) {
      throw new Error(`sideMenu.center is required`);
    }
    const children: LayoutNode[] = [];
    if (api.left) {
      children.push({
        id: api.left.id,
        type: LayoutType.SideMenuLeft,
        data: {},
        children: [this.parse(api.left)]
      });
    }
    children.push({
      id: api.center.id,
      type: LayoutType.SideMenuCenter,
      data: {},
      children: [this.parse(api.center)]
    });
    if (api.right) {
      children.push({
        id: api.right.id,
        type: LayoutType.SideMenuRight,
        data: {},
        children: [this.parse(api.right)]
      });
    }
    return children;
  }

  _bottomTabs(api): LayoutNode {
    return {
      id: api.id,
      type: LayoutType.BottomTabs,
      data: { options: api.options },
      children: _.map(api.children, this.parse)
    };
  }

  _stack(api): LayoutNode {
    return {
      id: api.id,
      type: LayoutType.Stack,
      data: { name: api.name, options: api.options },
      children: _.map(api.children, this.parse)
    };
  }

  _component(api): LayoutNode {
    return {
      id: api.id,
      type: LayoutType.Component,
      data: { name: api.name, options: api.options, passProps: api.passProps },
      children: []
    };
  }

  _externalComponent(api): LayoutNode {
    return {
      id: api.id,
      type: LayoutType.ExternalComponent,
      data: { name: api.name, options: api.options, passProps: api.passProps },
      children: []
    };
  }
}
