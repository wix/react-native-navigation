#import "RNNSearchBarOptions.h"

@implementation RNNSearchBarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];

    self.visible = [BoolParser parse:dict key:@"visible"];
    self.hideOnScroll = [BoolParser parse:dict key:@"hiddenWhenScrolling"];
    self.hideTopBarOnFocus = [BoolParser parse:dict key:@"hideTopBarOnFocus"];
    self.obscuresBackgroundDuringPresentation =
        [BoolParser parse:dict key:@"obscuresBackgroundDuringPresentation"];
    self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
    self.tintColor = [ColorParser parse:dict key:@"tintColor"];
    self.placeholder = [TextParser parse:dict key:@"placeholder"];
    return self;
}

@end
