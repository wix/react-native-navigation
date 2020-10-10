//
//  UITabBarItem+PPBadgeView.m
//  PPBadgeViewObjc
//
//  Created by AndyPang on 2017/6/17.
//  Copyright © 2017年 AndyPang. All rights reserved.
//

/*
 *********************************************************************************
 *
 * Weibo : jkpang-庞 ( http://weibo.com/jkpang )
 * Email : jkpang@outlook.com
 * QQ 群 : 323408051
 * GitHub: https://github.com/jkpang
 *
 *********************************************************************************
 */

#import "UITabBarItem+PPBadgeView.h"
#import "UIView+PPBadgeView.h"

@implementation UITabBarItem (PPBadgeView)

- (PPBadgeControl *)badgeView
{
    return [self bottomView].badgeView;
}

- (void)pp_addBadgeWithText:(NSString *)text
{
    [[self bottomView] pp_addBadgeWithText:text];
    [[self bottomView] pp_moveBadgeWithX:4 Y:3]; // 默认为系统badge所在的位置
}

- (void)pp_addBadgeWithNumber:(NSInteger)number
{
    [[self bottomView] pp_addBadgeWithNumber:number];
    [[self bottomView] pp_moveBadgeWithX:4 Y:3];
}

- (void)pp_addDotWithColor:(UIColor *)color
{
    [[self bottomView] pp_addDotWithColor:color];
}

- (void)pp_setBadgeHeight:(CGFloat)height
{
    [[self bottomView] pp_setBadgeHeight:height];
}

- (void)pp_setBadgeFlexMode:(PPBadgeViewFlexMode)flexMode
{
    [[self bottomView] pp_setBadgeFlexMode:flexMode];
}

- (void)pp_moveBadgeWithX:(CGFloat)x Y:(CGFloat)y
{
    [[self bottomView] pp_moveBadgeWithX:x Y:y];
}

- (void)pp_showBadge
{
    [[self bottomView] pp_showBadge];
}

- (void)pp_hiddenBadge
{
    [[self bottomView] pp_hiddenBadge];
}

- (void)pp_increase
{
    [[self bottomView] pp_increase];
}

- (void)pp_increaseBy:(NSInteger)number
{
    [[self bottomView] pp_increaseBy:number];
}

- (void)pp_decrease
{
    [[self bottomView] pp_decrease];
}

- (void)pp_decreaseBy:(NSInteger)number
{
    [[self bottomView] pp_decreaseBy:number];
}

#pragma mark - 获取Badge的父视图

- (UIView *)bottomView
{
    // 通过Xcode视图调试工具找到UITabBarItem原生Badge所在父视图
    UIView *tabBarButton = [self valueForKey:@"_view"];
    for (UIView *subView in tabBarButton.subviews) {
        if (subView.superclass == NSClassFromString(@"UIImageView")) {
            return subView;
        }
    }
    return tabBarButton;
}

@end
