#import <UIKit/UIKit.h>
#import "RNNDotIndicatorPresenter.h"
#import "UIViewController+LayoutProtocol.h"
#import "DotIndicatorOptions.h"
#import "UITabBarController+RNNUtils.h"

@implementation RNNDotIndicatorPresenter

- (void)apply:(UIViewController *)child {
    DotIndicatorOptions *options = [child resolveChildOptions:child].bottomTab.dotIndicator;
    if (![options hasValue]) return;

    if ([self shouldRemoveIndicator:child options:options]) {
        [self remove:child];
        return;
    }
    if ([self hasVisibleIndicator:child]) return;

    UIView *badge = [self createIndicator:options];
    [child tabBarItem].tag = badge.tag;

    UITabBarController *bottomTabs = [self getTabBarController:child];
    int index = (int) [[bottomTabs childViewControllers] indexOfObject:child];
    [[bottomTabs getTabView:index] addSubview:badge];
    [self applyConstraints:options badge:badge tabBar:bottomTabs index:index];
}

- (void)applyConstraints:(DotIndicatorOptions *)options badge:(UIView *)badge tabBar:(UITabBarController *)bottomTabs index:(int)index {
    UIView *icon = [bottomTabs getTabIcon:index];
    float size = [[options.size getWithDefaultValue:@6] floatValue];
    [NSLayoutConstraint activateConstraints:@[
            [badge.leftAnchor constraintEqualToAnchor:icon.rightAnchor constant:-size / 2],
            [badge.topAnchor constraintEqualToAnchor:icon.topAnchor constant:-size / 2],
            [badge.widthAnchor constraintEqualToConstant:size],
            [badge.heightAnchor constraintEqualToConstant:size]
    ]];
}

- (UIView *)createIndicator:(DotIndicatorOptions *)options {
    UIView *badge = [UIView new];
    badge.translatesAutoresizingMaskIntoConstraints = NO;
    badge.layer.cornerRadius = [[options.size getWithDefaultValue:@6] floatValue] / 2;
    badge.backgroundColor = [options.color getWithDefaultValue:[UIColor redColor]];
    badge.tag = arc4random();
    return badge;
}

- (signed char)hasVisibleIndicator:(UIViewController *)child {
    return [child tabBarItem].tag > 0;
}

- (void)remove:(UIViewController *)child {
    UIView *view = [[[child tabBarController] tabBar] viewWithTag:[child tabBarItem].tag];
    [view removeFromSuperview];
    [child tabBarItem].tag = -1;
}

- (signed char)shouldRemoveIndicator:(UIViewController *)child options:(DotIndicatorOptions *)options {
    return [options.visible isFalse] && [child tabBarItem].tag > 0;
}

- (UITabBarController *)getTabBarController:(id)viewController {
    return [viewController isKindOfClass:[UITabBarController class]] ? viewController : [viewController tabBarController];
}

@end