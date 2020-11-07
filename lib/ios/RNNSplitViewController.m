#import "RNNSplitViewController.h"
#import "UIViewController+LayoutProtocol.h"

@implementation RNNSplitViewController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                         presenter:(RNNBasePresenter *)presenter
                      eventEmitter:(RNNEventEmitter *)eventEmitter
              childViewControllers:(NSArray *)childViewControllers {
    if (@available(iOS 14.0, *)) {
        NSString* style = options.splitView.style;
        if ([style isEqualToString:@"tripleColumn"]) {
            self = [self initWithStyle:UISplitViewControllerStyleTripleColumn];
        } else if ([style isEqualToString:@"doubleColumn"]) {
            self = [self initWithStyle:UISplitViewControllerStyleDoubleColumn];
        } else {
            self = [self init];
        }
    } else {
        // Fallback on earlier versions
        self = [self init];
    }
    self.options = options;
    self.defaultOptions = defaultOptions;
    self.layoutInfo = layoutInfo;
    self.creator = creator;
    self.eventEmitter = eventEmitter;
    self.presenter = presenter;
    [self loadChildren:childViewControllers];
    [self.presenter bindViewController:self];
    self.extendedLayoutIncludesOpaqueBars = YES;
    [self.presenter applyOptionsOnInit:self.resolveOptions];

    return self;
}

- (void)setViewControllers:(NSArray<__kindof UIViewController *> *)viewControllers {
    [super setViewControllers:viewControllers];
    UIViewController<UISplitViewControllerDelegate> *primaryViewController = viewControllers[0];
    self.delegate = primaryViewController;
}

- (UIViewController *)getCurrentChild {
    return self.viewControllers[0];
}

#pragma mark - UIViewController overrides

- (void)willMoveToParentViewController:(UIViewController *)parent {
    [self.presenter willMoveToParentViewController:parent];
}

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
