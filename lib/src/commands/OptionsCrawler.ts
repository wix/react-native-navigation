import type { ComponentType } from 'react';
import merge from 'lodash/merge';
import isFunction from 'lodash/isFunction';
import { Store } from '../components/Store';
import { Options } from '../interfaces/Options';
import {
  Layout,
  LayoutBottomTabs,
  LayoutComponent,
  LayoutSideMenu,
  LayoutSplitView,
  LayoutStack,
  LayoutTopTabs,
} from '../interfaces/Layout';
import { UniqueIdProvider } from 'react-native-navigation/adapters/UniqueIdProvider';
import { LayoutType } from './LayoutType';

type ComponentWithOptions = ComponentType<any> & { options(passProps: any): Options };

export class OptionsCrawler {
  constructor(public readonly store: Store, public readonly uniqueIdProvider: UniqueIdProvider) {
    this.crawl = this.crawl.bind(this);
  }

  crawl(api?: Layout): void {
    if (!api) return;
    if (api.topTabs) {
      this.topTabs(api.topTabs);
    } else if (api.sideMenu) {
      return this.sideMenu(api.sideMenu);
    } else if (api.bottomTabs) {
      return this.bottomTabs(api.bottomTabs);
    } else if (api.stack) {
      return this.stack(api.stack);
    } else if (api.component) {
      return this.component(api.component);
    } else if (api.splitView) {
      return this.splitView(api.splitView);
    }
  }

  private topTabs(api: LayoutTopTabs): void {
    api.children?.map(this.crawl);
  }

  private sideMenu(sideMenu: LayoutSideMenu): void {
    this.crawl(sideMenu.center);
    this.crawl(sideMenu.left);
    this.crawl(sideMenu.right);
  }

  private bottomTabs(bottomTabs: LayoutBottomTabs): void {
    bottomTabs.children?.map(this.crawl);
  }

  private stack(stack: LayoutStack): void {
    stack.children?.map(this.crawl);
  }

  private splitView(splitView: LayoutSplitView): void {
    splitView.detail && this.crawl(splitView.detail);
    splitView.master && this.crawl(splitView.master);
  }

  private component(component: LayoutComponent): void {
    this.applyComponentId(component);
    this.applyStaticOptions(component);
  }

  private applyComponentId(component: LayoutComponent): void {
    component.id = component.id || this.uniqueIdProvider.generate(LayoutType.Component);
  }

  private isComponentWithOptions(component: any): component is ComponentWithOptions {
    return (component as ComponentWithOptions).options !== undefined;
  }

  private applyStaticOptions(layout: LayoutComponent) {
    const staticOptions = this.staticOptionsIfPossible(layout);
    layout.options = merge({}, staticOptions, layout.options);
  }

  private staticOptionsIfPossible(layout: LayoutComponent) {
    const foundReactGenerator = this.store.getComponentClassForName(layout.name!);
    const reactComponent = foundReactGenerator ? foundReactGenerator() : undefined;
    if (reactComponent && this.isComponentWithOptions(reactComponent)) {
      return isFunction(reactComponent.options)
        ? reactComponent.options({ componentId: layout.id, ...layout.passProps } || {})
        : reactComponent.options;
    }
    return {};
  }
}
