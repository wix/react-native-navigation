#import "RNNTitleOptions.h"

@implementation RNNTitleOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];
    
    self.text = [TextParser parse:dict key:@"text"];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.fontWeight = [TextParser parse:dict key:@"fontWeight"];
    self.color = [ColorParser parse:dict key:@"color"];
    
    self.component = [[RNNComponentOptions alloc] initWithDict:dict[@"component"]];
    
    return self;
}

- (BOOL)hasValue {
    return self.text.hasValue || self.fontFamily.hasValue || self.fontSize.hasValue || self.fontWeight.hasValue || self.color.hasValue || self.component.hasValue || self.componentAlignment.hasValue;
}

@end
