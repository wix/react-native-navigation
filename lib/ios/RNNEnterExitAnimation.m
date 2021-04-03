#import "RNNEnterExitAnimation.h"

@implementation RNNEnterExitAnimation

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.enter = [[TransitionOptions alloc] initWithDict:dict[@"enter"]];
    self.exit = [[TransitionOptions alloc] initWithDict:dict[@"exit"]];

    return self;
}

- (void)mergeOptions:(RNNEnterExitAnimation *)options {
    if (options.enter.hasAnimation)
        self.enter = options.enter;
    if (options.exit.hasAnimation)
        self.exit = options.exit;
}

- (BOOL)hasAnimation {
    return self.enter.hasAnimation || self.exit.hasAnimation;
}

- (NSTimeInterval)maxDuration {
    double maxDuration = 0;
    if (self.enter.maxDuration > maxDuration)
        maxDuration = self.enter.maxDuration;
    if (self.exit.maxDuration > maxDuration)
        maxDuration = self.exit.maxDuration;

    return maxDuration;
}

@end
