#import "RNNIconBackgroundOptions.h"

@implementation RNNIconBackgroundOptions {
    RNNBool *_enabled;
}

- (instancetype)initWithDict:(NSDictionary *)dict enabled:(RNNBool *)enabled {
    self = [super initWithDict:dict];
    self.color = [RNNColorParser parse:dict key:@"color"];
    self.disabledColor = [RNNColorParser parse:dict key:@"disabledColor"];
    self.cornerRadius = [RNNNumberParser parse:dict key:@"cornerRadius"];
    self.width = [RNNNumberParser parse:dict key:@"width"];
    self.height = [RNNNumberParser parse:dict key:@"height"];
    [self setEnabled:enabled];
    return self;
}

- (void)setEnabled:(RNNBool *)enabled {
    _enabled = enabled;
}

- (void)mergeOptions:(RNNIconBackgroundOptions *)options {
    if (options.color.hasValue)
        self.color = options.color;
    if (options.disabledColor.hasValue)
        self.disabledColor = options.disabledColor;
    if (options.cornerRadius.hasValue)
        self.cornerRadius = options.cornerRadius;
    if (options.width.hasValue)
        self.width = options.width;
    if (options.height.hasValue)
        self.height = options.height;
}

- (BOOL)hasValue {
    return self.color.hasValue;
}

- (RNNIconBackgroundOptions *)copyWithZone:(NSZone *)zone {
    RNNIconBackgroundOptions *newOptions = RNNIconBackgroundOptions.new;
    newOptions.color = self.color.copy;
    newOptions.disabledColor = self.disabledColor.copy;
    newOptions.cornerRadius = self.cornerRadius.copy;
    newOptions.width = self.width.copy;
    newOptions.height = self.height.copy;
    return newOptions;
}

@end
