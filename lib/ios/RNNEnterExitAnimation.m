#import "RNNEnterExitAnimation.h"
#import "OptionsArrayParser.h"

@implementation RNNEnterExitAnimation

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.enter = [[TransitionOptions alloc] initWithDict:dict[@"enter"]];
    self.exit = [[TransitionOptions alloc] initWithDict:dict[@"exit"]];
    self.sharedElementTransitions = [OptionsArrayParser parse:dict
                                                          key:@"sharedElementTransitions"
                                                      ofClass:SharedElementTransitionOptions.class];
    self.elementTransitions = [OptionsArrayParser parse:dict
                                                    key:@"elementTransitions"
                                                ofClass:ElementTransitionOptions.class];
    return self;
}

- (void)mergeOptions:(RNNEnterExitAnimation *)options {
    if (options.enter.hasValue)
        self.enter = options.enter;
    if (options.exit.hasValue)
        self.exit = options.exit;
    if (options.sharedElementTransitions)
        self.sharedElementTransitions = options.sharedElementTransitions;
    if (options.elementTransitions)
        self.elementTransitions = options.elementTransitions;
}

- (BOOL)hasAnimation {
    return self.enter.hasAnimation || self.exit.hasAnimation || self.elementTransitions ||
           self.sharedElementTransitions;
}

- (NSTimeInterval)maxDuration {
    double maxDuration = 0;
    if (self.enter.maxDuration > maxDuration)
        maxDuration = self.enter.maxDuration;
    if (self.exit.maxDuration > maxDuration)
        maxDuration = self.exit.maxDuration;

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
