#import "RNNBottomTabsCustomRowOptions.h"
#import "RNNCustomTabBarItemView.h"
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

/**
 * Replaces the visual content of `UITabBar` when `bottomTab.component` is
 * declared on every tab. Hosts the React-rendered cell views in equal-width
 * slots and dispatches taps back to the bottom-tabs controller.
 *
 * The native `UITabBar` is kept (with its visuals hidden) so that
 * `UITabBarController` keeps managing the bottom safe-area inset for the
 * selected child controller — this row is laid out on top of it.
 *
 * Visual chrome (height, background, corner radius, margins) is configured
 * via `RNNBottomTabsCustomRowOptions` and pushed in by the controller.
 */
@interface RNNBottomTabsCustomRow : UIView

/**
 * Block invoked when the user taps a cell. The argument is the 0-based index
 * of the tapped cell.
 */
@property(nonatomic, copy, nullable) void (^onTapAtIndex)(NSUInteger index);

/**
 * Replaces the cells displayed by this row. The previously held views are
 * removed from the hierarchy. Each cell is rendered at equal width inside
 * the safe-area-insetted content rect.
 */
- (void)setItemViews:(NSArray<RNNCustomTabBarItemView *> *)itemViews;

/**
 * Forwards the selected state to each hosted item view.
 */
- (void)setSelectedIndex:(NSUInteger)selectedIndex;

/**
 * Applies user-supplied chrome options (background, corner radius, margins).
 * Pass `nil` (or an options instance with no values) to use defaults.
 */
- (void)applyOptions:(nullable RNNBottomTabsCustomRowOptions *)options;

/**
 * Returns the total row height the row wants to occupy, given the native
 * tab bar height and its safe-area inset. The controller uses this to
 * compute the row's frame (anchored to the tab bar's bottom, extending
 * upward as needed).
 */
- (CGFloat)desiredRowHeightForNativeTabBarHeight:(CGFloat)nativeTabBarHeight
                                       safeBottom:(CGFloat)safeBottom;

@end

NS_ASSUME_NONNULL_END
