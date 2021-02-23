#import "RNNBottomTabsOptions.h"

@implementation RNNBottomTabsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.visible = [BoolParser parse:dict key:@"visible"];
    self.currentTabIndex = [IntNumberParser parse:dict key:@"currentTabIndex"];
    self.drawBehind = [BoolParser parse:dict key:@"drawBehind"];
    self.animate = [BoolParser parse:dict key:@"animate"];
    self.tabColor = [ColorParser parse:dict key:@"tabColor"];
    self.selectedTabColor = [ColorParser parse:dict key:@"selectedTabColor"];
    self.translucent = [BoolParser parse:dict key:@"translucent"];
    self.hideShadow = [BoolParser parse:dict key:@"hideShadow"];
    self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.testID = [TextParser parse:dict key:@"testID"];
    self.currentTabId = [TextParser parse:dict key:@"currentTabId"];
    self.barStyle = [TextParser parse:dict key:@"barStyle"];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
    self.titleDisplayMode = [TextParser parse:dict key:@"titleDisplayMode"];
    self.tabsAttachMode = (BottomTabsAttachMode *)[EnumParser parse:dict
                                                                key:@"tabsAttachMode"
                                                            ofClass:BottomTabsAttachMode.class];
    self.borderColor = [ColorParser parse:dict key:@"borderColor"];
    self.borderWidth = [NumberParser parse:dict key:@"borderWidth"];
    self.shadow = [[RNNShadowOptions alloc] initWithDict:dict[@"shadow"]];

    return self;
}

- (void)mergeOptions:(RNNBottomTabsOptions *)options {
    if (options.visible.hasValue)
        self.visible = options.visible;
    if (options.currentTabIndex.hasValue)
        self.currentTabIndex = options.currentTabIndex;
    if (options.drawBehind.hasValue)
        self.drawBehind = options.drawBehind;
    if (options.animate.hasValue)
        self.animate = options.animate;
    if (options.tabColor.hasValue)
        self.tabColor = options.tabColor;
    if (options.selectedTabColor.hasValue)
        self.selectedTabColor = options.selectedTabColor;
    if (options.translucent.hasValue)
        self.translucent = options.translucent;
    if (options.hideShadow.hasValue)
        self.hideShadow = options.hideShadow;
    if (options.backgroundColor.hasValue)
        self.backgroundColor = options.backgroundColor;
    if (options.fontSize.hasValue)
        self.fontSize = options.fontSize;
    if (options.testID.hasValue)
        self.testID = options.testID;
    if (options.currentTabId.hasValue)
        self.currentTabId = options.currentTabId;
    if (options.barStyle.hasValue)
        self.barStyle = options.barStyle;
    if (options.fontFamily.hasValue)
        self.fontFamily = options.fontFamily;
    if (options.titleDisplayMode.hasValue)
        self.titleDisplayMode = options.titleDisplayMode;
    if (options.tabsAttachMode.hasValue)
        self.tabsAttachMode = options.tabsAttachMode;
    if (options.borderColor.hasValue)
        self.borderColor = options.borderColor;
    if (options.borderWidth.hasValue)
        self.borderWidth = options.borderWidth;

    [self.shadow mergeOptions:options.shadow];
}

- (BOOL)shouldDrawBehind {
    return [self.drawBehind withDefault:NO] || [self.translucent withDefault:NO] ||
           ![self.visible withDefault:YES];
}

@end
