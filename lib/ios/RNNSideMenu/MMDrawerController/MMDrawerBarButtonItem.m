// Copyright (c) 2013 Mutual Mobile (http://mutualmobile.com/)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

#import "MMDrawerBarButtonItem.h"

@interface MMDrawerMenuButtonView : UIButton
@property(nonatomic, strong) UIColor *menuButtonNormalColor;
@property(nonatomic, strong) UIColor *menuButtonHighlightedColor;

@property(nonatomic, strong) UIColor *shadowNormalColor;
@property(nonatomic, strong) UIColor *shadowHighlightedColor;

- (UIColor *)menuButtonColorForState:(UIControlState)state;
- (void)setMenuButtonColor:(UIColor *)color forState:(UIControlState)state;

- (UIColor *)shadowColorForState:(UIControlState)state;
- (void)setShadowColor:(UIColor *)color forState:(UIControlState)state;

@end

@implementation MMDrawerMenuButtonView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self setMenuButtonNormalColor:[[UIColor whiteColor] colorWithAlphaComponent:0.9f]];
        [self setMenuButtonHighlightedColor:[UIColor colorWithRed:139.0 / 255.0
                                                            green:135.0 / 255.0
                                                             blue:136.0 / 255.0
                                                            alpha:0.9f]];

        [self setShadowNormalColor:[[UIColor blackColor] colorWithAlphaComponent:0.5f]];
        [self setShadowHighlightedColor:[[UIColor blackColor] colorWithAlphaComponent:0.2f]];
    }
    return self;
}

- (UIColor *)menuButtonColorForState:(UIControlState)state {
    UIColor *color;
    switch (state) {
    case UIControlStateNormal:
        color = self.menuButtonNormalColor;
        break;
    case UIControlStateHighlighted:
        color = self.menuButtonHighlightedColor;
        break;
    default:
        break;
    }
    return color;
}

- (void)setMenuButtonColor:(UIColor *)color forState:(UIControlState)state {
    switch (state) {
    case UIControlStateNormal:
        [self setMenuButtonNormalColor:color];
        break;
    case UIControlStateHighlighted:
        [self setMenuButtonHighlightedColor:color];
        break;
    default:
        break;
    }
    [self setNeedsDisplay];
}

- (UIColor *)shadowColorForState:(UIControlState)state {
    UIColor *color;
    switch (state) {
    case UIControlStateNormal:
        color = self.shadowNormalColor;
        break;
    case UIControlStateHighlighted:
        color = self.shadowHighlightedColor;
        break;
    default:
        break;
    }
    return color;
}

- (void)setShadowColor:(UIColor *)color forState:(UIControlState)state {
    switch (state) {
    case UIControlStateNormal:
        [self setShadowNormalColor:color];
        break;
    case UIControlStateHighlighted:
        [self setShadowHighlightedColor:color];
        break;
    default:
        break;
    }
    [self setNeedsDisplay];
}

- (void)drawRect:(CGRect)rect {
    //// General Declarations
    CGContextRef context = UIGraphicsGetCurrentContext();

    // Sizes
    CGFloat buttonWidth = CGRectGetWidth(self.bounds) * .80;
    CGFloat buttonHeight = CGRectGetHeight(self.bounds) * .16;
    CGFloat xOffset = CGRectGetWidth(self.bounds) * .10;
    CGFloat yOffset = CGRectGetHeight(self.bounds) * .12;
    CGFloat cornerRadius = 1.0;

    //// Color Declarations
    UIColor *buttonColor = [self menuButtonColorForState:self.state];
    UIColor *shadowColor = [self shadowColorForState:self.state];

    //// Shadow Declarations
    UIColor *shadow = shadowColor;
    CGSize shadowOffset = CGSizeMake(0.0, 1.0);
    CGFloat shadowBlurRadius = 0;

    //// Top Bun Drawing
    UIBezierPath *topBunPath = [UIBezierPath
        bezierPathWithRoundedRect:CGRectMake(xOffset, yOffset, buttonWidth, buttonHeight)
                     cornerRadius:cornerRadius];
    CGContextSaveGState(context);
    CGContextSetShadowWithColor(context, shadowOffset, shadowBlurRadius, shadow.CGColor);
    [buttonColor setFill];
    [topBunPath fill];
    CGContextRestoreGState(context);

    //// Meat Drawing
    UIBezierPath *meatPath =
        [UIBezierPath bezierPathWithRoundedRect:CGRectMake(xOffset, yOffset * 2 + buttonHeight,
                                                           buttonWidth, buttonHeight)
                                   cornerRadius:cornerRadius];
    CGContextSaveGState(context);
    CGContextSetShadowWithColor(context, shadowOffset, shadowBlurRadius, shadow.CGColor);
    [buttonColor setFill];
    [meatPath fill];
    CGContextRestoreGState(context);

    //// Bottom Bun Drawing
    UIBezierPath *bottomBunPath =
        [UIBezierPath bezierPathWithRoundedRect:CGRectMake(xOffset, yOffset * 3 + buttonHeight * 2,
                                                           buttonWidth, buttonHeight)
                                   cornerRadius:cornerRadius];
    CGContextSaveGState(context);
    CGContextSetShadowWithColor(context, shadowOffset, shadowBlurRadius, shadow.CGColor);
    [buttonColor setFill];
    [bottomBunPath fill];
    CGContextRestoreGState(context);
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesBegan:touches withEvent:event];
    [self setNeedsDisplay];
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesEnded:touches withEvent:event];
    [self setNeedsDisplay];
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesCancelled:touches withEvent:event];
    [self setNeedsDisplay];
}

- (void)setSelected:(BOOL)selected {
    [super setSelected:selected];
    [self setNeedsDisplay];
}

- (void)setHighlighted:(BOOL)highlighted {
    [super setHighlighted:highlighted];
    [self setNeedsDisplay];
}

- (void)setTintColor:(UIColor *)tintColor {
    if ([super respondsToSelector:@selector(setTintColor:)]) {
        [super setTintColor:tintColor];
    }
}

- (void)tintColorDidChange {
    [self setNeedsDisplay];
}

@end

@interface MMDrawerBarButtonItem ()
@property(nonatomic, strong) MMDrawerMenuButtonView *buttonView;

@end

@implementation MMDrawerBarButtonItem

+ (UIImage *)drawerButtonItemImage {

    static UIImage *drawerButtonImage = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
      UIGraphicsBeginImageContextWithOptions(CGSizeMake(26, 26), NO, 0);

      //// Color Declarations
      UIColor *fillColor = [UIColor whiteColor];

      //// Frames
      CGRect frame = CGRectMake(0, 0, 26, 26);

      //// Bottom Bar Drawing
      UIBezierPath *bottomBarPath = [UIBezierPath
          bezierPathWithRect:CGRectMake(CGRectGetMinX(frame) +
                                            floor((CGRectGetWidth(frame) - 16) * 0.50000 + 0.5),
                                        CGRectGetMinY(frame) +
                                            floor((CGRectGetHeight(frame) - 1) * 0.72000 + 0.5),
                                        16, 1)];
      [fillColor setFill];
      [bottomBarPath fill];

      //// Middle Bar Drawing
      UIBezierPath *middleBarPath = [UIBezierPath
          bezierPathWithRect:CGRectMake(CGRectGetMinX(frame) +
                                            floor((CGRectGetWidth(frame) - 16) * 0.50000 + 0.5),
                                        CGRectGetMinY(frame) +
                                            floor((CGRectGetHeight(frame) - 1) * 0.48000 + 0.5),
                                        16, 1)];
      [fillColor setFill];
      [middleBarPath fill];

      //// Top Bar Drawing
      UIBezierPath *topBarPath = [UIBezierPath
          bezierPathWithRect:CGRectMake(CGRectGetMinX(frame) +
                                            floor((CGRectGetWidth(frame) - 16) * 0.50000 + 0.5),
                                        CGRectGetMinY(frame) +
                                            floor((CGRectGetHeight(frame) - 1) * 0.24000 + 0.5),
                                        16, 1)];
      [fillColor setFill];
      [topBarPath fill];

      drawerButtonImage = UIGraphicsGetImageFromCurrentImageContext();
    });

    return drawerButtonImage;
}

- (instancetype)initWithTarget:(id)target action:(SEL)action {

    if ((floor(NSFoundationVersionNumber) > NSFoundationVersionNumber_iOS_6_1)) {
        return [self initWithImage:[self.class drawerButtonItemImage]
                             style:UIBarButtonItemStylePlain
                            target:target
                            action:action];
    } else {
        MMDrawerMenuButtonView *buttonView =
            [[MMDrawerMenuButtonView alloc] initWithFrame:CGRectMake(0, 0, 26, 26)];
        [buttonView addTarget:self
                       action:@selector(touchUpInside:)
             forControlEvents:UIControlEventTouchUpInside];
        self = [self initWithCustomView:buttonView];
        if (self) {
            [self setButtonView:buttonView];
        }
        self.action = action;
        self.target = target;
        return self;
    }
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder {
    // non-ideal way to get the target/action, but it works
    UIBarButtonItem *barButtonItem = [[UIBarButtonItem alloc] initWithCoder:aDecoder];
    return [self initWithTarget:barButtonItem.target action:barButtonItem.action];
}

- (void)touchUpInside:(id)sender {

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
    [self.target performSelector:self.action withObject:sender];
#pragma clang diagnostic pop;
}

- (UIColor *)menuButtonColorForState:(UIControlState)state {
    return [self.buttonView menuButtonColorForState:state];
}

- (void)setMenuButtonColor:(UIColor *)color forState:(UIControlState)state {
    [self.buttonView setMenuButtonColor:color forState:state];
}

- (UIColor *)shadowColorForState:(UIControlState)state {
    return [self.buttonView shadowColorForState:state];
}

- (void)setShadowColor:(UIColor *)color forState:(UIControlState)state {
    [self.buttonView setShadowColor:color forState:state];
}

- (void)setTintColor:(UIColor *)tintColor {
    if ([super respondsToSelector:@selector(setTintColor:)]) {
        [super setTintColor:tintColor];
    }
    if ([self.buttonView respondsToSelector:@selector(setTintColor:)]) {
        [self.buttonView setTintColor:tintColor];
    }
}

@end
