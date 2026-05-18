#import "RNNOptions.h"

/**
 * Visual options for the row that hosts custom-component bottom tab cells.
 *
 * Applies only when every tab declares `bottomTab.component`. All fields are
 * optional; if omitted the row uses sensible defaults (iOS 26 glass pill on
 * iOS 26+, blur with no inset on older versions).
 *
 * On Android these options are currently ignored (Android keeps the native
 * Material bottom bar chrome and overlays the React component on it).
 */
@interface RNNBottomTabsCustomRowOptions : RNNOptions

/**
 * Override the row's content height. The native tab bar (and its safe-area
 * inset) is preserved underneath — this only changes how tall the visible
 * floating row appears. Defaults to the native tab bar content height (+18pt
 * on iOS 26+ to match the larger native floating bar).
 */
@property(nonatomic, strong) Number *height;

/** Solid background color for the row. When set, overrides `backgroundEffect`. */
@property(nonatomic, strong) Color *backgroundColor;

/**
 * Visual effect for the row background. Values: `glass` | `blur` | `none`.
 * Default: `glass` on iOS 26+, `blur` on older versions.
 */
@property(nonatomic, strong) Text *backgroundEffect;

/** Corner radius of the row. Default: 28 on iOS 26+, 0 below. */
@property(nonatomic, strong) Number *cornerRadius;

/** Horizontal inset of the row from the screen edges. Default: 16 on iOS 26+, 0 below. */
@property(nonatomic, strong) Number *horizontalMargin;

/** Distance between the row's bottom edge and the safe-area bottom. Default: 0. */
@property(nonatomic, strong) Number *bottomMargin;

- (BOOL)hasValue;

@end
