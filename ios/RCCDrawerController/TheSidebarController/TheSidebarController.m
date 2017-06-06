// TheSidebarController.m
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


#import "TheSidebarController.h"


static const CGFloat kAnimationDuration = 0.3f;
static const CGFloat kVisibleWidth = 310.0f;


@interface TheSidebarController()

@property (assign, nonatomic) SidebarTransitionStyle selectedTransitionStyle;
@property (assign, nonatomic) Side selectedSide;
@property (strong, nonatomic) UIViewController *selectedSidebarViewController;
@property (strong, nonatomic) NSArray *sidebarAnimations;
@property (strong, nonatomic) UIViewController *contentContainerViewController;
@property (strong, nonatomic) UIViewController *leftSidebarContainerViewController;
@property (strong, nonatomic) UIViewController *rightSidebarContainerViewController;
@property (assign, nonatomic) CATransform3D contentTransform;

- (void)showSidebarViewControllerFromSide:(Side)side withTransitionStyle:(SidebarTransitionStyle)transitionStyle;
- (void)hideSidebarViewController;
- (void)setAnchorPoint:(CGPoint)anchorPoint forView:(UIView *)view;

@end


@implementation TheSidebarController

#pragma mark - Designated Initializer
- (id)init
{
    return [self initWithContentViewController:nil leftSidebarViewController:nil rightSidebarViewController:nil];
}

- (id)initWithContentViewController:(UIViewController *)contentViewController leftSidebarViewController:(UIViewController *)leftSidebarViewController
{
    return [self initWithContentViewController:contentViewController leftSidebarViewController:leftSidebarViewController rightSidebarViewController:nil];
}

- (id)initWithContentViewController:(UIViewController *)contentViewController rightSidebarViewController:(UIViewController *)rightSidebarViewController
{
    return [self initWithContentViewController:contentViewController leftSidebarViewController:nil rightSidebarViewController:rightSidebarViewController];
}

- (id)initWithContentViewController:(UIViewController *)contentViewController leftSidebarViewController:(UIViewController *)leftSidebarViewController rightSidebarViewController:(UIViewController *)rightSidebarViewController
{
    self = [super init];
    
    if(self)
    {
        _contentContainerViewController = [[UIViewController alloc] init];
        _leftSidebarContainerViewController = [[UIViewController alloc] init];
        _rightSidebarContainerViewController = [[UIViewController alloc] init];
        
        _contentContainerViewController.view.backgroundColor = [UIColor clearColor];
        _leftSidebarContainerViewController.view.backgroundColor = [UIColor clearColor];
        _rightSidebarContainerViewController.view.backgroundColor = [UIColor clearColor];
        
        _contentViewController = contentViewController;
        _leftSidebarViewController = leftSidebarViewController;
        _rightSidebarViewController = rightSidebarViewController;
        
        _animationDuration = kAnimationDuration;
        _visibleWidth = kVisibleWidth;
        _sidebarAnimations = @[SIDEBAR_ANIMATIONS];
        _sidebarIsPresenting = NO;
    }
    
    return self;
}

- (id)initWithContentViewController:(UIViewController *)contentViewController
          leftSidebarViewController:(UIViewController *)leftSidebarViewController
           storyboardsUseAutoLayout:(BOOL)storyboardsUseAutoLayout
{
    self.storyboardsUseAutolayout = storyboardsUseAutoLayout;
    return [self initWithContentViewController:contentViewController leftSidebarViewController:leftSidebarViewController];
}

- (id)initWithContentViewController:(UIViewController *)contentViewController
         rightSidebarViewController:(UIViewController *)rightSidebarViewController
           storyboardsUseAutoLayout:(BOOL)storyboardsUseAutoLayout
{
    self.storyboardsUseAutolayout = storyboardsUseAutoLayout;
    return [self initWithContentViewController:contentViewController rightSidebarViewController:rightSidebarViewController];
}

- (id)initWithContentViewController:(UIViewController *)contentViewController
          leftSidebarViewController:(UIViewController *)leftSidebarViewController
         rightSidebarViewController:(UIViewController *)rightSidebarViewController
           storyboardsUseAutoLayout:(BOOL)storyboardsUseAutoLayout
{
    self.storyboardsUseAutolayout = storyboardsUseAutoLayout;
    return [self initWithContentViewController:contentViewController leftSidebarViewController:leftSidebarViewController rightSidebarViewController:rightSidebarViewController];
}

#pragma mark - UIViewController Lifecycle
- (void)viewDidLoad
{
    NSAssert(self.contentViewController != nil, @"contentViewController was not set");
    
    [super viewDidLoad];
    self.view.translatesAutoresizingMaskIntoConstraints = self.storyboardsUseAutolayout;
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    
    if(self.leftSidebarViewController)
    {
        // Parent View Controller
        [self addChildViewController:self.leftSidebarContainerViewController];
        [self.view addSubview:self.leftSidebarContainerViewController.view];
        [self.leftSidebarContainerViewController didMoveToParentViewController:self];
        self.leftSidebarContainerViewController.view.translatesAutoresizingMaskIntoConstraints = self.storyboardsUseAutolayout;
        self.leftSidebarContainerViewController.view.hidden = YES;
        
        // Child View Controller
        [self.leftSidebarContainerViewController addChildViewController:self.leftSidebarViewController];
        [self.leftSidebarContainerViewController.view addSubview:self.leftSidebarViewController.view];
        [self.leftSidebarViewController didMoveToParentViewController:self.leftSidebarContainerViewController];
    }
    
    if(self.rightSidebarViewController)
    {
        // Parent View Controller
        [self addChildViewController:self.rightSidebarContainerViewController];
        [self.view addSubview:self.rightSidebarContainerViewController.view];
        [self.rightSidebarContainerViewController didMoveToParentViewController:self];
        self.rightSidebarContainerViewController.view.translatesAutoresizingMaskIntoConstraints = self.storyboardsUseAutolayout;
        self.rightSidebarContainerViewController.view.hidden = YES;
        
        // Child View Controller
        [self.rightSidebarContainerViewController addChildViewController:self.rightSidebarViewController];
        [self.rightSidebarContainerViewController.view addSubview:self.rightSidebarViewController.view];
        [self.rightSidebarViewController didMoveToParentViewController:self.rightSidebarContainerViewController];
    }
    
    
    // Parent View Controller
    [self addChildViewController:self.contentContainerViewController];
    [self.view addSubview:self.contentContainerViewController.view];
    [self.contentContainerViewController didMoveToParentViewController:self];
    
    // Child View Controller
    [self.contentContainerViewController addChildViewController:self.contentViewController];
    [self.contentContainerViewController.view addSubview:self.contentViewController.view];
    [self.contentViewController didMoveToParentViewController:self.contentContainerViewController];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

- (void)viewWillLayoutSubviews
{
    [super viewWillLayoutSubviews];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


#pragma mark - TheSidebarController Presentation Methods
- (void)dismissSidebarViewController
{
    [self hideSidebarViewController];
}

- (void)presentLeftSidebarViewController
{
    [self presentLeftSidebarViewControllerWithStyle:SidebarTransitionStyleFacebook];
}

- (void)presentLeftSidebarViewControllerWithStyle:(SidebarTransitionStyle)transitionStyle
{
    NSAssert(self.leftSidebarViewController != nil, @"leftSidebarViewController was not set");
    [self showSidebarViewControllerFromSide:LeftSide withTransitionStyle:transitionStyle];
}

- (void)presentRightSidebarViewController
{
    [self presentRightSidebarViewControllerWithStyle:SidebarTransitionStyleFacebook];
}

- (void)presentRightSidebarViewControllerWithStyle:(SidebarTransitionStyle)transitionStyle
{
    NSAssert(self.rightSidebarViewController != nil, @"rightSidebarViewController was not set");
    [self showSidebarViewControllerFromSide:RightSide withTransitionStyle:transitionStyle];
}


#pragma mark - TheSidebarController Private Methods
- (void)showSidebarViewControllerFromSide:(Side)side withTransitionStyle:(SidebarTransitionStyle)transitionStyle
{
    [[UIApplication sharedApplication] beginIgnoringInteractionEvents];
    self.view.autoresizingMask = UIViewAutoresizingNone;
    
    if(side == LeftSide)
    {
        self.leftSidebarContainerViewController.view.hidden = NO;
        self.rightSidebarContainerViewController.view.hidden = YES;
        self.selectedSidebarViewController = self.leftSidebarViewController;
        self.view.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
    }
    else if(side == RightSide)
    {
        self.rightSidebarContainerViewController.view.hidden = NO;
        self.leftSidebarContainerViewController.view.hidden = YES;
        self.selectedSidebarViewController = self.rightSidebarViewController;
        self.view.autoresizingMask = UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleHeight;
    }
    
    self.selectedSide = side;
    self.selectedTransitionStyle = transitionStyle;
    
    if([self.delegate conformsToProtocol:@protocol(TheSidebarControllerDelegate)] && [self.delegate respondsToSelector:@selector(sidebarController:willShowViewController:)])
    {
        [self.delegate sidebarController:self willShowViewController:self.selectedSidebarViewController];
    }
    
    NSString *animationClassName = self.sidebarAnimations[transitionStyle];
    Class animationClass = NSClassFromString(animationClassName);
    [animationClass setOverlayContentColor:self.overlayContentColor];
    [animationClass animateContentView:self.contentContainerViewController.view
                           sidebarView:self.selectedSidebarViewController.parentViewController.view
                              fromSide:self.selectedSide
                          visibleWidth:self.visibleWidth
                              duration:self.animationDuration
                            completion:^(BOOL finished) {
                                [[UIApplication sharedApplication] endIgnoringInteractionEvents];
                                self.sidebarIsPresenting = YES;
                                
                                if([self.delegate conformsToProtocol:@protocol(TheSidebarControllerDelegate)] && [self.delegate respondsToSelector:@selector(sidebarController:didShowViewController:)])
                                {
                                    [self.delegate sidebarController:self didShowViewController:self.selectedSidebarViewController];
                                }
                            }
     ];
}

- (void)hideSidebarViewController
{
    [[UIApplication sharedApplication] beginIgnoringInteractionEvents];
    
    if([self.delegate conformsToProtocol:@protocol(TheSidebarControllerDelegate)] && [self.delegate respondsToSelector:@selector(sidebarController:willHideViewController:)])
    {
        [self.delegate sidebarController:self willHideViewController:self.selectedSidebarViewController];
    }
    
    NSString *animationClassName = self.sidebarAnimations[self.selectedTransitionStyle];
    Class animationClass = NSClassFromString(animationClassName);
    [animationClass reverseAnimateContentView:self.contentContainerViewController.view
                                  sidebarView:self.selectedSidebarViewController.parentViewController.view
                                     fromSide:self.selectedSide
                                 visibleWidth:self.visibleWidth
                                     duration:self.animationDuration
                                   completion:^(BOOL finished) {
                                       [[UIApplication sharedApplication] endIgnoringInteractionEvents];
                                       self.sidebarIsPresenting = NO;
                                       
                                       if([self.delegate conformsToProtocol:@protocol(TheSidebarControllerDelegate)] && [self.delegate respondsToSelector:@selector(sidebarController:didHideViewController:)])
                                       {
                                           [self.delegate sidebarController:self didHideViewController:self.selectedSidebarViewController];
                                       }
                                   }
     ];
}


#pragma mark - UIViewController Setters
- (void)setContentViewController:(UIViewController *)contentViewController
{
    // Old View Controller
    UIViewController *oldViewController = self.contentViewController;
    [oldViewController willMoveToParentViewController:nil];
    [oldViewController.view removeFromSuperview];
    [oldViewController removeFromParentViewController];
    
    // New View Controller
    UIViewController *newViewController = contentViewController;
    [self.contentContainerViewController addChildViewController:newViewController];
    [self.contentContainerViewController.view addSubview:newViewController.view];
    [newViewController didMoveToParentViewController:self.contentContainerViewController];
    
    _contentViewController = newViewController;
}

- (void)setLeftSidebarViewController:(UIViewController *)leftSidebarViewController
{
    // Old View Controller
    UIViewController *oldViewController = self.leftSidebarViewController;
    [oldViewController willMoveToParentViewController:nil];
    [oldViewController.view removeFromSuperview];
    [oldViewController removeFromParentViewController];
    
    // New View Controller
    UIViewController *newViewController = leftSidebarViewController;
    [self.leftSidebarContainerViewController addChildViewController:newViewController];
    [self.leftSidebarContainerViewController.view addSubview:newViewController.view];
    [newViewController didMoveToParentViewController:self.leftSidebarContainerViewController];
    
    _leftSidebarViewController = newViewController;
}

- (void)setRightSidebarViewController:(UIViewController *)rightSidebarViewController
{
    // Old View Controller
    UIViewController *oldViewController = self.leftSidebarViewController;
    [oldViewController willMoveToParentViewController:nil];
    [oldViewController.view removeFromSuperview];
    [oldViewController removeFromParentViewController];
    
    // New View Controller
    UIViewController *newViewController = rightSidebarViewController;
    [self.rightSidebarContainerViewController addChildViewController:newViewController];
    [self.rightSidebarContainerViewController.view addSubview:newViewController.view];
    [newViewController didMoveToParentViewController:self.rightSidebarContainerViewController];
    
    _rightSidebarViewController = newViewController;
}


#pragma mark - Autorotation Delegates
- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration
{
    [super willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
    
    if((toInterfaceOrientation == UIInterfaceOrientationPortrait) ||
       (toInterfaceOrientation == UIInterfaceOrientationPortraitUpsideDown))
    {
        NSLog(@"Portrait");
    }
    else if((toInterfaceOrientation == UIInterfaceOrientationLandscapeLeft) ||
            (toInterfaceOrientation == UIInterfaceOrientationLandscapeRight))
    {
        NSLog(@"Landscape");
    }
    
    
}


#pragma mark - Helpers
- (void)setAnchorPoint:(CGPoint)anchorPoint forView:(UIView *)view
{
    CGPoint newPoint = CGPointMake(view.bounds.size.width * anchorPoint.x, view.bounds.size.height * anchorPoint.y);
    CGPoint oldPoint = CGPointMake(view.bounds.size.width * view.layer.anchorPoint.x, view.bounds.size.height * view.layer.anchorPoint.y);
    
    newPoint = CGPointApplyAffineTransform(newPoint, view.transform);
    oldPoint = CGPointApplyAffineTransform(oldPoint, view.transform);
    
    CGPoint position = view.layer.position;
    
    position.x -= oldPoint.x;
    position.x += newPoint.x;
    
    position.y -= oldPoint.y;
    position.y += newPoint.y;
    
    view.layer.position = position;
    view.layer.anchorPoint = anchorPoint;
}

@end


#pragma mark - TheSidebarController Category
@implementation UIViewController(TheSidebarController)

- (TheSidebarController *)sidebarController
{
    if([self.parentViewController.parentViewController isKindOfClass:[TheSidebarController class]])
    {
        return (TheSidebarController *)self.parentViewController.parentViewController;
    }
    else if([self.parentViewController isKindOfClass:[UINavigationController class]] &&
            [self.parentViewController.parentViewController.parentViewController isKindOfClass:[TheSidebarController class]])
    {
        return (TheSidebarController *)self.parentViewController.parentViewController.parentViewController;
    }
    
    return nil;
}

@end
