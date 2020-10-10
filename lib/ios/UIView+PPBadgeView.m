//
//  UIView+PPBadgeView.m
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

#import "UIView+PPBadgeView.h"
#import "PPBadgeControl.h"
#import <objc/runtime.h>

static NSString *const kBadgeView = @"kBadgeView";

@interface UIView ()

@end

@implementation UIView (PPBadgeView)

- (void)pp_addBadgeWithText:(NSString *)text
{
    [self pp_showBadge];
    self.badgeView.text = text;
    [self pp_setBadgeFlexMode:self.badgeView.flexMode];
    if (text) {
        if (self.badgeView.widthConstraint && self.badgeView.widthConstraint.relation == NSLayoutRelationGreaterThanOrEqual) { return; }
        self.badgeView.widthConstraint.active = NO;
        NSLayoutConstraint *constraint = [NSLayoutConstraint constraintWithItem:self.badgeView attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationGreaterThanOrEqual toItem:self.badgeView attribute:NSLayoutAttributeHeight multiplier:1.0 constant:0];
        [self.badgeView addConstraint:constraint];
    } else {
        if (self.badgeView.widthConstraint && self.badgeView.widthConstraint.relation == NSLayoutRelationEqual) { return; }
        self.badgeView.widthConstraint.active = NO;
        NSLayoutConstraint *constraint = [NSLayoutConstraint constraintWithItem:self.badgeView attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:self.badgeView attribute:(NSLayoutAttributeHeight) multiplier:1.0 constant:0];
        [self.badgeView addConstraint:constraint];
    }
}

- (void)pp_addBadgeWithNumber:(NSInteger)number
{
    if (number <= 0) {
        [self pp_addBadgeWithText:@"0"];
        [self pp_hiddenBadge];
        return;
    }
    [self pp_addBadgeWithText:[NSString stringWithFormat:@"%ld",number]];
}

- (void)pp_addDotWithColor:(UIColor *)color
{
    [self pp_addBadgeWithText:nil];
    [self pp_setBadgeHeight:8.0];
    self.badgeView.backgroundColor = color;
}

- (void)pp_moveBadgeWithX:(CGFloat)x Y:(CGFloat)y
{
    self.badgeView.offset = CGPointMake(x, y);
    [self centerYConstraintWithItem:self.badgeView].constant = y;
    
    CGFloat badgeHeight = self.badgeView.heightConstraint.constant;
    switch (self.badgeView.flexMode) {
        case PPBadgeViewFlexModeHead: // 1. 左伸缩  <==●
        {
            [self centerXConstraintWithItem:self.badgeView].active = NO;
            [self leadingConstraintWithItem:self.badgeView].active = NO;
            if ([self trailingConstraintWithItem:self.badgeView]) {
                [self trailingConstraintWithItem:self.badgeView].constant = x + badgeHeight*0.5;
                return;
            }
            NSLayoutConstraint *trailingConstraint = [NSLayoutConstraint constraintWithItem:self.badgeView attribute:NSLayoutAttributeTrailing relatedBy:(NSLayoutRelationEqual) toItem:self attribute:NSLayoutAttributeTrailing multiplier:1.0 constant:(x + badgeHeight*0.5)];
            [self addConstraint:trailingConstraint];
            break;
        }
        case PPBadgeViewFlexModeTail: // 2. 右伸缩 ●==>
        {
            [self centerXConstraintWithItem:self.badgeView].active = NO;
            [self trailingConstraintWithItem:self.badgeView].active = NO;
            if ([self leadingConstraintWithItem:self.badgeView]) {
                [self leadingConstraintWithItem:self.badgeView].constant = x - badgeHeight*0.5;
                return;
            }
            NSLayoutConstraint *leadingConstraint = [NSLayoutConstraint constraintWithItem:self.badgeView attribute:NSLayoutAttributeLeading relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeTrailing multiplier:1.0 constant:(x - badgeHeight*0.5)];
            [self addConstraint:leadingConstraint];
            break;
        }
        case PPBadgeViewFlexModeMiddle: // 3. 左右伸缩  <=●=>
        {
            [self leadingConstraintWithItem:self.badgeView].active = NO;
            [self trailingConstraintWithItem:self.badgeView].active = NO;
            if ([self centerXConstraintWithItem:self.badgeView]) {
                [self centerXConstraintWithItem:self.badgeView].constant = x;
                return;
            }
            NSLayoutConstraint *centerXConstraint = [NSLayoutConstraint constraintWithItem:self.badgeView attribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeTrailing multiplier:1.0 constant:x];
            [self addConstraint:centerXConstraint];
            break;
        }
    }
}

- (void)pp_setBadgeFlexMode:(PPBadgeViewFlexMode)flexMode
{
    self.badgeView.flexMode = flexMode;
    [self pp_moveBadgeWithX:self.badgeView.offset.x Y:self.badgeView.offset.y];
}

- (void)pp_setBadgeHeight:(CGFloat)height
{
    self.badgeView.layer.cornerRadius = height * 0.5;
    self.badgeView.heightConstraint.constant = height;
    [self pp_moveBadgeWithX:self.badgeView.offset.x Y:self.badgeView.offset.y];
}

- (void)pp_showBadge
{
    self.badgeView.hidden = NO;
}

- (void)pp_hiddenBadge
{
    self.badgeView.hidden = YES;
}

- (void)pp_increase
{
    [self pp_increaseBy:1];
}

- (void)pp_increaseBy:(NSInteger)number
{
    NSInteger result = self.badgeView.text.integerValue + number;
    if (result > 0) {
        [self pp_showBadge];
    }
    self.badgeView.text = [NSString stringWithFormat:@"%ld",result];
}

- (void)pp_decrease
{
    [self pp_decreaseBy:1];
}

- (void)pp_decreaseBy:(NSInteger)number
{
    NSInteger result = self.badgeView.text.integerValue - number;
    if (result <= 0) {
        [self pp_hiddenBadge];
        self.badgeView.text = @"0";
        return;
    }
    self.badgeView.text = [NSString stringWithFormat:@"%ld",result];
}

- (void)addBadgeViewLayoutConstraint
{
    [self.badgeView setTranslatesAutoresizingMaskIntoConstraints:NO];
    NSLayoutConstraint *centerXConstraint = [NSLayoutConstraint constraintWithItem:self.badgeView attribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeTrailing multiplier:1.0 constant:0];
    NSLayoutConstraint *centerYConstraint = [NSLayoutConstraint constraintWithItem:self.badgeView attribute:NSLayoutAttributeCenterY relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeTop multiplier:1.0 constant:0];
    NSLayoutConstraint *widthConstraint = [NSLayoutConstraint constraintWithItem:self.badgeView attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationGreaterThanOrEqual toItem:self.badgeView attribute:NSLayoutAttributeHeight multiplier:1.0 constant:0];
    NSLayoutConstraint *heightConstraint = [NSLayoutConstraint constraintWithItem:self.badgeView attribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:nil attribute:NSLayoutAttributeNotAnAttribute multiplier:1.0 constant:18];
    [self addConstraints:@[centerXConstraint, centerYConstraint]];
    [self.badgeView addConstraints:@[widthConstraint, heightConstraint]];
}

#pragma mark - setter/getter

- (PPBadgeControl *)badgeView
{
    PPBadgeControl *badgeView = objc_getAssociatedObject(self, &kBadgeView);
    if (!badgeView) {
        badgeView = [PPBadgeControl defaultBadge];
        [self addSubview:badgeView];
        [self bringSubviewToFront:badgeView];
        [self setBadgeView:badgeView];
        [self addBadgeViewLayoutConstraint];
    }
    return badgeView;
}

- (void)setBadgeView:(PPBadgeControl *)badgeView
{
    objc_setAssociatedObject(self, &kBadgeView, badgeView, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

@end

#pragma mark -------------------------------

@implementation UIView (Constraint)

- (NSLayoutConstraint *)widthConstraint
{
    return [self constraint:self attribute:NSLayoutAttributeWidth];
}

- (NSLayoutConstraint *)heightConstraint
{
    return [self constraint:self attribute:NSLayoutAttributeHeight];
}

- (NSLayoutConstraint *)topConstraintWithItem: (id)item
{
    return [self constraint:item attribute:NSLayoutAttributeTop];
}

- (NSLayoutConstraint *)leadingConstraintWithItem: (id)item
{
    return [self constraint:item attribute:NSLayoutAttributeLeading];
}

- (NSLayoutConstraint *)bottomConstraintWithItem:(id)item
{
    return [self constraint:item attribute:NSLayoutAttributeBottom];
}

- (NSLayoutConstraint *)trailingConstraintWithItem:(id)item
{
    return [self constraint:item attribute:NSLayoutAttributeTrailing];
}

- (NSLayoutConstraint *)centerXConstraintWithItem:(id)item
{
    return [self constraint:item attribute:NSLayoutAttributeCenterX];
}

- (NSLayoutConstraint *)centerYConstraintWithItem:(id)item
{
    return [self constraint:item attribute:NSLayoutAttributeCenterY];
}

- (NSLayoutConstraint *)constraint:(id)item attribute: (NSLayoutAttribute)layoutAttribute
{
    for (NSLayoutConstraint *constraint in self.constraints) {
        if (constraint.firstItem == item && constraint.firstAttribute == layoutAttribute) {
            return constraint;
        }
    }
    return nil;
}

@end
