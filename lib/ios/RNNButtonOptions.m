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
    self.sfSymbol = [TextParser parse:dict key:@"sfSymbol"];
    self.testID = [TextParser parse:dict key:@"testID"];
    self.accessibilityLabel = [TextParser parse:dict key:@"accessibilityLabel"];
    self.color = [ColorParser parse:dict key:@"color"];
    self.disabledColor = [ColorParser parse:dict key:@"disabledColor"];
    self.icon = [ImageParser parse:dict key:@"icon"];
    self.iconInsets = [[RNNInsetsOptions alloc] initWithDict:dict[@"iconInsets"]];
    self.enabled = [BoolParser parse:dict key:@"enabled"];
    self.selectTabOnPress = [BoolParser parse:dict key:@"selectTabOnPress"];
    self.iconBackground = [[RNNIconBackgroundOptions alloc] initWithDict:dict[@"iconBackground"]
                                                                 enabled:self.enabled];
    self.systemItem = [TextParser parse:dict key:@"systemItem"];

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
    newOptions.icon = self.icon.copy;
    newOptions.sfSymbol = self.sfSymbol.copy;
    newOptions.iconInsets = self.iconInsets.copy;
    newOptions.enabled = self.enabled.copy;
    newOptions.selectTabOnPress = self.selectTabOnPress.copy;
    newOptions.iconBackground = self.iconBackground.copy;
    newOptions.systemItem = self.systemItem.copy;
    return newOptions;
}

- (void)mergeOptions:(RNNButtonOptions *)options {
    [self.iconInsets mergeOptions:options.iconInsets];
    [self.iconBackground mergeOptions:options.iconBackground];

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
    if (options.icon.hasValue)
        self.icon = options.icon;
    if (options.sfSymbol.hasValue)
        self.sfSymbol = options.sfSymbol;
    if (options.enabled.hasValue) {
        self.enabled = options.enabled;
        [self.iconBackground setEnabled:self.enabled];
    }
    if (options.selectTabOnPress.hasValue)
        self.selectTabOnPress = options.selectTabOnPress;
    if (options.systemItem.hasValue)
        self.systemItem = options.systemItem;
}

- (BOOL)shouldCreateCustomView {
    return self.icon.hasValue && (self.iconBackground.hasValue || self.iconInsets.hasValue);
}

- (BOOL)isEnabled {
    return [self.enabled withDefault:YES];
}

- (UIColor *)resolveColor {
    if (![_enabled withDefault:YES] && _disabledColor.hasValue)
        return _disabledColor.get;
    else
        return [_color withDefault:nil];
}

- (RNNButtonOptions *)withDefault:(RNNButtonOptions *)defaultOptions {
    if (!defaultOptions)
        return self;
    RNNButtonOptions *withDefault = defaultOptions.copy;
    [withDefault mergeOptions:self];
    return withDefault;
}

- (RNNButtonOptions *)withDefaultColor:(Color *)color disabledColor:(Color *)disabledColor {
    if (!self.color.hasValue)
        self.color = color;
    if (!self.disabledColor.hasValue)
        self.disabledColor = disabledColor;
    return self;
}

@end
