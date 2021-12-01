#import "RNNDotIndicatorPresenter.h"
#import "DotIndicatorOptions.h"
#import "RNNNavigationOptions.h"
#import "UITabBarController+RNNUtils.h"
#import "UIViewController+LayoutProtocol.h"
#import <UIKit/UIKit.h>

@implementation RNNDotIndicatorPresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super init];
    _defaultOptions = defaultOptions;
    return self;
}

- (void)bottomTabsDidLayoutSubviews:(UITabBarController *)bottomTabs {
    for (UIViewController *child in bottomTabs.childViewControllers) {
        [self applyDotIndicator:child];
    }
}

- (void)applyDotIndicator:(UIViewController *)child {
    [self apply:child options:[child resolveOptions].bottomTab.dotIndicator];
}

- (void)mergeOptions:(RNNNavigationOptions *)mergeOptions
     resolvedOptions:(RNNNavigationOptions *)resolvedOptions
               child:(UIViewController *)child {
    RNNNavigationOptions *withDefault = (RNNNavigationOptions *)[[resolvedOptions
        withDefault:self.defaultOptions] mergeOptions:mergeOptions];

    if ([mergeOptions.bottomTab.dotIndicator hasValue]) {
        [self apply:child options:withDefault.bottomTab.dotIndicator];
    }
}

- (void)apply:(UIViewController *)child options:(DotIndicatorOptions *)options {
    if (![options hasValue])
        return;

    if ([options.visible isFalse]) {
        if ([child tabBarItem].tag > 0)
            [self remove:child];
        return;
    }
    if ([self currentIndicatorEquals:child options:options])
        return;

    if ([self hasIndicator:child])
        [self remove:child];

    UITabBarController *bottomTabs = [self getTabBarController:child];
    int index = (int)[[bottomTabs childViewControllers] indexOfObject:child];
    UIView *tabIcon = [bottomTabs getTabIcon:index];

    if (!tabIcon) {
        return;
    }

    UIView *indicator = [self createIndicator:options];
    [child tabBarItem].tag = indicator.tag;

    [tabIcon addSubview:indicator];
    [self applyConstraints:options badge:indicator tabBar:bottomTabs index:index];
}

- (UIView *)createIndicator:(DotIndicatorOptions *)options {
    UIView *indicator = [UIView new];
    indicator.translatesAutoresizingMaskIntoConstraints = NO;
    indicator.layer.cornerRadius = [[options.size withDefault:@6] floatValue] / 2;
    indicator.backgroundColor = [options.color withDefault:[UIColor redColor]];
    indicator.tag = arc4random();
    return indicator;
}

- (void)applyConstraints:(DotIndicatorOptions *)options
                   badge:(UIView *)badge
                  tabBar:(UITabBarController *)bottomTabs
                   index:(int)index {
    UIView *icon = [bottomTabs getTabIcon:index];
    float size = [[options.size withDefault:@6] floatValue];
    [NSLayoutConstraint activateConstraints:@[
        [badge.leftAnchor constraintEqualToAnchor:icon.rightAnchor constant:-size / 2],
        [badge.topAnchor constraintEqualToAnchor:icon.topAnchor constant:-size / 2],
        [badge.widthAnchor constraintEqualToConstant:size],
        [badge.heightAnchor constraintEqualToConstant:size]
    ]];
}

- (BOOL)currentIndicatorEquals:(UIViewController *)child options:(DotIndicatorOptions *)options {
    if (![self hasIndicator:child])
        return NO;
    UIView *currentIndicator = [self getCurrentIndicator:child];

    return
        [[currentIndicator backgroundColor] isEqual:[options.color withDefault:[UIColor redColor]]];
}

- (UIView *)getCurrentIndicator:(UIViewController *)child {
    UITabBarController *bottomTabs = [self getTabBarController:child];
    int tabIndex = (int)[[bottomTabs childViewControllers] indexOfObject:child];
    return [[bottomTabs getTabIcon:tabIndex] viewWithTag:[child tabBarItem].tag];
}

- (BOOL)hasIndicator:(UIViewController *)child {
    return [[[self getTabBarController:child] tabBar] viewWithTag:child.tabBarItem.tag];
}

- (void)remove:(UIViewController *)child {
    UIView *view = [[[child tabBarController] tabBar] viewWithTag:[child tabBarItem].tag];
    [view removeFromSuperview];
    [child tabBarItem].tag = -1;
}

- (UITabBarController *)getTabBarController:(id)viewController {
    return [viewController isKindOfClass:[UITabBarController class]]
               ? viewController
               : [viewController tabBarController];
}

@end
