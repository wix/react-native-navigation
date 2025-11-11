import { Options } from './Options';

export interface LayoutComponent<P = {}> {
  /**
   * Component reference id, Auto generated if empty
   */
  id?: string;
  /**
   * Name of your component
   */
  name: string | number;
  /**
   * Styling options
   */
  options?: Options;
  /**
   * Properties to pass down to the component
   */
  passProps?: P;
}

export interface LayoutStackChildren {
  /**
   * Set component
   */
  component?: LayoutComponent;
  /**
   * Set the external component
   */
  externalComponent?: ExternalComponent;
}

export interface LayoutStack {
  /**
   * Set ID of the stack so you can use Navigation.mergeOptions to
   * update options
   */
  id?: string;
  /**
   * Set children screens
   */
  children?: LayoutStackChildren[];
  /**
   * Set options
   */
  options?: Options;
}

export interface LayoutTabsChildren {
  /**
   * Set stack
   */
  stack?: LayoutStack;
  /**
   * Set component
   */
  component?: LayoutComponent;
  /**
   * Set the external component
   */
  externalComponent?: ExternalComponent;
  /**
   * Set the side menu
   */
  sideMenu?: LayoutSideMenu;
}

export interface LayoutBottomTabs {
  /**
   * Set ID of the stack so you can use Navigation.mergeOptions to
   * update options
   */
  id?: string;
  /**
   * Set the children screens
   */
  children?: LayoutTabsChildren[];
  /**
   * Set the bottom tabs options
   */
  options?: Options;
}

export interface LayoutSideMenu {
  /**
   * Set ID of the stack so you can use Navigation.mergeOptions to
   * update options
   */
  id?: string;
  /**
   * Set the left side bar
   */
  left?: Layout;
  /**
   * Set the center view
   */
  center: Layout;
  /**
   * Set the right side bar
   */
  right?: Layout;
  /**
   * Set the bottom tabs options
   */
  options?: Options;
}

export interface LayoutSplitView {
  /**
   * Set ID of the stack so you can use Navigation.mergeOptions to
   * update options
   */
  id?: string;
  /**
   * Set master layout (the smaller screen, sidebar)
   */
  master?: Layout;
  /**
   * Set detail layout (the larger screen, flexes)
   */
  detail?: Layout;
  /**
   * Configure split view
   */
  options?: Options;
}

export interface LayoutTopTabs {
  /**
   * Set the layout's id so Navigation.mergeOptions can be used to update options
   */
  id?: string;
  /**
   * Set the children screens
   */
  children?: LayoutTabsChildren[];
  /**
   * Configure top tabs
   */
  options?: Options;
}

export interface LayoutRoot {
  /**
   * Set the root
   */
  root: Layout;
  modals?: any;
  overlays?: any;
}

export interface ExternalComponent {
  /**
   * Set the screen's id so Navigation.mergeOptions can be used to update options
   */
  id?: string;
  /**
   * Name of your component
   */
  name: string | number;
  /**
   * Configure component options
   */
  options?: Options;
  /**
   * Properties to pass down to the component
   */
  passProps?: object;
}

export interface Layout<P = {}> {
  /**
   * Set the component
   */
  component?: LayoutComponent<P>;
  /**
   * Set the stack
   */
  stack?: LayoutStack;
  /**
   * Set the bottom tabs
   */
  bottomTabs?: LayoutBottomTabs;
  /**
   * Set the side menu
   */
  sideMenu?: LayoutSideMenu;
  /**
   * Set the split view
   */
  splitView?: LayoutSplitView;
  /**
   * Set the top tabs
   */
  topTabs?: LayoutTopTabs;
  /**
   * Set the external component
   */
  externalComponent?: ExternalComponent;
}
