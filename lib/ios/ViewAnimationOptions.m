#import "ViewAnimationOptions.h"
#import "OptionsArrayParser.h"

@implementation ViewAnimationOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.sharedElementTransitions = [OptionsArrayParser parse:dict
                                                          key:@"sharedElementTransitions"
                                                      ofClass:SharedElementTransitionOptions.class];
    self.elementTransitions = [OptionsArrayParser parse:dict
                                                    key:@"elementTransitions"
                                                ofClass:ElementTransitionOptions.class];

    return self;
}

- (void)mergeOptions:(ViewAnimationOptions *)options {
    if (options.sharedElementTransitions)
        self.sharedElementTransitions = options.sharedElementTransitions;
    if (options.elementTransitions)
        self.elementTransitions = options.elementTransitions;
}

- (BOOL)hasAnimation {
    return super.hasAnimation || self.sharedElementTransitions || self.elementTransitions;
}

- (BOOL)shouldWaitForRender {
    return [self.waitForRender getWithDefaultValue:NO] || self.hasAnimation;
}

- (NSTimeInterval)maxDuration {
    NSTimeInterval maxDuration = [super maxDuration];

    for (ElementTransitionOptions *elementTransition in self.elementTransitions) {
        if (elementTransition.maxDuration > maxDuration) {
            maxDuration = elementTransition.maxDuration;
        }
    }

    for (SharedElementTransitionOptions *sharedElementTransition in self.sharedElementTransitions) {
        if (sharedElementTransition.maxDuration > maxDuration) {
            maxDuration = sharedElementTransition.maxDuration;
        }
    }

    return maxDuration;
}

@end
