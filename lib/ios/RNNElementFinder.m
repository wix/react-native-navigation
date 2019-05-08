#import "RNNElementFinder.h"

@interface RNNElementFinder ()

@property (nonatomic, strong) NSArray* toVCTransitionElements;
@property (nonatomic, strong) NSArray* fromVCTransitionElements;

@end

@implementation RNNElementFinder 

- (instancetype)initWithToVC:(UIViewController *)toVC andfromVC:(UIViewController *)fromVC {
	self = [super init];
	
	self.toVCTransitionElements = [self findRNNElementViews:toVC.view];
	self.fromVCTransitionElements = [self findRNNElementViews:fromVC.view];
	
	return self;
}

- (instancetype)initWithFromVC:(UIViewController *)fromVC {
	self = [super init];

	self.fromVCTransitionElements = [self findRNNElementViews:fromVC.view];
	
	return self;
}

- (RNNElementView *)findViewToAnimate:(NSArray *)RNNTransitionElementViews withId:(NSString *)elementId{
	for (RNNElementView* view in RNNTransitionElementViews) {
		if ([view.elementId isEqualToString:elementId]){
			return view;
		}
	}
	return nil;
}

- (NSArray *)findRNNElementViews:(UIView*)view {
	NSMutableArray* elementViews = [NSMutableArray new];
	for (UIView *aView in view.subviews) {
		if([aView isMemberOfClass:[RNNElementView class]]) {
			[elementViews addObject:aView];
		} else{
			if ([aView subviews]) {
				[elementViews addObjectsFromArray:[self findRNNElementViews:aView]];
			}
		}
	}
	
	return elementViews;
}

- (RNNElementView *)findElementForId:(NSString *)elementId {
	if (elementId) {
		if ([self findViewToAnimate:self.toVCTransitionElements withId:elementId]) {
			return [self findViewToAnimate:self.toVCTransitionElements withId:elementId];
		} else if ([self findViewToAnimate:self.fromVCTransitionElements withId:elementId]){
			return [self findViewToAnimate:self.fromVCTransitionElements withId:elementId];
		} else {
			[[NSException exceptionWithName:NSInvalidArgumentException reason:[NSString stringWithFormat:@"elementId %@ does not exist", elementId] userInfo:nil] raise];
		}
	}
	
	return nil;
}

@end
