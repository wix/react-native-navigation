#import "RNNSideMenuController.h"
#import "MMDrawerVisualState.h"

@interface RNNSideMenuController ()

@property (readwrite) RNNSideMenuChildVC *center;
@property (readwrite) RNNSideMenuChildVC *left;
@property (readwrite) RNNSideMenuChildVC *right;

@end

@implementation RNNSideMenuController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo creator:(id<RNNComponentViewCreator>)creator childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNBasePresenter *)presenter eventEmitter:(RNNEventEmitter *)eventEmitter {
	[self setControllers:childViewControllers];
	self = [super initWithCenterViewController:self.center leftDrawerViewController:self.left rightDrawerViewController:self.right];
	
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

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	[self.presenter setDefaultOptions:defaultOptions];
}

- (void)setAnimationType:(NSString *)animationType {
	MMDrawerControllerDrawerVisualStateBlock animationTypeStateBlock = nil;
	if ([animationType isEqualToString:@"door"]) animationTypeStateBlock = [MMDrawerVisualState swingingDoorVisualStateBlock];
	else if ([animationType isEqualToString:@"parallax"]) animationTypeStateBlock = [MMDrawerVisualState parallaxVisualStateBlockWithParallaxFactor:2.0];
	else if ([animationType isEqualToString:@"slide"]) animationTypeStateBlock = [MMDrawerVisualState slideVisualStateBlock];
	else if ([animationType isEqualToString:@"slide-and-scale"]) animationTypeStateBlock = [MMDrawerVisualState slideAndScaleVisualStateBlock];
	
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

-(void)showSideMenu:(MMDrawerSide)side animated:(BOOL)animated {
	[self openDrawerSide:side animated:animated completion:nil];
}

-(void)hideSideMenu:(MMDrawerSide)side animated:(BOOL)animated {
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

-(void)setControllers:(NSArray*)controllers {
	for (id controller in controllers) {
		if ([controller isKindOfClass:[RNNSideMenuChildVC class]]) {
			RNNSideMenuChildVC *child = (RNNSideMenuChildVC*)controller;

			if (child.type == RNNSideMenuChildTypeCenter) {
				self.center = child;
			}
			else if(child.type == RNNSideMenuChildTypeLeft) {
				self.left = child;
			}
			else if(child.type == RNNSideMenuChildTypeRight) {
				self.right = child;
			}

			[self addChildViewController:child];
		}

		else {
			@throw [NSException exceptionWithName:@"UnknownSideMenuControllerType" reason:[@"Unknown side menu type " stringByAppendingString:[controller description]] userInfo:nil];
		}
	}
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.openedViewController.preferredStatusBarStyle;
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
    RNNNavigationOptions * options = super.resolveOptions;
    if (self.openedViewController != self.center) {
        [options.sideMenu mergeOptions:self.center.resolveOptions.sideMenu];
    }
    return options;
}

- (CGFloat)getTopBarHeight {
    for(UIViewController * child in [self childViewControllers]) {
        CGFloat childTopBarHeight = [child getTopBarHeight];
        if (childTopBarHeight > 0) return childTopBarHeight;
    }
    return [super getTopBarHeight];
}

@end
