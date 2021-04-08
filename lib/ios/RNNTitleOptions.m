#import "RNNTitleOptions.h"

@implementation RNNTitleOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.text = [TextParser parse:dict key:@"text"];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.fontWeight = [TextParser parse:dict key:@"fontWeight"];
    self.color = [ColorParser parse:dict key:@"color"];

    self.component = [[RNNComponentOptions alloc] initWithDict:dict[@"component"]];

    return self;
}

- (void)mergeOptions:(RNNTitleOptions *)options {
    [self.component mergeOptions:options.component];

    if (options.text.hasValue)
        self.text = options.text;
    if (options.fontFamily.hasValue)
        self.fontFamily = options.fontFamily;
    if (options.fontSize.hasValue)
        self.fontSize = options.fontSize;
    if (options.fontWeight.hasValue)
        self.fontWeight = options.fontWeight;
    if (options.color.hasValue)
        self.color = options.color;
}

- (BOOL)hasValue {
    return self.text.hasValue || self.fontFamily.hasValue || self.fontSize.hasValue ||
           self.fontWeight.hasValue || self.color.hasValue || self.component.hasValue ||
           self.componentAlignment.hasValue;
}

@end
