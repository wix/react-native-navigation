import { ImageRequireSource, ImageSourcePropType, Insets, OpaqueColorValue } from 'react-native';
export declare type Color = string | symbol | ThemeColor | OpaqueColorValue | null;
type FontFamily = string;
type FontStyle = 'normal' | 'italic';
type FontWeightIOS = 'normal' | 'ultralight' | 'thin' | 'light' | 'regular' | 'medium' | 'semibold' | 'demibold' | 'extrabold' | 'ultrabold' | 'bold' | 'heavy' | 'black';
type FontWeight = 'normal' | 'bold' | '100' | '200' | '300' | '400' | '500' | '600' | '700' | '800' | '900' | FontWeightIOS;
export type LayoutOrientation = 'all' | 'default' | 'portrait' | 'landscape' | 'upsideDown' | 'sensor' | 'sensorLandscape' | 'sensorPortrait';
type AndroidDensityNumber = number;
export type SystemItemIcon = 'done' | 'cancel' | 'edit' | 'save' | 'add' | 'flexibleSpace' | 'fixedSpace' | 'compose' | 'reply' | 'action' | 'organize' | 'bookmarks' | 'search' | 'refresh' | 'stop' | 'camera' | 'trash' | 'play' | 'pause' | 'rewind' | 'fastForward' | 'undo' | 'redo';
export type Interpolation = {
    type: 'accelerate';
    factor?: number;
} | {
    type: 'decelerate';
    factor?: number;
} | {
    type: 'decelerateAccelerate';
} | {
    type: 'accelerateDecelerate';
} | {
    type: 'fastOutSlowIn';
} | {
    type: 'linear';
} | {
    type: 'overshoot';
    tension?: number;
} | {
    type: 'spring';
    mass?: number;
    damping?: number;
    stiffness?: number;
    allowsOverdamping?: boolean;
    initialVelocity?: number;
};
interface ThemeColor {
    light?: string | symbol;
    dark?: string | symbol;
}
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
    /**
     * Set background style of sidebar. Currently works for Mac Catalyst apps only.
     * @default 'none'
     */
    primaryBackgroundStyle?: 'none' | 'sidebar';
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
    /**
     * Allows the StatusBar to be translucent (blurred)
     * #### (Android specific)
     */
    translucent?: boolean;
    /**
     * Animate StatusBar style changes.
     * #### (iOS specific)
     */
    animate?: boolean;
    /**
     * Automatically hide the StatusBar when the TopBar hides.
     * #### (iOS specific)
     */
    hideWithTopBar?: boolean;
    /**
     * Blur content beneath the StatusBar.
     * #### (iOS specific)
     */
    blur?: boolean;
}
export interface OptionsLayout {
    fitSystemWindows?: boolean;
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
    direction?: 'rtl' | 'ltr' | 'locale';
    /**
     * Controls the application's preferred home indicator auto-hiding.
     * #### (iOS specific)
     */
    autoHideHomeIndicator?: boolean;
    /**
     * Add insets to the top layout
     */
    insets?: Insets;
    /**
     * Resizes the layout when keyboard is visible
     * @default true
     * #### (Android specific)
     */
    adjustResize?: boolean;
}
export declare enum OptionsModalPresentationStyle {
    formSheet = "formSheet",
    pageSheet = "pageSheet",
    overFullScreen = "overFullScreen",
    overCurrentContext = "overCurrentContext",
    currentContext = "currentContext",
    popover = "popover",
    fullScreen = "fullScreen",
    none = "none"
}
export declare enum OptionsModalTransitionStyle {
    coverVertical = "coverVertical",
    crossDissolve = "crossDissolve",
    flipHorizontal = "flipHorizontal",
    partialCurl = "partialCurl"
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
     * Set the font family of the large title text
     */
    fontFamily?: FontFamily;
    /**
     * Set the font style of the large title text
     */
    fontStyle?: FontStyle;
    /**
     * Specifies font weight. The values 'normal' and 'bold' are supported
     * for most fonts. Not all fonts have a variant for each of the numeric
     * values, in that case the closest one is chosen.
     */
    fontWeight?: FontWeight;
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
     * Set the font family for the title
     */
    fontFamily?: FontFamily;
    /**
     * Set the font style for the title
     */
    fontStyle?: FontStyle;
    /**
     * Specifies font weight. The values 'normal' and 'bold' are supported
     * for most fonts. Not all fonts have a variant for each of the numeric
     * values, in that case the closest one is chosen.
     */
    fontWeight?: FontWeight;
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
     * Set the font family for the subtitle
     */
    fontFamily?: FontFamily;
    /**
     * Set the font style for a text
     */
    fontStyle?: FontStyle;
    /**
     * Specifies font weight. The values 'normal' and 'bold' are supported
     * for most fonts. Not all fonts have a variant for each of the numeric
     * values, in that case the closest one is chosen.
     */
    fontWeight?: FontWeight;
    /**
     * Set subtitle alignment
     */
    alignment?: 'center';
}
export interface OptionsTopBarBackButton {
    /**
     * Overrides the text that's read by the screen reader when the user interacts with the back button
     * #### (Android specific)
     */
    accessibilityLabel?: string;
    /**
     * Button id for reference press event
     */
    id?: string;
    /**
     * Image to show as the back button
     */
    icon?: ImageResource;
    /**
     * SF Symbol to show as the back button
     * #### (iOS 13+ specific)
     */
    sfSymbol?: string;
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
    /**
     * Set subtitle font size
     */
    fontSize?: number;
    /**
     * Set the font family for the back button
     * #### (iOS specific)
     */
    fontFamily?: FontFamily;
    /**
     * Set the font style for a text
     */
    fontStyle?: FontStyle;
    /**
     * Specifies font weight. The values 'normal' and 'bold' are supported
     * for most fonts. Not all fonts have a variant for each of the numeric
     * values, in that case the closest one is chosen.
     */
    fontWeight?: FontWeight;
    /**
     * Set testID for reference in E2E tests
     */
    testID?: string;
    /**
     * Enables iOS 14 back button menu display
     * #### (iOS specific)
     * @default true
     */
    enableMenu?: boolean;
    /**
     * Allows the NavBar to be translucent (blurred)
     * #### (iOS specific)
     */
    displayMode?: 'default' | 'generic' | 'minimal';
    /**
     * Controls whether the default back button should pop the Stack or not
     * @default true
     */
    popStackOnPress?: boolean;
}
export interface HardwareBackButtonOptions {
    /**
     * Controls whether the hardware back button should dismiss modal or not
     * #### (Android specific)
     * @default true
     */
    dismissModalOnPress?: boolean;
    /**
     * Controls whether the hardware back button should pop the Stack or not
     * #### (Android specific)
     * @default true
     */
    popStackOnPress?: boolean;
    /**
     * Controls hardware back button bottom tab selection behaviour
     */
    bottomTabsOnPress?: 'exit' | 'first' | 'previous';
}
export interface OptionsTopBarScrollEdgeAppearanceBackground {
    /**
     * Background color of the top bar
     */
    color?: Color;
    /**
     * Allows the NavBar to be translucent (blurred)
     * #### (iOS specific)
     */
    translucent?: boolean;
}
export interface OptionsTopBarScrollEdgeAppearance {
    background?: OptionsTopBarScrollEdgeAppearanceBackground;
    active: boolean;
    /**
     * Disable the border on bottom of the navbar
     * #### (iOS specific)
     * @default false
     */
    noBorder?: boolean;
    /**
     * Change the navbar border color
     */
    borderColor?: Color;
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
     * (Android only) Sets a textual button to be ALL CAPS. default value is true
     */
    allCaps?: boolean;
    /**
     * Button id for reference press event
     */
    id: string;
    /**
     * Set the button icon
     */
    icon?: ImageResource;
    /**
     * Set the SF symbol as icon (will be used primarily)
     * #### (iOS 13+ specific)
     */
    sfSymbol?: string;
    /**
     * Set the button icon insets
     */
    iconInsets?: IconInsets;
    /**
     * Set the button as a custom component
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
         * Properties to pass down to the component
         */
        passProps?: object;
        /**
         * (Android only) component width
         */
        width?: number;
        /**
         * (Android only) component height
         */
        height?: number;
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
     * Overrides the text that's read by the screen reader when the user interacts with the element
     */
    accessibilityLabel?: string;
    /**
     * Set the font family for the button's text
     */
    fontFamily?: FontFamily;
    /**
     * Set the font style for the button's text
     */
    fontStyle?: FontStyle;
    /**
     * Specifies font weight. The values 'normal' and 'bold' are supported
     * for most fonts. Not all fonts have a variant for each of the numeric
     * values, in that case the closest one is chosen.
     */
    fontWeight?: FontWeight;
    /**
     * Set the font size in dp
     */
    fontSize?: number;
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
     * Set icon background style
     */
    iconBackground?: IconBackgroundOptions;
    /**
     * Set testID for reference in E2E tests
     */
    testID?: string;
    /**
     * (Android only) Set showAsAction value
     * @see {@link https://developer.android.com/guide/topics/resources/menu-resource|Android developer guide: Menu resource}
     */
    showAsAction?: 'ifRoom' | 'withText' | 'always' | 'never';
}
export interface OptionsSearchBar {
    visible?: boolean;
    focus?: boolean;
    hideOnScroll?: boolean;
    hideTopBarOnFocus?: boolean;
    obscuresBackgroundDuringPresentation?: boolean;
    backgroundColor?: Color;
    tintColor?: Color;
    placeholder?: string;
    cancelText?: string;
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
    leftButtonBackgroundColor?: Color;
    rightButtonBackgroundColor?: Color;
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
     *
     */
    scrollEdgeAppearance?: OptionsTopBarScrollEdgeAppearance;
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
    searchBar?: OptionsSearchBar;
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
     * The background color of the UISearchBar's TextField
     * #### (iOS 13+ specific)
     */
    searchBarBackgroundColor?: string;
    /**
     * The tint color of the UISearchBar
     * #### (iOS 11+ specific)
     */
    searchBarTintColor?: string;
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
    /**
     * Toggles animation on left buttons bar upon changes
     */
    animateLeftButtons?: boolean;
    /**
     * Toggles animation on right buttons bar upon changes
     */
    animateRightButtons?: boolean;
}
export interface SharedElementTransition {
    fromId: string;
    toId: string;
    duration?: number;
    interpolation?: Interpolation;
}
export interface ElementTransition {
    id: string;
    alpha?: AppearingElementAnimation | DisappearingElementAnimation;
    translationX?: AppearingElementAnimation | DisappearingElementAnimation;
    translationY?: AppearingElementAnimation | DisappearingElementAnimation;
    scaleX?: AppearingElementAnimation | DisappearingElementAnimation;
    scaleY?: AppearingElementAnimation | DisappearingElementAnimation;
    rotationX?: AppearingElementAnimation | DisappearingElementAnimation;
    rotationY?: AppearingElementAnimation | DisappearingElementAnimation;
    x?: AppearingElementAnimation | DisappearingElementAnimation;
    y?: AppearingElementAnimation | DisappearingElementAnimation;
}
export interface AppearingElementAnimation extends ElementAnimation {
    from: number;
}
export interface DisappearingElementAnimation extends ElementAnimation {
    to: number;
}
export interface ElementAnimation {
    duration: number;
    startDelay?: number;
    interpolation?: Interpolation;
}
export interface OptionsFab {
    id: string;
    backgroundColor?: Color;
    clickColor?: Color;
    rippleColor?: Color;
    visible?: boolean;
    icon?: ImageResource;
    iconColor?: Color;
    alignHorizontally?: 'left' | 'right';
    hideOnScroll?: boolean;
    size?: 'mini' | 'regular';
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
     * Controls whether tab selection is animated or not
     * #### (android specific)
     * @default true
     */
    animateTabSelection?: boolean;
    /**
     * Use large icons when possible, even when three tabs without titles are displayed
     * #### (android specific)
     * @default false
     */
    preferLargeIcons?: boolean;
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
     * Overrides the text that's read by the screen reader when the user interacts with the element
     * #### (iOS specific)
     */
    accessibilityLabel?: string;
    /**
     * Draw screen component under the tab bar
     */
    drawBehind?: boolean;
    /**
     * Set a background color for the bottom tabs.<br/>
     * On Android - also applicable when translucence is applied, but a semi-transparent
     * color should be used (e.g. `rgba(255, 0, 0, 0.25)`).
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
     * Control the way the bottom tabs are laid out.
     * - `stretch`: Fill the entire width of the screen.
     * - `compact`: Occupy the minimum width needed to hold tab buttons. Recommended for
     * usage in conjunction with `drawBehind: true`.
     *
     * #### (Android specific)
     * @default 'stretch'
     */
    layoutStyle?: 'stretch' | 'compact';
    /**
     * Specify a corner-radius (in dip) in order to apply round corners to the tabs container.<br/>
     * Mainly suitable when used in conjunction with `layoutStyle: 'compact'`
     * #### (Android specific)
     */
    cornerRadius?: AndroidDensityNumber;
    /**
     * Bottom-margin to set in order to apply a "hover" effect.
     * Works best when used in conjunction with `layoutStyle: 'compact'` and `drawBehind: true`.
     * #### (Android specific)
     */
    bottomMargin?: AndroidDensityNumber;
    /**
     * Allows the bottom tabs to be translucent (blurred). Doesn't necessarily play
     * nice with shadow effects on Android.
     * #### Android: experimental, turn on using native toggle `TAB_BAR_TRANSLUCENCE`.
     */
    translucent?: boolean;
    /**
     * Set a custom radius to be used in the blur effect. Higher is blurrier, but
     * also more CPU-intensive.<br/>
     * Note: The blurring is performed following a bitmap downscale of x4.0, so
     * ultimately the actual radius is (4*blurRadius).
     * #### (Android specific)
     * @defaultValue 1.0
     */
    blurRadius?: AndroidDensityNumber;
    /**
     * Hide the top line of the Tab Bar
     * #### (iOS specific)
     */
    hideShadow?: boolean;
    /**
     * Control the text display mode below the tab icon
     * #### (Android specific)
     */
    titleDisplayMode?: 'alwaysShow' | 'showWhenActive' | 'alwaysHide' | 'showWhenActiveForce';
    /**
     * Set the elevation of the Bottom Tabs in dp
     * #### (Android specific)
     */
    elevation?: AndroidDensityNumber;
    /**
     * Hides the BottomTabs on scroll to increase the amount of content visible to the user.
     * The options requires that the scrollable view will be the root view of the screen and that it specifies `nestedScrollEnabled: true`.
     * #### (Android specific)
     */
    hideOnScroll?: boolean;
    /**
     * Control the top border color of the Bottom tabs bar
     */
    borderColor?: Color;
    /**
     * Control the top border width of the Bottom tabs bar
     */
    borderWidth?: number;
    /**
     * Control the shadow of the Bottom tabs bar
     */
    shadow?: ShadowOptions;
}
export interface ShadowOptions {
    /**
     * The opacity of the shadow
     */
    opacity?: number;
    /**
     * The color of the shadow
     */
    color?: Color;
    /**
     * The blur radius used to create the shadow
     */
    radius?: number;
}
export interface DotIndicatorOptions {
    color?: Color;
    size?: number;
    visible?: boolean;
}
export interface ImageSystemSource {
    system: string;
    fallback?: ImageRequireSource | string;
}
export type ImageResource = ImageSourcePropType | string | ImageSystemSource;
export interface OptionsBottomTab {
    dotIndicator?: DotIndicatorOptions;
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
    badgeColor?: Color;
    /**
     * Show the badge with the animation.
     * #### (Android specific)
     */
    animateBadge?: boolean;
    /**
     * Set a testID to reference the tab in E2E tests
     */
    testID?: string;
    /**
     * Set the tab icon
     */
    icon?: ImageResource;
    /**
     * Set the icon tint
     */
    iconColor?: Color;
    /**
     * Set the icon width
     * #### (Android specific)
     */
    iconWidth?: number;
    /**
     * Set the icon height
     * #### (Android specific)
     */
    iconHeight?: number;
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
     * Set the font family for the tab's text
     */
    fontFamily?: FontFamily;
    /**
     * Set the font style for the tab's text
     */
    fontStyle?: FontStyle;
    /**
     * Specifies font weight. The values 'normal' and 'bold' are supported
     * for most fonts. Not all fonts have a variant for each of the numeric
     * values, in that case the closest one is chosen.
     */
    fontWeight?: FontWeight;
    /**
     * Set the text font size
     */
    fontSize?: number;
    /**
     * Set the insets of the icon
     */
    iconInsets?: Insets;
    /**
     * Set selected icon image
     * #### (iOS specific)
     */
    selectedIcon?: ImageResource;
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
    /**
     * If it's set to false, pressing a tab won't select the tab
     * instead it will emit a bottomTabPressedEvent
     */
    selectTabOnPress?: boolean;
    /**
     * Pop to root of stack by tapping on already selected tab
     * #### (Android specific)
     */
    popToRoot?: boolean;
    /**
     * Set the SF symbol as icon (will be used primarily)
     * #### (iOS 13+ specific)
     */
    sfSymbol?: string;
    /**
     * Set the SF symbol as selected icon (will be used primarily)
     * #### (iOS 13+ specific)
     */
    sfSelectedSymbol?: string;
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
    /**
     * Set the width of the side menu
     */
    width?: number;
    /**
     * Set the height of the side menu
     */
    height?: number;
    /**
     * Stretch sideMenu contents when opened past the width
     *
     * **Not applicable when `openMode` is `aboveContent`**
     *
     * #### (iOS specific)
     * @default true
     */
    shouldStretchDrawer?: boolean;
    /**
     * Configure the opening mode of the side menu
     * #### (iOS specific)
     * @default 'pushContent'
     */
    openMode?: 'pushContent' | 'aboveContent';
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
     * Control whether this Overlay should handle Keyboard events.
     * Set this to true if your Overlay contains a TextInput.
     */
    handleKeyboardEvents?: boolean;
}
export interface ModalOptions {
    /**
     * Control whether this modal should be dismiss using swipe gesture when the modalPresentationStyle = 'pageSheet'
     * #### (iOS specific)
     */
    swipeToDismiss?: boolean;
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
    height?: number;
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
    interpolation?: Interpolation;
}
/**
 * Used to animate the actual content added to the hierarchy.
 * Content can be a React component (component) or any other layout (Stack, BottomTabs etc)
 */
export interface ScreenAnimationOptions {
    /**
     * Animate the element over x value
     */
    x?: OptionsAnimationPropertyConfig;
    /**
     * Animate the element over y value
     */
    y?: OptionsAnimationPropertyConfig;
    /**
     * Animate the element over translateX
     */
    translationX?: OptionsAnimationPropertyConfig;
    /**
     * Animate the element over translateY
     */
    translationY?: OptionsAnimationPropertyConfig;
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
export interface ColorAnimationOptions {
    /**
     * Color duration time; Default is as determined by the OS
     */
    duration?: number;
}
export interface ViewAnimationOptions extends ScreenAnimationOptions {
    /**
     * ID of the Top Bar we want to animate
     */
    id?: string;
}
export interface TopBarAnimationOptions extends ViewAnimationOptions {
    /**
     * Animation of the top-bar's background color, in case the top-bar background color
     * has been explicitly specified.
     *
     * Applicable only in transition of screens with color (non-component) backgrounds.
     */
    bkgColor?: ColorAnimationOptions;
}
export interface StatusBarAnimationOptions extends ViewAnimationOptions {
    /**
     * Animation of the status-bar's background color, in case its background color
     * has been explicitly specified.
     */
    bkgColor?: ColorAnimationOptions;
}
export interface EnterExitAnimationOptions {
    /**
     * Animate opening component
     */
    enter?: ViewAnimationOptions;
    /**
     * Animate closing component
     */
    exit?: ViewAnimationOptions;
}
export interface OldModalAnimationOptions extends ViewAnimationOptions {
    /**
     * Animations to be applied on elements which are shared between the appearing and disappearing screens
     */
    sharedElementTransitions?: SharedElementTransition[];
    /**
     * Animations to be applied on views in the appearing or disappearing screens
     */
    elementTransitions?: ElementTransition[];
}
export interface ModalAnimationOptions {
    /**
     * Animate opening modal
     */
    enter?: ViewAnimationOptions;
    /**
     * Animate closing modal
     */
    exit?: ViewAnimationOptions;
    /**
     * Animations to be applied on elements which are shared between the appearing and disappearing screens
     */
    sharedElementTransitions?: SharedElementTransition[];
    /**
     * Animations to be applied on views in the appearing or disappearing screens
     */
    elementTransitions?: ElementTransition[];
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
    topBar?: TopBarAnimationOptions | {
        enter?: TopBarAnimationOptions;
        exit?: TopBarAnimationOptions;
    };
    /**
     * Configure animations for the status bar (typically aligned
     * with the top-bar's)
     */
    statusBar?: StatusBarAnimationOptions | {
        enter?: StatusBarAnimationOptions;
        exit?: StatusBarAnimationOptions;
    };
    /**
     * Configure animations for the bottom tabs
     */
    bottomTabs?: ViewAnimationOptions | {
        enter?: ViewAnimationOptions;
        exit?: ViewAnimationOptions;
    };
    /**
     * Configure animations for the content (Screen)
     */
    content?: ViewAnimationOptions | {
        enter?: ViewAnimationOptions;
        exit?: ViewAnimationOptions;
    };
    /**
     * Animations to be applied on elements which are shared between the appearing and disappearing screens
     */
    sharedElementTransitions?: SharedElementTransition[];
    /**
     * Animations to be applied on views in the appearing or disappearing screens
     */
    elementTransitions?: ElementTransition[];
}
/**
 * Used for configuring command animations
 */
export interface AnimationOptions {
    /**
     * Configure the setStackRoot animation
     */
    setStackRoot?: ViewAnimationOptions | StackAnimationOptions;
    /**
     * Configure the setRoot animation
     */
    setRoot?: ViewAnimationOptions | EnterExitAnimationOptions;
    /**
     * Configure the animation of the pushed screen
     * #### (Android specific)
     */
    push?: StackAnimationOptions;
    /**
     * Configure what animates when a screen is popped
     */
    pop?: StackAnimationOptions;
    /**
     * Configure what animates when modal is shown
     */
    showModal?: OldModalAnimationOptions | ModalAnimationOptions;
    /**
     * Configure what animates when modal is dismissed
     */
    dismissModal?: OldModalAnimationOptions | ModalAnimationOptions;
}
/**
 * Configure Android's NavigationBar
 */
export interface NavigationBarOptions {
    backgroundColor?: Color;
    visible?: boolean;
}
/**
 * Used for configuring and controlling the main window in iOS
 */
export interface WindowOptions {
    /**
     * Configure the background color of the application's main window.
     */
    backgroundColor?: Color;
}
export interface IconBackgroundOptions {
    /**
     * Set background color
     */
    color: Color;
    /**
     * Set background color on disabled state
     */
    disabledColor?: Color;
    /**
     * Set corner radius
     */
    cornerRadius?: number;
    /**
     * Set width
     */
    width?: number;
    /**
     * Set height
     */
    height?: number;
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
     * Configure the modal
     */
    modal?: ModalOptions;
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
     * Configure Android's NavigationBar
     */
    navigationBar?: NavigationBarOptions;
    /**
     * Android Hardware Back button configuration
     */
    hardwareBackButton?: HardwareBackButtonOptions;
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
    backgroundImage?: ImageResource;
    /**
     * Background image for the Navigation View
     * #### (iOS specific)
     */
    rootBackgroundImage?: ImageResource;
    /**
     * Provides a way to configure the overall presentation of your application's main user interface
     * #### (iOS specific)
     */
    window?: WindowOptions;
    /**
     * Enable or disable automatically blurring focused input, dismissing keyboard on unmount
     * #### (Android specific)
     * @default false
     */
    blurOnUnmount?: boolean;
}
export {};
