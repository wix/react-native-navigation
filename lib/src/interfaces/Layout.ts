import { Options, OptionsSplitView } from './Options';

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

export interface LayoutBottomTabsChildren {
  /**
   * Set stack
   */
  stack?: LayoutStack;
  /**
   * Set component
   */
  component?: LayoutComponent;
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
  children?: LayoutBottomTabsChildren[];
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
  left?: LayoutStackChildren;
  /**
   * Set the center view
   */
  center?: Layout;
  /**
   * Set the right side bar
   */
  right?: LayoutStackChildren;
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
  options?: OptionsSplitView;
}

export interface LayoutRoot {
  /**
   * Set the root
   */
  root?: Layout;
  modals?: any;
  overlays?: any;
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
}
