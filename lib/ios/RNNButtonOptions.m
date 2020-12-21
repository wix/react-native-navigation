#import "RNNButtonOptions.h"

@implementation RNNButtonOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.identifier = [TextParser parse:dict key:@"id"];
    self.component = [[RNNComponentOptions alloc] initWithDict:dict[@"component"]];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
    self.fontWeight = [TextParser parse:dict key:@"fontWeight"];
    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.text = [TextParser parse:dict key:@"text"];
    self.testID = [TextParser parse:dict key:@"testID"];
    self.accessibilityLabel = [TextParser parse:dict key:@"accessibilityLabel"];
    self.color = [ColorParser parse:dict key:@"color"];
    self.disabledColor = [ColorParser parse:dict key:@"disabledColor"];
    self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
    self.icon = [ImageParser parse:dict key:@"icon"];
    self.iconInsets = [[RNNInsetsOptions alloc] initWithDict:dict[@"iconInsets"]];
    self.enabled = [BoolParser parse:dict key:@"enabled"];
    self.selectTabOnPress = [BoolParser parse:dict key:@"selectTabOnPress"];
    self.cornerRadius = [NumberParser parse:dict key:@"cornerRadius"];
    self.width = [NumberParser parse:dict key:@"width"];
    self.height = [NumberParser parse:dict key:@"height"];

    return self;
}

- (RNNButtonOptions *)copy {
    RNNButtonOptions *newOptions = RNNButtonOptions.new;
    newOptions.identifier = self.identifier.copy;
    newOptions.component = self.component.copy;
    newOptions.fontFamily = self.fontFamily.copy;
    newOptions.fontWeight = self.fontWeight.copy;
    newOptions.fontSize = self.fontSize.copy;
    newOptions.text = self.text.copy;
    newOptions.testID = self.testID.copy;
    newOptions.accessibilityLabel = self.accessibilityLabel.copy;
    newOptions.color = self.color.copy;
    newOptions.disabledColor = self.disabledColor.copy;
    newOptions.backgroundColor = self.backgroundColor.copy;
    newOptions.icon = self.icon.copy;
    newOptions.iconInsets = self.iconInsets.copy;
    newOptions.enabled = self.enabled.copy;
    newOptions.selectTabOnPress = self.selectTabOnPress.copy;
    newOptions.cornerRadius = self.cornerRadius.copy;
    newOptions.width = self.width.copy;
    newOptions.height = self.height.copy;
    return newOptions;
}

- (void)mergeOptions:(RNNButtonOptions *)options {
    [self.iconInsets mergeOptions:options.iconInsets];

    if (options.identifier.hasValue)
        self.identifier = options.identifier;
    if (options.component.hasValue)
        self.component = options.component;
    if (options.fontFamily.hasValue)
        self.fontFamily = options.fontFamily;
    if (options.text.hasValue)
        self.text = options.text;
    if (options.testID.hasValue)
        self.testID = options.testID;
    if (options.accessibilityLabel.hasValue)
        self.accessibilityLabel = options.accessibilityLabel;
    if (options.fontSize.hasValue)
        self.fontSize = options.fontSize;
    if (options.color.hasValue)
        self.color = options.color;
    if (options.disabledColor.hasValue)
        self.disabledColor = options.disabledColor;
    if (options.backgroundColor.hasValue)
        self.backgroundColor = options.backgroundColor;
    if (options.icon.hasValue)
        self.icon = options.icon;
    if (options.enabled.hasValue)
        self.enabled = options.enabled;
    if (options.selectTabOnPress.hasValue)
        self.selectTabOnPress = options.selectTabOnPress;
    if (options.cornerRadius.hasValue)
        self.cornerRadius = options.cornerRadius;
    if (options.width.hasValue)
        self.width = options.width;
    if (options.height.hasValue)
        self.height = options.height;
}

- (BOOL)shouldCreateCustomView {
    return self.icon && (self.backgroundColor.hasValue || self.cornerRadius.hasValue ||
                         self.iconInsets.hasValue || self.width.hasValue || self.height.hasValue);
}

- (RNNButtonOptions *)withDefault:(RNNButtonOptions *)defaultOptions {
    if (!defaultOptions)
        return self;
    RNNButtonOptions *withDefault = defaultOptions.copy;
    [withDefault mergeOptions:self];
    return withDefault;
}

@end
