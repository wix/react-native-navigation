// SidebarLuvocracyAnimation.m
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


#import "SidebarLuvocracyAnimation.h"

@implementation SidebarLuvocracyAnimation

+ (void)animateContentView:(UIView *)contentView sidebarView:(UIView *)sidebarView fromSide:(Side)side visibleWidth:(CGFloat)visibleWidth duration:(NSTimeInterval)animationDuration completion:(void (^)(BOOL))completion
{
    [self resetSidebarPosition:sidebarView];
    [self resetContentPosition:contentView];
    
    // Animation settings for content view
    CATransform3D contentTransform = CATransform3DIdentity;
    contentTransform.m34 = -1.0f / 800.0f;
    contentView.layer.zPosition = 100;
    
    // Animation settings for sidebar view
    CATransform3D sidebarTransform = CATransform3DIdentity;
    sidebarTransform = CATransform3DScale(sidebarTransform, 1.7, 1.7, 1.7);
    sidebarView.layer.transform = sidebarTransform;
    
    sidebarTransform = CATransform3DIdentity;
    sidebarTransform = CATransform3DScale(sidebarTransform, 1.0, 1.0, 1.0);
    
    if(side == LeftSide)
    {
        contentTransform = CATransform3DTranslate(contentTransform, visibleWidth - (contentView.frame.size.width / 2 * 0.4), 0.0, 0.0);
        contentTransform = CATransform3DScale(contentTransform, 0.6, 0.6, 0.6);
    }
    else
    {
        contentTransform = CATransform3DTranslate(contentTransform, 0 - visibleWidth + (contentView.frame.size.width / 2 * 0.4), 0.0, 0.0);
        contentTransform = CATransform3DScale(contentTransform, 0.6, 0.6, 0.6);
    }
    
    UIView *overlayContentView = [self overlayContentView:contentView.bounds];
    [contentView addSubview:overlayContentView];
    
    // Animate
    [UIView animateWithDuration:animationDuration
                          delay:0.0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{
                         contentView.layer.transform = contentTransform;
                         sidebarView.layer.transform = sidebarTransform;
                         overlayContentView.backgroundColor = [self overlayContentColor];
                     }
                     completion:^(BOOL finished) {
                         completion(finished);
                     }];
}


+ (void)reverseAnimateContentView:(UIView *)contentView sidebarView:(UIView *)sidebarView fromSide:(Side)side visibleWidth:(CGFloat)visibleWidth duration:(NSTimeInterval)animationDuration completion:(void (^)(BOOL))completion
{
    // Animation settings for content view
    CATransform3D contentTransform = CATransform3DIdentity;
    contentTransform.m34 = -1.0f / 800.0f;
    contentView.layer.zPosition = 100;
    contentTransform = CATransform3DTranslate(contentTransform, 0.0, 0.0, 0.0);
    contentTransform = CATransform3DScale(contentTransform, 1.0, 1.0, 1.0);
    
    // Animation settings for menu view
    __block CATransform3D sidebarTransform = CATransform3DIdentity;
    sidebarTransform = CATransform3DScale(sidebarTransform, 1.7, 1.7, 1.7);
    
    UIView *overlayContentView = [self overlayContentView:contentView.bounds];
    [contentView addSubview:overlayContentView];
    
    // Animate
    [UIView animateWithDuration:animationDuration
                          delay:0.0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{
                         contentView.layer.transform = contentTransform;
                         sidebarView.layer.transform = sidebarTransform;
                         overlayContentView.backgroundColor = [UIColor clearColor];
                     }
                     completion:^(BOOL finished) {
                         completion(finished);
                         sidebarTransform = CATransform3DIdentity;
                         sidebarTransform = CATransform3DScale(sidebarTransform, 1.0, 1.0, 1.0);
                         sidebarView.layer.transform = sidebarTransform;
                         [overlayContentView removeFromSuperview];
                     }];
}


@end
