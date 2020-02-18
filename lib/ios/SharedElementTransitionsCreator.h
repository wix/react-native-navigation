#import <Foundation/Foundation.h>
#import "AnimatedReactView.h"
#import "DisplayLinkAnimatorDelegate.h"

@interface SharedElementTransitionsCreator : NSObject

+ (NSArray<DisplayLinkAnimatorDelegate>*)create:(NSArray<SharedElementTransitionOptions *>*)sharedElementTransitions
                                         fromVC:(UIViewController *)fromVC
                                           toVC:(UIViewController *)toVC
                                  containerView:(UIView *)containerView;

@end
