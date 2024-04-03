#import "RNNTopBarOptions.h"
#import "RNNButtonsParser.h"
#import "RNNCustomTitleView.h"
#import "UINavigationController+RNNOptions.h"
#import "UIViewController+RNNOptions.h"

@implementation RNNTopBarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.visible = [RNNBoolParser parse:dict key:@"visible"];
    self.hideOnScroll = [RNNBoolParser parse:dict key:@"hideOnScroll"];
    self.leftButtonColor = [RNNColorParser parse:dict key:@"leftButtonColor"];
    self.rightButtonColor = [RNNColorParser parse:dict key:@"rightButtonColor"];
    self.leftButtonDisabledColor = [RNNColorParser parse:dict key:@"leftButtonDisabledColor"];
    self.rightButtonDisabledColor = [RNNColorParser parse:dict key:@"rightButtonDisabledColor"];
    self.leftButtonBackgroundColor = [RNNColorParser parse:dict key:@"leftButtonBackgroundColor"];
    self.rightButtonBackgroundColor = [RNNColorParser parse:dict key:@"rightButtonBackgroundColor"];
    self.drawBehind = [RNNBoolParser parse:dict key:@"drawBehind"];
    self.noBorder = [RNNBoolParser parse:dict key:@"noBorder"];
    self.borderColor = [RNNColorParser parse:dict key:@"borderColor"];
    self.animate = [RNNBoolParser parse:dict key:@"animate"];
    self.animateLeftButtons = [RNNBoolParser parse:dict key:@"animateLeftButtons"];
    self.animateRightButtons = [RNNBoolParser parse:dict key:@"animateRightButtons"];
    self.searchBarHiddenWhenScrolling = [RNNBoolParser parse:dict key:@"searchBarHiddenWhenScrolling"];
    self.hideNavBarOnFocusSearchBar = [RNNBoolParser parse:dict key:@"hideNavBarOnFocusSearchBar"];
    self.testID = [RNNTextParser parse:dict key:@"testID"];
    self.barStyle = [RNNTextParser parse:dict key:@"barStyle"];
    self.searchBarPlaceholder = [RNNTextParser parse:dict key:@"searchBarPlaceholder"];
    self.searchBarBackgroundColor = [RNNColorParser parse:dict key:@"searchBarBackgroundColor"];
    self.searchBarTintColor = [RNNColorParser parse:dict key:@"searchBarTintColor"];
    self.searchBar = [[RNNSearchBarOptions alloc] initWithDict:dict[@"searchBar"]];
    self.largeTitle = [[RNNLargeTitleOptions alloc] initWithDict:dict[@"largeTitle"]];
    self.title = [[RNNTitleOptions alloc] initWithDict:dict[@"title"]];
    self.subtitle = [[RNNSubtitleOptions alloc] initWithDict:dict[@"subtitle"]];
    self.background = [[RNNBackgroundOptions alloc] initWithDict:dict[@"background"]];
    self.scrollEdgeAppearance =
        [[RNNScrollEdgeAppearanceOptions alloc] initWithDict:dict[@"scrollEdgeAppearance"]];
    self.backButton = [[RNNBackButtonOptions alloc] initWithDict:dict[@"backButton"]];

    self.leftButtons = [RNNButtonsParser parse:dict[@"leftButtons"]];
    self.rightButtons = [RNNButtonsParser parse:dict[@"rightButtons"]];

    return self;
}

- (void)mergeOptions:(RNNTopBarOptions *)options {
    [self.searchBar mergeOptions:options.searchBar];
    [self.largeTitle mergeOptions:options.largeTitle];
    [self.title mergeOptions:options.title];
    [self.subtitle mergeOptions:options.subtitle];
    [self.background mergeOptions:options.background];
    [self.scrollEdgeAppearance mergeOptions:options.scrollEdgeAppearance];
    [self.backButton mergeOptions:options.backButton];

    if (options.visible.hasValue)
        self.visible = options.visible;
    if (options.hideOnScroll)
        self.hideOnScroll = options.hideOnScroll;
    if (options.leftButtonColor.hasValue)
        self.leftButtonColor = options.leftButtonColor;
    if (options.rightButtonColor.hasValue)
        self.rightButtonColor = options.rightButtonColor;
    if (options.leftButtonDisabledColor.hasValue)
        self.leftButtonDisabledColor = options.leftButtonDisabledColor;
    if (options.rightButtonDisabledColor.hasValue)
        self.rightButtonDisabledColor = options.rightButtonDisabledColor;
    if (options.leftButtonBackgroundColor.hasValue)
        self.leftButtonBackgroundColor = options.leftButtonBackgroundColor;
    if (options.rightButtonBackgroundColor.hasValue)
        self.rightButtonBackgroundColor = options.rightButtonBackgroundColor;
    if (options.drawBehind.hasValue)
        self.drawBehind = options.drawBehind;
    if (options.noBorder.hasValue)
        self.noBorder = options.noBorder;
    if (options.borderColor.hasValue)
        self.borderColor = options.borderColor;
    if (options.animate.hasValue)
        self.animate = options.animate;
    if (options.animateLeftButtons.hasValue)
        self.animateLeftButtons = options.animateLeftButtons;
    if (options.animateRightButtons.hasValue)
        self.animateRightButtons = options.animateRightButtons;
    if (options.searchBarHiddenWhenScrolling.hasValue)
        self.searchBarHiddenWhenScrolling = options.searchBarHiddenWhenScrolling;
    if (options.hideNavBarOnFocusSearchBar.hasValue)
        self.hideNavBarOnFocusSearchBar = options.hideNavBarOnFocusSearchBar;
    if (options.testID.hasValue)
        self.testID = options.testID;
    if (options.barStyle.hasValue)
        self.barStyle = options.barStyle;
    if (options.searchBarPlaceholder.hasValue)
        self.searchBarPlaceholder = options.searchBarPlaceholder;
    if (options.searchBarBackgroundColor.hasValue)
        self.searchBarBackgroundColor = options.searchBarBackgroundColor;
    if (options.searchBarTintColor.hasValue)
        self.searchBarTintColor = options.searchBarTintColor;
    if (options.leftButtons)
        self.leftButtons = options.leftButtons;
    if (options.rightButtons)
        self.rightButtons = options.rightButtons;
}

- (BOOL)shouldDrawBehind {
    return [self.drawBehind withDefault:NO] || [self.background.translucent withDefault:NO] ||
           ![self.visible withDefault:YES] || [self.largeTitle.visible withDefault:NO];
}

@end
