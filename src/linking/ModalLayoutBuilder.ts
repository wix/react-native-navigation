import { Layout } from '../interfaces/Layout';
import { RouteMatch, RouteMatchSegment } from './types';

/**
 * Props that React treats specially and must never be forwarded as
 * regular component props. If a URL contains query parameters or path
 * parameters using these names, they are dropped from `passProps`
 * (with a dev-mode warning) to avoid crashing the screen.
 */
const RESERVED_PROP_NAMES = new Set(['ref', 'key']);

/**
 * Builds the default modal layout for a matched deep link.
 *
 * Behavior:
 *   - Wraps the matched chain in a `stack`. Even a single-component match
 *     gets wrapped so the screen has a topBar where the user can mount a
 *     close button via `topBar.leftButtons`.
 *   - Each segment becomes a `component` child of the stack, in match order.
 *     The first segment is the root of the stack; subsequent segments are
 *     pushed on top.
 *   - Path params for each segment are merged with query params and passed
 *     as `passProps` to that segment's component. Path params win on key
 *     collision. React-reserved keys (`ref`, `key`) are filtered out.
 */
export class ModalLayoutBuilder {
  public build(match: RouteMatch): Layout {
    return {
      stack: {
        children: match.path.map((segment) => ({
          component: {
            name: segment.screen,
            passProps: this.mergeProps(segment, match.queryParams),
          },
        })),
      },
    };
  }

  private mergeProps(
    segment: RouteMatchSegment,
    queryParams: Record<string, string>
  ): Record<string, string> {
    const merged: Record<string, string> = { ...queryParams, ...segment.params };
    const safe: Record<string, string> = {};
    for (const key of Object.keys(merged)) {
      if (RESERVED_PROP_NAMES.has(key)) {
        if (__DEV__) {
          console.warn(
            `[RNN linking] Dropping reserved prop "${key}" from passProps for screen "${segment.screen}". ` +
              `Rename the URL parameter to avoid conflicting with React-reserved names.`
          );
        }
        continue;
      }
      safe[key] = merged[key];
    }
    return safe;
  }
}
