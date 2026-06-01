import { Layout } from '../interfaces/Layout';

/**
 * A single matched segment of a deep link route.
 */
export interface RouteMatchSegment {
  /** Name of the registered RNN component this segment maps to. */
  screen: string;
  /** Path parameters extracted from this segment's pattern (e.g. `:id`). */
  params: Record<string, string>;
}

/**
 * Result of matching a URL against the configured `screens` tree.
 */
export interface RouteMatch {
  /** Original URL that was matched. */
  url: string;
  /**
   * Ordered chain of matched screens, from outermost to innermost.
   * For a nested route like `settings/notifications`, this contains
   * `[{ screen: 'Settings' }, { screen: 'Notifications' }]`.
   */
  path: RouteMatchSegment[];
  /** Query string parameters from the URL. */
  queryParams: Record<string, string>;
}

/**
 * Internal representation of a parsed URL after prefix stripping.
 */
export interface ParsedURL {
  /** Decoded path with leading/trailing slashes removed. */
  path: string;
  /** Query string parameters. */
  queryParams: Record<string, string>;
}

/**
 * Internal compiled representation of a single screen entry in the route tree.
 */
export interface RouteNode {
  /** Screen name (the key from `screens`). */
  screen: string;
  /** Path pattern as written by the user, or `null` for grouping nodes. */
  pattern: string | null;
  /** Pattern split into segments, ready for matching. */
  segments: string[];
  /** Nested children. */
  children: RouteNode[];
}

/**
 * Per-screen configuration. Either a path pattern string, or an object with
 * an optional path and nested screens.
 *
 * Examples:
 *   `'home'`                                   - leaf route
 *   `'user/:id'`                               - leaf with path parameter
 *   `{ path: 'settings', screens: {...} }`     - nested route
 *   `{ screens: {...} }`                       - grouping node (no path)
 */
export type ScreenConfig =
  | string
  | {
      path?: string;
      screens?: ScreensConfig;
    };

export interface ScreensConfig {
  [screenName: string]: ScreenConfig;
}

/**
 * Configuration passed to `Navigation.setLinking`.
 *
 * When a deep link is received, RNN parses it, matches it against the
 * `screens` tree, and by default presents the matched chain as a modal
 * (wrapped in a stack so a topBar is available for a close button).
 *
 * To customize the modal layout, provide `getModal`. To bypass the modal
 * behavior entirely (e.g. push onto an existing stack), provide `onLink`.
 */
export interface LinkingConfig {
  /** URL prefixes your app handles (custom schemes and universal-link hosts). */
  prefixes: string[];
  /** Screen-to-path mapping. */
  config: {
    screens: ScreensConfig;
  };
  /**
   * Customize the modal layout for a matched route. Return `undefined` to
   * skip presenting a modal for this match (useful for conditional skipping).
   * When omitted, the default builder wraps the matched chain in a stack.
   */
  getModal?: (match: RouteMatch) => Layout | undefined;
  /**
   * Full escape hatch. When provided, RNN does not present a modal; the
   * handler is responsible for executing whatever navigation commands it
   * wants (push, setRoot, dismissAllModals + showModal, etc.).
   * When set, `getModal` is ignored.
   */
  onLink?: (match: RouteMatch) => void;
  /**
   * Called when a received URL does not match any configured route or has
   * no matching prefix. Useful for logging or routing to a "not found" flow.
   */
  fallback?: (url: string) => void;
  /**
   * Predicate evaluated before each link is processed. When it returns
   * `false`, the link is queued and replayed once `setLinkingReady(true)`
   * is called. Use this to defer links until e.g. authentication completes.
   */
  isReady?: () => boolean;
}
