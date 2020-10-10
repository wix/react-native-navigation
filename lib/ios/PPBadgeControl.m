//
//  PPBadgeControl.m
//  PPBadgeViewObjc
//
//  Created by AndyPang on 2019/1/7.
//  Copyright © 2019 AndyPang. All rights reserved.
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

#import "PPBadgeControl.h"
#import "UIView+PPBadgeView.h"

@interface PPBadgeControl ()

@property (nonatomic, strong) UILabel *textLabel;
@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) UIColor *badgeViewColor;
@property (nonatomic, strong) NSLayoutConstraint *badgeViewHeightConstraint;

@end

@implementation PPBadgeControl

+ (instancetype)defaultBadge
{
    return [[self alloc] initWithFrame:CGRectZero];
}

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        [self setupSubviews];
    }
    return self;
}

- (void)setupSubviews
{
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 9.0;
    self.backgroundColor = UIColor.redColor;
    self.flexMode = PPBadgeViewFlexModeTail;
    
    [self setTranslatesAutoresizingMaskIntoConstraints:NO];
    [self setContentHuggingPriority:251 forAxis:UILayoutConstraintAxisHorizontal];
    [self setContentHuggingPriority:251 forAxis:UILayoutConstraintAxisVertical];
    [self addSubview:self.imageView];
    [self addSubview:self.textLabel];
    [self addLayoutWith:self.imageView leading:0 trailing:0];
    [self addLayoutWith:self.textLabel leading:5 trailing:-5];
}

- (void)addLayoutWith:(UIView *)view leading:(CGFloat)leading trailing:(CGFloat)trailing
{
    [view setTranslatesAutoresizingMaskIntoConstraints:NO];
    NSLayoutConstraint *topConstraint = [NSLayoutConstraint constraintWithItem:view attribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeTop multiplier:1.0 constant:0];
    NSLayoutConstraint *leadingConstraint = [NSLayoutConstraint constraintWithItem:view attribute:NSLayoutAttributeLeading relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeLeading multiplier:1.0 constant:leading];
    NSLayoutConstraint *bottomConstraint = [NSLayoutConstraint constraintWithItem:view attribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeBottom multiplier:1.0 constant:0];
    NSLayoutConstraint *trailingConstraint = [NSLayoutConstraint constraintWithItem:view attribute:NSLayoutAttributeTrailing relatedBy:NSLayoutRelationEqual toItem:self attribute:NSLayoutAttributeTrailing multiplier:1.0 constant:trailing];
    leadingConstraint.priority = 999;
    trailingConstraint.priority = 999;
    [self addConstraints:@[topConstraint, leadingConstraint, bottomConstraint, trailingConstraint]];
}

#pragma mark - Setter-Getter

- (void)setText:(NSString *)text
{
    _text = text;
    self.textLabel.text = text;
}

- (void)setAttributedText:(NSAttributedString *)attributedText
{
    _attributedText = attributedText;
    self.textLabel.attributedText = attributedText;
}

- (void)setFont:(UIFont *)font
{
    _font = font;
    self.textLabel.font = font;
}

- (void)setBackgroundImage:(UIImage *)backgroundImage
{
    _backgroundImage = backgroundImage;
    self.imageView.image = backgroundImage;
    if (backgroundImage) {
        if (self.heightConstraint) {
            self.badgeViewHeightConstraint = self.heightConstraint;
            [self removeConstraint:self.heightConstraint];
        }
        self.backgroundColor = UIColor.clearColor;
    } else {
        if (!self.heightConstraint && self.badgeViewHeightConstraint) {
            [self addConstraint:self.badgeViewHeightConstraint];
        }
        self.backgroundColor = self.badgeViewColor;
    }
}

- (void)setBackgroundColor:(UIColor *)backgroundColor
{
    [super setBackgroundColor:backgroundColor];
    if (backgroundColor && backgroundColor != UIColor.clearColor) {
        self.badgeViewColor = backgroundColor;
    }
}

#pragma mark - Lazy

- (UILabel *)textLabel
{
    if (!_textLabel) {
        _textLabel = [[UILabel alloc] init];
        _textLabel.textColor = [UIColor whiteColor];
        _textLabel.font = [UIFont systemFontOfSize:13];
        _textLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _textLabel;
}

- (UIImageView *)imageView
{
    if (!_imageView) {
        _imageView = [[UIImageView alloc] init];
    }
    return _imageView;
}

@end
