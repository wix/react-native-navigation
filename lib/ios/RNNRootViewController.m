
#import "RNNRootViewController.h"
#import <React/RCTConvert.h>
#import "RNNAnimator.h"
#import "RNNCustomTitleView.h"

@interface RNNRootViewController()
@property (nonatomic, strong) NSString* componentName;
@property (nonatomic) BOOL _statusBarHidden;
@property (nonatomic) BOOL isNativeComponent;
@end

@implementation RNNRootViewController

-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withComponentId:(NSString*)componentId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter
		  isNativeComponent:(BOOL)isNativeComponent {
	self = [super init];
	self.componentId = componentId;
	self.componentName = name;
	self.options = options;
	self.eventEmitter = eventEmitter;
	self.animator = [[RNNAnimator alloc] initWithTransitionOptions:self.options.customTransition];
	self.creator = creator;
	self.isNativeComponent = isNativeComponent;
	
	if (self.isNativeComponent) {
		[self addExternalVC:name];
	} else {
		self.view = [creator createRootView:self.componentName rootViewId:self.componentId];
	}
	
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
	[self setCustomNavigationTitleView];
	[self setCustomNavigationBarView];
}

-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	[self.eventEmitter sendComponentDidAppear:self.componentId];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
}

-(void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[self.eventEmitter sendComponentDidDisappear:self.componentId];
}

- (void)viewDidLoad {
	[super viewDidLoad];
}

- (void)mergeOptions:(NSDictionary *)options {
	[self.options mergeWith:options];
}

- (void)setCustomNavigationTitleView {
	if (self.options.topBar.customTitleViewName) {
		UIView *reactView = [_creator createRootView:self.options.topBar.customTitleViewName rootViewId:self.options.topBar.customTitleViewName];
		
		RNNCustomTitleView *titleView = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:nil];
		self.navigationItem.titleView = titleView;
	}
}

- (void)setCustomNavigationBarView {
	if (self.options.topBar.customViewName) {
		UIView *reactView = [_creator createRootView:self.options.topBar.customViewName rootViewId:@"navBar"];
		
		RNNCustomTitleView *titleView = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:nil];
		[self.navigationController.navigationBar addSubview:titleView];
	}
}

-(BOOL)isCustomTransitioned {
	return self.options.customTransition != nil;
}

- (BOOL)isAnimated {
	return self.options.animated ? [self.options.animated boolValue] : YES;
}

- (BOOL)isCustomViewController {
	return self.isNativeComponent;
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

-(void)addExternalVC:(NSString*)className {
	if (className != nil) {
		Class class = NSClassFromString(className);
		if (class != NULL) {
			id obj = [[class alloc] init];
			if (obj != nil && [obj isKindOfClass:[UIViewController class]]) {
				UIViewController *viewController = (UIViewController*)obj;
				[self addChildViewController:viewController];
				self.view = [[UIView alloc] init];
				self.view.backgroundColor = [UIColor whiteColor];
				[self.view addSubview:viewController.view];
			}
			else {
				NSLog(@"addExternalVC: could not create instance. Make sure that your class is a UIViewController whihc confirms to RCCExternalViewControllerProtocol");
			}
		}
		else {
			NSLog(@"addExternalVC: could not create class from string. Check that the proper class name wass passed in ExternalNativeScreenClass");
		}
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
