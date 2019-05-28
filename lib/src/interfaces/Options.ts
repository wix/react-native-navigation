// tslint:disable jsdoc-format
import { ImageRequireSource, Insets } from 'react-native';

type Color = string;
type FontFamily = string;
type LayoutOrientation = 'portrait' | 'landscape';
type AndroidDensityNumber = number;
type SystemItemIcon = 'done' | 'cancel' | 'edit'
  | 'save' | 'add' | 'flexibleSpace' | 'fixedSpace'
  | 'compose' | 'reply' | 'action' | 'organize'
  | 'bookmarks' | 'search' | 'refresh' | 'stop'
  | 'camera' | 'trash' | 'play' | 'pause'
  | 'rewind' | 'fastForward' | 'undo' | 'redo';

export interface OptionsSplitView {
  /**
   * Master view display mode
   * @default 'auto'
   */
  displayMode?: 'auto' | 'visible' | 'hidden' | 'overlay';
  /**
   * Master view side. Leading is left. Trailing is right.
   * @default 'leading'
   */
  primaryEdge?: 'leading' | 'trailing';
  /**
   * Set the minimum width of master view
   */
  minWidth?: number;
  /**
   * Set the maximum width of master view
   */
  maxWidth?: number;
}

export interface OptionsStatusBar {
  /**
   * Set the status bar visibility
   * @default true
   */
  visible?: boolean;
  /**
   * Set the text color of the status bar
   * @default 'light'
   */
  style?: 'light' | 'dark';
  /**
   * Set the background color of the status bar
   * #### (Android specific)
   */
  backgroundColor?: Color;
  /**
   * Draw screen behind the status bar
   * #### (Android specific)
   */
  drawBehind?: boolean;
}

export interface OptionsLayout {
  /**
   * Set the screen background color
   */
  backgroundColor?: Color;
  /**
   * Set background color only for components, helps reduce overdraw if background color is set in default options.
   * #### (Android specific)
   */
  componentBackgroundColor?: Color;
  /**
   * Set the allowed orientations
   */
  orientation?: LayoutOrientation[];
  /**
   * Layout top margin
   * #### (Android specific)
   */
  topMargin?: number;

  /**
   * Set language direction.
   * only works with DefaultOptions
   */
  direction?: 'rtl' | 'ltr';
}

export enum OptionsModalPresentationStyle {
  formSheet = 'formSheet',
  pageSheet = 'pageSheet',
  overFullScreen = 'overFullScreen',
  overCurrentContext = 'overCurrentContext',
  currentContext = 'currentContext',
  popOver = 'popOver',
  fullScreen = 'fullScreen',
  none = 'none'
}

export enum OptionsModalTransitionStyle {
  coverVertical = 'coverVertical',
  crossDissolve = 'crossDissolve',
  flipHorizontal = 'flipHorizontal',
  partialCurl = 'partialCurl'
}

export interface OptionsTopBarLargeTitle {
  /**
   * Enable large titles
   */
  visible?: boolean;
  /**
   * Set the font size of large title's text
   */
  fontSize?: number;
  /**
   * Set the color of large title's text
   */
  color?: Color;
  /**
   * Set the font family of large title's text
   */
  fontFamily?: FontFamily;
}

export interface OptionsTopBarTitle {
  /**
   * Text to display in the title area
   */
  text?: string;
  /**
   * Font size
   */
  fontSize?: number;
  /**
   * Text color
   */
  color?: Color;
  /**
   * Title font family
   *
   * Make sure that the font is available
   */
  fontFamily?: FontFamily;
  /**
   * Custom component as the title view
   */
  component?: {
    /**
     * Component reference id, Auto generated if empty
     */
    id?: string;
    /**
     * Name of your component
     */
    name: string;
    /**
     * Set component alignment
     */
    alignment?: 'center' | 'fill';
    /**
     * Properties to pass down to the component
     */
    passProps?: object;
  };
  /**
   * Top Bar title height in densitiy pixels
   * #### (Android specific)
   */
  height?: number;
  /**
   * Title alignment
   * #### (Android specific)
   */
  alignment?: 'center' | 'fill';
}

export interface OptionsTopBarSubtitle {
  /**
   * Set subtitle text
   */
  text?: string;
  /**
   * Set subtitle font size
   */
  fontSize?: number;
  /**
   * Set subtitle color
   */
  color?: Color;
  /**
   * Set subtitle font family
   */
  fontFamily?: FontFamily;
  /**
   * Set subtitle alignment
   */
  alignment?: 'center';
}

export interface OptionsTopBarBackButton {
  /**
   * Image to show as the back button
   */
  icon?: ImageRequireSource;
  /**
   * Weither the back button is visible or not
   * @default true
   */
  visible?: boolean;
  /**
   * Set the back button title
   * #### (iOS specific)
   */
  title?: string;
  /**
   * Show title or just the icon
   * #### (iOS specific)
   */
  showTitle?: boolean;
  /**
   * Back button icon and text color
   */
  color?: Color;
}

export interface OptionsTopBarBackground {
  /**
   * Background color of the top bar
   */
  color?: Color;
  /**
   * Clip the top bar background to bounds if set to true.
   * #### (iOS specific)
   */
  clipToBounds?: boolean;
  /**
   * Set a custom component for the Top Bar background
   */
  component?: {
    name?: string;
    /**
     * Properties to pass down to the component
     */
    passProps?: object;
  };
  /**
   * Allows the NavBar to be translucent (blurred)
   * #### (iOS specific)
   */
  translucent?: boolean;
  /**
   * Enable background blur
   * #### (iOS specific)
   */
  blur?: boolean;
}

export interface OptionsTopBarButton {
  /**
   * Button id for reference press event
   */
  id: string;
  /**
   * Set the button icon
   */
  icon?: ImageRequireSource;
  /**
  * Set the button icon insets
  */
  iconInsets?: IconInsets;
  /**
   * Set the button as a custom component
   */
  component?: {
    name: string;
    /**
     * Properties to pass down to the component
     */
    passProps?: object;
  };
  /**
   * (iOS only) Set the button as an iOS system icon
   */
  systemItem?: SystemItemIcon;
  /**
   * Set the button text
   */
  text?: string;
  /**
   * Set the button font family
   */
  fontFamily?: string;
  /**
   * Set the button enabled or disabled
   * @default true
   */
  enabled?: boolean;
  /**
   * Disable icon tinting
   */
  disableIconTint?: boolean;
  /**
   * Set text color
   */
  color?: Color;
  /**
   * Set text color in disabled state
   */
  disabledColor?: Color;
  /**
   * Set testID for reference in E2E tests
   */
  testID?: string;
}

export interface OptionsTopBar {
  /**
   * Show or hide the top bar
   */
  visible?: boolean;
  /**
   * Controls whether TopBar visibility changes should be animated
   */
  animate?: boolean;
  /**
   * Top bar will hide and show based on users scroll direction
   */
  hideOnScroll?: boolean;
  /**
   * Change button colors in the top bar
   */

  leftButtonColor?: Color;
  rightButtonColor?: Color;
  leftButtonDisabledColor?: Color;
  rightButtonDisabledColor?: Color;
  /**
   * Draw behind the navbar
   */
  drawBehind?: boolean;
  /**
   * Can be used to reference the top bar in E2E tests
   */
  testID?: string;
  /**
   * Title configuration
   */
  title?: OptionsTopBarTitle;
  /**
   * Subtitle configuration
   */
  subtitle?: OptionsTopBarSubtitle;
  /**
   * Back button configuration
   */
  backButton?: OptionsTopBarBackButton;
  /**
   * List of buttons to the left
   */
  leftButtons?: OptionsTopBarButton[];
  /**
   * List of buttons to the right
   */
  rightButtons?: OptionsTopBarButton[];
  /**
   * Background configuration
   */
  background?: OptionsTopBarBackground;
  /**
   * Control the NavBar blur style
   * #### (iOS specific)
   * @requires translucent: true
   * @default 'default'
   */
  barStyle?: 'default' | 'black';
  /**
   * Disable the border on bottom of the navbar
   * #### (iOS specific)
   * @default false
   */
  noBorder?: boolean;
  /**
   * Show a UISearchBar in the Top Bar
   * #### (iOS 11+ specific)
   */
  searchBar?: boolean;
  /**
   * Hides the UISearchBar when scrolling
   * #### (iOS 11+ specific)
   */
  searchBarHiddenWhenScrolling?: boolean;
  /**
   * The placeholder value in the UISearchBar
   * #### (iOS 11+ specific)
   */
  searchBarPlaceholder?: string;
  /**
   * Controls Hiding NavBar on focus UISearchBar
   * #### (iOS 11+ specific)
   */
  hideNavBarOnFocusSearchBar?: boolean;
  /**
   * Control the Large Title configuration
   * #### (iOS 11+ specific)
   */
  largeTitle?: OptionsTopBarLargeTitle;
  /**
   * Set the height of the navbar in dp
   * #### (Android specific)
   */
  height?: AndroidDensityNumber;
  /**
   * Change the navbar border color
   * #### (Android specific)
   */
  borderColor?: Color;
  /**
   * Set the border height of the navbar in dp
   * #### (Android specific)
   */
  borderHeight?: AndroidDensityNumber;
  /**
   * Set the elevation of the navbar in dp
   * #### (Android specific)
   */
  elevation?: AndroidDensityNumber;
  /**
   * Layout top margin
   * #### (Android specific)
   */
  topMargin?: number;
}

export interface OptionsFab {
  id: string;
  backgroundColor?: Color;
  clickColor?: Color;
  rippleColor?: Color;
  visible?: boolean;
  icon?: ImageRequireSource;
  iconColor?: Color;
  alignHorizontally?: 'left' | 'right';
  alignVertically?: 'top' | 'bottom';
  hideOnScroll?: boolean;
  size?: number;
  actions?: OptionsFab[];
}

export interface OptionsBottomTabs {
  /**
   * Show or hide the bottom tabs
   */
  visible?: boolean;
  /**
   * Enable animations when toggling visibility
   */
  animate?: boolean;
  /**
   * Switch to another screen within the bottom tabs via index (starting from 0)
   */
  currentTabIndex?: number;
  /**
   * Switch to another screen within the bottom tabs via screen name
   */
  currentTabId?: string;
  /**
   * Set a testID to reference the bottom tabs
   */
  testID?: string;
  /**
   * Draw screen component under the tab bar
   */
  drawBehind?: boolean;
  /**
   * Set a background color for the bottom tabs
   */
  backgroundColor?: Color;
  /**
   * Set when tabs are attached to hierarchy consequently when the
   * RootView's constructor is called.
   */
  tabsAttachMode?: 'together' | 'afterInitialTab' | 'onSwitchToTab';
  /**
   * Control the Bottom Tabs blur style
   * #### (iOS specific)
   * @requires translucent: true
   * @default 'default'
   */
  barStyle?: 'default' | 'black';
  /**
   * Allows the Bottom Tabs to be translucent (blurred)
   * #### (iOS specific)
   */
  translucent?: boolean;
  /**
   * Hide the top line of the Tab Bar
   * #### (iOS specific)
   */
  hideShadow?: boolean;
  /**
   * Control the text display mode below the tab icon
   * #### (Android specific)
   */
  titleDisplayMode?: 'alwaysShow' | 'showWhenActive' | 'alwaysHide';
  /**
   * Set the elevation of the Bottom Tabs in dp
   * #### (Android specific)
   */
  elevation?: AndroidDensityNumber;
}

export interface OptionsBottomTab {
  /**
   * Set the text to display below the icon
   */
  text?: string;
  /**
   * Set the text in a badge that is overlayed over the component
   */
  badge?: string;
  /**
   * Set the background color of the badge that is overlayed over the component
   */
  badgeColor?: string;
  /**
   * Set a testID to reference the tab in E2E tests
   */
  testID?: string;
  /**
   * Set the tab icon
   */
  icon?: ImageRequireSource;
  /**
   * Set the icon tint
   */
  iconColor?: Color;
  /**
   * Set the text color
   */
  textColor?: Color;
  /**
   * Set the selected icon tint
   */
  selectedIconColor?: Color;
  /**
   * Set the selected text color
   */
  selectedTextColor?: Color;
  /**
   * Set the text font family
   */
  fontFamily?: FontFamily;
  /**
   * Set the text font size
   */
  fontSize?: number;
  /**
   * Set the insets of the icon
   * #### (iOS specific)
   */
  iconInsets?: Insets;
  /**
   * Set selected icon image
   * #### (iOS specific)
   */
  selectedIcon?: ImageRequireSource;
  /**
   * Set true if you want to disable the icon tinting
   * #### (iOS specific)
   */
  disableIconTint?: boolean;
  /**
   * Set true if you want to disable the text tinting
   * #### (iOS specific)
   */
  disableSelectedIconTint?: boolean;
  /**
   * Set the font size for selected tabs
   * #### (Android specific)
   */
  selectedFontSize?: number;
}

export interface SideMenuSide {
  /**
   * Show or hide the side menu
   */
  visible?: boolean;
  /**
   * Enable or disable the side menu
   */
  enabled?: boolean;
}

export interface OptionsSideMenu {
  /**
   * Configure the left side menu
   */
  left?: SideMenuSide;
  /**
   * Configure the right side menu
   */
  right?: SideMenuSide;
  /**
   * Configure how a user is allowed to open a drawer using gestures
   * #### (iOS specific)
   * @default 'entireScreen'
   */
  openGestureMode?: 'entireScreen' | 'bezel';
}

export interface OverlayOptions {
  /**
   * Capture touches outside of the Component View
   */
  interceptTouchOutside?: boolean;
  /**
   * Control wether this Overlay should handle Keyboard events.
   * Set this to true if your Overlay contains a TextInput.
   */
  handleKeyboardEvents?: boolean;
}

export interface OptionsPreviewAction {
  /**
   * Reference ID to get callbacks from
   */
  id: string;
  /**
   * Action text
   */
  title: string;
  /**
   * Action style
   */
  style?: 'default' | 'selected' | 'destructive';
  /**
   * Subactions that will be shown when this action is pressed.
   */
  actions?: OptionsPreviewAction[];
}

export interface OptionsPreview {
  /**
   * Pass a react node tag to mark a SourceRect for a specific
   * peek and pop preview element.
   */
  reactTag?: number;
  /**
   * You can set this property specify the width of the preview.
   * If the width is greater than the device width, it will be zoomed in.
   */
  width?: number;
  /**
   * Height of the preview
   */
  height?: 100;
  /**
   * You can control if the users gesture will result in pushing
   * the preview screen into the stack.
   */
  commit?: boolean;
  /**
   * List of actions that will appear underneath the preview window.
   * They can be nested for sub actions.
   */
  actions?: OptionsPreviewAction[];
}

export interface OptionsAnimationPropertyConfig {
  /**
   * Animate from this value, ex. 0
   */
  from?: number;
  /**
   * Animate to this value, ex. 1
   */
  to?: number;
  /**
   * Animation duration
   * @default 300
   */
  duration?: number;
  /**
   * Animation delay
   * @default 0
   */
  startDelay?: number;
  /**
   * Animation interplation
   */
  interpolation?: 'accelerate' | 'decelerate';
}

/**
 * Used to animate the actual content added to the hierarchy.
 * Content can be a React component (component) or any other layout (Stack, BottomTabs etc)
 */
export interface ScreenAnimationOptions {
  /**
   * Animate the element over translateX
   */
  x?: OptionsAnimationPropertyConfig;
  /**
   * Animate the element over translateY
   */
  y?: OptionsAnimationPropertyConfig;
  /**
   * Animate the element over opacity
   */
  alpha?: OptionsAnimationPropertyConfig;
  /**
   * Animate the element over scaleX
   */
  scaleX?: OptionsAnimationPropertyConfig;
  /**
   * Animate the element over scaleY
   */
  scaleY?: OptionsAnimationPropertyConfig;
  /**
   * Animate the element over rotationX
   */
  rotationX?: OptionsAnimationPropertyConfig;
  /**
   * Animate the element over rotationY
   */
  rotationY?: OptionsAnimationPropertyConfig;
  /**
   * Animate the element over rotation
   */
  rotation?: OptionsAnimationPropertyConfig;
  /**
   * Wait for the root view to render before start animation
   */
  waitForRender?: boolean;
  /**
   * Enable or disable the animation
   * @default true
   */
  enabled?: boolean;
}

export interface IconInsets {
  /**
   * Configure top inset
   */
  top?: number;
  /**
   * Configure left inset
   */
  left?: number;
  /**
   * Configure bottom inset
   */
  bottom?: number;
  /**
   * Configure right inset
   */
  right?: number;
}

export interface ViewAnimationOptions extends ScreenAnimationOptions {
  /**
   * ID of the Top Bar we want to animate
   */
  id?: string;
}

/**
 * Used for describing stack commands animations.
 */
export interface StackAnimationOptions {
  /**
   * Wait for the View to render before start animation
   */
  waitForRender?: boolean;
  /**
   * Enable or disable the animation
   * @default true
   */
  enabled?: boolean;
  /**
   * Configure animations for the top bar
   */
  topBar?: ViewAnimationOptions;
  /**
   * Configure animations for the bottom tabs
   */
  bottomTabs?: ViewAnimationOptions;
  /**
   * Configure animations for the content (Screen)
   */
  content?: ViewAnimationOptions;
}

/**
 * Used for configuring command animations
 */
export interface AnimationOptions {
  /**
   * Configure the setRoot animation
   */
  setRoot?: ScreenAnimationOptions;
  /**
   * Configure what animates when a screen is pushed
   */
  push?: StackAnimationOptions;
  /**
   * Configure what animates when a screen is popped
   */
  pop?: StackAnimationOptions;
  /**
   * Configure what animates when modal is shown
   */
  showModal?: ScreenAnimationOptions;
  /**
   * Configure what animates when modal is dismissed
   */
  dismissModal?: ScreenAnimationOptions;
}

export interface OptionsCustomTransition {
  animations: OptionsCustomTransitionAnimation[];
  duration?: number;
}

export interface OptionsCustomTransitionAnimation {
  /**
   * Animation type, only support sharedElement currently
   */
  type: 'sharedElement';
  /**
   * Transition from element Id
   */
  fromId: string;
  /**
   * Transition to element Id
   */
  toId: string;
  /**
   * Animation delay
   */
  startDelay?: number;
  /**
   * Animation spring Velocity
   */
  springVelocity?: number;
  /**
   * Animation duration
   */
  duration?: number;
}

export interface Options {
  /**
   * Configure the status bar
   */
  statusBar?: OptionsStatusBar;
  /**
   * Configure the layout
   */
  layout?: OptionsLayout;
  /**
   * Configure the presentation style of the modal
   */
  modalPresentationStyle?: OptionsModalPresentationStyle;
  /**
   * Configure the transition style of the modal
   *
   * #### (Android specific)
   */
  modalTransitionStyle?: OptionsModalTransitionStyle;
  /**
   * Configure the top bar
   */
  topBar?: OptionsTopBar;
  fab?: OptionsFab;
  /**
   * Configure the bottom tabs
   */
  bottomTabs?: OptionsBottomTabs;
  /**
   * Configure the bottom tab associated to the screen
   */
  bottomTab?: OptionsBottomTab;
  /**
   * Configure the side menu
   */
  sideMenu?: OptionsSideMenu;
  /**
   * Configure the splitView controller
   */
  splitView?: OptionsSplitView;
  /**
   * Configure the overlay
   */
  overlay?: OverlayOptions;
  /**
   * Animation used for navigation commands that modify the layout
   * hierarchy can be controlled in options.
   *
   * Animations can be modified per command and it's also possible
   * to change the default animation for each command.
   *
   * Example:
```js
setRoot: {
  y: {
    from: 1000,
    to: 0,
    duration: 500,
    interpolation: 'accelerate',
  },
  alpha: {
    from: 0,
    to: 1,
    duration: 400,
    startDelay: 100,
    interpolation: 'accelerate'
  }
}
```
   */
  animations?: AnimationOptions;

  /**
   * Custom Transition used for animate shared element between two screens
   * Example:
  ```js
  Navigation.push(this.props.componentId, {
    component: {
      name: 'second.screen',
      options: {
        customTransition: {
          animations: [
            { type: 'sharedElement', fromId: 'image1', toId: 'image2', startDelay: 0, springVelocity: 0.2, duration: 0.5 }
          ],
          duration: 0.8
        }
      }
    }
  });
  ```
  */
  customTransition?: OptionsCustomTransition;
  /**
   * Preview configuration for Peek and Pop
   * #### (iOS specific)
   */
  preview?: OptionsPreview;
  /**
   * Enable or disable swipe back to pop gesture
   * #### (iOS specific)
   * @default true
   */
  popGesture?: boolean;
  /**
   * Background image for the screen
   * #### (iOS specific)
   */
  backgroundImage?: ImageRequireSource;
  /**
   * Background image for the Navigation View
   * #### (iOS specific)
   */
  rootBackgroundImage?: ImageRequireSource;
  /**
   * Enable or disable automatically blurring focused input, dismissing keyboard on unmount
   * #### (Android specific)
   * @default false
   */
  blurOnUnmount?: boolean;
}
