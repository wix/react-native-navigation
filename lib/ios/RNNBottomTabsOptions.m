#import "RNNBottomTabsOptions.h"

@implementation RNNBottomTabsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.visible = [RNNBoolParser parse:dict key:@"visible"];
    self.currentTabIndex = [RNNIntNumberParser parse:dict key:@"currentTabIndex"];
    self.drawBehind = [RNNBoolParser parse:dict key:@"drawBehind"];
    self.animate = [RNNBoolParser parse:dict key:@"animate"];
    self.tabColor = [RNNColorParser parse:dict key:@"tabColor"];
    self.selectedTabColor = [RNNColorParser parse:dict key:@"selectedTabColor"];
    self.translucent = [RNNBoolParser parse:dict key:@"translucent"];
    self.hideShadow = [RNNBoolParser parse:dict key:@"hideShadow"];
    self.backgroundColor = [RNNColorParser parse:dict key:@"backgroundColor"];
    self.fontSize = [RNNNumberParser parse:dict key:@"fontSize"];
    self.testID = [RNNTextParser parse:dict key:@"testID"];
    self.currentTabId = [RNNTextParser parse:dict key:@"currentTabId"];
    self.barStyle = [RNNTextParser parse:dict key:@"barStyle"];
    self.fontFamily = [RNNTextParser parse:dict key:@"fontFamily"];
    self.titleDisplayMode = [RNNTextParser parse:dict key:@"titleDisplayMode"];
    self.tabsAttachMode = (RNNBottomTabsAttachMode *)[RNNEnumParser
                                                      parse:dict
                                                        key:@"tabsAttachMode"
                                                    ofClass:RNNBottomTabsAttachMode.class];
    self.borderColor = [RNNColorParser parse:dict key:@"borderColor"];
    self.borderWidth = [RNNNumberParser parse:dict key:@"borderWidth"];
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
