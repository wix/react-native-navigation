#import "RNNScreenTransition.h"
#import <Foundation/Foundation.h>

@interface RNNScreenTransitionsCreator : NSObject

+ (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC
                                toVC:(UIViewController *)toVC
                       containerView:(UIView *)containerView
                   contentTransition:(RNNEnterExitAnimation *)contentTransitionOptions
                  elementTransitions:
                      (NSArray<ElementTransitionOptions *> *)elementTransitionsOptions;

@end
