#import "RNNBackgroundOptions.h"

@implementation RNNBackgroundOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.color = [RNNColorParser parse:dict key:@"color"];
    self.translucent = [RNNBoolParser parse:dict key:@"translucent"];
    self.blur = [RNNBoolParser parse:dict key:@"blur"];
    self.clipToBounds = [RNNBoolParser parse:dict key:@"clipToBounds"];
    self.component = [[RNNComponentOptions alloc] initWithDict:dict[@"component"]];

    return self;
}

- (void)mergeOptions:(RNNBackgroundOptions *)options {
    [self.component mergeOptions:options.component];

    if (options.color.hasValue)
        self.color = options.color;
    if (options.translucent.hasValue)
        self.translucent = options.translucent;
    if (options.blur.hasValue)
        self.blur = options.blur;
    if (options.clipToBounds.hasValue)
        self.clipToBounds = options.clipToBounds;
}

@end
