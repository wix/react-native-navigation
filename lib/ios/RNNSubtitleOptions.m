#import "RNNSubtitleOptions.h"

@implementation RNNSubtitleOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.text = [TextParser parse:dict key:@"text"];
    self.alignment = [TextParser parse:dict key:@"alignment"];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.fontWeight = [TextParser parse:dict key:@"fontWeight"];
    self.color = [ColorParser parse:dict key:@"color"];

    return self;
}

- (void)mergeOptions:(RNNSubtitleOptions *)options {
    if (options.text.hasValue)
        self.text = options.text;
    if (options.alignment.hasValue)
        self.alignment = options.alignment;
    if (options.fontFamily.hasValue)
        self.fontFamily = options.fontFamily;
    if (options.fontSize.hasValue)
        self.fontSize = options.fontSize;
    if (options.fontWeight.hasValue)
        self.fontWeight = options.fontWeight;
    if (options.color.hasValue)
        self.color = options.color;
}

@end
