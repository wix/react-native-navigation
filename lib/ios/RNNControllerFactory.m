
#import "RNNControllerFactory.h"
#import "RNNLayoutNode.h"
#import "RNNRootViewController.h"
#import "RNNSideMenuController.h"
#import "RNNSideMenuChildVC.h"
#import "RNNNavigationOptions.h"
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopTabsViewController.h"

@implementation RNNControllerFactory {
	id<RNNRootViewCreator> _creator;
	RNNStore *_store;
	RCTBridge *_bridge;
}

# pragma mark public


- (instancetype)initWithRootViewCreator:(id <RNNRootViewCreator>)creator
								  store:(RNNStore *)store
						   eventEmitter:(RNNEventEmitter*)eventEmitter
							  andBridge:(RCTBridge *)bridge {
	
	self = [super init];
	_creator = creator;
	_store = store;
	_eventEmitter = eventEmitter;
	_bridge = bridge;
	
	return self;
}

- (UIViewController<RNNRootViewProtocol> *)createLayoutAndSaveToStore:(NSDictionary*)layout {
	return [self fromTree:layout];
}

# pragma mark private

- (UIViewController<RNNRootViewProtocol> *)fromTree:(NSDictionary*)json {
	RNNLayoutNode* node = [RNNLayoutNode create:json];
	
	UIViewController<RNNRootViewProtocol> *result;
	
	if (node.isComponent) {
		result = [self createComponent:node];
	}
	
	else if (node.isStack)	{
		result = [self createStack:node];
	}
	
	else if (node.isTabs) {
		result = [self createTabs:node];
	}
	
	else if (node.isTopTabs) {
		result = [self createTopTabs:node];
	}
	
	else if (node.isSideMenuRoot) {
		result = [self createSideMenu:node];
	}
	
	else if (node.isSideMenuCenter) {
		result = [self createSideMenuChild:node type:RNNSideMenuChildTypeCenter];
	}
	
	else if (node.isSideMenuLeft) {
		result = [self createSideMenuChild:node type:RNNSideMenuChildTypeLeft];
	}
	
	else if (node.isSideMenuRight) {
		result = [self createSideMenuChild:node type:RNNSideMenuChildTypeRight];
	}
	
	else if (node.isExternalComponent) {
		result = [self createExternalComponent:node];
	}
	
	if (!result) {
		@throw [NSException exceptionWithName:@"UnknownControllerType" reason:[@"Unknown controller type " stringByAppendingString:node.type] userInfo:nil];
	}

	[_store setComponent:result componentId:node.nodeId];
	
	return result;
}

- (UIViewController<RNNRootViewProtocol> *)createComponent:(RNNLayoutNode*)node {
	NSString* name = node.data[@"name"];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:_defaultOptionsDict];
	[options mergeWith:node.data[@"options"]];

	NSString* componentId = node.nodeId;
	RNNRootViewController* component = [[RNNRootViewController alloc] initWithName:name withOptions:options withComponentId:componentId rootViewCreator:_creator eventEmitter:_eventEmitter isExternalComponent:NO];
	if (!component.isCustomViewController) {
		CGSize availableSize = UIApplication.sharedApplication.delegate.window.bounds.size;
		[_bridge.uiManager setAvailableSize:availableSize forRootView:component.view];
	}
	return component;
}

- (UIViewController<RNNRootViewProtocol> *)createExternalComponent:(RNNLayoutNode*)node {
	NSString* name = node.data[@"name"];
	NSDictionary* props = node.data[@"passProps"];
	
	UIViewController* externalVC = [_store getExternalComponent:name props:props bridge:_bridge];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:_defaultOptionsDict];
	[options mergeWith:node.data[@"options"]];
	
	NSString* componentId = node.nodeId;
	RNNRootViewController* component = [[RNNRootViewController alloc] initWithName:name withOptions:options withComponentId:componentId rootViewCreator:_creator eventEmitter:_eventEmitter isExternalComponent:YES];
	
	[component addChildViewController:externalVC];
	component.view = [[UIView alloc] init];
	component.view.backgroundColor = [UIColor whiteColor];
	[component.view addSubview:externalVC.view];
	
	return component;
}


- (UIViewController<RNNRootViewProtocol> *)createStack:(RNNLayoutNode*)node {
	RNNNavigationController* vc = [[RNNNavigationController alloc] init];
	NSDictionary* options = node.data[@"options"];
	NSMutableArray* controllers = [NSMutableArray new];
	for (NSDictionary* child in node.children) {
		[controllers addObject:[self fromTree:child]];
	}
	[vc setViewControllers:controllers];
	[vc mergeOptions:options];
	
	return vc;
}

-(UIViewController<RNNRootViewProtocol> *)createTabs:(RNNLayoutNode*)node {
	RNNTabBarController* vc = [[RNNTabBarController alloc] init];
	NSDictionary* options = node.data[@"options"];

	NSMutableArray* controllers = [NSMutableArray new];
	for (NSDictionary *child in node.children) {
		UIViewController* childVc = (UIViewController*)[self fromTree:child];
		RNNRootViewController* rootView = (RNNRootViewController *)childVc.childViewControllers.firstObject;
		[rootView applyTabBarItem];
		
		[controllers addObject:childVc];
	}
	[vc setViewControllers:controllers];
	[vc mergeOptions:options];
	
	return vc;
}

- (UIViewController<RNNRootViewProtocol> *)createTopTabs:(RNNLayoutNode*)node {
	RNNTopTabsViewController* vc = [[RNNTopTabsViewController alloc] init];
	
	NSMutableArray* controllers = [NSMutableArray new];
	for (NSDictionary *child in node.children) {
		RNNRootViewController* childVc = (RNNRootViewController*)[self fromTree:child];
		childVc.topTabsViewController = vc;
		[controllers addObject:childVc];
		[_bridge.uiManager setAvailableSize:vc.contentView.bounds.size forRootView:childVc.view];
	}
	
	[vc setViewControllers:controllers];
	
	return vc;
}

- (UIViewController<RNNRootViewProtocol> *)createSideMenu:(RNNLayoutNode*)node {
	NSMutableArray* childrenVCs = [NSMutableArray new];
	
	
	for (NSDictionary *child in node.children) {
		UIViewController *vc = [self fromTree:child];
		[childrenVCs addObject:vc];
	}
	RNNSideMenuController *sideMenu = [[RNNSideMenuController alloc] initWithControllers:childrenVCs];
	return sideMenu;
}


- (UIViewController<RNNRootViewProtocol> *)createSideMenuChild:(RNNLayoutNode*)node type:(RNNSideMenuChildType)type {
	UIViewController* child = (UIViewController*)[self fromTree:node.children[0]];
	RNNSideMenuChildVC *sideMenuChild = [[RNNSideMenuChildVC alloc] initWithChild: child type:type];
	
	return sideMenuChild;
}

- (UIViewController<RNNRootViewProtocol> *)createOverlay:(NSDictionary*)layout {
	UIViewController<RNNRootViewProtocol> *vc = [self fromTree:layout];
	RCTRootView* rootView = (RCTRootView*)vc.view;
	rootView.backgroundColor = [UIColor clearColor];
	CGSize availableSize = UIApplication.sharedApplication.delegate.window.bounds.size;
	rootView.frame = CGRectMake(0, 0, availableSize.width, availableSize.height);
	[_bridge.uiManager setAvailableSize:availableSize forRootView:vc.view];
	
	return vc;
}

@end
