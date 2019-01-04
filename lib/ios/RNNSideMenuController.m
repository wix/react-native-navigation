#import "RNNSideMenuController.h"
#import "RNNSideMenuChildVC.h"
#import "MMDrawerController.h"
#import "MMDrawerVisualState.h"

@interface RNNSideMenuController ()

@property (readwrite) RNNSideMenuChildVC *center;
@property (readwrite) RNNSideMenuChildVC *left;
@property (readwrite) RNNSideMenuChildVC *right;

@end

@implementation RNNSideMenuController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNViewControllerPresenter *)presenter {
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

- (void)willMoveToParentViewController:(UIViewController *)parent {
	if (parent) {
		[_presenter applyOptionsOnWillMoveToParentViewController:self.resolveOptions];
	}
}

- (void)onChildWillAppear {
	[_presenter applyOptions:self.resolveOptions];
	[((UIViewController<RNNParentProtocol> *)self.parentViewController) onChildWillAppear];
}

- (RNNNavigationOptions *)resolveOptions {
	return [(RNNNavigationOptions *)[self.getCurrentChild.resolveOptions.copy mergeOptions:self.options] withDefault:self.defaultOptions];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[_presenter mergeOptions:options currentOptions:self.options defaultOptions:self.defaultOptions];
	[((UIViewController<RNNLayoutProtocol> *)self.parentViewController) mergeOptions:options];
}

- (void)overrideOptions:(RNNNavigationOptions *)options {
	[self.options overrideOptions:options];
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
		}
		
		else {
			@throw [NSException exceptionWithName:@"UnknownSideMenuControllerType" reason:[@"Unknown side menu type " stringByAppendingString:[controller description]] userInfo:nil];
		}
	}
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.openedViewController.preferredStatusBarStyle;
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
			break;
	}
}

- (UIViewController<RNNLayoutProtocol> *)getCurrentChild {
	return self.center;
}

- (UIViewController<RNNLeafProtocol> *)getCurrentLeaf {
	return [[self getCurrentChild] getCurrentLeaf];
}

@end
