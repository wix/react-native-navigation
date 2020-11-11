#import "RNNScreenTransition.h"
#import <Foundation/Foundation.h>

@interface RNNScreenTransitionsCreator : NSObject

+ (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC
                                toVC:(UIViewController *)toVC
                       containerView:(UIView *)containerView
                   contentTransition:(TransitionOptions *)contentTransitionOptions
                  elementTransitions:
                      (NSArray<ElementTransitionOptions *> *)elementTransitionsOptions
            sharedElementTransitions:
                (NSArray<SharedElementTransitionOptions *> *)sharedElementTransitionsOptions
                            reversed:(BOOL)reversed;

@end
