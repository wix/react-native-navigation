#import "TransitionOptions.h"
#import "ElementTransitionOptions.h"
#import "SharedElementTransitionOptions.h"

@interface ViewAnimationOptions : TransitionOptions

@property (nonatomic, strong) NSArray<ElementTransitionOptions *>* elementTransitions;
@property (nonatomic, strong) NSArray<SharedElementTransitionOptions *>* sharedElementTransitions;

- (BOOL)shouldWaitForRender;

@end
