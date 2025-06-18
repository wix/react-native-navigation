#import "RNNBackButtonOptions.h"

@implementation RNNBackButtonOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.identifier = [TextParser parse:dict key:@"identifier"];
    self.icon = [ImageParser parse:dict key:@"icon"];
    self.sfSymbol = [TextParser parse:dict key:@"sfSymbol"];
    self.title = [TextParser parse:dict key:@"title"];
    self.transition = [TextParser parse:dict key:@"transition"];
    self.color = [ColorParser parse:dict key:@"color"];
    self.showTitle = [BoolParser parse:dict key:@"showTitle"];
    self.visible = [BoolParser parse:dict key:@"visible"];
    self.testID = [TextParser parse:dict key:@"testID"];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.enableMenu = [BoolParser parse:dict key:@"enableMenu"];
    self.displayMode = [TextParser parse:dict key:@"displayMode"];
    self.popStackOnPress = [BoolParser parse:dict key:@"popStackOnPress"];

    return self;
}

- (void)mergeOptions:(RNNBackButtonOptions *)options {
    if (options.identifier.hasValue)
        self.identifier = options.identifier;
    if (options.icon.hasValue)
        self.icon = options.icon;
    if (options.title.hasValue)
        self.title = options.title;
    if (options.transition.hasValue)
        self.transition = options.transition;
    if (options.color.hasValue)
        self.color = options.color;
    if (options.showTitle.hasValue)
        self.showTitle = options.showTitle;
    if (options.visible.hasValue)
        self.visible = options.visible;
    if (options.testID.hasValue)
        self.testID = options.testID;
    if (options.fontFamily.hasValue)
        self.fontFamily = options.fontFamily;
    if (options.fontSize.hasValue)
        self.fontSize = options.fontSize;
    if (options.enableMenu.hasValue)
        self.enableMenu = options.enableMenu;
    if (options.displayMode.hasValue)
        self.displayMode = options.displayMode;
    if (options.popStackOnPress.hasValue)
        self.popStackOnPress = options.popStackOnPress;
    if (options.sfSymbol.hasValue)
        self.sfSymbol = options.sfSymbol;
}

- (BOOL)hasValue {
    return self.icon.hasValue || self.showTitle.hasValue || self.color.hasValue ||
           self.fontFamily.hasValue || self.fontSize.hasValue || self.title.hasValue ||
           self.enableMenu.hasValue || self.displayMode.hasValue || self.sfSymbol.hasValue;
}

@end
