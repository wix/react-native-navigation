#import "RNNAnimationsOptions.h"

@implementation RNNAnimationsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.push = [[RNNScreenTransition alloc] initWithDict:dict[@"push"]];
    self.pop = [[RNNScreenTransition alloc] initWithDict:dict[@"pop"]];
    self.showModal = [[RNNEnterExitAnimation alloc] initWithDict:dict[@"showModal"]];
    self.dismissModal = [[RNNEnterExitAnimation alloc] initWithDict:dict[@"dismissModal"]];
    self.setStackRoot = [[RNNScreenTransition alloc] initWithDict:dict[@"setStackRoot"]];
    self.setRoot = [[TransitionOptions alloc] initWithDict:dict[@"setRoot"]];

    return self;
}

- (void)mergeOptions:(RNNAnimationsOptions *)options {
    [self.push mergeOptions:options.push];
    [self.pop mergeOptions:options.pop];
    [self.showModal mergeOptions:options.showModal];
    [self.dismissModal mergeOptions:options.dismissModal];
    [self.setStackRoot mergeOptions:options.setStackRoot];
    [self.setRoot mergeOptions:options.setRoot];
}

@end
