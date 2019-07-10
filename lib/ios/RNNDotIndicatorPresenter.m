#import <UIKit/UIKit.h>
#import "RNNDotIndicatorPresenter.h"
#import "UITabBar+Utils.h"
#import "UIViewController+LayoutProtocol.h"
#import "DotIndicatorOptions.h"

@implementation RNNDotIndicatorPresenter

- (void)apply:(UIViewController *)child {
    DotIndicatorOptions *options = [child resolveChildOptions:child].bottomTab.dotIndicator;
    if (![options hasValue]) return;

    if ([options.visible isFalse] && [child tabBarItem].tag > 0) {
        UIView *view = [[[child tabBarController] tabBar] viewWithTag:[child tabBarItem].tag];
        [view removeFromSuperview];
        [child tabBarItem].tag = -1;
        return;
    }

    if ([child tabBarItem].tag > 0) return;

    UITabBarController *bottomTabs = [self getTabBarController:child];
    int index = (int) [[bottomTabs childViewControllers] indexOfObject:child];
    UITabBar *tabBar = [bottomTabs tabBar];
    UIView *tab = [tabBar getTabView:index];
    UIView *icon = [tabBar getTabIcon:index];

    float size = [[options.size getWithDefaultValue:@6] floatValue];
    UIView *badge = [UIView new];
    badge.translatesAutoresizingMaskIntoConstraints = NO;

    badge.layer.cornerRadius = size / 2;
    badge.backgroundColor = [options.color getWithDefaultValue:[UIColor redColor]];
    badge.tag = arc4random();

    [child tabBarItem].tag = badge.tag;
    [tab addSubview:badge];

    [NSLayoutConstraint activateConstraints:@[
            [badge.leftAnchor constraintEqualToAnchor:icon.rightAnchor constant:-size / 2],
            [badge.topAnchor constraintEqualToAnchor:icon.topAnchor constant:-size / 2],
            [badge.widthAnchor constraintEqualToConstant:size],
            [badge.heightAnchor constraintEqualToConstant:size]
    ]];
}

- (UITabBarController *)getTabBarController:(id) viewController {
    return [viewController isKindOfClass:[UITabBarController class]] ? viewController : [viewController tabBarController];
}

@end