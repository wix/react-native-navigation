//
//  UIView+PPBadgeView.h
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

#import <UIKit/UIKit.h>

@class PPBadgeControl;

typedef NS_ENUM(NSUInteger, PPBadgeViewFlexMode);

#pragma mark - Protocol

@protocol PPBadgeView <NSObject>

@required

@property (nonatomic, strong, readonly) PPBadgeControl *badgeView;

/**
 添加带文本内容的Badge, 默认右上角, 红色, 18pts
 
 Add Badge with text content, the default upper right corner, red backgroundColor, 18pts
 */
- (void)pp_addBadgeWithText:(NSString *)text;

/**
 添加带数字的Badge, 默认右上角,红色,18pts
 
 Add the Badge with numbers, the default upper right corner, red backgroundColor, 18pts
 */
- (void)pp_addBadgeWithNumber:(NSInteger)number;

/**
 添加带颜色的小圆点, 默认右上角, 红色, 8pts
 
 Add small dots with color, the default upper right corner, red backgroundColor, 8pts
 */
- (void)pp_addDotWithColor:(UIColor *)color;

/**
 设置Badge的高度,因为Badge宽度是动态可变的,通过改变Badge高度,其宽度也按比例变化,方便布局
 
 (注意: 此方法需要将Badge添加到控件上后再调用!!!)
 
 Set the height of Badge, because the Badge width is dynamically and  variable.
 By changing the Badge height in proportion to facilitate the layout.
 
 (Note: this method needs to add Badge to the controls and then use it !!!)
 
 @param height 高度大小
 */
- (void)pp_setBadgeHeight:(CGFloat)height;

/**
 设置Badge的偏移量, Badge中心点默认为其父视图的右上角
 
 Set Badge offset, Badge center point defaults to the top right corner of its parent view
 
 @param x X轴偏移量 (x<0: 左移, x>0: 右移) axis offset (x <0: left, x> 0: right)
 @param y Y轴偏移量 (y<0: 上移, y>0: 下移) axis offset ( Y <0: up, y> 0: down)
 */
- (void)pp_moveBadgeWithX:(CGFloat)x Y:(CGFloat)y;


/**
 设置Badge伸缩的方向
 
 Setting the direction of Badge expansion
 
 PPBadgeViewFlexModeHead,    左伸缩 Head Flex    : <==●
 PPBadgeViewFlexModeTail,    右伸缩 Tail Flex    : ●==>
 PPBadgeViewFlexModeMiddle   左右伸缩 Middle Flex : <=●=>
 
 @param flexMode : Default is PPBadgeViewFlexModeTail
 */
- (void)pp_setBadgeFlexMode:(PPBadgeViewFlexMode)flexMode;

/// 显示Badge
- (void)pp_showBadge;

/// 隐藏Badge
- (void)pp_hiddenBadge;

/// 数字增加/减少, 注意:以下方法只适用于Badge内容为纯数字的情况
/// Digital increase /decrease, note: the following method applies only to cases where the Badge content is purely numeric
- (void)pp_increase;
- (void)pp_increaseBy:(NSInteger)number;
- (void)pp_decrease;
- (void)pp_decreaseBy:(NSInteger)number;

@end

#pragma mark - Category

@interface UIView (PPBadgeView) <PPBadgeView>

@end

@interface UIView (Constraint)
- (NSLayoutConstraint *)widthConstraint;
- (NSLayoutConstraint *)heightConstraint;
- (NSLayoutConstraint *)topConstraintWithItem:(id)item;
- (NSLayoutConstraint *)leadingConstraintWithItem:(id)item;
- (NSLayoutConstraint *)bottomConstraintWithItem:(id)item;
- (NSLayoutConstraint *)trailingConstraintWithItem:(id)item;
- (NSLayoutConstraint *)centerXConstraintWithItem:(id)item;
- (NSLayoutConstraint *)centerYConstraintWithItem:(id)item;
@end
