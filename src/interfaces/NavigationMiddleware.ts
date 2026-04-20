import { Layout, LayoutRoot } from './Layout';
import { Options } from './Options';
import { CommandName } from './CommandName';

/**
 * Structured representation of a navigation command intercepted by a middleware.
 * Fields are populated based on the `commandName`:
 * - setRoot           → layoutRoot
 * - setDefaultOptions → options
 * - mergeOptions      → componentId, options
 * - updateProps       → componentId, props
 * - showModal         → layout
 * - dismissModal      → componentId, mergeOptions
 * - dismissAllModals  → mergeOptions
 * - push              → componentId, layout
 * - pop / popTo / popToRoot → componentId, mergeOptions
 * - setStackRoot      → componentId, children
 * - showOverlay       → layout
 * - dismissOverlay    → componentId
 * - dismissAllOverlays / getLaunchArgs → (no extra fields)
 */
export interface NavigationCommand<P = any> {
  commandName: CommandName;
  componentId?: string;
  layout?: Layout<P>;
  layoutRoot?: LayoutRoot;
  children?: Layout<P>[];
  options?: Options;
  mergeOptions?: Options;
  props?: object;
}

/**
 * A middleware intercepts a navigation command before it is processed and sent to native.
 *
 * Return value:
 * - the same or a modified `NavigationCommand`  → command proceeds (with any modifications)
 * - `null`                                      → command is cancelled (no-op)
 *
 * Middleware run in registration order; each receives the output of the previous one.
 *
 * Use cases: analytics, auth guards, deep link resolution, A/B testing.
 */
export interface NavigationMiddleware {
  (command: NavigationCommand): NavigationCommand | null;
}
