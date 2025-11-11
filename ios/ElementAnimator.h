#import "BaseAnimator.h"
#import "ElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface ElementAnimator : BaseAnimator

- (instancetype)initWithTransitionOptions:(TransitionOptions *)transitionOptions
                                     view:(UIView *)view
                            containerView:(UIView *)containerView;

- (NSMutableArray<id<DisplayLinkAnimation>> *)createAnimations:
    (ElementTransitionOptions *)transitionOptions;

@end
