#import "RNNBackgroundOptions.h"

@implementation RNNBackgroundOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.color = [ColorParser parse:dict key:@"color"];
    self.translucent = [BoolParser parse:dict key:@"translucent"];
    self.blur = [BoolParser parse:dict key:@"blur"];
    self.clipToBounds = [BoolParser parse:dict key:@"clipToBounds"];
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
