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

#import "MMDrawerController.h"
#import "UIViewController+MMDrawerController.h"

#import <QuartzCore/QuartzCore.h>

CGFloat const MMDrawerDefaultWidth = 280.0f;
CGFloat const MMDrawerDefaultAnimationVelocity = 840.0f;

NSTimeInterval const MMDrawerDefaultFullAnimationDelay = 0.10f;

CGFloat const MMDrawerDefaultBounceDistance = 50.0f;

NSTimeInterval const MMDrawerDefaultBounceAnimationDuration = 0.2f;
CGFloat const MMDrawerDefaultSecondBounceDistancePercentage = .25f;

CGFloat const MMDrawerDefaultShadowRadius = 10.0f;
CGFloat const MMDrawerDefaultShadowOpacity = 0.8;

NSTimeInterval const MMDrawerMinimumAnimationDuration = 0.15f;

CGFloat const MMDrawerBezelRange = 20.0f;

CGFloat const MMDrawerPanVelocityXAnimationThreshold = 200.0f;

/** The amount of overshoot that is panned linearly. The remaining percentage nonlinearly asymptotes
 * to the max percentage. */
CGFloat const MMDrawerOvershootLinearRangePercentage = 0.75f;

/** The percent of the possible overshoot width to use as the actual overshoot percentage. */
CGFloat const MMDrawerOvershootPercentage = 0.1f;

typedef void (^MMDrawerGestureStartedBlock)(MMDrawerController *drawerController,
                                            UIGestureRecognizer *gesture);
typedef BOOL (^MMDrawerGestureShouldRecognizeTouchBlock)(MMDrawerController *drawerController,
                                                         UIGestureRecognizer *gesture,
                                                         UITouch *touch);
typedef void (^MMDrawerGestureCompletionBlock)(MMDrawerController *drawerController,
                                               UIGestureRecognizer *gesture);

static CAKeyframeAnimation *bounceKeyFrameAnimationForDistanceOnView(CGFloat distance,
                                                                     UIView *view) {
    CGFloat factors[32] = {0,  32, 60, 83, 100, 114, 124, 128, 128, 124, 114, 100, 83, 60, 32, 0,
                           24, 42, 54, 62, 64,  62,  54,  42,  24,  0,   18,  28,  32, 28, 18, 0};

    NSMutableArray *values = [NSMutableArray array];

    for (int i = 0; i < 32; i++) {
        CGFloat positionOffset = factors[i] / 128.0f * distance + CGRectGetMidX(view.bounds);
        [values addObject:@(positionOffset)];
    }

    CAKeyframeAnimation *animation = [CAKeyframeAnimation animationWithKeyPath:@"position.x"];
    animation.repeatCount = 1;
    animation.duration = .8;
    animation.fillMode = kCAFillModeForwards;
    animation.values = values;
    animation.removedOnCompletion = YES;
    animation.autoreverses = NO;

    return animation;
}

static NSString *MMDrawerLeftDrawerKey = @"MMDrawerLeftDrawer";
static NSString *MMDrawerRightDrawerKey = @"MMDrawerRightDrawer";
static NSString *MMDrawerCenterKey = @"MMDrawerCenter";
static NSString *MMDrawerOpenSideKey = @"MMDrawerOpenSide";

@interface MMDrawerCenterContainerView : UIView
@property(nonatomic, assign) MMDrawerOpenCenterInteractionMode centerInteractionMode;
@property(nonatomic, assign) MMDrawerSide openSide;

@property(nonatomic, strong) UIView *overlayView;

@end

@implementation MMDrawerCenterContainerView

- (UIView *)overlayView {
    if (!_overlayView) {
        _overlayView = [[UIView alloc] initWithFrame:self.bounds];
        _overlayView.userInteractionEnabled = NO;
        _overlayView.alpha = 0.0;
    }
    return _overlayView;
}

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event {
    UIView *hitView = [super hitTest:point withEvent:event];
    if (hitView && self.openSide != MMDrawerSideNone) {
        UINavigationBar *navBar = [self navigationBarContainedWithinSubviewsOfView:self];
        CGRect navBarFrame = [navBar convertRect:navBar.bounds toView:self];
        if ((self.centerInteractionMode == MMDrawerOpenCenterInteractionModeNavigationBarOnly &&
             CGRectContainsPoint(navBarFrame, point) == NO) ||
            self.centerInteractionMode == MMDrawerOpenCenterInteractionModeNone) {
            hitView = nil;
        }
    }
    return hitView;
}

- (UINavigationBar *)navigationBarContainedWithinSubviewsOfView:(UIView *)view {
    UINavigationBar *navBar = nil;
    for (UIView *subview in [view subviews]) {
        if ([view isKindOfClass:[UINavigationBar class]]) {
            navBar = (UINavigationBar *)view;
            break;
        } else {
            navBar = [self navigationBarContainedWithinSubviewsOfView:subview];
            if (navBar != nil) {
                break;
            }
        }
    }
    return navBar;
}

- (void)setFrame:(CGRect)frame withLayoutAlpha:(CGFloat)layoutAlpha {
    [super setFrame:frame];

    self.overlayView.alpha = layoutAlpha;

    if (![self.overlayView isDescendantOfView:self]) {
        [self addSubview:self.overlayView];
    } else {
        [self bringSubviewToFront:self.overlayView];
    }
}

@end

@interface MMDrawerController () <UIGestureRecognizerDelegate> {
    CGFloat _maximumRightDrawerWidth;
    CGFloat _maximumLeftDrawerWidth;
    UIColor *_statusBarViewBackgroundColor;
    MMDrawerOpenMode _leftDrawerOpenMode;
    MMDrawerOpenMode _rightDrawerOpenMode;
}

@property(nonatomic, assign, readwrite) MMDrawerSide openSide;

@property(nonatomic, strong) UIView *childControllerContainerView;
@property(nonatomic, strong) MMDrawerCenterContainerView *centerContainerView;
@property(nonatomic, strong) UIView *dummyStatusBarView;

@property(nonatomic, assign) CGRect startingPanRect;
@property(nonatomic, copy) MMDrawerControllerDrawerVisualStateBlock drawerVisualState;
@property(nonatomic, copy) MMDrawerGestureShouldRecognizeTouchBlock gestureShouldRecognizeTouch;
@property(nonatomic, copy) MMDrawerGestureCompletionBlock gestureStart;
@property(nonatomic, copy) MMDrawerGestureCompletionBlock gestureCompletion;
@property(nonatomic, assign, getter=isAnimatingDrawer) BOOL animatingDrawer;
@property(nonatomic, strong) UIGestureRecognizer *pan;
@property (nonatomic, strong) UIView *centerContentOverlay;
@property (nonatomic, assign) MMDrawerSide startingDrawerSide;


@end

@implementation MMDrawerController

#pragma mark - Init

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        [self commonSetup];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
        [self commonSetup];
    }
    return self;
}

- (instancetype)initWithCenterViewController:(UIViewController *)centerViewController
                    leftDrawerViewController:(UIViewController *)leftDrawerViewController
                   rightDrawerViewController:(UIViewController *)rightDrawerViewController {
    NSParameterAssert(centerViewController);
    self = [super init];
    if (self) {
        [self setCenterViewController:centerViewController];
        [self setLeftDrawerViewController:leftDrawerViewController];
        [self setRightDrawerViewController:rightDrawerViewController];
    }
    return self;
}

- (instancetype)initWithCenterViewController:(UIViewController *)centerViewController
                    leftDrawerViewController:(UIViewController *)leftDrawerViewController {
    return [self initWithCenterViewController:centerViewController
                     leftDrawerViewController:leftDrawerViewController
                    rightDrawerViewController:nil];
}

- (instancetype)initWithCenterViewController:(UIViewController *)centerViewController
                   rightDrawerViewController:(UIViewController *)rightDrawerViewController {
    return [self initWithCenterViewController:centerViewController
                     leftDrawerViewController:nil
                    rightDrawerViewController:rightDrawerViewController];
}

- (void)commonSetup {
    [self setMaximumLeftDrawerWidth:MMDrawerDefaultWidth];
    [self setMaximumRightDrawerWidth:MMDrawerDefaultWidth];

    [self setAnimationVelocityLeft:MMDrawerDefaultAnimationVelocity];
    [self setAnimationVelocityRight:MMDrawerDefaultAnimationVelocity];

    [self setShowsShadow:YES];
    [self setShouldStretchLeftDrawer:YES];
    [self setShouldStretchRightDrawer:YES];
    [self side:MMDrawerSideRight openMode:MMDrawerOpenModePushContent];
    [self side:MMDrawerSideLeft openMode:MMDrawerOpenModePushContent];

    [self setOpenDrawerGestureModeMask:MMOpenDrawerGestureModeNone];
    [self setCloseDrawerGestureModeMask:MMCloseDrawerGestureModeNone];
    [self setCenterHiddenInteractionMode:MMDrawerOpenCenterInteractionModeNavigationBarOnly];

    // set shadow related default values
    [self setShadowOpacity:MMDrawerDefaultShadowOpacity];
    [self setShadowRadius:MMDrawerDefaultShadowRadius];
    [self setShadowOffset:CGSizeMake(0, -3)];
    [self setShadowColor:[UIColor blackColor]];

    // set default bezel range for panGestureReconizer
    [self setBezelPanningCenterViewRange:MMDrawerBezelRange];

    // set defualt panVelocityXAnimationThreshold
    [self setPanVelocityXAnimationThreshold:MMDrawerPanVelocityXAnimationThreshold];

    _rightSideEnabled = _leftSideEnabled = YES;
}

#pragma mark - State Restoration
- (void)encodeRestorableStateWithCoder:(NSCoder *)coder {
    [super encodeRestorableStateWithCoder:coder];
    if (self.leftDrawerViewController) {
        [coder encodeObject:self.leftDrawerViewController forKey:MMDrawerLeftDrawerKey];
    }

    if (self.rightDrawerViewController) {
        [coder encodeObject:self.rightDrawerViewController forKey:MMDrawerRightDrawerKey];
    }

    if (self.centerViewController) {
        [coder encodeObject:self.centerViewController forKey:MMDrawerCenterKey];
    }

    [coder encodeInteger:self.openSide forKey:MMDrawerOpenSideKey];
}

- (void)decodeRestorableStateWithCoder:(NSCoder *)coder {
    UIViewController *controller;
    MMDrawerSide openside;

    [super decodeRestorableStateWithCoder:coder];

    if ((controller = [coder decodeObjectForKey:MMDrawerLeftDrawerKey])) {
        self.leftDrawerViewController = controller;
    }

    if ((controller = [coder decodeObjectForKey:MMDrawerRightDrawerKey])) {
        self.rightDrawerViewController = controller;
    }

    if ((controller = [coder decodeObjectForKey:MMDrawerCenterKey])) {
        self.centerViewController = controller;
    }

    if ((openside = [coder decodeIntegerForKey:MMDrawerOpenSideKey])) {
        [self openDrawerSide:openside animated:false completion:nil];
    }
}
#pragma mark - Open/Close methods
- (void)toggleDrawerSide:(MMDrawerSide)drawerSide
                animated:(BOOL)animated
              completion:(void (^)(BOOL finished))completion {
    NSParameterAssert(drawerSide != MMDrawerSideNone);
    if (self.openSide == MMDrawerSideNone) {
        [self openDrawerSide:drawerSide animated:animated completion:completion];
    } else {
        if ((drawerSide == MMDrawerSideLeft && self.openSide == MMDrawerSideLeft) ||
            (drawerSide == MMDrawerSideRight && self.openSide == MMDrawerSideRight)) {
            [self closeDrawerAnimated:animated completion:completion];
        } else if (completion) {
            completion(NO);
        }
    }
}

- (void)closeDrawerAnimated:(BOOL)animated completion:(void (^)(BOOL finished))completion {
    CGFloat velocity = self.openSide == MMDrawerSideLeft ? self.animationVelocityLeft
                                                         : self.animationVelocityRight;
    [self closeDrawerAnimated:animated
                     velocity:velocity
             animationOptions:UIViewAnimationOptionCurveEaseInOut
                   completion:completion];
}

- (void)closeDrawerAnimated:(BOOL)animated
                   velocity:(CGFloat)velocity
           animationOptions:(UIViewAnimationOptions)options
                 completion:(void (^)(BOOL finished))completion {
    if (self.isAnimatingDrawer) {
        if (completion) {
            completion(NO);
        }
    } else {
        [self setAnimatingDrawer:animated];
        MMDrawerSide visibleSide = self.openSide;
        
        if (visibleSide == MMDrawerSideNone) {
            [self setAnimatingDrawer:NO];
            if (completion) {
                completion(NO);
            }
            return;
        }
        
        UIViewController *sideDrawerViewController = [self sideDrawerViewControllerForSide:visibleSide];
        [sideDrawerViewController beginAppearanceTransition:NO animated:animated];
        
        MMDrawerOpenMode openMode = (visibleSide == MMDrawerSideLeft) ? 
                             self.leftDrawerOpenMode : 
                             self.rightDrawerOpenMode;
        
        if (openMode == MMDrawerOpenModeAboveContent) {
            // OVERLAY MODE
            // Get maximum drawer width
            CGFloat maximumDrawerWidth = (visibleSide == MMDrawerSideLeft) ? 
                                        self.maximumLeftDrawerWidth : 
                                        self.maximumRightDrawerWidth;
            
            // Prepare drawer frames
            CGRect currentFrame = sideDrawerViewController.view.frame;
            CGRect finalFrame = currentFrame;
            
            // Set final position based on side
            if (visibleSide == MMDrawerSideLeft) {
                finalFrame.origin.x = -maximumDrawerWidth; // Off-screen left
            } else { // MMDrawerSideRight
                finalFrame.origin.x = self.view.bounds.size.width; // Off-screen right
            }
            
            // Ensure overlay is in view hierarchy
            if (self.centerContentOverlay && self.centerContentOverlay.superview == nil) {
                [self.centerContainerView addSubview:self.centerContentOverlay];
                [self.centerContainerView bringSubviewToFront:self.centerContentOverlay];
                self.centerContentOverlay.alpha = 0.5;
            }
            
            // Calculate animation duration
            CGFloat distance = ABS(currentFrame.origin.x - finalFrame.origin.x);
            NSTimeInterval duration = MAX(distance / ABS(velocity), MMDrawerMinimumAnimationDuration);
            
            // Animate closure
            [UIView animateWithDuration:(animated ? duration : 0.0)
                                  delay:0.0
                                options:options
                             animations:^{
                                 [self setNeedsStatusBarAppearanceUpdateIfSupported];
                                 
                                 // Move drawer off-screen
                                 [sideDrawerViewController.view setFrame:finalFrame];
                                 
                                 // Fade out overlay
                                 self.centerContentOverlay.alpha = 0.0;
                                 
                                 // Update visual state
                                 [self updateDrawerVisualStateForDrawerSide:visibleSide percentVisible:0.0];
                             }
                             completion:^(BOOL finished) {
                                 // Complete appearance transition
                                 [sideDrawerViewController endAppearanceTransition];
                                 
                                 // Update state
                                 [self setOpenSide:MMDrawerSideNone];
                                 [self resetDrawerVisualStateForDrawerSide:visibleSide];
                                 
                                 // Remove overlay
                                 [self.centerContentOverlay removeFromSuperview];
                                 
                                 [self setAnimatingDrawer:NO];
                                 if (completion) {
                                     completion(finished);
                                 }
                             }];
        } else {
            // ORIGINAL PUSH MODE
            CGRect newFrame = self.childControllerContainerView.bounds;
            
            CGFloat distance = ABS(CGRectGetMinX(self.centerContainerView.frame));
            NSTimeInterval duration = MAX(distance / ABS(velocity), MMDrawerMinimumAnimationDuration);
            
            [UIView animateWithDuration:(animated ? duration : 0.0)
                                  delay:0.0
                                options:options
                             animations:^{
                                 [self setNeedsStatusBarAppearanceUpdateIfSupported];
                                 [self.centerContainerView setFrame:newFrame];
                                 [self updateDrawerVisualStateForDrawerSide:visibleSide percentVisible:0.0];
                             }
                             completion:^(BOOL finished) {
                                 // Complete appearance transition
                                 [sideDrawerViewController endAppearanceTransition];
                                 
                                 // Update state
                                 [self setOpenSide:MMDrawerSideNone];
                                 [self resetDrawerVisualStateForDrawerSide:visibleSide];
                                 [self setAnimatingDrawer:NO];
                                 
                                 if (completion) {
                                     completion(finished);
                                 }
                             }];
        }
    }
}

- (void)openDrawerSide:(MMDrawerSide)drawerSide
              animated:(BOOL)animated
            completion:(void (^)(BOOL finished))completion {
    NSParameterAssert(drawerSide != MMDrawerSideNone);
    CGFloat velocity =
        drawerSide == MMDrawerSideLeft ? self.animationVelocityLeft : self.animationVelocityRight;
    [self openDrawerSide:drawerSide
                animated:animated
                velocity:velocity
        animationOptions:UIViewAnimationOptionCurveEaseInOut
              completion:completion];
}

- (void)openDrawerSide:(MMDrawerSide)drawerSide
              animated:(BOOL)animated
              velocity:(CGFloat)velocity
      animationOptions:(UIViewAnimationOptions)options
            completion:(void (^)(BOOL finished))completion {
    NSParameterAssert(drawerSide != MMDrawerSideNone);
    if (self.isAnimatingDrawer) {
        if (completion) {
            completion(NO);
        }
    } else {
        [self setAnimatingDrawer:animated];
        UIViewController *sideDrawerViewController = [self sideDrawerViewControllerForSide:drawerSide];
        
        if (self.openSide != drawerSide) {
            [self prepareToPresentDrawer:drawerSide animated:animated];
        }

        if (sideDrawerViewController) {
            // Check if this drawer should use overlay mode
            MMDrawerOpenMode openMode = (drawerSide == MMDrawerSideLeft) ? 
                             self.leftDrawerOpenMode : 
                             self.rightDrawerOpenMode;

            if (openMode == MMDrawerOpenModeAboveContent) {
                // OVERLAY MODE
                CGFloat maximumDrawerWidth = (drawerSide == MMDrawerSideLeft) ? 
                                           self.maximumLeftDrawerWidth : 
                                           self.maximumRightDrawerWidth;
                
                // Configure drawer frames
                CGRect drawerFrame = sideDrawerViewController.view.frame;
                CGRect initialFrame = sideDrawerViewController.view.frame;
                
                // Set proper width
                drawerFrame.size.width = maximumDrawerWidth;
                initialFrame.size.width = maximumDrawerWidth;
                
                // Set proper positions
                if (drawerSide == MMDrawerSideLeft) {
                    drawerFrame.origin.x = 0; // Final position
                    
                    if (self.openSide != drawerSide) {
                        initialFrame.origin.x = -maximumDrawerWidth; // Start off-screen
                        [sideDrawerViewController.view setFrame:initialFrame];
                    }
                } else { // MMDrawerSideRight
                    CGFloat screenWidth = self.view.bounds.size.width;
                    drawerFrame.origin.x = screenWidth - maximumDrawerWidth; // Final position
                    
                    if (self.openSide != drawerSide) {
                        initialFrame.origin.x = screenWidth; // Start off-screen
                        [sideDrawerViewController.view setFrame:initialFrame];
                    }
                }
                
                // Setup overlay
                [self setupCenterContentOverlay];
                [self.centerContainerView addSubview:self.centerContentOverlay];
                [self.centerContainerView bringSubviewToFront:self.centerContentOverlay];
                self.centerContentOverlay.alpha = 0.0; // Start transparent
                
                // Make sure drawer is visible and in front
                sideDrawerViewController.view.hidden = NO;
                [self.childControllerContainerView bringSubviewToFront:sideDrawerViewController.view];
                
                // Calculate animation duration
                CGFloat distance = ABS(initialFrame.origin.x - drawerFrame.origin.x);
                NSTimeInterval duration = MAX(distance / ABS(velocity), MMDrawerMinimumAnimationDuration);
                
                // Animate opening
                [UIView animateWithDuration:(animated ? duration : 0.0)
                                      delay:0.0
                                    options:options
                                 animations:^{
                                     [self setNeedsStatusBarAppearanceUpdateIfSupported];
                                     
                                     // Move drawer to final position
                                     [sideDrawerViewController.view setFrame:drawerFrame];
                                     
                                     // Fade in overlay
                                     self.centerContentOverlay.alpha = 0.5;
                                     
                                     // Update visual state
                                     [self updateDrawerVisualStateForDrawerSide:drawerSide percentVisible:1.0];
                                 }
                                 completion:^(BOOL finished) {
                                     // Complete appearance transition
                                     if (drawerSide != self.openSide) {
                                         [sideDrawerViewController endAppearanceTransition];
                                     }
                                     
                                     // Update state
                                     [self setOpenSide:drawerSide];
                                     [self resetDrawerVisualStateForDrawerSide:drawerSide];
                                     [self setAnimatingDrawer:NO];
                                     
                                     if (completion) {
                                         completion(finished);
                                     }
                                 }];
            } else {
                // ORIGINAL PUSH MODE
                CGRect newFrame;
                CGRect oldFrame = self.centerContainerView.frame;
                
                if (drawerSide == MMDrawerSideLeft) {
                    newFrame = self.centerContainerView.frame;
                    newFrame.origin.x = self.maximumLeftDrawerWidth;
                } else {
                    newFrame = self.centerContainerView.frame;
                    newFrame.origin.x = 0 - self.maximumRightDrawerWidth;
                }
                
                // Calculate animation duration
                CGFloat distance = ABS(CGRectGetMinX(oldFrame) - newFrame.origin.x);
                NSTimeInterval duration = MAX(distance / ABS(velocity), MMDrawerMinimumAnimationDuration);
                
                // Animate center container
                [UIView animateWithDuration:(animated ? duration : 0.0)
                                      delay:0.0
                                    options:options
                                 animations:^{
                                     [self setNeedsStatusBarAppearanceUpdateIfSupported];
                                     [self.centerContainerView setFrame:newFrame];
                                     [self updateDrawerVisualStateForDrawerSide:drawerSide percentVisible:1.0];
                                 }
                                 completion:^(BOOL finished) {
                                     // Complete appearance transition
                                     if (drawerSide != self.openSide) {
                                         [sideDrawerViewController endAppearanceTransition];
                                     }
                                     
                                     // Update state
                                     [self setOpenSide:drawerSide];
                                     [self resetDrawerVisualStateForDrawerSide:drawerSide];
                                     [self setAnimatingDrawer:NO];
                                     
                                     if (completion) {
                                         completion(finished);
                                     }
                                 }];
            }
        }
    }
}

#pragma mark - Updating the Center View Controller
// If animated is NO, then we need to handle all the appearance calls within this method. Otherwise,
// let the method calling this one handle proper appearance methods since they will have more
// context
- (void)setCenterViewController:(UIViewController *)centerViewController animated:(BOOL)animated {
    if ([self.centerViewController isEqual:centerViewController]) {
        return;
    }

    if (_centerContainerView == nil) {
        // This is related to Issue #152
        // (https://github.com/mutualmobile/MMDrawerController/issues/152)
        // also fixed below in the getter for `childControllerContainerView`. Turns out we have
        // two center container views getting added to the view during init,
        // because the first request self.centerContainerView.bounds was kicking off a
        // viewDidLoad, which caused us to be able to fall through this check twice.
        //
        // The fix is to grab the bounds, and then check again that the child container view has
        // not been created.

        CGRect centerFrame = self.childControllerContainerView.bounds;
        if (_centerContainerView == nil) {
            _centerContainerView = [[MMDrawerCenterContainerView alloc] initWithFrame:centerFrame];
            [self.centerContainerView setAutoresizingMask:UIViewAutoresizingFlexibleWidth |
                                                          UIViewAutoresizingFlexibleHeight];
            [self.centerContainerView setBackgroundColor:[UIColor clearColor]];
            [self.centerContainerView setOpenSide:self.openSide];
            [self.centerContainerView setCenterInteractionMode:self.centerHiddenInteractionMode];
            [self.childControllerContainerView addSubview:self.centerContainerView];
        }
    }

    UIViewController *oldCenterViewController = self.centerViewController;
    if (oldCenterViewController) {
        [oldCenterViewController willMoveToParentViewController:nil];
        if (animated == NO) {
            [oldCenterViewController beginAppearanceTransition:NO animated:NO];
        }
        [oldCenterViewController.view removeFromSuperview];
        if (animated == NO) {
            [oldCenterViewController endAppearanceTransition];
        }
        [oldCenterViewController removeFromParentViewController];
    }

    _centerViewController = centerViewController;

    [self addChildViewController:self.centerViewController];
    [self.centerViewController.view setFrame:self.childControllerContainerView.bounds];
    [self.centerContainerView addSubview:self.centerViewController.view];
    [self.childControllerContainerView bringSubviewToFront:self.centerContainerView];
    [self.centerViewController.view
        setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight];
    [self updateShadowForCenterView];

    if (animated == NO) {
        // If drawer is offscreen, then viewWillAppear: will take care of this
        if (self.view.window) {
            [self.centerViewController beginAppearanceTransition:YES animated:NO];
            [self.centerViewController endAppearanceTransition];
        }
        [self.centerViewController didMoveToParentViewController:self];
    }
}

- (void)setCenterViewController:(UIViewController *)newCenterViewController
             withCloseAnimation:(BOOL)animated
                     completion:(void (^)(BOOL finished))completion {

    if (self.openSide == MMDrawerSideNone) {
        // If a side drawer isn't open, there is nothing to animate...
        animated = NO;
    }

    BOOL forwardAppearanceMethodsToCenterViewController =
        ([self.centerViewController isEqual:newCenterViewController] == NO);

    UIViewController *oldCenterViewController = self.centerViewController;
    // This is related to issue 363 (https://github.com/novkostya/MMDrawerController/pull/363)
    // This needs to be refactored so the appearance logic is easier
    // to follow across the multiple close/setter methods
    if (animated && forwardAppearanceMethodsToCenterViewController) {
        [oldCenterViewController beginAppearanceTransition:NO animated:NO];
    }

    [self setCenterViewController:newCenterViewController animated:animated];

    // Related to note above.
    if (animated && forwardAppearanceMethodsToCenterViewController) {
        [oldCenterViewController endAppearanceTransition];
    }

    if (animated) {
        [self updateDrawerVisualStateForDrawerSide:self.openSide percentVisible:1.0];
        if (forwardAppearanceMethodsToCenterViewController) {
            [self.centerViewController beginAppearanceTransition:YES animated:animated];
        }
        [self closeDrawerAnimated:animated
                       completion:^(BOOL finished) {
                         if (forwardAppearanceMethodsToCenterViewController) {
                             [self.centerViewController endAppearanceTransition];
                             [self.centerViewController didMoveToParentViewController:self];
                         }
                         if (completion) {
                             completion(finished);
                         }
                       }];
    } else {
        if (completion) {
            completion(YES);
        }
    }
}

- (void)setCenterViewController:(UIViewController *)newCenterViewController
         withFullCloseAnimation:(BOOL)animated
                     completion:(void (^)(BOOL finished))completion {
    if (self.openSide != MMDrawerSideNone && animated) {

        BOOL forwardAppearanceMethodsToCenterViewController =
            ([self.centerViewController isEqual:newCenterViewController] == NO);

        UIViewController *sideDrawerViewController =
            [self sideDrawerViewControllerForSide:self.openSide];

        CGFloat targetClosePoint = 0.0f;
        if (self.openSide == MMDrawerSideRight) {
            targetClosePoint = -CGRectGetWidth(self.childControllerContainerView.bounds);
        } else if (self.openSide == MMDrawerSideLeft) {
            targetClosePoint = CGRectGetWidth(self.childControllerContainerView.bounds);
        }

        CGFloat distance = ABS(self.centerContainerView.frame.origin.x - targetClosePoint);
        NSTimeInterval firstDuration = [self animationDurationForAnimationDistance:distance];

        CGRect newCenterRect = self.centerContainerView.frame;

        [self setAnimatingDrawer:animated];

        UIViewController *oldCenterViewController = self.centerViewController;
        if (forwardAppearanceMethodsToCenterViewController) {
            [oldCenterViewController beginAppearanceTransition:NO animated:animated];
        }
        newCenterRect.origin.x = targetClosePoint;
        [UIView animateWithDuration:firstDuration
            delay:0.0
            options:UIViewAnimationOptionCurveEaseInOut
            animations:^{
              [self.centerContainerView setFrame:newCenterRect withLayoutAlpha:1.0];
              [sideDrawerViewController.view setFrame:self.childControllerContainerView.bounds];
            }
            completion:^(BOOL finished) {
              CGRect oldCenterRect = self.centerContainerView.frame;
              [self setCenterViewController:newCenterViewController animated:animated];
              [self.centerContainerView setFrame:oldCenterRect withLayoutAlpha:1.0];
              [self updateDrawerVisualStateForDrawerSide:self.openSide percentVisible:1.0];
              if (forwardAppearanceMethodsToCenterViewController) {
                  [oldCenterViewController endAppearanceTransition];
                  [self.centerViewController beginAppearanceTransition:YES animated:animated];
              }
              [sideDrawerViewController beginAppearanceTransition:NO animated:animated];
              [UIView animateWithDuration:[self animationDurationForAnimationDistance:
                                                    CGRectGetWidth(
                                                        self.childControllerContainerView.bounds)]
                  delay:MMDrawerDefaultFullAnimationDelay
                  options:UIViewAnimationOptionCurveEaseInOut
                  animations:^{
                    [self.centerContainerView setFrame:self.childControllerContainerView.bounds
                                       withLayoutAlpha:1.0];
                    [self updateDrawerVisualStateForDrawerSide:self.openSide percentVisible:0.0];
                  }
                  completion:^(BOOL finished) {
                    if (forwardAppearanceMethodsToCenterViewController) {
                        [self.centerViewController endAppearanceTransition];
                        [self.centerViewController didMoveToParentViewController:self];
                    }
                    [sideDrawerViewController endAppearanceTransition];
                    [self resetDrawerVisualStateForDrawerSide:self.openSide];

                    [sideDrawerViewController.view
                        setFrame:sideDrawerViewController.mm_visibleDrawerFrame];

                    [self setOpenSide:MMDrawerSideNone];
                    [self setAnimatingDrawer:NO];
                    if (completion) {
                        completion(finished);
                    }
                  }];
            }];
    } else {
        [self setCenterViewController:newCenterViewController animated:animated];
        if (self.openSide != MMDrawerSideNone) {
            [self closeDrawerAnimated:animated completion:completion];
        } else if (completion) {
            completion(YES);
        }
    }
}

- (void)setCenterOverlayColor:(UIColor *)color {
    if (color) {
        self.centerContainerView.overlayView.backgroundColor = color;
    }
}

#pragma mark - Size Methods
- (void)setMaximumLeftDrawerWidth:(CGFloat)width
                         animated:(BOOL)animated
                       completion:(void (^)(BOOL finished))completion {
    [self setMaximumDrawerWidth:width
                        forSide:MMDrawerSideLeft
                       animated:animated
                     completion:completion];
}

- (void)setMaximumRightDrawerWidth:(CGFloat)width
                          animated:(BOOL)animated
                        completion:(void (^)(BOOL finished))completion {
    [self setMaximumDrawerWidth:width
                        forSide:MMDrawerSideRight
                       animated:animated
                     completion:completion];
}

- (void)setMaximumDrawerWidth:(CGFloat)width
                      forSide:(MMDrawerSide)drawerSide
                     animated:(BOOL)animated
                   completion:(void (^)(BOOL finished))completion {
    NSParameterAssert(width > 0);
    NSParameterAssert(drawerSide != MMDrawerSideNone);

    UIViewController *sideDrawerViewController = [self sideDrawerViewControllerForSide:drawerSide];
    CGFloat oldWidth = 0.f;
    NSInteger drawerSideOriginCorrection = 1;
    if (drawerSide == MMDrawerSideLeft) {
        oldWidth = _maximumLeftDrawerWidth;
        _maximumLeftDrawerWidth = width;
    } else if (drawerSide == MMDrawerSideRight) {
        oldWidth = _maximumRightDrawerWidth;
        _maximumRightDrawerWidth = width;
        drawerSideOriginCorrection = -1;
    }

    CGFloat distance = ABS(width - oldWidth);
    NSTimeInterval duration = [self animationDurationForAnimationDistance:distance];

    if (self.openSide == drawerSide) {
        CGRect newCenterRect = self.centerContainerView.frame;
        newCenterRect.origin.x = drawerSideOriginCorrection * width;
        [UIView animateWithDuration:(animated ? duration : 0)
            delay:0.0
            options:UIViewAnimationOptionCurveEaseInOut
            animations:^{
              [self.centerContainerView setFrame:newCenterRect withLayoutAlpha:1.0];
              [sideDrawerViewController.view
                  setFrame:sideDrawerViewController.mm_visibleDrawerFrame];
            }
            completion:^(BOOL finished) {
              if (completion != nil) {
                  completion(finished);
              }
            }];
    } else {
        [sideDrawerViewController.view setFrame:sideDrawerViewController.mm_visibleDrawerFrame];
        if (completion != nil) {
            completion(YES);
        }
    }
}

#pragma mark - Bounce Methods
- (void)bouncePreviewForDrawerSide:(MMDrawerSide)drawerSide
                        completion:(void (^)(BOOL finished))completion {
    NSParameterAssert(drawerSide != MMDrawerSideNone);
    [self bouncePreviewForDrawerSide:drawerSide
                            distance:MMDrawerDefaultBounceDistance
                          completion:completion];
}

- (void)bouncePreviewForDrawerSide:(MMDrawerSide)drawerSide
                          distance:(CGFloat)distance
                        completion:(void (^)(BOOL finished))completion {
    NSParameterAssert(drawerSide != MMDrawerSideNone);

    UIViewController *sideDrawerViewController = [self sideDrawerViewControllerForSide:drawerSide];

    if (sideDrawerViewController == nil || self.openSide != MMDrawerSideNone) {
        if (completion) {
            completion(NO);
        }
        return;
    } else {
        [self prepareToPresentDrawer:drawerSide animated:YES];

        [self updateDrawerVisualStateForDrawerSide:drawerSide percentVisible:1.0];

        [CATransaction begin];
        [CATransaction setCompletionBlock:^{
          [sideDrawerViewController endAppearanceTransition];
          [sideDrawerViewController beginAppearanceTransition:NO animated:NO];
          [sideDrawerViewController endAppearanceTransition];
          if (completion) {
              completion(YES);
          }
        }];

        CGFloat modifier = ((drawerSide == MMDrawerSideLeft) ? 1.0 : -1.0);
        CAKeyframeAnimation *animation =
            bounceKeyFrameAnimationForDistanceOnView(distance * modifier, self.centerContainerView);
        [self.centerContainerView.layer addAnimation:animation forKey:@"bouncing"];

        [CATransaction commit];
    }
}

#pragma mark - Setting Drawer Visual State
- (void)setDrawerVisualStateBlock:(void (^)(MMDrawerController *, MMDrawerSide,
                                            CGFloat))drawerVisualStateBlock {
    [self setDrawerVisualState:drawerVisualStateBlock];
}

#pragma mark - Setting Custom Gesture Handler Block
- (void)setGestureShouldRecognizeTouchBlock:(BOOL (^)(MMDrawerController *, UIGestureRecognizer *,
                                                      UITouch *))gestureShouldRecognizeTouchBlock {
    [self setGestureShouldRecognizeTouch:gestureShouldRecognizeTouchBlock];
}

#pragma mark - Setting the Gesture Completion Block
- (void)setGestureCompletionBlock:(void (^)(MMDrawerController *,
                                            UIGestureRecognizer *))gestureCompletionBlock {
    [self setGestureCompletion:gestureCompletionBlock];
}

#pragma mark - Setting the Gesture Start Block
- (void)setGestureStartBlock:(void (^)(MMDrawerController *,
                                       UIGestureRecognizer *))gestureStartBlock {
    [self setGestureStart:gestureStartBlock];
}

#pragma mark - Subclass Methods
- (BOOL)shouldAutomaticallyForwardAppearanceMethods {
    return NO;
}

- (BOOL)shouldAutomaticallyForwardRotationMethods {
    return NO;
}

- (BOOL)automaticallyForwardAppearanceAndRotationMethodsToChildViewControllers {
    return NO;
}

#pragma mark - View Lifecycle

- (void)viewDidLoad {
    [super viewDidLoad];

    [self.view setBackgroundColor:[UIColor blackColor]];

    [self setupGestureRecognizers];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.centerViewController beginAppearanceTransition:YES animated:animated];

    if (self.openSide == MMDrawerSideLeft) {
        [self.leftDrawerViewController beginAppearanceTransition:YES animated:animated];
    } else if (self.openSide == MMDrawerSideRight) {
        [self.rightDrawerViewController beginAppearanceTransition:YES animated:animated];
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self updateShadowForCenterView];
    [self.centerViewController endAppearanceTransition];

    if (self.openSide == MMDrawerSideLeft) {
        [self.leftDrawerViewController endAppearanceTransition];
    } else if (self.openSide == MMDrawerSideRight) {
        [self.rightDrawerViewController endAppearanceTransition];
    }
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.centerViewController beginAppearanceTransition:NO animated:animated];
    if (self.openSide == MMDrawerSideLeft) {
        [self.leftDrawerViewController beginAppearanceTransition:NO animated:animated];
    } else if (self.openSide == MMDrawerSideRight) {
        [self.rightDrawerViewController beginAppearanceTransition:NO animated:animated];
    }
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self.centerViewController endAppearanceTransition];
    if (self.openSide == MMDrawerSideLeft) {
        [self.leftDrawerViewController endAppearanceTransition];
    } else if (self.openSide == MMDrawerSideRight) {
        [self.rightDrawerViewController endAppearanceTransition];
    }
}

#pragma mark Rotation

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
                                duration:(NSTimeInterval)duration {
    [super willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
    // If a rotation begins, we are going to cancel the current gesture and reset transform and
    // anchor points so everything works correctly
    BOOL gestureInProgress = NO;
    for (UIGestureRecognizer *gesture in self.view.gestureRecognizers) {
        if (gesture.state == UIGestureRecognizerStateChanged) {
            [gesture setEnabled:NO];
            [gesture setEnabled:YES];
            gestureInProgress = YES;
        }
        if (gestureInProgress) {
            [self resetDrawerVisualStateForDrawerSide:self.openSide];
        }
    }

    if ([self needsManualForwardingOfRotationEvents]) {
        for (UIViewController *childViewController in self.childViewControllers) {
            [childViewController willRotateToInterfaceOrientation:toInterfaceOrientation
                                                         duration:duration];
        }
    }
}
- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
                                         duration:(NSTimeInterval)duration {
    [super willAnimateRotationToInterfaceOrientation:toInterfaceOrientation duration:duration];
    // We need to support the shadow path rotation animation
    // Inspired from here:
    // http://blog.radi.ws/post/8348898129/calayers-shadowpath-and-uiview-autoresizing
    if (self.showsShadow) {
        CGPathRef oldShadowPath = self.centerContainerView.layer.shadowPath;
        if (oldShadowPath) {
            CFRetain(oldShadowPath);
        }

        [self updateShadowForCenterView];

        if (oldShadowPath) {
            [self.centerContainerView.layer
                addAnimation:((^{
                  CABasicAnimation *transition =
                      [CABasicAnimation animationWithKeyPath:@"shadowPath"];
                  transition.fromValue = (__bridge id)oldShadowPath;
                  transition.timingFunction =
                      [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
                  transition.duration = duration;
                  return transition;
                })())
                      forKey:@"transition"];
            CFRelease(oldShadowPath);
        }
    }

    if ([self needsManualForwardingOfRotationEvents]) {
        for (UIViewController *childViewController in self.childViewControllers) {
            [childViewController willAnimateRotationToInterfaceOrientation:toInterfaceOrientation
                                                                  duration:duration];
        }
    }
}

- (BOOL)shouldAutorotate {
    return YES;
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [super didRotateFromInterfaceOrientation:fromInterfaceOrientation];

    if ([self needsManualForwardingOfRotationEvents]) {
        for (UIViewController *childViewController in self.childViewControllers) {
            [childViewController didRotateFromInterfaceOrientation:fromInterfaceOrientation];
        }
    }
}

- (bool)hasPan {
    for (UIGestureRecognizer *recognizer in self.view.gestureRecognizers) {
        if (recognizer == _pan) {
            return YES;
        }
    }
    return NO;
}

#pragma mark - Setters
- (void)setRightDrawerViewController:(UIViewController *)rightDrawerViewController {
    [self setDrawerViewController:rightDrawerViewController forSide:MMDrawerSideRight];
}

- (void)setLeftDrawerViewController:(UIViewController *)leftDrawerViewController {
    [self setDrawerViewController:leftDrawerViewController forSide:MMDrawerSideLeft];
}

- (void)setDrawerViewController:(UIViewController *)viewController
                        forSide:(MMDrawerSide)drawerSide {
    NSParameterAssert(drawerSide != MMDrawerSideNone);

    UIViewController *currentSideViewController = [self sideDrawerViewControllerForSide:drawerSide];

    if (currentSideViewController == viewController) {
        return;
    }

    if (currentSideViewController != nil) {
        [currentSideViewController beginAppearanceTransition:NO animated:NO];
        [currentSideViewController.view removeFromSuperview];
        [currentSideViewController endAppearanceTransition];
        [currentSideViewController willMoveToParentViewController:nil];
        [currentSideViewController removeFromParentViewController];
    }

    UIViewAutoresizing autoResizingMask = 0;
    if (drawerSide == MMDrawerSideLeft) {
        _leftDrawerViewController = viewController;
        autoResizingMask = UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleHeight;

    } else if (drawerSide == MMDrawerSideRight) {
        _rightDrawerViewController = viewController;
        autoResizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
    }

    if (viewController) {
        [self addChildViewController:viewController];

        if ((self.openSide == drawerSide) &&
            [self.childControllerContainerView.subviews containsObject:self.centerContainerView]) {
            [self.childControllerContainerView insertSubview:viewController.view
                                                belowSubview:self.centerContainerView];
            [viewController beginAppearanceTransition:YES animated:NO];
            [viewController endAppearanceTransition];
        } else {
            [self.childControllerContainerView addSubview:viewController.view];
            [self.childControllerContainerView sendSubviewToBack:viewController.view];
            [viewController.view setHidden:YES];
        }
        [viewController didMoveToParentViewController:self];
        [viewController.view setAutoresizingMask:autoResizingMask];
        [viewController.view setFrame:viewController.mm_visibleDrawerFrame];
    }
}

- (void)setCenterViewController:(UIViewController *)centerViewController {
    [self setCenterViewController:centerViewController animated:NO];
}

- (void)setShowsShadow:(BOOL)showsShadow {
    _showsShadow = showsShadow;
    [self updateShadowForCenterView];
}

- (void)setShadowRadius:(CGFloat)shadowRadius {
    _shadowRadius = shadowRadius;
    [self updateShadowForCenterView];
}

- (void)setShadowOpacity:(CGFloat)shadowOpacity {
    _shadowOpacity = shadowOpacity;
    [self updateShadowForCenterView];
}

- (void)setShadowOffset:(CGSize)shadowOffset {
    _shadowOffset = shadowOffset;
    [self updateShadowForCenterView];
}

- (void)setShadowColor:(UIColor *)shadowColor {
    _shadowColor = shadowColor;
    [self updateShadowForCenterView];
}

- (void)setOpenSide:(MMDrawerSide)openSide {
    if (_openSide != openSide) {
        _openSide = openSide;
        [self.centerContainerView setOpenSide:openSide];
        if (openSide == MMDrawerSideNone) {
            [self.leftDrawerViewController.view setHidden:YES];
            [self.rightDrawerViewController.view setHidden:YES];
        }
        [self setNeedsStatusBarAppearanceUpdateIfSupported];
    }
}

- (void)setCenterHiddenInteractionMode:
    (MMDrawerOpenCenterInteractionMode)centerHiddenInteractionMode {
    if (_centerHiddenInteractionMode != centerHiddenInteractionMode) {
        _centerHiddenInteractionMode = centerHiddenInteractionMode;
        [self.centerContainerView setCenterInteractionMode:centerHiddenInteractionMode];
    }
}

- (void)setMaximumLeftDrawerWidth:(CGFloat)maximumLeftDrawerWidth {
    [self setMaximumLeftDrawerWidth:maximumLeftDrawerWidth animated:NO completion:nil];
}

- (void)setMaximumRightDrawerWidth:(CGFloat)maximumRightDrawerWidth {
    [self setMaximumRightDrawerWidth:maximumRightDrawerWidth animated:NO completion:nil];
}

- (void)setShowsStatusBarBackgroundView:(BOOL)showsDummyStatusBar {
    if (showsDummyStatusBar != _showsStatusBarBackgroundView) {
        _showsStatusBarBackgroundView = showsDummyStatusBar;
        CGRect frame = self.childControllerContainerView.frame;
        if (_showsStatusBarBackgroundView) {
            frame.origin.y = 20;
            frame.size.height = CGRectGetHeight(self.view.bounds) - 20;
        } else {
            frame.origin.y = 0;
            frame.size.height = CGRectGetHeight(self.view.bounds);
        }
        [self.childControllerContainerView setFrame:frame];
        [self.dummyStatusBarView setHidden:!showsDummyStatusBar];
    }
}

- (void)setStatusBarViewBackgroundColor:(UIColor *)dummyStatusBarColor {
    _statusBarViewBackgroundColor = dummyStatusBarColor;
    [self.dummyStatusBarView setBackgroundColor:_statusBarViewBackgroundColor];
}

- (void)setLeftDrawerOpenMode:(MMDrawerOpenMode)openMode {
    _leftDrawerOpenMode = openMode;
}
    
- (void)setRightDrawerOpenMode:(MMDrawerOpenMode)openMode {
    _rightDrawerOpenMode = openMode;
}

- (void)setAnimatingDrawer:(BOOL)animatingDrawer {
    _animatingDrawer = animatingDrawer;
    [self.view setUserInteractionEnabled:!animatingDrawer];
}

- (void)setLeftSideEnabled:(BOOL)leftSideEnabled {
    _leftSideEnabled = leftSideEnabled;
    [self updatePanHandlersState];
}

- (void)setRightSideEnabled:(BOOL)rightSideEnabled {
    _rightSideEnabled = rightSideEnabled;
    [self updatePanHandlersState];
}


#pragma mark - Getters
- (CGFloat)maximumLeftDrawerWidth {
    if (self.leftDrawerViewController) {
        return _maximumLeftDrawerWidth;
    } else {
        return 0;
    }
}

- (CGFloat)maximumRightDrawerWidth {
    if (self.rightDrawerViewController) {
        return _maximumRightDrawerWidth;
    } else {
        return 0;
    }
}

- (CGFloat)visibleLeftDrawerWidth {
    return MAX(0.0, CGRectGetMinX(self.centerContainerView.frame));
}

- (CGFloat)visibleRightDrawerWidth {
    if (CGRectGetMinX(self.centerContainerView.frame) < 0) {
        return CGRectGetWidth(self.childControllerContainerView.bounds) -
               CGRectGetMaxX(self.centerContainerView.frame);
    } else {
        return 0.0f;
    }
}

- (UIView *)childControllerContainerView {
    if (_childControllerContainerView == nil) {
        // Issue #152 (https://github.com/mutualmobile/MMDrawerController/issues/152)
        // Turns out we have two child container views getting added to the view during init,
        // because the first request self.view.bounds was kicking off a viewDidLoad, which
        // caused us to be able to fall through this check twice.
        //
        // The fix is to grab the bounds, and then check again that the child container view has
        // not been created.
        CGRect childContainerViewFrame = self.view.bounds;
        if (_childControllerContainerView == nil) {
            _childControllerContainerView = [[UIView alloc] initWithFrame:childContainerViewFrame];
            [_childControllerContainerView setBackgroundColor:[UIColor clearColor]];
            [_childControllerContainerView setAutoresizingMask:UIViewAutoresizingFlexibleHeight |
                                                               UIViewAutoresizingFlexibleWidth];
            [self.view addSubview:_childControllerContainerView];
        }
    }
    return _childControllerContainerView;
}

- (UIView *)dummyStatusBarView {
    if (_dummyStatusBarView == nil) {
        _dummyStatusBarView =
            [[UIView alloc] initWithFrame:CGRectMake(0, 0, CGRectGetWidth(self.view.bounds), 20)];
        [_dummyStatusBarView setAutoresizingMask:UIViewAutoresizingFlexibleWidth];
        [_dummyStatusBarView setBackgroundColor:self.statusBarViewBackgroundColor];
        [_dummyStatusBarView setHidden:!_showsStatusBarBackgroundView];
        [self.view addSubview:_dummyStatusBarView];
    }
    return _dummyStatusBarView;
}

- (UIColor *)statusBarViewBackgroundColor {
    if (_statusBarViewBackgroundColor == nil) {
        _statusBarViewBackgroundColor = [UIColor blackColor];
    }
    return _statusBarViewBackgroundColor;
}

- (MMDrawerOpenMode)rightDrawerOpenMode {
    return _rightDrawerOpenMode;
}

- (MMDrawerOpenMode)leftDrawerOpenMode {
    return _leftDrawerOpenMode;
}

#pragma mark - Gesture Handlers

- (void)tapGestureCallback:(UITapGestureRecognizer *)tapGesture {
    if (self.openSide != MMDrawerSideNone && self.isAnimatingDrawer == NO) {
        [self closeDrawerAnimated:YES
                       completion:^(BOOL finished) {
                         if (self.gestureCompletion) {
                             self.gestureCompletion(self, tapGesture);
                         }
                       }];
    }
}

- (void)panGestureCallback:(UIPanGestureRecognizer *)panGesture {
    switch (panGesture.state) {
    case UIGestureRecognizerStateBegan: {
        // Call gesture start callback
        if (self.gestureStart) {
            self.gestureStart(self, panGesture);
        }
        
        // Don't proceed if drawer is currently animating
        if (self.animatingDrawer) {
            [panGesture setEnabled:NO];
            break;
        }
        
        // Determine which drawer to work with
        CGPoint velocity = [panGesture velocityInView:self.view];
        MMDrawerSide drawerSide = self.openSide;
        
        if (drawerSide == MMDrawerSideNone) {
            // Determine based on gesture direction
            drawerSide = (velocity.x > 0) ? MMDrawerSideLeft : MMDrawerSideRight;
            
            // Check if the side is enabled
            if ((drawerSide == MMDrawerSideLeft && !_leftSideEnabled) || 
                (drawerSide == MMDrawerSideRight && !_rightSideEnabled)) {
                drawerSide = (drawerSide == MMDrawerSideLeft) ? MMDrawerSideRight : MMDrawerSideLeft;
                
                if ((drawerSide == MMDrawerSideLeft && !_leftSideEnabled) || 
                    (drawerSide == MMDrawerSideRight && !_rightSideEnabled)) {
                    return;
                }
            }
        }
        
        // Store which drawer we're working with for this gesture
        self.startingDrawerSide = drawerSide;
        
        MMDrawerOpenMode openMode = (drawerSide == MMDrawerSideLeft) ? 
                             self.leftDrawerOpenMode : 
                             self.rightDrawerOpenMode;

        if (openMode == MMDrawerOpenModeAboveContent) {
            // OVERLAY MODE
            UIViewController *drawerViewController = [self sideDrawerViewControllerForSide:drawerSide];
            CGFloat maximumDrawerWidth = (drawerSide == MMDrawerSideLeft) ? 
                                        self.maximumLeftDrawerWidth : 
                                        self.maximumRightDrawerWidth;
            
            // Store current drawer frame
            self.startingPanRect = drawerViewController.view.frame;
            
            // If drawer is closed, set up initial position
            if (self.openSide == MMDrawerSideNone) {
                CGRect drawerFrame = drawerViewController.view.frame;
                
                // Set proper width
                drawerFrame.size.width = maximumDrawerWidth;
                
                // Position off-screen based on side
                if (drawerSide == MMDrawerSideLeft) {
                    drawerFrame.origin.x = -maximumDrawerWidth;
                } else { // MMDrawerSideRight
                    drawerFrame.origin.x = self.view.bounds.size.width;
                }
                
                // Apply initial frame
                [drawerViewController.view setFrame:drawerFrame];
                
                // Ensure drawer is visible and in front
                drawerViewController.view.hidden = NO;
                [self.childControllerContainerView bringSubviewToFront:drawerViewController.view];
                
                // Update starting rect
                self.startingPanRect = drawerFrame;
            }
        } else {
            // Original push mode - get center container position as starting point
            self.startingPanRect = self.centerContainerView.frame;
        }
        
        break;
    }
    case UIGestureRecognizerStateChanged: {
        self.view.userInteractionEnabled = NO;
        
        // Get translation
        CGPoint translatedPoint = [panGesture translationInView:self.view];
        
        // Use the drawer side we're working with
        MMDrawerSide drawerSide = self.startingDrawerSide;
        if (drawerSide == MMDrawerSideNone) {
            drawerSide = self.openSide;
            if (drawerSide == MMDrawerSideNone) {
                drawerSide = (translatedPoint.x > 0) ? MMDrawerSideLeft : MMDrawerSideRight;
            }
        }
        
        MMDrawerOpenMode openMode = (drawerSide == MMDrawerSideLeft) ? 
                             self.leftDrawerOpenMode : 
                             self.rightDrawerOpenMode;

        if (openMode == MMDrawerOpenModeAboveContent) {
            // OVERLAY MODE
            UIViewController *drawerViewController = [self sideDrawerViewControllerForSide:drawerSide];
            if (!drawerViewController) {
                return;
            }
            
            CGFloat maximumDrawerWidth = (drawerSide == MMDrawerSideLeft) ? 
                                        self.maximumLeftDrawerWidth : 
                                        self.maximumRightDrawerWidth;
            
            // Calculate new drawer position
            CGRect newFrame = drawerViewController.view.frame;
            if (self.openSide == drawerSide) {
                // If drawer is already open, adjust from starting position
                newFrame.origin.x = self.startingPanRect.origin.x + translatedPoint.x;
            } else {
                // If drawer is closed, calculate from off-screen position
                if (drawerSide == MMDrawerSideLeft) {
                    newFrame.origin.x = -maximumDrawerWidth + translatedPoint.x;
                } else { // MMDrawerSideRight
                    CGFloat screenWidth = self.view.bounds.size.width;
                    newFrame.origin.x = screenWidth + translatedPoint.x;
                }
            }
            
            // Apply constraints based on drawer side
            if (drawerSide == MMDrawerSideLeft) {
                newFrame.origin.x = MIN(0, newFrame.origin.x);
                newFrame.origin.x = MAX(-maximumDrawerWidth, newFrame.origin.x);
            } else { // MMDrawerSideRight
                CGFloat screenWidth = self.view.bounds.size.width;
                newFrame.origin.x = MAX(screenWidth - maximumDrawerWidth, newFrame.origin.x);
                newFrame.origin.x = MIN(screenWidth, newFrame.origin.x);
            }
            
            // Calculate visibility percentage
            CGFloat percentVisible;
            if (drawerSide == MMDrawerSideLeft) {
                percentVisible = (maximumDrawerWidth + newFrame.origin.x) / maximumDrawerWidth;
            } else { // MMDrawerSideRight
                CGFloat rightEdge = self.view.bounds.size.width;
                percentVisible = (rightEdge - newFrame.origin.x) / maximumDrawerWidth;
            }
            percentVisible = MAX(0, MIN(1.0, percentVisible));
            
            // Handle overlay
            [self setupCenterContentOverlay];
            if (self.centerContentOverlay.superview != self.centerContainerView) {
                [self.centerContainerView addSubview:self.centerContentOverlay];
                [self.centerContainerView bringSubviewToFront:self.centerContentOverlay];
            }
            self.centerContentOverlay.alpha = percentVisible * 0.5;
            
            // Determine visible side based on percentage
            MMDrawerSide visibleSide = (percentVisible > 0.15) ? drawerSide : MMDrawerSideNone;
            
            // Update appearance transitions
            if (self.openSide != visibleSide) {
                if (self.openSide != MMDrawerSideNone) {
                    UIViewController *sideDrawerVC = [self sideDrawerViewControllerForSide:self.openSide];
                    [sideDrawerVC beginAppearanceTransition:NO animated:NO];
                    [sideDrawerVC endAppearanceTransition];
                }
                
                if (visibleSide != MMDrawerSideNone) {
                    [self prepareToPresentDrawer:visibleSide animated:NO];
                    UIViewController *visibleDrawerVC = [self sideDrawerViewControllerForSide:visibleSide];
                    [visibleDrawerVC endAppearanceTransition];
                }
                
                [self setOpenSide:visibleSide];
            }
            
            // Apply new frame
            drawerViewController.view.frame = newFrame;
            [self.childControllerContainerView bringSubviewToFront:drawerViewController.view];
        } else {
            // ORIGINAL PUSH MODE
            CGRect newFrame = self.startingPanRect;
            
            // Calculate new center container position
            newFrame.origin.x = self.startingPanRect.origin.x + translatedPoint.x;
            
            // Apply constraints
            CGFloat minX = -self.maximumRightDrawerWidth;
            CGFloat maxX = self.maximumLeftDrawerWidth;
            newFrame.origin.x = MAX(minX, MIN(maxX, newFrame.origin.x));
            
            // Determine visible side and percentage
            CGFloat xOffset = newFrame.origin.x;
            MMDrawerSide visibleSide = MMDrawerSideNone;
            CGFloat percentVisible = 0.0;
            
            if (xOffset > 0) {
                visibleSide = MMDrawerSideLeft;
                percentVisible = xOffset / self.maximumLeftDrawerWidth;
            } else if (xOffset < 0) {
                visibleSide = MMDrawerSideRight;
                percentVisible = ABS(xOffset) / self.maximumRightDrawerWidth;
            }
            
            // Check if side is enabled
            if ((!_leftSideEnabled && visibleSide == MMDrawerSideLeft) ||
                (!_rightSideEnabled && visibleSide == MMDrawerSideRight)) {
                return;
            }
            
            // Handle appearance transitions
            UIViewController *visibleSideDrawerViewController = [self sideDrawerViewControllerForSide:visibleSide];
            
            if (self.openSide != visibleSide) {
                // Handle existing drawer disappearing
                UIViewController *sideDrawerVC = [self sideDrawerViewControllerForSide:self.openSide];
                [sideDrawerVC beginAppearanceTransition:NO animated:NO];
                [sideDrawerVC endAppearanceTransition];
                
                // Handle new drawer appearing
                [self prepareToPresentDrawer:visibleSide animated:NO];
                [visibleSideDrawerViewController endAppearanceTransition];
                
                [self setOpenSide:visibleSide];
            } else if (visibleSide == MMDrawerSideNone) {
                [self setOpenSide:MMDrawerSideNone];
            }
            
            // Update visual state and position center container
            [self updateDrawerVisualStateForDrawerSide:visibleSide percentVisible:percentVisible];
            [self.centerContainerView setFrame:newFrame];
        }
        
        self.view.userInteractionEnabled = YES;
        break;
    }
    case UIGestureRecognizerStateEnded:
    case UIGestureRecognizerStateCancelled: {
        // Get tracked drawer side
        MMDrawerSide drawerSide = self.startingDrawerSide;
        
        if (drawerSide == MMDrawerSideNone) {
            drawerSide = self.openSide;
            if (drawerSide == MMDrawerSideNone) {
                CGPoint velocity = [panGesture velocityInView:self.view];
                drawerSide = (velocity.x > 0) ? MMDrawerSideLeft : MMDrawerSideRight;
            }
        }
        
        MMDrawerOpenMode openMode = (drawerSide == MMDrawerSideLeft) ? 
                             self.leftDrawerOpenMode : 
                             self.rightDrawerOpenMode;

        if (openMode == MMDrawerOpenModeAboveContent) {
            // OVERLAY MODE
            // Get drawer view controller
            UIViewController *drawerVC = [self sideDrawerViewControllerForSide:drawerSide];
            if (!drawerVC) {
                self.startingDrawerSide = MMDrawerSideNone;
                self.startingPanRect = CGRectNull;
                self.view.userInteractionEnabled = YES;
                break;
            }
            
            // Get position and velocity
            CGFloat currentX = drawerVC.view.frame.origin.x;
            CGPoint velocity = [panGesture velocityInView:self.view];
            CGFloat maximumDrawerWidth = (drawerSide == MMDrawerSideLeft) ? 
                                    self.maximumLeftDrawerWidth : 
                                    self.maximumRightDrawerWidth;
            
            // Determine if drawer should open or close
            BOOL shouldOpen = NO;
            CGFloat screenWidth = self.view.bounds.size.width;
            
            if (drawerSide == MMDrawerSideLeft) {
                // Left drawer logic
                if (velocity.x > 500) shouldOpen = YES; // Fast right swipe
                else if (velocity.x < -500) shouldOpen = NO; // Fast left swipe
                else shouldOpen = (currentX > -maximumDrawerWidth/2.0); // Based on position
            } else { // MMDrawerSideRight
                // Right drawer logic
                if (velocity.x < -500) shouldOpen = YES; // Fast left swipe
                else if (velocity.x > 500) shouldOpen = NO; // Fast right swipe
                else shouldOpen = (currentX < screenWidth - maximumDrawerWidth/2.0); // Based on position
            }
            
            // Animate to final position
            [UIView animateWithDuration:0.25
                            animations:^{
                                CGRect frame = drawerVC.view.frame;
                                
                                if (shouldOpen) {
                                    // Open drawer
                                    if (drawerSide == MMDrawerSideLeft) {
                                        frame.origin.x = 0;
                                    } else { // MMDrawerSideRight
                                        frame.origin.x = screenWidth - maximumDrawerWidth;
                                    }
                                    
                                    // Show overlay
                                    self.centerContentOverlay.alpha = 0.5;
                                } else {
                                    // Close drawer
                                    if (drawerSide == MMDrawerSideLeft) {
                                        frame.origin.x = -maximumDrawerWidth;
                                    } else { // MMDrawerSideRight
                                        frame.origin.x = screenWidth;
                                    }
                                    
                                    // Hide overlay
                                    self.centerContentOverlay.alpha = 0.0;
                                }
                                
                                drawerVC.view.frame = frame;
                            } completion:^(BOOL finished) {
                                if (shouldOpen) {
                                    [self setOpenSide:drawerSide];
                                } else {
                                    [self setOpenSide:MMDrawerSideNone];
                                    [self.centerContentOverlay removeFromSuperview];
                                }
                                
                                self.startingDrawerSide = MMDrawerSideNone;
                                
                                if (self.gestureCompletion) {
                                    self.gestureCompletion(self, panGesture);
                                }
                            }];
        } else {
            // ORIGINAL PUSH MODE
            // Use original finishAnimationForPanGesture method
            CGPoint velocity = [panGesture velocityInView:self.childControllerContainerView];
            [self finishAnimationForPanGestureWithXVelocity:velocity.x
                                                completion:^(BOOL finished) {
                                                if (self.gestureCompletion) {
                                                    self.gestureCompletion(self, panGesture);
                                                }
                                                }];
        }
        
        // Reset tracking variables
        self.startingPanRect = CGRectNull;
        self.view.userInteractionEnabled = YES;
        break;
    }
    default:
        break;
    }
}

- (void)updatePanHandlersState {
    if (_leftSideEnabled == NO && _rightSideEnabled == NO) {
        if ([self hasPan]) {
            [self.view removeGestureRecognizer:_pan];
        }
    } else {
        if (![self hasPan]) {
            [self.view addGestureRecognizer:_pan];
        }
    }
}

- (void)setupCenterContentOverlay {
    if (!self.centerContentOverlay) {
        // Create overlay view if it doesn't exist
        self.centerContentOverlay = [[UIView alloc] initWithFrame:self.centerContainerView.bounds];
        self.centerContentOverlay.backgroundColor = [UIColor blackColor];
        self.centerContentOverlay.alpha = 0.0; // Start fully transparent
        self.centerContentOverlay.userInteractionEnabled = YES;
        
        // Add tap gesture to close drawer when tapping overlay
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] 
                                             initWithTarget:self 
                                             action:@selector(overlayTapped:)];
        [self.centerContentOverlay addGestureRecognizer:tapGesture];
    }
    
    // Update frame to match current center container bounds
    self.centerContentOverlay.frame = self.centerContainerView.bounds;
}

- (void)overlayTapped:(UITapGestureRecognizer *)tapGesture {
    // Close drawer when overlay is tapped
    [self closeDrawerAnimated:YES completion:nil];
}

#pragma mark - iOS 7 Status Bar Helpers
- (UIViewController *)childViewControllerForStatusBarStyle {
    return [self childViewControllerForSide:self.openSide];
}

- (UIViewController *)childViewControllerForStatusBarHidden {
    return [self childViewControllerForSide:self.openSide];
}

- (void)setNeedsStatusBarAppearanceUpdateIfSupported {
    if ([self respondsToSelector:@selector(setNeedsStatusBarAppearanceUpdate)]) {
        [self performSelector:@selector(setNeedsStatusBarAppearanceUpdate)];
    }
}

#pragma mark - iOS 8 Rotation Helpers
- (BOOL)needsManualForwardingOfRotationEvents {
    BOOL isIOS8 = (floor(NSFoundationVersionNumber) > NSFoundationVersionNumber_iOS_7_1);
    return !isIOS8;
}

#pragma mark - Animation helpers
- (void)finishAnimationForPanGestureWithXVelocity:(CGFloat)xVelocity
                                       completion:(void (^)(BOOL finished))completion {
    CGFloat currentOriginX = CGRectGetMinX(self.centerContainerView.frame);

    CGFloat animationVelocity = MAX(ABS(xVelocity), self.panVelocityXAnimationThreshold * 2);

    if (self.openSide == MMDrawerSideLeft) {
        CGFloat midPoint = self.maximumLeftDrawerWidth / 2.0;
        if (xVelocity > self.panVelocityXAnimationThreshold) {
            [self openDrawerSide:MMDrawerSideLeft
                        animated:YES
                        velocity:animationVelocity
                animationOptions:UIViewAnimationOptionCurveEaseOut
                      completion:completion];
        } else if (xVelocity < -self.panVelocityXAnimationThreshold) {
            [self closeDrawerAnimated:YES
                             velocity:animationVelocity
                     animationOptions:UIViewAnimationOptionCurveEaseOut
                           completion:completion];
        } else if (currentOriginX < midPoint) {
            [self closeDrawerAnimated:YES completion:completion];
        } else {
            [self openDrawerSide:MMDrawerSideLeft animated:YES completion:completion];
        }
    } else if (self.openSide == MMDrawerSideRight) {
        currentOriginX = CGRectGetMaxX(self.centerContainerView.frame);
        CGFloat midPoint = (CGRectGetWidth(self.childControllerContainerView.bounds) -
                            self.maximumRightDrawerWidth) +
                           (self.maximumRightDrawerWidth / 2.0);
        if (xVelocity > self.panVelocityXAnimationThreshold) {
            [self closeDrawerAnimated:YES
                             velocity:animationVelocity
                     animationOptions:UIViewAnimationOptionCurveEaseOut
                           completion:completion];
        } else if (xVelocity < -self.panVelocityXAnimationThreshold) {
            [self openDrawerSide:MMDrawerSideRight
                        animated:YES
                        velocity:animationVelocity
                animationOptions:UIViewAnimationOptionCurveEaseOut
                      completion:completion];
        } else if (currentOriginX > midPoint) {
            [self closeDrawerAnimated:YES completion:completion];
        } else {
            [self openDrawerSide:MMDrawerSideRight animated:YES completion:completion];
        }
    } else {
        if (completion) {
            completion(NO);
        }
    }
}

- (void)updateDrawerVisualStateForDrawerSide:(MMDrawerSide)drawerSide
                              percentVisible:(CGFloat)percentVisible {
    if (self.drawerVisualState) {
        self.drawerVisualState(self, drawerSide, percentVisible);
    } else if ([self shouldStretchForSide:drawerSide]) {
        [self applyOvershootScaleTransformForDrawerSide:drawerSide percentVisible:percentVisible];
    }
}

- (BOOL)shouldStretchForSide:(MMDrawerSide)drawerSide {
    switch (drawerSide) {
    case MMDrawerSideLeft:
        return self.shouldStretchLeftDrawer;
        break;
    case MMDrawerSideRight:
        return self.shouldStretchRightDrawer;
        break;
    default:
        return YES;
        break;
    }
}

- (void)side:(MMDrawerSide)drawerSide openMode:(MMDrawerOpenMode)openMode {
    if (drawerSide == MMDrawerSideLeft) {
        self.leftDrawerOpenMode = openMode;
    } else if (drawerSide == MMDrawerSideRight) {
        self.rightDrawerOpenMode = openMode;
    }
}

- (void)applyOvershootScaleTransformForDrawerSide:(MMDrawerSide)drawerSide
                                   percentVisible:(CGFloat)percentVisible {

    if (percentVisible >= 1.f) {
        CATransform3D transform = CATransform3DIdentity;
        UIViewController *sideDrawerViewController =
            [self sideDrawerViewControllerForSide:drawerSide];
        if (drawerSide == MMDrawerSideLeft) {
            transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
            transform = CATransform3DTranslate(
                transform, self.maximumLeftDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
        } else if (drawerSide == MMDrawerSideRight) {
            transform = CATransform3DMakeScale(percentVisible, 1.f, 1.f);
            transform = CATransform3DTranslate(
                transform, -self.maximumRightDrawerWidth * (percentVisible - 1.f) / 2, 0.f, 0.f);
        }
        sideDrawerViewController.view.layer.transform = transform;
    }
}

- (void)resetDrawerVisualStateForDrawerSide:(MMDrawerSide)drawerSide {
    UIViewController *sideDrawerViewController = [self sideDrawerViewControllerForSide:drawerSide];

    [sideDrawerViewController.view.layer setAnchorPoint:CGPointMake(0.5f, 0.5f)];
    [sideDrawerViewController.view.layer setTransform:CATransform3DIdentity];
    [sideDrawerViewController.view setAlpha:1.0];
}

- (CGFloat)roundedOriginXForDrawerConstriants:(CGFloat)originX {

    if (originX < -self.maximumRightDrawerWidth) {
        if (self.shouldStretchRightDrawer && self.rightDrawerViewController) {
            CGFloat maxOvershoot =
                (CGRectGetWidth(self.centerContainerView.frame) - self.maximumRightDrawerWidth) *
                MMDrawerOvershootPercentage;
            return originXForDrawerOriginAndTargetOriginOffset(
                originX, -self.maximumRightDrawerWidth, maxOvershoot);
        } else {
            return -self.maximumRightDrawerWidth;
        }
    } else if (originX > self.maximumLeftDrawerWidth) {
        if (self.shouldStretchLeftDrawer && self.leftDrawerViewController) {
            CGFloat maxOvershoot =
                (CGRectGetWidth(self.centerContainerView.frame) - self.maximumLeftDrawerWidth) *
                MMDrawerOvershootPercentage;
            return originXForDrawerOriginAndTargetOriginOffset(originX, self.maximumLeftDrawerWidth,
                                                               maxOvershoot);
        } else {
            return self.maximumLeftDrawerWidth;
        }
    }

    return originX;
}

static inline CGFloat originXForDrawerOriginAndTargetOriginOffset(CGFloat originX,
                                                                  CGFloat targetOffset,
                                                                  CGFloat maxOvershoot) {
    CGFloat delta = ABS(originX - targetOffset);
    CGFloat maxLinearPercentage = MMDrawerOvershootLinearRangePercentage;
    CGFloat nonLinearRange = maxOvershoot * maxLinearPercentage;
    CGFloat nonLinearScalingDelta = (delta - nonLinearRange);
    CGFloat overshoot = nonLinearRange + nonLinearScalingDelta * nonLinearRange /
                                             sqrt(pow(nonLinearScalingDelta, 2.f) + 15000);

    if (delta < nonLinearRange) {
        return originX;
    } else if (targetOffset < 0) {
        return targetOffset - round(overshoot);
    } else {
        return targetOffset + round(overshoot);
    }
}

#pragma mark - Helpers
- (void)setupGestureRecognizers {
    _pan = [[UIPanGestureRecognizer alloc] initWithTarget:self
                                                   action:@selector(panGestureCallback:)];
    [_pan setDelegate:self];
    [self.view addGestureRecognizer:_pan];

    UITapGestureRecognizer *tap =
        [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGestureCallback:)];
    [tap setDelegate:self];
    [self.view addGestureRecognizer:tap];
}

- (void)prepareToPresentDrawer:(MMDrawerSide)drawer animated:(BOOL)animated {
    MMDrawerSide drawerToHide = MMDrawerSideNone;
    if (drawer == MMDrawerSideLeft) {
        drawerToHide = MMDrawerSideRight;
    } else if (drawer == MMDrawerSideRight) {
        drawerToHide = MMDrawerSideLeft;
    }

    UIViewController *sideDrawerViewControllerToPresent =
        [self sideDrawerViewControllerForSide:drawer];
    UIViewController *sideDrawerViewControllerToHide =
        [self sideDrawerViewControllerForSide:drawerToHide];

    [self.childControllerContainerView sendSubviewToBack:sideDrawerViewControllerToHide.view];
    [sideDrawerViewControllerToHide.view setHidden:YES];
    [sideDrawerViewControllerToPresent.view setHidden:NO];
    [self resetDrawerVisualStateForDrawerSide:drawer];
    [sideDrawerViewControllerToPresent.view
        setFrame:sideDrawerViewControllerToPresent.mm_visibleDrawerFrame];
    [sideDrawerViewControllerToPresent
            .view setNeedsLayout]; // Added to make SafeAreaView in sideDrawerView work (#3418)
    [self updateDrawerVisualStateForDrawerSide:drawer percentVisible:0.0];
    [sideDrawerViewControllerToPresent beginAppearanceTransition:YES animated:animated];
}

- (void)updateShadowForCenterView {
    UIView *centerView = self.centerContainerView;
    if (self.showsShadow) {
        centerView.layer.masksToBounds = NO;
        centerView.layer.shadowRadius = self.shadowRadius;
        centerView.layer.shadowOpacity = self.shadowOpacity;
        centerView.layer.shadowOffset = self.shadowOffset;
        centerView.layer.shadowColor = [self.shadowColor CGColor];

        /** In the event this gets called a lot, we won't update the shadowPath
         unless it needs to be updated (like during rotation) */
        if (centerView.layer.shadowPath == NULL) {
            centerView.layer.shadowPath =
                [[UIBezierPath bezierPathWithRect:self.centerContainerView.bounds] CGPath];
        } else {
            CGRect currentPath = CGPathGetPathBoundingBox(centerView.layer.shadowPath);
            if (CGRectEqualToRect(currentPath, centerView.bounds) == NO) {
                centerView.layer.shadowPath =
                    [[UIBezierPath bezierPathWithRect:self.centerContainerView.bounds] CGPath];
            }
        }
    } else if (centerView.layer.shadowPath != NULL) {
        centerView.layer.shadowRadius = 0.f;
        centerView.layer.shadowOpacity = 0.f;
        centerView.layer.shadowOffset = CGSizeMake(0, -3);
        centerView.layer.shadowPath = NULL;
        centerView.layer.masksToBounds = YES;
    }
}

- (NSTimeInterval)animationDurationForAnimationDistance:(CGFloat)distance {
    CGFloat velocity = self.openSide == MMDrawerSideLeft ? self.animationVelocityLeft
                                                         : self.animationVelocityRight;
    NSTimeInterval duration = MAX(distance / velocity, MMDrawerMinimumAnimationDuration);
    return duration;
}

- (UIViewController *)sideDrawerViewControllerForSide:(MMDrawerSide)drawerSide {
    UIViewController *sideDrawerViewController = nil;
    if (drawerSide != MMDrawerSideNone) {
        sideDrawerViewController = [self childViewControllerForSide:drawerSide];
    }
    return sideDrawerViewController;
}

- (UIViewController *)childViewControllerForSide:(MMDrawerSide)drawerSide {
    UIViewController *childViewController = nil;
    switch (drawerSide) {
    case MMDrawerSideLeft:
        childViewController = self.leftDrawerViewController;
        break;
    case MMDrawerSideRight:
        childViewController = self.rightDrawerViewController;
        break;
    case MMDrawerSideNone:
        childViewController = self.centerViewController;
        break;
    }
    return childViewController;
}

#pragma mark - UIGestureRecognizerDelegate
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer
       shouldReceiveTouch:(UITouch *)touch {

    if (self.openSide == MMDrawerSideNone) {
        MMOpenDrawerGestureMode possibleOpenGestureModes =
            [self possibleOpenGestureModesForGestureRecognizer:gestureRecognizer withTouch:touch];
        return ((self.openDrawerGestureModeMask & possibleOpenGestureModes) > 0) &&
               [self shouldReceiveTouch:touch];
    } else {
        MMCloseDrawerGestureMode possibleCloseGestureModes =
            [self possibleCloseGestureModesForGestureRecognizer:gestureRecognizer withTouch:touch];
        return ((self.closeDrawerGestureModeMask & possibleCloseGestureModes) > 0) &&
               [self shouldReceiveTouch:touch];
    }
}

#pragma mark Gesture Recogizner Delegate Helpers
- (BOOL)shouldReceiveTouch:(UITouch *)touch {
    CGPoint point = [touch locationInView:self.childControllerContainerView];
    if ([self isPointContainedWithinLeftBezelRect:point] && self.leftDrawerViewController &&
        !self.leftSideEnabled) {
        return NO;
    } else if ([self isPointContainedWithinRightBezelRect:point] &&
               self.rightDrawerViewController && !self.rightSideEnabled) {
        return NO;
    }

    return YES;
}

- (MMCloseDrawerGestureMode)possibleCloseGestureModesForGestureRecognizer:
                                (UIGestureRecognizer *)gestureRecognizer
                                                                withTouch:(UITouch *)touch {
    CGPoint point = [touch locationInView:self.childControllerContainerView];
    MMCloseDrawerGestureMode possibleCloseGestureModes = MMCloseDrawerGestureModeNone;
    if ([gestureRecognizer isKindOfClass:[UITapGestureRecognizer class]]) {
        if ([self isPointContainedWithinNavigationRect:point]) {
            possibleCloseGestureModes |= MMCloseDrawerGestureModeTapNavigationBar;
        }
        if ([self isPointContainedWithinCenterViewContentRect:point]) {
            possibleCloseGestureModes |= MMCloseDrawerGestureModeTapCenterView;
        }
    } else if ([gestureRecognizer isKindOfClass:[UIPanGestureRecognizer class]]) {
        if ([self isPointContainedWithinNavigationRect:point]) {
            possibleCloseGestureModes |= MMCloseDrawerGestureModePanningNavigationBar;
        }
        if ([self isPointContainedWithinCenterViewContentRect:point]) {
            possibleCloseGestureModes |= MMCloseDrawerGestureModePanningCenterView;
        }
        if ([self isPointContainedWithinRightBezelRect:point] &&
            self.openSide == MMDrawerSideLeft) {
            possibleCloseGestureModes |= MMCloseDrawerGestureModeBezelPanningCenterView;
        }
        if ([self isPointContainedWithinLeftBezelRect:point] &&
            self.openSide == MMDrawerSideRight) {
            possibleCloseGestureModes |= MMCloseDrawerGestureModeBezelPanningCenterView;
        }
        if ([self isPointContainedWithinCenterViewContentRect:point] == NO &&
            [self isPointContainedWithinNavigationRect:point] == NO) {
            possibleCloseGestureModes |= MMCloseDrawerGestureModePanningDrawerView;
        }
    }
    if ((self.closeDrawerGestureModeMask & MMCloseDrawerGestureModeCustom) > 0 &&
        self.gestureShouldRecognizeTouch) {
        if (self.gestureShouldRecognizeTouch(self, gestureRecognizer, touch)) {
            possibleCloseGestureModes |= MMCloseDrawerGestureModeCustom;
        }
    }
    return possibleCloseGestureModes;
}

- (MMOpenDrawerGestureMode)possibleOpenGestureModesForGestureRecognizer:
                               (UIGestureRecognizer *)gestureRecognizer
                                                              withTouch:(UITouch *)touch {
    CGPoint point = [touch locationInView:self.childControllerContainerView];
    MMOpenDrawerGestureMode possibleOpenGestureModes = MMOpenDrawerGestureModeNone;
    if ([gestureRecognizer isKindOfClass:[UIPanGestureRecognizer class]]) {
        if ([self isPointContainedWithinNavigationRect:point]) {
            possibleOpenGestureModes |= MMOpenDrawerGestureModePanningNavigationBar;
        }
        if ([self isPointContainedWithinCenterViewContentRect:point]) {
            possibleOpenGestureModes |= MMOpenDrawerGestureModePanningCenterView;
        }
        if ([self isPointContainedWithinLeftBezelRect:point] && self.leftDrawerViewController) {
            possibleOpenGestureModes |= MMOpenDrawerGestureModeBezelPanningCenterView;
        }
        if ([self isPointContainedWithinRightBezelRect:point] && self.rightDrawerViewController) {
            possibleOpenGestureModes |= MMOpenDrawerGestureModeBezelPanningCenterView;
        }
    }
    if ((self.openDrawerGestureModeMask & MMOpenDrawerGestureModeCustom) > 0 &&
        self.gestureShouldRecognizeTouch) {
        if (self.gestureShouldRecognizeTouch(self, gestureRecognizer, touch)) {
            possibleOpenGestureModes |= MMOpenDrawerGestureModeCustom;
        }
    }
    return possibleOpenGestureModes;
}

- (BOOL)isPointContainedWithinNavigationRect:(CGPoint)point {
    CGRect navigationBarRect = CGRectNull;
    if ([self.centerViewController isKindOfClass:[UINavigationController class]]) {
        UINavigationBar *navBar =
            [(UINavigationController *)self.centerViewController navigationBar];
        navigationBarRect = [navBar convertRect:navBar.bounds
                                         toView:self.childControllerContainerView];
        navigationBarRect =
            CGRectIntersection(navigationBarRect, self.childControllerContainerView.bounds);
    }
    return CGRectContainsPoint(navigationBarRect, point);
}

- (BOOL)isPointContainedWithinCenterViewContentRect:(CGPoint)point {
    CGRect centerViewContentRect = self.centerContainerView.frame;
    centerViewContentRect =
        CGRectIntersection(centerViewContentRect, self.childControllerContainerView.bounds);
    return (CGRectContainsPoint(centerViewContentRect, point) &&
            [self isPointContainedWithinNavigationRect:point] == NO);
}

- (BOOL)isPointContainedWithinLeftBezelRect:(CGPoint)point {
    CGRect leftBezelRect = CGRectNull;
    CGRect tempRect;
    CGRectDivide(self.childControllerContainerView.bounds, &leftBezelRect, &tempRect,
                 self.bezelPanningCenterViewRange, CGRectMinXEdge);
    return (CGRectContainsPoint(leftBezelRect, point) &&
            [self isPointContainedWithinCenterViewContentRect:point]);
}

- (BOOL)isPointContainedWithinRightBezelRect:(CGPoint)point {
    CGRect rightBezelRect = CGRectNull;
    CGRect tempRect;
    CGRectDivide(self.childControllerContainerView.bounds, &rightBezelRect, &tempRect,
                 self.bezelPanningCenterViewRange, CGRectMaxXEdge);

    return (CGRectContainsPoint(rightBezelRect, point) &&
            [self isPointContainedWithinCenterViewContentRect:point]);
}
@end
