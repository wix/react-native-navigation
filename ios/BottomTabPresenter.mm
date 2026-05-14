#import "BottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"

@implementation BottomTabPresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions
                            tabCreator:(RNNTabBarItemCreator *)tabCreator {
    self = [super initWithDefaultOptions:defaultOptions];
    _tabCreator = tabCreator;
    return self;
}

- (void)applyOptions:(RNNNavigationOptions *)options child:(UIViewController *)child {
    RNNNavigationOptions *withDefault = [options withDefault:self.defaultOptions];

    [self createTabBarItem:child bottomTabOptions:withDefault.bottomTab];
    [child setTabBarItemBadge:[withDefault.bottomTab.badge withDefault:[NSNull null]]];
    [child setTabBarItemBadgeColor:[withDefault.bottomTab.badgeColor withDefault:nil]];
}

- (void)mergeOptions:(RNNNavigationOptions *)mergeOptions
     resolvedOptions:(RNNNavigationOptions *)resolvedOptions
               child:(UIViewController *)child {
    RNNNavigationOptions *withDefault = (RNNNavigationOptions *)[[resolvedOptions
        withDefault:self.defaultOptions] mergeOptions:mergeOptions];

    if (mergeOptions.bottomTab.hasValue)
        [self createTabBarItem:child bottomTabOptions:withDefault.bottomTab];
    if (mergeOptions.bottomTab.badge.hasValue)
        [child setTabBarItemBadge:mergeOptions.bottomTab.badge.get];
    if (mergeOptions.bottomTab.badgeColor.hasValue)
        [child setTabBarItemBadgeColor:mergeOptions.bottomTab.badgeColor.get];
}

- (void)createTabBarItem:(UIViewController *)child
        bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    if (_useCustomItemViews && bottomTabOptions.component.name.hasValue) {
        UITabBarItem *blankItem = [self createBlankTabBarItem:child
                                             bottomTabOptions:bottomTabOptions];
        if (blankItem != child.tabBarItem) {
            child.tabBarItem = blankItem;
        }
        return;
    }

    UITabBarItem *updatedItem = [_tabCreator createTabBarItem:bottomTabOptions mergeItem:child.tabBarItem];
    if (updatedItem != child.tabBarItem) {
        child.tabBarItem = updatedItem;
    }
}

// Builds a `UITabBarItem` that holds a slot in the native bar but renders
// nothing visible. We provide a transparent 24×24 placeholder image (rather
// than `nil`) so that iOS 26's floating pill reserves its normal size — the
// React-rendered `RNNCustomTabBarItemView` is overlaid on the resulting tab
// button by `RNNBottomTabsController`. Title is left empty for the same
// reason: an empty string still lets UIKit size the pill consistently.
- (UITabBarItem *)createBlankTabBarItem:(UIViewController *)child
                       bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    UITabBarItem *item = child.tabBarItem ?: [UITabBarItem new];
    UIImage *placeholder = [BottomTabPresenter rnn_transparentPlaceholderImage];
    item.image = placeholder;
    item.selectedImage = placeholder;
    item.title = @"";
    item.tag = bottomTabOptions.tag;
    item.accessibilityIdentifier = [bottomTabOptions.testID withDefault:nil];
    item.accessibilityLabel = [bottomTabOptions.accessibilityLabel withDefault:nil];
    item.imageInsets = UIEdgeInsetsZero;
    return item;
}

+ (UIImage *)rnn_transparentPlaceholderImage {
    static UIImage *image;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        CGSize size = CGSizeMake(24, 24);
        UIGraphicsImageRenderer *renderer = [[UIGraphicsImageRenderer alloc] initWithSize:size];
        UIImage *rendered = [renderer imageWithActions:^(UIGraphicsImageRendererContext *ctx) {
            [UIColor.clearColor setFill];
            UIRectFill(CGRectMake(0, 0, size.width, size.height));
        }];
        image = [rendered imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    });
    return image;
}

@end
