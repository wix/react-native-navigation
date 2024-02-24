#import "BaseAnimator.h"
#import "ElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNElementAnimator : BaseAnimator

- (instancetype)initWithTransitionOptions:(TransitionOptions *)transitionOptions
                                     view:(UIView *)view
                            containerView:(UIView *)containerView;

- (NSMutableArray<id<RNNDisplayLinkAnimation>> *)createAnimations:
    (ElementTransitionOptions *)transitionOptions;

@end
