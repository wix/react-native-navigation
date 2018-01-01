
#import "RNNRootViewController.h"
#import <React/RCTConvert.h>
#import "RNNAnimator.h"
#import "RNNNavigationButtons.h"

@interface RNNRootViewController()
@property (nonatomic, strong) NSString* containerName;
@property (nonatomic) BOOL _statusBarHidden;
@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

@end

@implementation RNNRootViewController

-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withContainerId:(NSString*)containerId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter
				   animator:(RNNAnimator *)animator {
	self = [super init];
	self.containerId = containerId;
	self.containerName = name;
	self.navigationOptions = options;
	self.eventEmitter = eventEmitter;
	self.animator = animator;
	self.view = [creator createRootView:self.containerName rootViewId:self.containerId];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJsReload)
												 name:RCTJavaScriptWillStartLoadingNotification
											   object:nil];
	self.navigationController.modalPresentationStyle = UIModalPresentationCustom;
	self.navigationController.delegate = self;
	self.navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:self];
	return self;
}
	
-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
	[self.navigationOptions applyOn:self];
	[self applyNavigationButtons];
}

- (void)viewDidLoad {
	[super viewDidLoad];
}

-(BOOL)isAnimated {
	return self.animator;
}

- (BOOL)prefersStatusBarHidden {
	if ([self.navigationOptions.statusBarHidden boolValue]) {
		return YES;
	} else if ([self.navigationOptions.statusBarHideWithTopBar boolValue]) {
		return self.navigationController.isNavigationBarHidden;
	}
	return NO;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.navigationOptions.supportedOrientations;
}

- (BOOL)hidesBottomBarWhenPushed
{
	if (self.navigationOptions.bottomTabs && self.navigationOptions.bottomTabs.hidden) {
		return [self.navigationOptions.bottomTabs.hidden boolValue];
	}
	return NO;
}

-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	[self.eventEmitter sendContainerDidAppear:self.containerId];
}

-(void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[self.eventEmitter sendContainerDidDisappear:self.containerId];
}

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated{
	RNNRootViewController* vc =  (RNNRootViewController*)viewController;
	if (![vc.navigationOptions.backButtonTransition isEqualToString:@"custom"]){
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

-(void)applyNavigationButtons{
	[self.navigationButtons applyLeftButtons:self.navigationOptions.leftButtons rightButtons:self.navigationOptions.rightButtons];
}

-(void)applyTabBarItem {
	[self.navigationOptions applyTabBarItemOptions:self];
}

-(void)applyTopTabsOptions {
	[self.navigationOptions applyTopTab:self];
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
