
#import "RNNNavigationController.h"
#import "RNNModalAnimation.h"
#import "RNNRootViewController.h"

const NSInteger TOP_BAR_TRANSPARENT_TAG = 78264803;

@interface RNNNavigationController()

@property (nonatomic, strong) NSMutableDictionary* originalTopBarImages;

@end

@implementation RNNNavigationController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNNavigationControllerPresenter *)presenter {
	self = [super init];

	self.presenter = presenter;
	[self.presenter bindViewController:self];
	
	self.defaultOptions = defaultOptions;
	self.options = options;
	
	self.layoutInfo = layoutInfo;
	
	[self setViewControllers:childViewControllers];
	
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

- (UITabBarItem *)tabBarItem {
	return self.viewControllers.firstObject.tabBarItem;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.getCurrentChild.supportedInterfaceOrientations;
}

- (UINavigationController *)navigationController {
	return self;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.getCurrentChild.preferredStatusBarStyle;
}

- (UIModalPresentationStyle)modalPresentationStyle {
	return self.getCurrentChild.modalPresentationStyle;
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated {
	if (self.viewControllers.count > 1) {
		UIViewController *controller = self.viewControllers[self.viewControllers.count - 2];
		if ([controller isKindOfClass:[RNNRootViewController class]]) {
			RNNRootViewController *rnnController = (RNNRootViewController *)controller;
			[self setTopBarBackgroundColor:[rnnController.resolveOptions.topBar.background.color getWithDefaultValue:nil]];
		}
	}
	
	return [super popViewControllerAnimated:animated];
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getCurrentChild.resolveOptions.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getCurrentChild.resolveOptions.animations.dismissModal isDismiss:YES];
}

- (UIViewController *)getCurrentChild {
	return ((UIViewController<RNNParentProtocol>*)self.topViewController);
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

- (void)setTopBarBackgroundColor:(UIColor *)backgroundColor {
	if (backgroundColor) {
		CGFloat bgColorAlpha = CGColorGetAlpha(backgroundColor.CGColor);
		
		if (bgColorAlpha == 0.0) {
			if (![self.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG]){
				[self storeOriginalTopBarImages:self];
				UIView *transparentView = [[UIView alloc] initWithFrame:CGRectZero];
				transparentView.backgroundColor = [UIColor clearColor];
				transparentView.tag = TOP_BAR_TRANSPARENT_TAG;
				[self.navigationBar insertSubview:transparentView atIndex:0];
			}
			self.navigationBar.translucent = YES;
			[self.navigationBar setBackgroundColor:[UIColor clearColor]];
			self.navigationBar.shadowImage = [UIImage new];
			[self.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
		} else {
			self.navigationBar.barTintColor = backgroundColor;
			UIView *transparentView = [self.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
			if (transparentView){
				[transparentView removeFromSuperview];
				[self.navigationBar setBackgroundImage:self.originalTopBarImages[@"backgroundImage"] forBarMetrics:UIBarMetricsDefault];
				self.navigationBar.shadowImage = self.originalTopBarImages[@"shadowImage"];
				self.originalTopBarImages = nil;
			}
		}
	} else {
		UIView *transparentView = [self.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
		if (transparentView){
			[transparentView removeFromSuperview];
			[self.navigationBar setBackgroundImage:self.originalTopBarImages[@"backgroundImage"] ? self.originalTopBarImages[@"backgroundImage"] : [self.navigationBar backgroundImageForBarMetrics:UIBarMetricsDefault] forBarMetrics:UIBarMetricsDefault];
			self.navigationBar.shadowImage = self.originalTopBarImages[@"shadowImage"] ? self.originalTopBarImages[@"shadowImage"] : self.navigationBar.shadowImage;
			self.originalTopBarImages = nil;
		}
		
		self.navigationBar.barTintColor = nil;
	}
}

- (void)storeOriginalTopBarImages:(UINavigationController *)navigationController {
	NSMutableDictionary *originalTopBarImages = [@{} mutableCopy];
	UIImage *bgImage = [navigationController.navigationBar backgroundImageForBarMetrics:UIBarMetricsDefault];
	if (bgImage != nil) {
		originalTopBarImages[@"backgroundImage"] = bgImage;
	}
	UIImage *shadowImage = navigationController.navigationBar.shadowImage;
	if (shadowImage != nil) {
		originalTopBarImages[@"shadowImage"] = shadowImage;
	}
	self.originalTopBarImages = originalTopBarImages;
}


@end
