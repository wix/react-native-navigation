#import "RNNTopBarOptions.h"
#import "RNNCustomTitleView.h"
#import "RNNNavigationButtons.h"
#import "UINavigationController+RNNOptions.h"
#import "UIViewController+RNNOptions.h"

@interface RNNTopBarOptions ()

@property(nonatomic, strong) RNNNavigationButtons *navigationButtons;

@end

@implementation RNNTopBarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.visible = [BoolParser parse:dict key:@"visible"];
    self.hideOnScroll = [BoolParser parse:dict key:@"hideOnScroll"];
    self.leftButtonColor = [ColorParser parse:dict key:@"leftButtonColor"];
    self.rightButtonColor = [ColorParser parse:dict key:@"rightButtonColor"];
    self.leftButtonDisabledColor = [ColorParser parse:dict key:@"leftButtonDisabledColor"];
    self.rightButtonDisabledColor = [ColorParser parse:dict key:@"rightButtonDisabledColor"];
    self.drawBehind = [BoolParser parse:dict key:@"drawBehind"];
    self.noBorder = [BoolParser parse:dict key:@"noBorder"];
    self.borderColor = [ColorParser parse:dict key:@"borderColor"];
    self.animate = [BoolParser parse:dict key:@"animate"];
    self.searchBarHiddenWhenScrolling = [BoolParser parse:dict key:@"searchBarHiddenWhenScrolling"];
    self.hideNavBarOnFocusSearchBar = [BoolParser parse:dict key:@"hideNavBarOnFocusSearchBar"];
    self.testID = [TextParser parse:dict key:@"testID"];
    self.barStyle = [TextParser parse:dict key:@"barStyle"];
    self.searchBarPlaceholder = [TextParser parse:dict key:@"searchBarPlaceholder"];
    self.searchBarBackgroundColor = [ColorParser parse:dict key:@"searchBarBackgroundColor"];
    self.searchBarTintColor = [ColorParser parse:dict key:@"searchBarTintColor"];
    self.searchBar = [[RNNSearchBarOptions alloc] initWithDict:dict[@"searchBar"]];
    self.largeTitle = [[RNNLargeTitleOptions alloc] initWithDict:dict[@"largeTitle"]];
    self.title = [[RNNTitleOptions alloc] initWithDict:dict[@"title"]];
    self.subtitle = [[RNNSubtitleOptions alloc] initWithDict:dict[@"subtitle"]];
    self.background = [[RNNBackgroundOptions alloc] initWithDict:dict[@"background"]];
    self.scrollEdgeAppearance =
        [[RNNScrollEdgeAppearanceOptions alloc] initWithDict:dict[@"scrollEdgeAppearance"]];
    self.backButton = [[RNNBackButtonOptions alloc] initWithDict:dict[@"backButton"]];
    self.leftButtonStyle = [[RNNButtonOptions alloc] initWithDict:dict[@"leftButtonStyle"]];
    self.rightButtonStyle = [[RNNButtonOptions alloc] initWithDict:dict[@"rightButtonStyle"]];

    if (self.leftButtonColor.hasValue) {
        self.leftButtonStyle.color = self.leftButtonColor;
    }

    if (self.rightButtonColor.hasValue) {
        self.rightButtonStyle.color = self.rightButtonColor;
    }

    if (self.leftButtonDisabledColor.hasValue) {
        self.leftButtonStyle.disabledColor = self.rightButtonDisabledColor;
    }

    if (self.rightButtonDisabledColor.hasValue) {
        self.rightButtonStyle.disabledColor = self.rightButtonDisabledColor;
    }

    self.leftButtons = dict[@"leftButtons"];
    self.rightButtons = dict[@"rightButtons"];

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
    [self.leftButtonStyle mergeOptions:options.leftButtonStyle];
    [self.rightButtonStyle mergeOptions:options.rightButtonStyle];

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
    if (options.drawBehind.hasValue)
        self.drawBehind = options.drawBehind;
    if (options.noBorder.hasValue)
        self.noBorder = options.noBorder;
    if (options.borderColor.hasValue)
        self.borderColor = options.borderColor;
    if (options.animate.hasValue)
        self.animate = options.animate;
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
    return [self.drawBehind getWithDefaultValue:NO] ||
           [self.background.translucent getWithDefaultValue:NO] ||
           ![self.visible getWithDefaultValue:YES] ||
           [self.largeTitle.visible getWithDefaultValue:NO];
}

@end
