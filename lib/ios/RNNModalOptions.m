#import "RNNModalOptions.h"
#import "OptionsArrayParser.h"

@implementation RNNModalOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.swipeToDismiss = [BoolParser parse:dict key:@"swipeToDismiss"];
	self.prefersScrollingExpandsWhenScrolledToEdge = [BoolParser parse:dict key:@"prefersScrollingExpandsWhenScrolledToEdge"];
	self.prefersEdgeAttachedInCompactHeight = [BoolParser parse:dict key:@"prefersEdgeAttachedInCompactHeight"];
	self.widthFollowsPreferredContentSizeWhenEdgeAttached = [BoolParser parse:dict key:@"widthFollowsPreferredContentSizeWhenEdgeAttached"];
	self.prefersGrabberVisible = [BoolParser parse:dict key:@"prefersGrabberVisible"];
	self.preferredCornerRadius = [NumberParser parse:dict key:@"preferredCornerRadius"];
	self.largestUndimmedDetent = [TextParser parse:dict key:@"largestUndimmedDetent"];
	//FIXME: Probably incorrect usage of the parser and incorrect class
	self.detents = [OptionsArrayParser parse:dict key:@"detents" ofClass:Text.class];
    return self;
}

- (void)mergeOptions:(RNNModalOptions *)options {
    if (options.swipeToDismiss.hasValue)
        self.swipeToDismiss = options.swipeToDismiss;
	if (options.prefersScrollingExpandsWhenScrolledToEdge.hasValue) {
		self.prefersScrollingExpandsWhenScrolledToEdge = options.prefersScrollingExpandsWhenScrolledToEdge;
	}
	if (options.prefersEdgeAttachedInCompactHeight.hasValue)
		self.prefersEdgeAttachedInCompactHeight = options.prefersEdgeAttachedInCompactHeight;
	if (options.widthFollowsPreferredContentSizeWhenEdgeAttached.hasValue) {
		self.widthFollowsPreferredContentSizeWhenEdgeAttached = options.widthFollowsPreferredContentSizeWhenEdgeAttached;
	}
	if (options.prefersGrabberVisible.hasValue)
		self.prefersGrabberVisible = options.prefersGrabberVisible;
	if (options.preferredCornerRadius.hasValue)
		self.preferredCornerRadius = options.preferredCornerRadius;
	if (options.largestUndimmedDetent.hasValue)
		self.largestUndimmedDetent = options.largestUndimmedDetent;
	if (options.detents)
		self.detents = options.detents;
	
}

@end
