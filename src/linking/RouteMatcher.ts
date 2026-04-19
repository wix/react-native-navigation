import { ScreensConfig, ScreenConfig, RouteNode, RouteMatch, RouteMatchSegment } from './types';

export class RouteMatcher {
  public buildRouteTree(screens: ScreensConfig): RouteNode[] {
    const nodes: RouteNode[] = [];
    for (const [screenName, config] of Object.entries(screens)) {
      const node = this.buildNode(screenName, config);
      nodes.push(node);
    }
    return nodes;
  }

  private buildNode(screenName: string, config: string | ScreenConfig): RouteNode {
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

  /**
   * Match a parsed path (e.g. "user/42") against the route tree.
   * Returns the full matched path as an array of segments with extracted params,
   * or null if no match.
   */
  public match(path: string, url: string, queryParams: Record<string, string>): RouteMatch | null {
    const pathSegments = path.split('/').filter((s) => s.length > 0);
    const tree = this.routeTree;
    if (!tree) return null;

    const result = this.matchRecursive(pathSegments, 0, tree);
    if (!result) return null;

    return {
      url,
      path: result,
      queryParams,
    };
  }

  private routeTree: RouteNode[] | null = null;

  public setRouteTree(tree: RouteNode[]): void {
    this.routeTree = tree;
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
    // Nodes with no pattern act as grouping containers — try children directly
    if (node.pattern === null && node.segments.length === 0) {
      if (node.children.length > 0) {
        const childMatch = this.matchRecursive(pathSegments, offset, node.children);
        if (childMatch) {
          return [{ screen: node.screen, params: {} }, ...childMatch];
        }
      }
      return null;
    }

    const params: Record<string, string> = {};
    const patternSegments = node.segments;

    if (offset + patternSegments.length > pathSegments.length) {
      return null;
    }

    for (let i = 0; i < patternSegments.length; i++) {
      const pattern = patternSegments[i];
      const actual = pathSegments[offset + i];
      if (pattern.startsWith(':')) {
        params[pattern.slice(1)] = decodeURIComponent(actual);
      } else if (pattern !== actual) {
        return null;
      }
    }

    const newOffset = offset + patternSegments.length;
    const segment: RouteMatchSegment = { screen: node.screen, params };

    // Exact match — all segments consumed and no children to match
    if (newOffset === pathSegments.length) {
      return [segment];
    }

    // Remaining segments — try matching children
    if (node.children.length > 0) {
      const childMatch = this.matchRecursive(pathSegments, newOffset, node.children);
      if (childMatch) {
        return [segment, ...childMatch];
      }
    }

    return null;
  }
}
