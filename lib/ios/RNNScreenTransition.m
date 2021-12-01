#import "RNNScreenTransition.h"
#import "OptionsArrayParser.h"

@implementation RNNScreenTransition

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.topBar = [[ElementTransitionOptions alloc] initWithDict:dict[@"topBar"]];
    self.content = [[RNNEnterExitAnimation alloc] initWithDict:dict[@"content"]];
    self.bottomTabs = [[ElementTransitionOptions alloc] initWithDict:dict[@"bottomTabs"]];
    self.enable = [BoolParser parse:dict key:@"enabled"];
    self.waitForRender = [BoolParser parse:dict key:@"waitForRender"];
    self.duration = [TimeIntervalParser parse:dict key:@"duration"];
    self.sharedElementTransitions = [OptionsArrayParser parse:dict
                                                          key:@"sharedElementTransitions"
                                                      ofClass:SharedElementTransitionOptions.class];
    self.elementTransitions = [OptionsArrayParser parse:dict
                                                    key:@"elementTransitions"
                                                ofClass:ElementTransitionOptions.class];

    return self;
}

- (void)mergeOptions:(RNNScreenTransition *)options {
    [self.topBar mergeOptions:options.topBar];
    [self.content mergeOptions:options.content];
    [self.bottomTabs mergeOptions:options.bottomTabs];

    if (options.enable.hasValue)
        self.enable = options.enable;
    if (options.waitForRender.hasValue)
        self.waitForRender = options.waitForRender;
    if (options.duration.hasValue)
        self.duration = options.duration;
    if (options.sharedElementTransitions)
        self.sharedElementTransitions = options.sharedElementTransitions;
    if (options.elementTransitions)
        self.elementTransitions = options.elementTransitions;
}

- (BOOL)hasCustomAnimation {
    return (self.topBar.hasAnimation || self.content.hasAnimation || self.bottomTabs.hasAnimation ||
            self.sharedElementTransitions || self.elementTransitions);
}

- (BOOL)shouldWaitForRender {
    return [self.waitForRender withDefault:NO] || self.hasCustomAnimation;
}

- (NSTimeInterval)maxDuration {
    NSTimeInterval maxDuration = 0;
    if ([self.topBar maxDuration] > maxDuration) {
        maxDuration = [self.topBar maxDuration];
    }

    if ([self.content maxDuration] > maxDuration) {
        maxDuration = [self.content maxDuration];
    }

    if ([self.bottomTabs maxDuration] > maxDuration) {
        maxDuration = [self.bottomTabs maxDuration];
    }

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
