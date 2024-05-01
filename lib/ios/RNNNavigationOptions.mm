#import "RNNNavigationOptions.h"
#import "RNNBottomTabsController.h"
#import "RNNComponentViewController.h"
#import "RNNSideMenuController.h"
#import "RNNSplitViewController.h"
#import "RNNSplitViewOptions.h"
#import "RNNStackController.h"
#import "RNNTopBarOptions.h"
#import "UINavigationController+RNNOptions.h"
#import "UIViewController+RNNOptions.h"
#import <React/RCTConvert.h>

@implementation RNNNavigationOptions

+ (instancetype)emptyOptions {
    return [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (instancetype)initEmptyOptions {
    self = [self initWithDict:@{}];
    return self;
}

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];

    self.topBar = [[RNNTopBarOptions alloc] initWithDict:dict[@"topBar"]];
    self.bottomTabs = [[RNNBottomTabsOptions alloc] initWithDict:dict[@"bottomTabs"]];
    self.bottomTab = [[RNNBottomTabOptions alloc] initWithDict:dict[@"bottomTab"]];
    self.topTabs = [[RNNTopTabsOptions alloc] initWithDict:dict[@"topTabs"]];
    self.topTab = [[RNNTopTabOptions alloc] initWithDict:dict[@"topTab"]];
    self.sideMenu = [[RNNSideMenuOptions alloc] initWithDict:dict[@"sideMenu"]];
    self.splitView = [[RNNSplitViewOptions alloc] initWithDict:dict[@"splitView"]];
    self.overlay = [[RNNOverlayOptions alloc] initWithDict:dict[@"overlay"]];
    self.animations = [[RNNAnimationsOptions alloc] initWithDict:dict[@"animations"]];
    self.statusBar = [[RNNStatusBarOptions alloc] initWithDict:dict[@"statusBar"]];
    self.preview = [[RNNPreviewOptions alloc] initWithDict:dict[@"preview"]];
    self.layout = [[RNNLayoutOptions alloc] initWithDict:dict[@"layout"]];
    self.modal = [[RNNModalOptions alloc] initWithDict:dict[@"modal"]];
    self.deprecations = [[DeprecationOptions alloc] initWithDict:dict[@"deprecations"]];
    self.window = [[WindowOptions alloc] initWithDict:dict[@"window"]];

    self.popGesture = [[Bool alloc] initWithValue:dict[@"popGesture"]];
    self.backgroundImage = [ImageParser parse:dict key:@"backgroundImage"];
    self.rootBackgroundImage = [ImageParser parse:dict key:@"rootBackgroundImage"];
    self.modalPresentationStyle = [[Text alloc] initWithValue:dict[@"modalPresentationStyle"]];
    self.modalTransitionStyle = [[Text alloc] initWithValue:dict[@"modalTransitionStyle"]];

    return self;
}

- (RNNNavigationOptions *)mergeOptions:(RNNNavigationOptions *)options {
    if (!options) {
        return self;
    }

    RNNNavigationOptions *result = self;
    [result.topBar mergeOptions:options.topBar];
    [result.bottomTabs mergeOptions:options.bottomTabs];
    [result.bottomTab mergeOptions:options.bottomTab];
    [result.topTabs mergeOptions:options.topTabs];
    [result.topTab mergeOptions:options.topTab];
    [result.sideMenu mergeOptions:options.sideMenu];
    [result.splitView mergeOptions:options.splitView];
    [result.overlay mergeOptions:options.overlay];
    [result.animations mergeOptions:options.animations];
    [result.statusBar mergeOptions:options.statusBar];
    [result.preview mergeOptions:options.preview];
    [result.layout mergeOptions:options.layout];
    [result.modal mergeOptions:options.modal];
    [result.deprecations mergeOptions:options.deprecations];
    [result.window mergeOptions:options.window];

    if (options.popGesture.hasValue)
        result.popGesture = options.popGesture;
    if (options.backgroundImage.hasValue)
        result.backgroundImage = options.backgroundImage;
    if (options.rootBackgroundImage.hasValue)
        result.rootBackgroundImage = options.rootBackgroundImage;
    if (options.modalPresentationStyle.hasValue)
        result.modalPresentationStyle = options.modalPresentationStyle;
    if (options.modalTransitionStyle.hasValue)
        result.modalTransitionStyle = options.modalTransitionStyle;

    return result;
}

- (RNNNavigationOptions *)copy {
    RNNNavigationOptions *newOptions = [RNNNavigationOptions emptyOptions];
    [newOptions.topBar mergeOptions:self.topBar];
    [newOptions.bottomTabs mergeOptions:self.bottomTabs];
    [newOptions.bottomTab mergeOptions:self.bottomTab];
    [newOptions.topTabs mergeOptions:self.topTabs];
    [newOptions.topTab mergeOptions:self.topTab];
    [newOptions.sideMenu mergeOptions:self.sideMenu];
    [newOptions.splitView mergeOptions:self.splitView];
    [newOptions.overlay mergeOptions:self.overlay];
    [newOptions.animations mergeOptions:self.animations];
    [newOptions.statusBar mergeOptions:self.statusBar];
    [newOptions.preview mergeOptions:self.preview];
    [newOptions.layout mergeOptions:self.layout];
    [newOptions.modal mergeOptions:self.modal];
    [newOptions.deprecations mergeOptions:self.deprecations];
    [newOptions.window mergeOptions:self.window];

    newOptions.popGesture = self.popGesture;
    newOptions.backgroundImage = self.backgroundImage;
    newOptions.rootBackgroundImage = self.rootBackgroundImage;
    newOptions.modalPresentationStyle = self.modalPresentationStyle;
    newOptions.modalTransitionStyle = self.modalTransitionStyle;

    return newOptions;
}

- (RNNNavigationOptions *)withDefault:(RNNNavigationOptions *)defaultOptions {
    return defaultOptions ? [defaultOptions.copy mergeOptions:self] : self;
}

@end
