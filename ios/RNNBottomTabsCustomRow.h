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

@end

NS_ASSUME_NONNULL_END
