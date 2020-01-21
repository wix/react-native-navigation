#import <Foundation/Foundation.h>
#import "RNNScreenTransition.h"
#import "DisplayLinkAnimatorDelegate.h"

@interface ScreenTransitionsCreator : NSObject

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition;

- (NSArray<id<DisplayLinkAnimatorDelegate>> *)createFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView;

- (NSTimeInterval)minDuration;

@end
