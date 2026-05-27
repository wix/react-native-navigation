import {
  ScreensConfig,
  ScreenConfig,
  RouteNode,
  RouteMatch,
  RouteMatchSegment,
} from './types';

/**
 * Compiles a `screens` config into a tree of `RouteNode`s and resolves
 * parsed paths to a matched `RouteMatch`.
 *
 * Matching is depth-first, in insertion order. The first node whose pattern
 * (and nested children) consume the entire path wins.
 */
export class RouteMatcher {
  private routeTree: RouteNode[] | null = null;

  public buildRouteTree(screens: ScreensConfig): RouteNode[] {
    const nodes: RouteNode[] = [];
    for (const [screenName, config] of Object.entries(screens)) {
      nodes.push(this.buildNode(screenName, config));
    }
    return nodes;
  }

  public setRouteTree(tree: RouteNode[]): void {
    this.routeTree = tree;
  }

  /**
   * Match a parsed path (e.g. `"user/42"`) against the configured tree.
   * Returns the matched chain or `null` when no route matches.
   */
  public match(
    path: string,
    url: string,
    queryParams: Record<string, string>
  ): RouteMatch | null {
    if (!this.routeTree) return null;

    const pathSegments = path.split('/').filter((s) => s.length > 0);
    const result = this.matchRecursive(pathSegments, 0, this.routeTree);
    if (!result) return null;

    return { url, path: result, queryParams };
  }

  private buildNode(screenName: string, config: ScreenConfig): RouteNode {
    if (typeof config === 'string') {
      return {
        screen: screenName,
        pattern: config,
        segments: this.splitPattern(config),
        children: [],
      };
    }

    const children = config.screens ? this.buildRouteTree(config.screens) : [];
    return {
      screen: screenName,
      pattern: config.path ?? null,
      segments: config.path ? this.splitPattern(config.path) : [],
      children,
    };
  }

  private splitPattern(pattern: string): string[] {
    return pattern.split('/').filter((s) => s.length > 0);
  }

  private matchRecursive(
    pathSegments: string[],
    offset: number,
    nodes: RouteNode[]
  ): RouteMatchSegment[] | null {
    for (const node of nodes) {
      const result = this.tryMatchNode(pathSegments, offset, node);
      if (result) return result;
    }
    return null;
  }

  private tryMatchNode(
    pathSegments: string[],
    offset: number,
    node: RouteNode
  ): RouteMatchSegment[] | null {
    if (node.pattern === null && node.segments.length === 0) {
      if (node.children.length === 0) return null;
      const childMatch = this.matchRecursive(pathSegments, offset, node.children);
      if (!childMatch) return null;
      return [{ screen: node.screen, params: {} }, ...childMatch];
    }

    const patternSegments = node.segments;
    if (offset + patternSegments.length > pathSegments.length) {
      return null;
    }

    const params: Record<string, string> = {};
    for (let i = 0; i < patternSegments.length; i++) {
      const pattern = patternSegments[i];
      const actual = pathSegments[offset + i];
      if (pattern.startsWith(':')) {
        params[pattern.slice(1)] = actual;
      } else if (pattern !== actual) {
        return null;
      }
    }

    const newOffset = offset + patternSegments.length;
    const segment: RouteMatchSegment = { screen: node.screen, params };

    if (newOffset === pathSegments.length) {
      return [segment];
    }

    if (node.children.length > 0) {
      const childMatch = this.matchRecursive(pathSegments, newOffset, node.children);
      if (childMatch) return [segment, ...childMatch];
    }

    return null;
  }
}
