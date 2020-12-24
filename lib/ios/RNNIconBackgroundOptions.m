#import "RNNIconBackgroundOptions.h"

@implementation RNNIconBackgroundOptions {
    Bool *_enabled;
}

- (instancetype)initWithDict:(NSDictionary *)dict enabled:(Bool *)enabled {
    self = [super initWithDict:dict];
    self.color = [ColorParser parse:dict key:@"color"];
    self.disabledColor = [ColorParser parse:dict key:@"disabledColor"];
    self.cornerRadius = [NumberParser parse:dict key:@"cornerRadius"];
    self.width = [NumberParser parse:dict key:@"width"];
    self.height = [NumberParser parse:dict key:@"height"];
    [self setEnabled:enabled];
    return self;
}

- (void)setEnabled:(Bool *)enabled {
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
    return self.color.hasValue &&
           (self.cornerRadius.hasValue || self.width.hasValue || self.height.hasValue);
}

- (Color *)color {
    if (![_enabled getWithDefaultValue:YES] && _disabledColor.hasValue)
        return _disabledColor;
    else
        return _color;
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
