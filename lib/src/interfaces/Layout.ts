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

export interface LayoutStackChildren<P = {}> {
  /**
   * Set component
   */
  component?: LayoutComponent<P>;
  /**
   * Set the external component
   */
  externalComponent?: ExternalComponent<P>;
}

export interface LayoutStack<P = {}> {
  /**
   * Set ID of the stack so you can use Navigation.mergeOptions to
   * update options
   */
  id?: string;
  /**
   * Set children screens
   */
  children?: LayoutStackChildren<P>[];
  /**
   * Set options
   */
  options?: Options;
}

export interface LayoutTabsChildren<P = {}> {
  /**
   * Set stack
   */
  stack?: LayoutStack<P>;
  /**
   * Set component
   */
  component?: LayoutComponent<P>;
  /**
   * Set the external component
   */
  externalComponent?: ExternalComponent<P>;
  /**
   * Set the side menu
   */
  sideMenu?: LayoutSideMenu<P>;
}

export interface LayoutBottomTabs<P> {
  /**
   * Set ID of the stack so you can use Navigation.mergeOptions to
   * update options
   */
  id?: string;
  /**
   * Set the children screens
   */
  children?: LayoutTabsChildren<P>[];
  /**
   * Set the bottom tabs options
   */
  options?: Options;
}

export interface LayoutSideMenu<P = {}> {
  /**
   * Set ID of the stack so you can use Navigation.mergeOptions to
   * update options
   */
  id?: string;
  /**
   * Set the left side bar
   */
  left?: Layout<P>;
  /**
   * Set the center view
   */
  center: Layout<P>;
  /**
   * Set the right side bar
   */
  right?: Layout<P>;
  /**
   * Set the bottom tabs options
   */
  options?: Options;
}

export interface LayoutSplitView<P = {}> {
  /**
   * Set ID of the stack so you can use Navigation.mergeOptions to
   * update options
   */
  id?: string;
  /**
   * Set master layout (the smaller screen, sidebar)
   */
  master?: Layout<P>;
  /**
   * Set detail layout (the larger screen, flexes)
   */
  detail?: Layout<P>;
  /**
   * Configure split view
   */
  options?: Options;
}

export interface LayoutTopTabs<P = {}> {
  /**
   * Set the layout's id so Navigation.mergeOptions can be used to update options
   */
  id?: string;
  /**
   * Set the children screens
   */
  children?: LayoutTabsChildren<P>[];
  /**
   * Configure top tabs
   */
  options?: Options;
}

export interface LayoutRoot<P = {}> {
  /**
   * Set the root
   */
  root: Layout<P>;
  modals?: any;
  overlays?: any;
}

export interface ExternalComponent<P = {}> {
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
  passProps?: P;
}

export interface Layout<P = {}> {
  /**
   * Set the component
   */
  component?: LayoutComponent<P>;
  /**
   * Set the stack
   */
  stack?: LayoutStack<P>;
  /**
   * Set the bottom tabs
   */
  bottomTabs?: LayoutBottomTabs<P>;
  /**
   * Set the side menu
   */
  sideMenu?: LayoutSideMenu<P>;
  /**
   * Set the split view
   */
  splitView?: LayoutSplitView<P>;
  /**
   * Set the top tabs
   */
  topTabs?: LayoutTopTabs<P>;
  /**
   * Set the external component
   */
  externalComponent?: ExternalComponent<P>;
}
