#import "RNNReactView.h"
#import <UIKit/UIKit.h>

/**
 * Hosts an `RNNReactView` that renders a user-supplied React component as a
 * bottom tab item. The view is laid out on top of the underlying
 * `UITabBarButton` and forwards touches to it so native selection,
 * accessibility focus and `selectTabOnPress: false` keep working.
 *
 * The hosted component receives `componentId`, `tabIndex`, `selected` and
 * `badge` props. Selected state is updated via `setSelected:`.
 */
@interface RNNCustomTabBarItemView : UIView

@property(nonatomic, readonly, strong) RNNReactView *reactView;

- (instancetype)initWithReactView:(RNNReactView *)reactView
                         tabIndex:(NSUInteger)tabIndex
                         selected:(BOOL)selected
                            badge:(NSString *)badge;

- (void)setSelected:(BOOL)selected;

- (void)setBadge:(NSString *)badge;

@end
