#import "RNNOptions.h"

@interface RNNModalOptions : RNNOptions

@property(nonatomic, strong) Bool *swipeToDismiss;
@property(nonatomic, strong) Bool *prefersScrollingExpandsWhenScrolledToEdge;
@property(nonatomic, strong) Bool *prefersEdgeAttachedInCompactHeight;
@property(nonatomic, strong) Bool *widthFollowsPreferredContentSizeWhenEdgeAttached;
@property(nonatomic, strong) Bool *prefersGrabberVisible;
@property(nonatomic, strong) Number *preferredCornerRadius;
@property(nonatomic, strong) NSArray<Text *> *detents;
@property(nonatomic, strong) Text *largestUndimmedDetent;
@property(nonatomic, strong) Text *selectedDetentIdentifier;

@end
