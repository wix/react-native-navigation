
#import "RNNNavigationController.h"
#import "RNNModalAnimation.h"
#import "RNNRootViewController.h"

const NSInteger TOP_BAR_TRANSPARENT_TAG = 78264803;

@interface RNNNavigationController()

@property (nonatomic, strong) NSMutableDictionary* originalTopBarImages;

@end

@implementation RNNNavigationController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options presenter:(RNNViewControllerPresenter *)presenter {
	UIViewController* rootViewController = childViewControllers.count ? childViewControllers[0] : nil;
	self = [super initWithRootViewController:rootViewController];

	self.presenter = presenter;
	[self.presenter bindViewController:self];
	self.options = options;
	self.options.delegate = self;
	
	self.layoutInfo = layoutInfo;
	
	if (childViewControllers.count > 1) {
		[self setViewControllers:childViewControllers];
	}
	
	return self;
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	if (parent) {
		[_presenter presentOnWillMoveToParent:self.options];
	}
}

- (void)viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	[_presenter presentOnViewWillAppear:self.options];
}

- (void)optionsDidUpdatedWithOptions:(RNNNavigationOptions *)otherOptions {
	[_presenter presentOnViewWillAppear:otherOptions];
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.viewControllers.lastObject.supportedInterfaceOrientations;
}

- (UINavigationController *)navigationController {
	return self;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.getLeafViewController.preferredStatusBarStyle;
}

- (UIModalPresentationStyle)modalPresentationStyle {
	return self.getLeafViewController.modalPresentationStyle;
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated {
	if (self.viewControllers.count > 1) {
		UIViewController *controller = self.viewControllers[self.viewControllers.count - 2];
		if ([controller isKindOfClass:[RNNRootViewController class]]) {
			RNNRootViewController *rnnController = (RNNRootViewController *)controller;
			[rnnController.presenter presentOnViewWillAppear:rnnController.options];
		}
	}
	
	return [super popViewControllerAnimated:animated];
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getLeafViewController.options.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getLeafViewController.options.animations.dismissModal isDismiss:YES];
}

- (UIViewController *)getLeafViewController {
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
			[self.navigationBar setBackgroundImage:self.originalTopBarImages[@"backgroundImage"] forBarMetrics:UIBarMetricsDefault];
			self.navigationBar.shadowImage = self.originalTopBarImages[@"shadowImage"];
			self.originalTopBarImages = nil;
		}
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
