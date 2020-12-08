#import "RNNSearchBarOptions.h"

@implementation RNNSearchBarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
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

- (void)mergeOptions:(RNNSearchBarOptions *)options {
    if (options.visible.hasValue)
        self.visible = options.visible;
    if (options.hideOnScroll.hasValue)
        self.hideOnScroll = options.hideOnScroll;
    if (options.hideTopBarOnFocus.hasValue)
        self.hideTopBarOnFocus = options.hideTopBarOnFocus;
    if (options.obscuresBackgroundDuringPresentation.hasValue)
        self.obscuresBackgroundDuringPresentation = options.obscuresBackgroundDuringPresentation;
    if (options.backgroundColor.hasValue)
        self.backgroundColor = options.backgroundColor;
    if (options.tintColor.hasValue)
        self.tintColor = options.tintColor;
    if (options.placeholder.hasValue)
        self.placeholder = options.placeholder;
}

@end
