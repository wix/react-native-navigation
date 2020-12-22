#import "RNNIconBackgroundOptions.h"

@implementation RNNIconBackgroundOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.color = [ColorParser parse:dict key:@"color"];
    self.cornerRadius = [NumberParser parse:dict key:@"cornerRadius"];
    self.width = [NumberParser parse:dict key:@"width"];
    self.height = [NumberParser parse:dict key:@"height"];
    return self;
}

- (void)mergeOptions:(RNNIconBackgroundOptions *)options {
    if (options.color.hasValue)
        self.color = options.color;
    if (options.cornerRadius.hasValue)
        self.cornerRadius = options.cornerRadius;
    if (options.width.hasValue)
        self.width = options.width;
    if (options.height.hasValue)
        self.height = options.height;
}

- (BOOL)hasValue {
    return self.color.hasValue || self.cornerRadius.hasValue || self.width.hasValue ||
           self.height.hasValue;
}

- (RNNIconBackgroundOptions *)copyWithZone:(NSZone *)zone {
    RNNIconBackgroundOptions *newOptions = RNNIconBackgroundOptions.new;
    newOptions.color = self.color.copy;
    newOptions.cornerRadius = self.cornerRadius.copy;
    newOptions.width = self.width.copy;
    newOptions.height = self.height.copy;
    return newOptions;
}

@end
