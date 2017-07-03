//
//  KYDrawerController.m
//  KYDrawerController
//
//  Created by Yifei Zhou on 1/8/16.
//  Copyright © 2016 Yifei Zhou. All rights reserved.
//

#import "KYDrawerController.h"

static CGFloat const kContainerViewMaxAlpha = 0.2;
static NSTimeInterval const kDrawerAnimationDuration = 0.25;

@interface KYDrawerController () <UIGestureRecognizerDelegate>

@property (strong, nonatomic) NSLayoutConstraint *drawerConstraint;

@property (strong, nonatomic) NSLayoutConstraint *drawerWidthConstraint;

@property (assign, nonatomic) CGPoint panStartLocation;

@property (assign, nonatomic) CGFloat panDelta;

@property (strong, nonatomic) UIView *containerView;

/// Returns `true` if `beginAppearanceTransition()` has been called with `true` as the first parameter, and `false`
/// if the first parameter is `false`. Returns `nil` if appearance transition is not in progress.
@property (strong, nonatomic) NSNumber *isAppearing;

@property (readwrite, strong, nonatomic) UIScreenEdgePanGestureRecognizer *screenEdgePanGesture;

@property (readwrite, strong, nonatomic) UIPanGestureRecognizer *panGesture;

@end

@implementation KYDrawerController

@synthesize screenEdgePanGesture = _screenEdgePanGesture;
@synthesize panGesture = _panGesture;
@synthesize containerViewTapGesture = _containerViewTapGesture;

- (void)awakeFromNib
{
    [super awakeFromNib];
    [self _commonInit];
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        [self _commonInit];
    }
    return self;
}

- (instancetype)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        [self _commonInit];
    }
    return self;
}

- (instancetype)initWithDrawerDirection:(KYDrawerControllerDrawerDirection)drawerDirection drawerWidth:(CGFloat)drawerWidth
{
    self = [super init];
    if (self) {
        [self _commonInit];
        self.drawerDirection = drawerDirection;
        self.drawerWidth = drawerWidth;
    }
    return self;
}

- (void)_commonInit
{
    _containerViewMaxAlpha = kContainerViewMaxAlpha;
    _drawerAnimationDuration = kDrawerAnimationDuration;
    
    _drawerWidth = 280.0f;
    _screenEdgePanGestreEnabled = YES;
    _drawerDirection = KYDrawerControllerDrawerDirectionLeft;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    NSDictionary *viewDictionary = @{ @"_containerView" : self.containerView };

    [self.view addGestureRecognizer:self.screenEdgePanGesture];
    [self.view addGestureRecognizer:self.panGesture];
    [self.view addSubview:self.containerView];
    [self.view
        addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[_containerView]-0-|" options:kNilOptions metrics:nil views:viewDictionary]];
    [self.view
        addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[_containerView]-0-|" options:kNilOptions metrics:nil views:viewDictionary]];

    self.containerView.hidden = YES;
    
    if (self.mainSegueIdentifier) {
        [self performSegueWithIdentifier:self.mainSegueIdentifier sender:self];
    }
    
    if (self.drawerSegueIdentifier) {
        [self performSegueWithIdentifier:self.drawerSegueIdentifier sender:self];
    }
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.displayingViewController beginAppearanceTransition:YES animated:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.displayingViewController endAppearanceTransition];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.displayingViewController beginAppearanceTransition:NO animated:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [self.displayingViewController endAppearanceTransition];
}

- (BOOL)shouldAutomaticallyForwardAppearanceMethods
{
    return NO;
}

- (void)setDrawerState:(KYDrawerControllerDrawerState)drawerState animated:(BOOL)animated
{
    self.containerView.hidden = NO;

    NSTimeInterval duration = animated ? self.drawerAnimationDuration : 0;
    
    BOOL isAppearing = drawerState == KYDrawerControllerDrawerStateOpened;
    if (!_isAppearing || [_isAppearing boolValue] != isAppearing) {
        _isAppearing = @(isAppearing);
        [self.drawerViewController beginAppearanceTransition:isAppearing animated:animated];
        [self.mainViewController beginAppearanceTransition:!isAppearing animated:animated];
    }

    [UIView animateWithDuration:duration
        delay:0
        options:UIViewAnimationOptionCurveEaseOut
        animations:^{
          if (drawerState == KYDrawerControllerDrawerStateClosed) {
              self.drawerConstraint.constant = 0;
              self.containerView.backgroundColor = [UIColor colorWithWhite:0 alpha:0];
          }
          else if (drawerState == KYDrawerControllerDrawerStateOpened) {
              CGFloat constant;
              if (self.drawerDirection == KYDrawerControllerDrawerDirectionLeft) {
                  constant = self.drawerWidth;
              }
              else {
                  constant = -self.drawerWidth;
              }
              self.drawerConstraint.constant = constant;
              self.containerView.backgroundColor = [UIColor colorWithWhite:0 alpha:self.containerViewMaxAlpha];
          }
          [self.containerView layoutIfNeeded];
        }
        completion:^(BOOL finished) {
          if (drawerState == KYDrawerControllerDrawerStateClosed) {
              self.containerView.hidden = YES;
          }
          [self.drawerViewController endAppearanceTransition];
          [self.mainViewController endAppearanceTransition];
          self.isAppearing = nil;
          if ([self.delegate respondsToSelector:@selector(drawerController:stateDidChange:)]) {
              [self.delegate drawerController:self stateDidChange:drawerState];
          }
        }];
}

#pragma mark - Actions

- (IBAction)didTapContainerView:(id)sender
{
    [self setDrawerState:KYDrawerControllerDrawerStateClosed animated:YES];
}

- (IBAction)handlePanGesture:(id)sender
{
    self.containerView.hidden = NO;

    if (![sender isKindOfClass:[UIGestureRecognizer class]]) {
        return;
    }

    UIGestureRecognizer *gesture = sender;
    if (gesture.state == UIGestureRecognizerStateBegan) {
        self.panStartLocation = [gesture locationInView:self.view];
    }

    CGFloat delta = [gesture locationInView:self.view].x - self.panStartLocation.x;
    CGFloat constant;
    CGFloat backGroundAlpha;
    KYDrawerControllerDrawerState drawerState = KYDrawerControllerDrawerStateOpened;

    if (self.drawerDirection == KYDrawerControllerDrawerDirectionLeft) {
        drawerState = self.panDelta <= 0 ? KYDrawerControllerDrawerStateClosed : KYDrawerControllerDrawerStateOpened;
        constant = fmin(self.drawerConstraint.constant + delta, self.drawerWidth);
        backGroundAlpha = fmin(self.containerViewMaxAlpha, self.containerViewMaxAlpha * fabs(constant) / self.drawerWidth);
    }
    else {
        drawerState = self.panDelta >= 0 ? KYDrawerControllerDrawerStateClosed : KYDrawerControllerDrawerStateOpened;
        constant = fmax(self.drawerConstraint.constant + delta, -self.drawerWidth);
        backGroundAlpha = fmin(self.containerViewMaxAlpha, self.containerViewMaxAlpha * fabs(constant) / self.drawerWidth);
    }

    self.drawerConstraint.constant = constant;
    self.containerView.backgroundColor = [UIColor colorWithWhite:0 alpha:backGroundAlpha];

    if (gesture.state == UIGestureRecognizerStateChanged) {
        BOOL isAppearing = drawerState != KYDrawerControllerDrawerStateOpened;
        if (_isAppearing == nil) {
            _isAppearing = @(isAppearing);
            [self.drawerViewController beginAppearanceTransition:isAppearing animated:YES];
            [self.mainViewController beginAppearanceTransition:!isAppearing animated:YES];
        }
        
        self.panStartLocation = [gesture locationInView:self.view];
        self.panDelta = delta;
    }
    else if (gesture.state == UIGestureRecognizerStateEnded || gesture.state == UIGestureRecognizerStateCancelled) {
        [self setDrawerState:drawerState animated:YES];
    }
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if (gestureRecognizer == self.panGesture) {
        return self.drawerState == KYDrawerControllerDrawerStateOpened;
    }
    else if (gestureRecognizer == self.screenEdgePanGesture) {
        return self.screenEdgePanGestreEnabled ? (self.drawerState == KYDrawerControllerDrawerStateClosed) : NO;
    }
    else {
        return touch.view == gestureRecognizer.view;
    }
}

#pragma mark - Getters & Setters

- (UIViewController *)displayingViewController
{
    switch (self.drawerState) {
        case KYDrawerControllerDrawerStateClosed:
            return self.mainViewController;
        case KYDrawerControllerDrawerStateOpened:
            return self.drawerViewController;
        default:
            return nil;
    }
}

- (void)setMainViewController:(UIViewController *)mainViewController
{
    UIViewController *oldController = _mainViewController;

    {
        [oldController willMoveToParentViewController:nil];
        [oldController.view removeFromSuperview];
        [oldController removeFromParentViewController];
    }

    _mainViewController = mainViewController;

    if (!_mainViewController) {
        return;
    }

    [self addChildViewController:_mainViewController];

    _mainViewController.view.translatesAutoresizingMaskIntoConstraints = NO;
    [self.view insertSubview:_mainViewController.view atIndex:0];

    NSDictionary *viewDictionary = @{ @"mainView" : _mainViewController.view };

    [self.view addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[mainView]-0-|" options:kNilOptions metrics:nil views:viewDictionary]];

    [self.view addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[mainView]-0-|" options:kNilOptions metrics:nil views:viewDictionary]];

    [_mainViewController didMoveToParentViewController:self];
}

- (void)setDrawerDirection:(KYDrawerControllerDrawerDirection)drawerDirection
{
    _drawerDirection = drawerDirection;

    if (_drawerDirection == KYDrawerControllerDrawerDirectionLeft) {
        self.screenEdgePanGesture.edges = UIRectEdgeLeft;
    }
    else if (_drawerDirection == KYDrawerControllerDrawerDirectionRight) {
        self.screenEdgePanGesture.edges = UIRectEdgeRight;
    }

    self.drawerViewController = _drawerViewController;
}

- (void)setDrawerViewController:(UIViewController *)drawerViewController
{
    UIViewController *oldController = _drawerViewController;

    {
        [oldController willMoveToParentViewController:nil];
        [oldController.view removeFromSuperview];
        [oldController removeFromParentViewController];
    }

    _drawerViewController = drawerViewController;

    if (!_drawerViewController) {
        return;
    }

    [self addChildViewController:_drawerViewController];
    
    _drawerViewController.view.layer.shadowColor = [UIColor blackColor].CGColor;
    _drawerViewController.view.layer.shadowOpacity = 0.4f;
    _drawerViewController.view.layer.shadowRadius = 5.0f;
    _drawerViewController.view.translatesAutoresizingMaskIntoConstraints = NO;
    [self.containerView addSubview:_drawerViewController.view];

    NSLayoutAttribute itemAttribute;
    NSLayoutAttribute toItemAttribute;

    if (self.drawerDirection == KYDrawerControllerDrawerDirectionLeft) {
        itemAttribute = NSLayoutAttributeRight;
        toItemAttribute = NSLayoutAttributeLeft;
    }
    else {
        itemAttribute = NSLayoutAttributeLeft;
        toItemAttribute = NSLayoutAttributeRight;
    }
   
    NSDictionary *viewDictionary = @{ @"drawerView" : _drawerViewController.view };

    self.drawerWidthConstraint = [NSLayoutConstraint constraintWithItem:_drawerViewController.view
                                                              attribute:NSLayoutAttributeWidth
                                                              relatedBy:NSLayoutRelationEqual
                                                                 toItem:nil
                                                              attribute:NSLayoutAttributeWidth
                                                             multiplier:1
                                                               constant:self.drawerWidth];

    [_drawerViewController.view addConstraint:self.drawerWidthConstraint];

    self.drawerConstraint = [NSLayoutConstraint constraintWithItem:_drawerViewController.view
                                                         attribute:itemAttribute
                                                         relatedBy:NSLayoutRelationEqual
                                                            toItem:self.containerView
                                                         attribute:toItemAttribute
                                                        multiplier:1
                                                          constant:0];

    [self.containerView addConstraint:_drawerConstraint];
    [self.containerView
        addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[drawerView]-0-|" options:kNilOptions metrics:nil views:viewDictionary]];
    [self.containerView updateConstraints];

    [_drawerViewController updateViewConstraints];
    [_drawerViewController didMoveToParentViewController:self];
}

- (KYDrawerControllerDrawerState)drawerState
{
    return self.containerView.hidden ? KYDrawerControllerDrawerStateClosed : KYDrawerControllerDrawerStateOpened;
}

- (void)setDrawerState:(KYDrawerControllerDrawerState)drawerState
{
    [self setDrawerState:drawerState animated:NO];
}

- (void)setDrawerWidth:(CGFloat)drawerWidth
{
    _drawerWidth = drawerWidth;
    self.drawerWidthConstraint.constant = drawerWidth;
}

#pragma mark - Lazy initializer

- (UIView *)containerView
{
    if (!_containerView) {
        UIView *view = [[UIView alloc] initWithFrame:self.view.frame];

        view.translatesAutoresizingMaskIntoConstraints = NO;
        view.backgroundColor = [UIColor clearColor];
        [view addGestureRecognizer:self.containerViewTapGesture];

        _containerView = view;
    }
    return _containerView;
}

- (UITapGestureRecognizer *)containerViewTapGesture
{
    if (!_containerViewTapGesture) {
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(didTapContainerView:)];
        tapGesture.delegate = self;
        _containerViewTapGesture = tapGesture;
    }
    return _containerViewTapGesture;
}

- (void)setContainerViewTapGesture:(UITapGestureRecognizer *)containerViewTapGesture
{
    _containerViewTapGesture != nil ? ([self.containerView removeGestureRecognizer:_containerViewTapGesture]) : nil;
    _containerViewTapGesture = containerViewTapGesture;
    [self.containerView addGestureRecognizer:_containerViewTapGesture];
}

- (UIScreenEdgePanGestureRecognizer *)screenEdgePanGesture
{
    if (!_screenEdgePanGesture) {
        UIScreenEdgePanGestureRecognizer *gesture = [[UIScreenEdgePanGestureRecognizer alloc] initWithTarget:self action:@selector(handlePanGesture:)];
        if (self.drawerDirection == KYDrawerControllerDrawerDirectionLeft) {
            gesture.edges = UIRectEdgeLeft;
        }
        else if (self.drawerDirection == KYDrawerControllerDrawerDirectionRight) {
            gesture.edges = UIRectEdgeRight;
        }
        gesture.delegate = self;

        _screenEdgePanGesture = gesture;
    }
    return _screenEdgePanGesture;
}

- (UIPanGestureRecognizer *)panGesture
{
    if (!_panGesture) {
        UIPanGestureRecognizer *gesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(handlePanGesture:)];
        gesture.delegate = self;

        _panGesture = gesture;
    }
    return _panGesture;
}

@end
