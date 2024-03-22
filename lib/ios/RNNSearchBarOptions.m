#import "RNNSearchBarOptions.h"

@implementation RNNSearchBarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.visible = [RNNBoolParser parse:dict key:@"visible"];
    self.focus = [RNNBoolParser parse:dict key:@"focus"];
    self.hideOnScroll = [RNNBoolParser parse:dict key:@"hideOnScroll"];
    self.hideTopBarOnFocus = [RNNBoolParser parse:dict key:@"hideTopBarOnFocus"];
    self.obscuresBackgroundDuringPresentation =
        [RNNBoolParser parse:dict key:@"obscuresBackgroundDuringPresentation"];
    self.backgroundColor = [RNNColorParser parse:dict key:@"backgroundColor"];
    self.tintColor = [RNNColorParser parse:dict key:@"tintColor"];
    self.placeholder = [RNNTextParser parse:dict key:@"placeholder"];
    self.cancelText = [RNNTextParser parse:dict key:@"cancelText"];
    return self;
}

- (void)mergeOptions:(RNNSearchBarOptions *)options {
    if (options.visible.hasValue)
        self.visible = options.visible;
    if (options.focus.hasValue)
        self.focus = options.focus;
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
    if (options.cancelText.hasValue)
        self.cancelText = options.cancelText;
}

@end
