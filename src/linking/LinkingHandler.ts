import { Linking } from 'react-native';
import { URLParser } from './URLParser';
import { RouteMatcher } from './RouteMatcher';
import { DeferredLinkQueue } from './DeferredLinkQueue';
import { LinkingConfig, RouteMatch } from './types';

type SetRootFn = (layout: any) => Promise<string>;

export interface LinkingAPI {
  addEventListener: (type: string, handler: (event: { url: string }) => void) => { remove: () => void };
  getInitialURL: () => Promise<string | null>;
}

export class LinkingHandler {
  private readonly urlParser: URLParser;
  private readonly routeMatcher: RouteMatcher;
  private readonly deferredQueue: DeferredLinkQueue;
  private config: LinkingConfig | null = null;
  private linkingSubscription: { remove: () => void } | null = null;
  private setRoot: SetRootFn;
  private linkingAPI: LinkingAPI;

  constructor(setRoot: SetRootFn, linkingAPI?: LinkingAPI) {
    this.urlParser = new URLParser();
    this.routeMatcher = new RouteMatcher();
    this.deferredQueue = new DeferredLinkQueue();
    this.setRoot = setRoot;
    this.linkingAPI = linkingAPI || Linking;

    this.deferredQueue.setFlushCallback((url) => this.processURL(url));
  }

  public configure(config: LinkingConfig): void {
    this.teardown();
    this.config = config;

    const tree = this.routeMatcher.buildRouteTree(config.config.screens);
    this.routeMatcher.setRouteTree(tree);

    if (config.isReady) {
      this.deferredQueue.setReady(config.isReady());
    } else {
      this.deferredQueue.setReady(true);
    }

    this.subscribe();
  }

  private subscribe(): void {
    this.linkingSubscription = this.linkingAPI.addEventListener('url', (event) => {
      this.handleURL(event.url);
    });

    this.linkingAPI.getInitialURL().then((url) => {
      if (url) {
        this.handleURL(url);
      }
    });
  }

  public handleURL(url: string): void {
    if (!this.config) return;

    if (this.config.isReady && !this.config.isReady()) {
      this.deferredQueue.process(url);
      return;
    }

    this.processURL(url);
  }

  private processURL(url: string): void {
    if (!this.config) return;

    const match = this.resolve(url);
    if (!match) return;

    if (this.config.onLink) {
      this.config.onLink(match);
      return;
    }

    if (this.config.getLayout) {
      const layout = this.config.getLayout(match);
      if (layout) {
        this.setRoot(layout);
      }
    }
  }

  public resolve(url: string): RouteMatch | null {
    if (!this.config) return null;

    const parsed = this.urlParser.parse(url, this.config.prefixes);
    if (!parsed) return null;

    return this.routeMatcher.match(parsed.path, url, parsed.queryParams);
  }

  public setReady(ready: boolean): void {
    this.deferredQueue.setReady(ready);
  }

  public teardown(): void {
    if (this.linkingSubscription) {
      this.linkingSubscription.remove();
      this.linkingSubscription = null;
    }
    this.deferredQueue.clear();
    this.config = null;
  }
}
