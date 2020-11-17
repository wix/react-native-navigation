#import "ElementTransitionOptions.h"
#import "SharedElementTransitionOptions.h"
#import "TransitionOptions.h"

@interface ViewAnimationOptions : TransitionOptions

@property(nonatomic, strong) NSArray<ElementTransitionOptions *> *elementTransitions;
@property(nonatomic, strong) NSArray<SharedElementTransitionOptions *> *sharedElementTransitions;

- (BOOL)shouldWaitForRender;

@end
