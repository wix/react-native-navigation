// SidebarAnimation.m
// TheSidebarController
//
// Copyright (c) 2014 Jon Danao (danao.org | jondanao)
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


#import "SidebarAnimation.h"



@implementation SidebarAnimation

static UIColor *overlayContentColor;

+(void)setOverlayContentColor:(UIColor*)color {
    overlayContentColor = color;
}

+(UIColor*)overlayContentColor {
    return overlayContentColor;
}

+ (void)animateContentView:(UIView *)contentView sidebarView:(UIView *)sidebarView fromSide:(Side)side visibleWidth:(CGFloat)visibleWidth duration:(NSTimeInterval)animationDuration completion:(void (^)(BOOL))completion
{
    
}

+ (void)reverseAnimateContentView:(UIView *)contentView sidebarView:(UIView *)sidebarView fromSide:(Side)side visibleWidth:(CGFloat)visibleWidth duration:(NSTimeInterval)animationDuration completion:(void (^)(BOOL))completion
{
    
}

+ (void)resetSidebarPosition:(UIView *)sidebarView
{
    CATransform3D resetTransform = CATransform3DIdentity;
    resetTransform = CATransform3DRotate(resetTransform, DEG2RAD(0), 1, 1, 1);
    resetTransform = CATransform3DScale(resetTransform, 1.0, 1.0, 1.0);
    resetTransform = CATransform3DTranslate(resetTransform, 0.0, 0.0, 0.0);
    sidebarView.layer.transform = resetTransform;
    
    CGRect resetFrame = sidebarView.frame;
    resetFrame.origin.x = 0;
    resetFrame.origin.y = 0;
    sidebarView.frame = resetFrame;
    
    [sidebarView.superview sendSubviewToBack:sidebarView];
    sidebarView.layer.zPosition = 0;
}

+ (void)resetContentPosition:(UIView *)contentView
{
    CATransform3D resetTransform = CATransform3DIdentity;
    resetTransform = CATransform3DRotate(resetTransform, DEG2RAD(0), 1, 1, 1);
    resetTransform = CATransform3DScale(resetTransform, 1.0, 1.0, 1.0);
    resetTransform = CATransform3DTranslate(resetTransform, 0.0, 0.0, 0.0);
    contentView.layer.transform = resetTransform;
    
    CGRect resetFrame = contentView.frame;
    resetFrame.origin.x = 0;
    resetFrame.origin.y = 0;
    contentView.frame = resetFrame;
    
    [contentView.superview bringSubviewToFront:contentView];
    contentView.layer.zPosition = 0;
}


+(UIView*)overlayContentView:(CGRect)frame {
    
    static UIView *overlayView;
    static dispatch_once_t onceToken;
    
    dispatch_once(&onceToken, ^{
        overlayView = [[UIView alloc] initWithFrame:frame];
        overlayView.backgroundColor = [UIColor clearColor];
        overlayView.userInteractionEnabled = NO;
    });
    
    return overlayView;
}


@end
