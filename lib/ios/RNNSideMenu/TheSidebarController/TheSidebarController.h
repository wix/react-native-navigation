// TheSidebarController.h
// TheSidebarController
//
// Copyright (c) 2013 Jon Danao (danao.org | jondanao)
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


#import <UIKit/UIKit.h>
#import "SidebarAnimation.h"

@protocol TheSidebarControllerDelegate;

@interface TheSidebarController : UIViewController

@property (strong, nonatomic) UIViewController *contentViewController;
@property (strong, nonatomic) UIViewController *leftSidebarViewController;
@property (strong, nonatomic) UIViewController *rightSidebarViewController;

@property (assign, nonatomic) NSTimeInterval animationDuration;
@property (assign, nonatomic) CGFloat visibleWidth;
@property (assign, nonatomic) BOOL sidebarIsPresenting;
@property (assign, nonatomic) id<TheSidebarControllerDelegate> delegate;
@property (assign, nonatomic) BOOL storyboardsUseAutolayout;
@property (nonatomic, strong) UIColor *overlayContentColor;

- (id)initWithContentViewController:(UIViewController *)contentViewController
          leftSidebarViewController:(UIViewController *)leftSidebarViewController;

- (id)initWithContentViewController:(UIViewController *)contentViewController
         rightSidebarViewController:(UIViewController *)rightSidebarViewController;

- (id)initWithContentViewController:(UIViewController *)contentViewController
          leftSidebarViewController:(UIViewController *)leftSidebarViewController
         rightSidebarViewController:(UIViewController *)rightSidebarViewController;

- (id)initWithContentViewController:(UIViewController *)contentViewController
          leftSidebarViewController:(UIViewController *)leftSidebarViewController
           storyboardsUseAutoLayout:(BOOL)storyboardsUseAutoLayout;

- (id)initWithContentViewController:(UIViewController *)contentViewController
         rightSidebarViewController:(UIViewController *)rightSidebarViewController
           storyboardsUseAutoLayout:(BOOL)storyboardsUseAutoLayout;

- (id)initWithContentViewController:(UIViewController *)contentViewController
          leftSidebarViewController:(UIViewController *)leftSidebarViewController
         rightSidebarViewController:(UIViewController *)rightSidebarViewController
           storyboardsUseAutoLayout:(BOOL)storyboardsUseAutoLayout;

- (void)dismissSidebarViewController;
- (void)presentLeftSidebarViewController;
- (void)presentLeftSidebarViewControllerWithStyle:(SidebarTransitionStyle)transitionStyle;
- (void)presentRightSidebarViewController;
- (void)presentRightSidebarViewControllerWithStyle:(SidebarTransitionStyle)transitionStyle;

@end



@protocol TheSidebarControllerDelegate <NSObject>

@optional
- (void)sidebarController:(TheSidebarController *)sidebarController willShowViewController:(UIViewController *)viewController;
- (void)sidebarController:(TheSidebarController *)sidebarController didShowViewController:(UIViewController *)viewController;
- (void)sidebarController:(TheSidebarController *)sidebarController willHideViewController:(UIViewController *)viewController;
- (void)sidebarController:(TheSidebarController *)sidebarController didHideViewController:(UIViewController *)viewController;
@end



@interface UIViewController(TheSidebarController)

@property (strong, readonly, nonatomic) TheSidebarController *sidebarController;

@end
