
#import "RNNRootViewController.h"
#import <React/RCTConvert.h>
#import "RNNAnimator.h"
#import "RNNCustomTitleView.h"
#import "RNNPushAnimation.h"

@interface RNNRootViewController() {
	UIView* _customTitleView;
	UIView* _customTopBar;
	UIView* _customTopBarBackground;
}
@property (nonatomic, strong) NSString* componentName;
@property (nonatomic) BOOL _statusBarHidden;
@property (nonatomic) BOOL isExternalComponent;
@property (nonatomic) BOOL _optionsApplied;
@property (nonatomic, copy) void (^rotationBlock)(void);
@end

@implementation RNNRootViewController

-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withComponentId:(NSString*)componentId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter
		isExternalComponent:(BOOL)isExternalComponent {
	self = [super init];
	self.componentId = componentId;
	self.componentName = name;
	self.options = options;
	self.eventEmitter = eventEmitter;
	self.animator = [[RNNAnimator alloc] initWithTransitionOptions:self.options.customTransition];
	self.creator = creator;
	self.isExternalComponent = isExternalComponent;
	
	if (!self.isExternalComponent) {
		self.view = [creator createRootView:self.componentName rootViewId:self.componentId];
	}

	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJsReload)
												 name:RCTJavaScriptWillStartLoadingNotification
											   object:nil];
	self.navigationController.delegate = self;
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(orientationDidChange:)
												 name:UIDeviceOrientationDidChangeNotification
											   object:nil];
	return self;
}

-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
	[self.options applyOn:self];
	[self optionsUpdated];
}

-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	[self.eventEmitter sendComponentDidAppear:self.componentId componentName:self.componentName];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
}

-(void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[self.eventEmitter sendComponentDidDisappear:self.componentId componentName:self.componentName];
}

- (void)viewDidLoad {
	[super viewDidLoad];
}

- (void)optionsUpdated {
	[self setCustomNavigationTitleView];
	[self setCustomNavigationBarView];
	[self setCustomNavigationComponentBackground];
}

- (void)mergeOptions:(NSDictionary *)options {
	[self.options mergeIfEmptyWith:options];
}

- (void)setCustomNavigationTitleView {
	if (!_customTitleView) {
		if (self.options.topBar.title.component.name) {
			RCTRootView *reactView = (RCTRootView*)[_creator createRootViewFromComponentOptions:self.options.topBar.title.component];
			
			_customTitleView = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:self.options.topBar.title.component.alignment];
			reactView.backgroundColor = UIColor.clearColor;
			_customTitleView.backgroundColor = UIColor.clearColor;
			self.navigationItem.titleView = _customTitleView;
		} if ([self.navigationItem.title isKindOfClass:[RNNCustomTitleView class]] && !_customTitleView) {
			self.navigationItem.title = nil;
		}
	}
}

- (void)setCustomNavigationBarView {
	if (!_customTopBar) {
		if (self.options.topBar.component.name) {
			RCTRootView *reactView = (RCTRootView*)[_creator createRootViewFromComponentOptions:self.options.topBar.component];
			
			_customTopBar = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:@"fill"];
			reactView.backgroundColor = UIColor.clearColor;
			_customTopBar.backgroundColor = UIColor.clearColor;
			[self.navigationController.navigationBar addSubview:_customTopBar];
		} else if ([[self.navigationController.navigationBar.subviews lastObject] isKindOfClass:[RNNCustomTitleView class]] && !_customTopBar) {
			[[self.navigationController.navigationBar.subviews lastObject] removeFromSuperview];
		}
	}
}

- (void)setCustomNavigationComponentBackground {
	if (!_customTopBarBackground) {
		if (self.options.topBar.background.component.name) {
			RCTRootView *reactView = (RCTRootView*)[_creator createRootViewFromComponentOptions:self.options.topBar.background.component];
			
			_customTopBarBackground = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:@"fill"];
			[self.navigationController.navigationBar insertSubview:_customTopBarBackground atIndex:1];
			self.navigationController.navigationBar.clipsToBounds = YES;
		} else if ([[self.navigationController.navigationBar.subviews objectAtIndex:1] isKindOfClass:[RNNCustomTitleView class]]) {
			[[self.navigationController.navigationBar.subviews objectAtIndex:1] removeFromSuperview];
			self.navigationController.navigationBar.clipsToBounds = NO;
		}
	}
}

-(BOOL)isCustomTransitioned {
	return self.options.customTransition.animations != nil;
}

- (BOOL)isCustomViewController {
	return self.isExternalComponent;
}

- (BOOL)prefersStatusBarHidden {
	if ([self.options.statusBarHidden boolValue]) {
		return YES;
	} else if ([self.options.statusBarHideWithTopBar boolValue]) {
		return self.navigationController.isNavigationBarHidden;
	}
	return NO;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	if (self.options.statusBarStyle && [self.options.statusBarStyle isEqualToString:@"light"]) {
		return UIStatusBarStyleLightContent;
	} else {
		return UIStatusBarStyleDefault;
	}
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.options.supportedOrientations;
}

- (BOOL)hidesBottomBarWhenPushed
{
	if (self.options.bottomTabs && self.options.bottomTabs.visible) {
		return ![self.options.bottomTabs.visible boolValue];
	}
	return NO;
}

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated{
	RNNRootViewController* vc =  (RNNRootViewController*)viewController;
	if (![vc.options.backButtonTransition isEqualToString:@"custom"]){
		navigationController.delegate = nil;
	}
}

- (id<UIViewControllerAnimatedTransitioning>)navigationController:(UINavigationController *)navigationController
								  animationControllerForOperation:(UINavigationControllerOperation)operation
											   fromViewController:(UIViewController*)fromVC
												 toViewController:(UIViewController*)toVC {
{
	if (self.animator) {
		return self.animator;
	} else if (operation == UINavigationControllerOperationPush && self.options.animations.push.hasCustomAnimation) {
		return [[RNNPushAnimation alloc] initWithScreenTransition:self.options.animations.push];
	} else if (operation == UINavigationControllerOperationPop && self.options.animations.pop.hasCustomAnimation) {
		return [[RNNPushAnimation alloc] initWithScreenTransition:self.options.animations.pop];
	} else {
		return nil;
	}
}
	return nil;
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.options.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.options.animations.dismissModal isDismiss:YES];
}

-(void)applyTabBarItem {
	[self.options.bottomTab applyOn:self];
}

-(void)applyTopTabsOptions {
	[self.options.topTab applyOn:self];
}

- (void)performOnRotation:(void (^)(void))block {
	_rotationBlock = block;
}

- (void)orientationDidChange:(NSNotification*)notification {
	if (_rotationBlock) {
		_rotationBlock();
	}
}

/**
 *	fix for #877, #878
 */
-(void)onJsReload {
	[self cleanReactLeftovers];
}

/**
 * fix for #880
 */
-(void)dealloc {
	[self cleanReactLeftovers];
}

-(void)cleanReactLeftovers {
	[[NSNotificationCenter defaultCenter] removeObserver:self];
	[[NSNotificationCenter defaultCenter] removeObserver:self.view];
	self.view = nil;
}

@end
