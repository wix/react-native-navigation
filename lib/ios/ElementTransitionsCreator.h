#import <Foundation/Foundation.h>
#import "SharedElementTransitionOptions.h"
#import "ElementTransition.h"
#import "DisplayLinkAnimatorDelegate.h"

@interface ElementTransitionsCreator : NSObject

- (instancetype)initWithElementTransitions:(NSArray<ElementTransitionOptions *>*)elementTransitions;

- (NSArray<DisplayLinkAnimatorDelegate>*)createFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView;

@end
