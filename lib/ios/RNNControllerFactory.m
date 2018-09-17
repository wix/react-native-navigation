#import "RNNControllerFactory.h"
#import "RNNLayoutNode.h"
#import "RNNSplitViewController.h"
#import "RNNSplitViewOptions.h"
#import "RNNSideMenuController.h"
#import "RNNSideMenuChildVC.h"
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopTabsViewController.h"
#import "RNNLayoutInfo.h"
#import "RNNOptionsManager.h"

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
	_optionsManager = [RNNOptionsManager new];
	
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
	
	else if (node.isSplitView) {
		result = [self createSplitView:node];
	}
	
	if (!result) {
		@throw [NSException exceptionWithName:@"UnknownControllerType" reason:[@"Unknown controller type " stringByAppendingString:node.type] userInfo:nil];
	}

	[_store setComponent:result componentId:node.nodeId];
	
	return result;
}

- (UIViewController<RNNRootViewProtocol> *)createComponent:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node optionsManager:_optionsManager];

	RNNRootViewController* component = [[RNNRootViewController alloc] initWithLayoutInfo:layoutInfo rootViewCreator:_creator eventEmitter:_eventEmitter isExternalComponent:NO];

	if (!component.isCustomViewController) {
		CGSize availableSize = UIApplication.sharedApplication.delegate.window.bounds.size;
		[_bridge.uiManager setAvailableSize:availableSize forRootView:component.view];
	}
	return (UIViewController<RNNRootViewProtocol> *)component;
}

- (UIViewController<RNNRootViewProtocol> *)createExternalComponent:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node optionsManager:_optionsManager];

	UIViewController* externalVC = [_store getExternalComponent:layoutInfo bridge:_bridge];
	
	RNNRootViewController* component = [[RNNRootViewController alloc] initWithLayoutInfo:layoutInfo rootViewCreator:_creator eventEmitter:_eventEmitter isExternalComponent:YES];
	
	[component addChildViewController:externalVC];
	[component.view addSubview:externalVC.view];
	[externalVC didMoveToParentViewController:component];
	
	return (UIViewController<RNNRootViewProtocol> *)component;
}


- (UIViewController<RNNRootViewProtocol> *)createStack:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node optionsManager:_optionsManager];
	
	RNNNavigationController* vc = [[RNNNavigationController alloc] initWithLayoutInfo:layoutInfo];

	NSMutableArray* controllers = [NSMutableArray new];
	for (NSDictionary* child in node.children) {
		[controllers addObject:[self fromTree:child]];
	}
	
	[vc setViewControllers:controllers];
	
	return vc;
}

-(UIViewController<RNNRootViewProtocol> *)createTabs:(RNNLayoutNode*)node {
	RNNTabBarController* vc = [[RNNTabBarController alloc] initWithEventEmitter:_eventEmitter];
	vc.layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node optionsManager:_optionsManager];
	
	NSMutableArray* controllers = [NSMutableArray new];
	for (NSDictionary *child in node.children) {
		UIViewController<RNNRootViewProtocol>* childVc = [self fromTree:child];
		[childVc.layoutInfo.options applyOn:childVc];
		[childVc.getLeafViewController.layoutInfo.options applyOn:childVc.getLeafViewController];
		
		[controllers addObject:childVc];
	}
	[vc setViewControllers:controllers];
	
	return vc;
}

- (UIViewController<RNNRootViewProtocol> *)createTopTabs:(RNNLayoutNode*)node {
	RNNTopTabsViewController* vc = [[RNNTopTabsViewController alloc] init];
	vc.layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node optionsManager:_optionsManager];
	
	NSMutableArray* controllers = [NSMutableArray new];
	for (NSDictionary *child in node.children) {
		RNNRootViewController* childVc = (RNNRootViewController*)[self fromTree:child];
		[controllers addObject:childVc];
		[_bridge.uiManager setAvailableSize:vc.contentView.bounds.size forRootView:childVc.view];
	}
	
	[vc setViewControllers:controllers];
	
	return vc;
}

- (UIViewController<RNNRootViewProtocol> *)createSideMenu:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node optionsManager:_optionsManager];

	NSMutableArray* childrenVCs = [NSMutableArray new];
	
	for (NSDictionary *child in node.children) {
		UIViewController *vc = [self fromTree:child];
		[childrenVCs addObject:vc];
	}
	RNNSideMenuController *sideMenu = [[RNNSideMenuController alloc] initWithControllers:childrenVCs];
	sideMenu.layoutInfo = layoutInfo;
	
	return sideMenu;
}


- (UIViewController<RNNRootViewProtocol> *)createSideMenuChild:(RNNLayoutNode*)node type:(RNNSideMenuChildType)type {
	UIViewController<RNNRootViewProtocol>* child = [self fromTree:node.children[0]];
	RNNSideMenuChildVC *sideMenuChild = [[RNNSideMenuChildVC alloc] initWithChild: child type:type];
	
	return sideMenuChild;
}

- (UIViewController<RNNRootViewProtocol> *)createSplitView:(RNNLayoutNode*)node {

	NSString* componentId = node.nodeId;
	
	RNNSplitViewOptions* options = [[RNNSplitViewOptions alloc] initWithDict:_optionsManager.defaultOptionsDict];
	[options mergeWith:node.data[@"options"]];

	RNNSplitViewController* svc = [[RNNSplitViewController alloc] initWithOptions:options withComponentId:componentId rootViewCreator:_creator eventEmitter:_eventEmitter];

	// We need two children of the node for successful Master / Detail
	NSDictionary *master = node.children[0];
	NSDictionary *detail = node.children[1];

	// Create view controllers
	RNNRootViewController* masterVc = (RNNRootViewController*)[self fromTree:master];
	RNNRootViewController* detailVc = (RNNRootViewController*)[self fromTree:detail];

	// Set the controllers and delegate to masterVC
	svc.viewControllers = [NSArray arrayWithObjects:masterVc, detailVc, nil];
	svc.delegate = masterVc;

	return svc;
}

@end
