import { Linking } from 'react-native';
import { Layout } from '../interfaces/Layout';
import { URLParser } from './URLParser';
import { RouteMatcher } from './RouteMatcher';
import { DeferredLinkQueue } from './DeferredLinkQueue';
import { ModalLayoutBuilder } from './ModalLayoutBuilder';
import { LinkingConfig, RouteMatch } from './types';

/** A function that shows a layout as a modal. Mirrors `Commands.showModal`. */
export type ShowModal = (layout: Layout) => Promise<string>;

/**
 * Minimal abstraction over React Native's `Linking` module so the handler
 * can be tested without touching native bindings.
 */
export interface LinkingAPI {
  addEventListener(
    type: 'url',
    handler: (event: { url: string }) => void
  ): { remove: () => void };
  getInitialURL(): Promise<string | null>;
}

/**
 * Orchestrates deep linking:
 *
 *   1. Subscribes to URL events from `Linking` and the initial URL.
 *   2. Parses + matches each URL against the configured `screens` tree.
 *   3. Defers processing until both:
 *        - the first `setRoot` has resolved (so a modal can be presented), and
 *        - the user-supplied `isReady` predicate (if any) returns `true`.
 *   4. Resolves matches in this order:
 *        - `onLink(match)` if provided (full escape hatch), else
 *        - `getModal(match)` if provided, else
 *        - the default `ModalLayoutBuilder` output.
 *      Result is presented via `showModal`.
 *   5. Calls `fallback(url)` when a URL has no matching prefix or route.
 */
export class LinkingHandler {
  private readonly urlParser: URLParser;
  private readonly routeMatcher: RouteMatcher;
  private readonly deferredQueue: DeferredLinkQueue;
  private readonly modalLayoutBuilder: ModalLayoutBuilder;
  private readonly showModal: ShowModal;
  private readonly linkingAPI: LinkingAPI;

  private config: LinkingConfig | null = null;
  private linkingSubscription: { remove: () => void } | null = null;
  private rootReady = false;
  private userReadyOverride: boolean | null = null;

  constructor(showModal: ShowModal, linkingAPI?: LinkingAPI) {
    this.urlParser = new URLParser();
    this.routeMatcher = new RouteMatcher();
    this.deferredQueue = new DeferredLinkQueue();
    this.modalLayoutBuilder = new ModalLayoutBuilder();
    this.showModal = showModal;
    this.linkingAPI = linkingAPI || (Linking as unknown as LinkingAPI);
    this.deferredQueue.setFlushCallback((url) => this.processURL(url));
  }

  public configure(config: LinkingConfig): void {
    this.teardown();
    this.config = config;

    const tree = this.routeMatcher.buildRouteTree(config.config.screens);
    this.routeMatcher.setRouteTree(tree);

    this.refreshReady();
    this.subscribe();
  }

  /**
   * Manually feed a URL into the pipeline as if it came from the OS.
   * Useful for URLs received from push notifications or other sources.
   */
  public handleURL(url: string): void {
    if (!this.config) return;
    if (!this.isReady()) {
      this.deferredQueue.enqueue(url);
      return;
    }
    this.processURL(url);
  }

  /**
   * Called by `NavigationRoot` after the first `setRoot` resolves. Subsequent
   * calls are no-ops.
   */
  public setRootReady(): void {
    if (this.rootReady) return;
    this.rootReady = true;
    this.refreshReady();
  }

  /**
   * Public toggle for the app-supplied readiness gate. Overrides the
   * `config.isReady` predicate from the moment it's first called.
   */
  public setLinkingReady(ready: boolean): void {
    this.userReadyOverride = ready;
    this.refreshReady();
  }

  /**
   * Resolve a URL to a `RouteMatch` without side effects. Returns `null`
   * when the URL doesn't match any configured prefix or route.
   */
  public resolve(url: string): RouteMatch | null {
    if (!this.config) return null;
    const parsed = this.urlParser.parse(url, this.config.prefixes);
    if (!parsed) return null;
    return this.routeMatcher.match(parsed.path, url, parsed.queryParams);
  }

  public teardown(): void {
    if (this.linkingSubscription) {
      this.linkingSubscription.remove();
      this.linkingSubscription = null;
    }
    this.deferredQueue.clear();
    this.config = null;
    this.userReadyOverride = null;
  }

  private subscribe(): void {
    this.linkingSubscription = this.linkingAPI.addEventListener('url', (event) => {
      this.handleURL(event.url);
    });

    this.linkingAPI.getInitialURL().then((url) => {
      if (url) this.handleURL(url);
    });
  }

  private processURL(url: string): void {
    if (!this.config) return;

    const match = this.resolve(url);
    if (!match) {
      this.config.fallback?.(url);
      return;
    }

    if (this.config.onLink) {
      this.config.onLink(match);
      return;
    }

    const layout = this.config.getModal
      ? this.config.getModal(match)
      : this.modalLayoutBuilder.build(match);

    if (layout) {
      this.showModal(layout);
    }
  }

  private isReady(): boolean {
    if (!this.rootReady) return false;
    if (this.userReadyOverride !== null) return this.userReadyOverride;
    if (this.config?.isReady) return this.config.isReady();
    return true;
  }

  private refreshReady(): void {
    this.deferredQueue.setReady(this.isReady());
  }
}
