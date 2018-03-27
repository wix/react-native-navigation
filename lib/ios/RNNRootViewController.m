
#import "RNNRootViewController.h"
#import <React/RCTConvert.h>
#import "RNNAnimator.h"
#import "RNNCustomTitleView.h"
#import "RNNPushAnimation.h"

@interface RNNRootViewController()
@property (nonatomic, strong) NSString* componentName;
@property (nonatomic) BOOL _statusBarHidden;
@property (nonatomic) BOOL isExternalComponent;
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
	if (self.options.topBar.title.component) {
		RCTRootView *reactView = (RCTRootView*)[_creator createRootView:self.options.topBar.title.component rootViewId:self.options.topBar.title.component];

		RNNCustomTitleView *titleView = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:self.options.topBar.title.componentAlignment];
        reactView.backgroundColor = UIColor.clearColor;
		titleView.backgroundColor = UIColor.clearColor;
		self.navigationItem.titleView = titleView;
	} if ([self.navigationItem.title isKindOfClass:[RNNCustomTitleView class]]) {
		self.navigationItem.title = nil;
	}
}

- (void)setCustomNavigationBarView {
	if (self.options.topBar.componentName) {
		RCTRootView *reactView = (RCTRootView*)[_creator createRootView:self.options.topBar.componentName rootViewId:@"navBar"];

		RNNCustomTitleView *titleView = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:@"fill"];
		reactView.backgroundColor = UIColor.clearColor;
		titleView.backgroundColor = UIColor.clearColor;
		[self.navigationController.navigationBar addSubview:titleView];
	} else if ([[self.navigationController.navigationBar.subviews lastObject] isKindOfClass:[RNNCustomTitleView class]]) {
		[[self.navigationController.navigationBar.subviews lastObject] removeFromSuperview];
	}
}

- (void)setCustomNavigationComponentBackground {
	if (self.options.topBar.background.component) {
		RCTRootView *reactView = (RCTRootView*)[_creator createRootView:self.options.topBar.background.component rootViewId:@"navBarBackground"];

		RNNCustomTitleView *titleView = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:@"fill"];
		[self.navigationController.navigationBar insertSubview:titleView atIndex:1];
		self.navigationController.navigationBar.clipsToBounds = YES;
	} else if ([[self.navigationController.navigationBar.subviews objectAtIndex:1] isKindOfClass:[RNNCustomTitleView class]]) {
		[[self.navigationController.navigationBar.subviews objectAtIndex:1] removeFromSuperview];
		self.navigationController.navigationBar.clipsToBounds = NO;
	}
}

-(BOOL)isCustomTransitioned {
	return self.options.customTransition.animations != nil;
}

- (BOOL)isAnimated {
	return self.options.animated ? [self.options.animated boolValue] : YES;
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
	} else if (operation == UINavigationControllerOperationPush && self.options.animations.push) {
		return [[RNNPushAnimation alloc] initWithScreenTransition:self.options.animations.push];
	} else if (operation == UINavigationControllerOperationPop && self.options.animations.pop) {
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
