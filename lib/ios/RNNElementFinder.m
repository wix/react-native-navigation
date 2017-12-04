#import "RNNElementFinder.h"

@implementation RNNElementFinder 

-(instancetype)initWithToVC:(UIViewController*)toVC andfromVC:(UIViewController*)fromVC {
	self = [super init];
	self.toVCTransitionElements = [self findRNNElementViews:toVC.view];
	self.fromVCTransitionElements = [self findRNNElementViews:fromVC.view];
	return self;
}

-(RNNElementView*)findViewToAnimate:(NSArray*)RNNTransitionElementViews withId:(NSString*)elementId{
	for (RNNElementView* view in RNNTransitionElementViews) {
		if ([view.elementId isEqualToString:elementId]){
			return view;
		}
	}
	return nil;
}

-(NSArray*)findRNNElementViews:(UIView*)view{
	NSMutableArray* elementViews = [NSMutableArray new];
	for(UIView *aView in view.subviews){
		if([aView isMemberOfClass:[RNNElementView class]]){
			[elementViews addObject:aView];
		} else{
			if ([aView subviews]) {
				[elementViews addObjectsFromArray:[self findRNNElementViews:aView]];
			}
		}
	}
	return elementViews;
}

-(void)findElementsInTransition:(RNNTransitionStateHolder*)transitionStateHolder {
	if ([self findViewToAnimate:self.toVCTransitionElements withId:transitionStateHolder.fromId]) {
		transitionStateHolder.fromElement = [self findViewToAnimate:self.toVCTransitionElements withId:transitionStateHolder.fromId];
		transitionStateHolder.isFromVC = false;
	} else if ([self findViewToAnimate:self.fromVCTransitionElements withId:transitionStateHolder.fromId]){
		transitionStateHolder.fromElement = [self findViewToAnimate:self.fromVCTransitionElements withId:transitionStateHolder.fromId];
		transitionStateHolder.isFromVC = true;
	} else {
		[[NSException exceptionWithName:NSInvalidArgumentException reason:[NSString stringWithFormat:@"elementId %@ does not exist", transitionStateHolder.fromId] userInfo:nil] raise];
	}
	if (transitionStateHolder.toId) {
		if ([self findViewToAnimate:self.toVCTransitionElements withId:transitionStateHolder.toId]) {
			transitionStateHolder.toElement = [self findViewToAnimate:self.toVCTransitionElements withId:transitionStateHolder.toId];
		} else if ([self findViewToAnimate:self.fromVCTransitionElements withId:transitionStateHolder.toId]){
			transitionStateHolder.toElement = [self findViewToAnimate:self.fromVCTransitionElements withId:transitionStateHolder.toId];
		} else {
			[[NSException exceptionWithName:NSInvalidArgumentException reason:[NSString stringWithFormat:@"elementId %@ does not exist", transitionStateHolder.toId] userInfo:nil] raise];
		}
	}
}

@end
