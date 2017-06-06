// SidebarWunderlistAnimation.m
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


#import "SidebarWunderlistAnimation.h"

@implementation SidebarWunderlistAnimation

+ (void)animateContentView:(UIView *)contentView sidebarView:(UIView *)sidebarView fromSide:(Side)side visibleWidth:(CGFloat)visibleWidth duration:(NSTimeInterval)animationDuration completion:(void (^)(BOOL))completion
{
    [self resetSidebarPosition:sidebarView];
    [self resetContentPosition:contentView];
    
    
    CGRect contentFrame = contentView.frame;
    CGRect sidebarFrame = sidebarView.frame;
    
    if(side == LeftSide)
    {
        contentFrame.origin.x += visibleWidth;
        sidebarFrame.origin.x -= 50;
    }
    else
    {
        contentFrame.origin.x -= visibleWidth;
        sidebarFrame.origin.x += 50;
    }
    
    sidebarView.frame = sidebarFrame;
    sidebarFrame.origin.x = 0;
    
    UIView *overlayContentView = [self overlayContentView:contentView.bounds];
    [contentView addSubview:overlayContentView];
    
    [UIView animateWithDuration:animationDuration
                          delay:0.0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{
                         contentView.frame = contentFrame;
                         sidebarView.frame = sidebarFrame;
                         overlayContentView.backgroundColor = [self overlayContentColor];
                     }
                     completion:^(BOOL finished) {
                         completion(finished);
                     }];
}


+ (void)reverseAnimateContentView:(UIView *)contentView sidebarView:(UIView *)sidebarView fromSide:(Side)side visibleWidth:(CGFloat)visibleWidth duration:(NSTimeInterval)animationDuration completion:(void (^)(BOOL))completion
{
    CGRect contentFrame = contentView.frame;
    contentFrame.origin.x = 0;
    
    CGRect sidebarFrame = sidebarView.frame;
    
    if(side == LeftSide)
    {
        sidebarFrame.origin.x -= 50;
    }
    else
    {
        sidebarFrame.origin.x += 50;
    }
    
    UIView *overlayContentView = [self overlayContentView:contentView.bounds];
    [contentView addSubview:overlayContentView];
    
    [UIView animateWithDuration:animationDuration
                          delay:0.0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{
                         contentView.frame = contentFrame;
                         sidebarView.frame = sidebarFrame;
                         overlayContentView.backgroundColor = [UIColor clearColor];
                     }
                     completion:^(BOOL finished) {
                         completion(finished);
                         [overlayContentView removeFromSuperview];
                     }];
}


@end
