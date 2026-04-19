import { LayoutRoot } from '../interfaces/Layout';

export interface ScreenConfig {
  path?: string;
  screens?: ScreensConfig;
}

export interface ScreensConfig {
  [screenName: string]: string | ScreenConfig;
}

export interface LinkingConfig {
  prefixes: string[];
  config: {
    screens: ScreensConfig;
  };
  /**
   * Build the LayoutRoot for a matched deep link. Receives the full match
   * result including the matched screen path and extracted params.
   * Return a LayoutRoot to call setRoot, or return undefined to skip.
   */
  getLayout?: (match: RouteMatch) => LayoutRoot | undefined;
  /**
   * Custom handler called instead of auto-setRoot. Use when you need
   * to call push/showModal/etc. instead of setRoot.
   * When provided, getLayout is ignored.
   */
  onLink?: (match: RouteMatch) => void;
  /**
   * Predicate checked before processing a deep link. When it returns false,
   * the link is queued and replayed when setLinkingReady(true) is called.
   */
  isReady?: () => boolean;
}

export interface RouteMatchSegment {
  screen: string;
  params: Record<string, string>;
}

export interface RouteMatch {
  url: string;
  path: RouteMatchSegment[];
  queryParams: Record<string, string>;
}

export interface ParsedURL {
  path: string;
  queryParams: Record<string, string>;
}

export interface RouteNode {
  screen: string;
  pattern: string | null;
  segments: string[];
  children: RouteNode[];
}
