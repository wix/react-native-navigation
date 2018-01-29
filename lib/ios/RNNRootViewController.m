
#import "RNNRootViewController.h"
#import <React/RCTConvert.h>
#import "RNNAnimator.h"

@interface RNNRootViewController()
@property (nonatomic, strong) NSString* componentName;
@property (nonatomic) BOOL _statusBarHidden;

@end

@implementation RNNRootViewController

-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withComponentId:(NSString*)componentId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter
				   animator:(RNNAnimator *)animator {
	self = [super init];
	self.componentId = componentId;
	self.componentName = name;
	self.options = options;
	self.eventEmitter = eventEmitter;
	self.animator = animator;
	self.view = [creator createRootView:self.componentName rootViewId:self.componentId];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJsReload)
												 name:RCTJavaScriptWillStartLoadingNotification
											   object:nil];
	self.navigationController.modalPresentationStyle = UIModalPresentationCustom;
	self.navigationController.delegate = self;

	return self;
}
	
-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
	[self.options applyOn:self];
	[self sendLifecycleEvent:kDidMount];
}

-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	[self.eventEmitter sendComponentDidAppear:self.componentId];
	[self sendLifecycleEvent:kDidAppear];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	[self sendLifecycleEvent:kWillUnmount];
}

-(void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[self.eventEmitter sendComponentDidDisappear:self.componentId];
	[self sendLifecycleEvent:kDidDisappear];
}

- (void)viewDidLoad {
	[super viewDidLoad];
}

- (void)sendLifecycleEvent:(LifecycleEvent)event {
	[self.eventEmitter sendLifecycleEvent:[RNNComponentLifecycleEvent create:event componentName:self.componentName componentId:self.componentId]];
}

-(BOOL)isCustomTransitioned {
	return self.animator != nil;
}

- (BOOL)isAnimated {
	return self.options.animated ? [self.options.animated boolValue] : YES;
}

- (BOOL)prefersStatusBarHidden {
	if ([self.options.statusBarHidden boolValue]) {
		return YES;
	} else if ([self.options.statusBarHideWithTopBar boolValue]) {
		return self.navigationController.isNavigationBarHidden;
	}
	return NO;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.options.supportedOrientations;
}

- (BOOL)hidesBottomBarWhenPushed
{
	if (self.options.bottomTabs && self.options.bottomTabs.hidden) {
		return [self.options.bottomTabs.hidden boolValue];
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
	if (operation == UINavigationControllerOperationPush) {
		return self.animator;
	} else if (operation == UINavigationControllerOperationPop) {
		return self.animator;
	} else {
		return nil;
	}
}
	return nil;

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
