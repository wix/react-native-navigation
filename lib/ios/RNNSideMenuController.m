#import "RNNSideMenuController.h"
#import "MMDrawerVisualState.h"

@interface RNNSideMenuController ()

@property(nonatomic, strong) NSArray *childViewControllers;
@property(readwrite) RNNSideMenuChildVC *center;
@property(readwrite) RNNSideMenuChildVC *left;
@property(readwrite) RNNSideMenuChildVC *right;

@end

@implementation RNNSideMenuController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
              childViewControllers:(NSArray *)childViewControllers
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                         presenter:(RNNBasePresenter *)presenter
                      eventEmitter:(RNNEventEmitter *)eventEmitter {
    self = [super init];

    self.childViewControllers = childViewControllers;

    self.presenter = presenter;
    [self.presenter bindViewController:self];

    self.defaultOptions = defaultOptions;
    self.options = options;

    self.layoutInfo = layoutInfo;

    self.closeDrawerGestureModeMask = MMCloseDrawerGestureModeAll;

    [self.presenter applyOptionsOnInit:self.resolveOptions];

    // Fixes #3697
    [self setExtendedLayoutIncludesOpaqueBars:YES];
    self.edgesForExtendedLayout |= UIRectEdgeBottom;

    return self;
}

- (void)loadView {
    [super loadView];
    [self setCenterViewController:self.center];
    [self setLeftDrawerViewController:self.left];
    [self setRightDrawerViewController:self.right];
}

- (void)render {
    [super render];
    UIViewController *currentChild = self.getCurrentChild;
    for (UIViewController *vc in self.childViewControllers) {
        if (vc != currentChild) {
            [vc render];
        }
    }
}

- (void)setAnimationType:(NSString *)animationType {
    MMDrawerControllerDrawerVisualStateBlock animationTypeStateBlock = nil;
    if ([animationType isEqualToString:@"door"])
        animationTypeStateBlock = [MMDrawerVisualState swingingDoorVisualStateBlock];
    else if ([animationType isEqualToString:@"parallax"])
        animationTypeStateBlock =
            [MMDrawerVisualState parallaxVisualStateBlockWithParallaxFactor:2.0];
    else if ([animationType isEqualToString:@"slide"])
        animationTypeStateBlock = [MMDrawerVisualState slideVisualStateBlock];
    else if ([animationType isEqualToString:@"slide-and-scale"])
        animationTypeStateBlock = [MMDrawerVisualState slideAndScaleVisualStateBlock];

    if (animationTypeStateBlock) {
        [self setDrawerVisualStateBlock:animationTypeStateBlock];
    }
}

- (void)side:(MMDrawerSide)side width:(double)width {
    switch (side) {
    case MMDrawerSideRight:
        self.maximumRightDrawerWidth = width;
        [self.right setWidth:width];
        break;
    case MMDrawerSideLeft:
        self.maximumLeftDrawerWidth = width;
        [self.left setWidth:width];
    default:
        break;
    }
}

- (void)side:(MMDrawerSide)side visible:(BOOL)visible {
    if (visible) {
        [self showSideMenu:side animated:YES];
    } else {
        [self hideSideMenu:side animated:YES];
    }
}

- (void)showSideMenu:(MMDrawerSide)side animated:(BOOL)animated {
    [self openDrawerSide:side animated:animated completion:nil];
}

- (void)hideSideMenu:(MMDrawerSide)side animated:(BOOL)animated {
    [self closeDrawerAnimated:animated completion:nil];
}

- (void)side:(MMDrawerSide)side enabled:(BOOL)enabled {
    switch (side) {
    case MMDrawerSideRight:
        self.rightSideEnabled = enabled;
        break;
    case MMDrawerSideLeft:
        self.leftSideEnabled = enabled;
    default:
        break;
    }
}

- (void)setChildViewControllers:(NSArray *)childViewControllers {
    _childViewControllers = childViewControllers;
    for (id controller in childViewControllers) {
        if ([controller isKindOfClass:[RNNSideMenuChildVC class]]) {
            RNNSideMenuChildVC *child = (RNNSideMenuChildVC *)controller;

            if (child.type == RNNSideMenuChildTypeCenter) {
                self.center = child;
            } else if (child.type == RNNSideMenuChildTypeLeft) {
                self.left = child;
            } else if (child.type == RNNSideMenuChildTypeRight) {
                self.right = child;
            }
        }

        else {
            @throw
                [NSException exceptionWithName:@"UnknownSideMenuControllerType"
                                        reason:[@"Unknown side menu type "
                                                   stringByAppendingString:[controller description]]
                                      userInfo:nil];
        }
    }
}

- (UIViewController<RNNLayoutProtocol> *)getCurrentChild {
    return self.openedViewController;
}

- (UIViewController *)openedViewController {
    switch (self.openSide) {
    case MMDrawerSideNone:
        return self.center;
    case MMDrawerSideLeft:
        return self.left;
    case MMDrawerSideRight:
        return self.right;
    default:
        return self.center;
    }
}

- (RNNNavigationOptions *)resolveOptions {
    RNNNavigationOptions *options = super.resolveOptions;
    if (self.openedViewController != self.center) {
        [options.sideMenu mergeOptions:self.center.resolveOptions.sideMenu];
    }
    return options;
}

#pragma mark - UIViewController overrides

- (UIStatusBarStyle)preferredStatusBarStyle {
    return [self.presenter getStatusBarStyle];
}

- (BOOL)prefersStatusBarHidden {
    return [self.presenter getStatusBarVisibility];
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return [self.presenter getOrientation];
}

- (BOOL)hidesBottomBarWhenPushed {
    return [self.presenter hidesBottomBarWhenPushed];
}

@end
