#import <Foundation/Foundation.h>
#import "AnimatedReactView.h"
#import "DisplayLinkAnimatorDelegate.h"

@interface SharedElementTransitionsCreator : NSObject

- (NSArray<DisplayLinkAnimatorDelegate>*)createFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView;

- (instancetype)initWithSharedElementTransitions:(NSArray<SharedElementTransitionOptions *>*)sharedElementTransitions;

@end
