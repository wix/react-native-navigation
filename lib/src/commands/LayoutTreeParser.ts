import { LayoutType } from './LayoutType';
import { LayoutNode } from './LayoutTreeCrawler';
import { Deprecations } from './Deprecations';
import {
  Layout,
  LayoutTopTabs,
  LayoutComponent,
  LayoutStack,
  LayoutBottomTabs,
  LayoutSideMenu,
  LayoutSplitView,
  ExternalComponent,
} from '../interfaces/Layout';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';

export class LayoutTreeParser {
  constructor(private uniqueIdProvider: UniqueIdProvider, private deprecations: Deprecations) {
    this.parse = this.parse.bind(this);
  }

  public parse(api: Layout): LayoutNode {
    if (api.topTabs) {
      return this.topTabs(api.topTabs);
    } else if (api.sideMenu) {
      return this.sideMenu(api.sideMenu);
    } else if (api.bottomTabs) {
      return this.bottomTabs(api.bottomTabs);
    } else if (api.stack) {
      return this.stack(api.stack);
    } else if (api.component) {
      return this.component(api.component);
    } else if (api.externalComponent) {
      return this.externalComponent(api.externalComponent);
    } else if (api.splitView) {
      if (api.splitView.master || api.splitView.detail) {
        // Deprecated
        this.deprecations.onParseLayout(api);
      }
      return this.splitView(api.splitView);
    }
    throw new Error(`unknown LayoutType "${Object.keys(api)}"`);
  }

  private topTabs(api: LayoutTopTabs): LayoutNode {
    return {
      id: api.id || this.uniqueIdProvider.generate(LayoutType.TopTabs),
      type: LayoutType.TopTabs,
      data: { options: api.options },
      children: api.children ? api.children.map(this.parse) : [],
    };
  }

  private sideMenu(api: LayoutSideMenu): LayoutNode {
    return {
      id: api.id || this.uniqueIdProvider.generate(LayoutType.SideMenuRoot),
      type: LayoutType.SideMenuRoot,
      data: { options: api.options },
      children: this.sideMenuChildren(api),
    };
  }

  private sideMenuChildren(api: LayoutSideMenu): LayoutNode[] {
    const children: LayoutNode[] = [];
    if (api.left) {
      children.push({
        id: this.uniqueIdProvider.generate(LayoutType.SideMenuLeft),
        type: LayoutType.SideMenuLeft,
        data: {},
        children: [this.parse(api.left)],
      });
    }
    children.push({
      id: this.uniqueIdProvider.generate(LayoutType.SideMenuCenter),
      type: LayoutType.SideMenuCenter,
      data: {},
      children: [this.parse(api.center)],
    });
    if (api.right) {
      children.push({
        id: this.uniqueIdProvider.generate(LayoutType.SideMenuRight),
        type: LayoutType.SideMenuRight,
        data: {},
        children: [this.parse(api.right)],
      });
    }
    return children;
  }

  private bottomTabs(api: LayoutBottomTabs): LayoutNode {
    return {
      id: api.id || this.uniqueIdProvider.generate(LayoutType.BottomTabs),
      type: LayoutType.BottomTabs,
      data: { options: api.options },
      children: api.children ? api.children.map(this.parse) : [],
    };
  }

  private stack(api: LayoutStack): LayoutNode {
    return {
      id: api.id || this.uniqueIdProvider.generate(LayoutType.Stack),
      type: LayoutType.Stack,
      data: { options: api.options },
      children: api.children ? api.children.map(this.parse) : [],
    };
  }

  private component(api: LayoutComponent): LayoutNode {
    return {
      id: api.id || this.uniqueIdProvider.generate(LayoutType.Component),
      type: LayoutType.Component,
      data: {
        name: api.name.toString(),
        options: api.options,
        passProps: api.passProps,
      },
      children: [],
    };
  }

  private externalComponent(api: ExternalComponent): LayoutNode {
    return {
      id: api.id || this.uniqueIdProvider.generate(LayoutType.ExternalComponent),
      type: LayoutType.ExternalComponent,
      data: {
        name: api.name.toString(),
        options: api.options,
        passProps: api.passProps,
      },
      children: [],
    };
  }

  private splitView(api: LayoutSplitView): LayoutNode {
    return {
      id: api.id || this.uniqueIdProvider.generate(LayoutType.SplitView),
      type: LayoutType.SplitView,
      data: { options: api.options },
      children: this.splitViewChildren(api),
    };
  }

  private splitViewChildren(api: LayoutSplitView): LayoutNode[] {
    const children: LayoutNode[] = [];
    if (api.primary) {
      children.push(this.parse(api.primary));
    } else if (api.master) {
      // Deprecated -- treat as `primary`
      children.push(this.parse(api.master));
    }
    if (api.supplementary) {
      children.push(this.parse(api.supplementary));
    }
    if (api.secondary) {
      children.push(this.parse(api.secondary));
    } else if (api.detail) {
      // Deprecated -- treat as `secondary`
      children.push(this.parse(api.detail));
    }
    return children;
  }
}
