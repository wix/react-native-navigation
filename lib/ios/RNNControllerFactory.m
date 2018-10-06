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
#import "RNNViewControllerPresenter.h"
#import "RNNTabBarPresenter.h"
#import "RNNRootViewController.h"

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

- (UIViewController<RNNParentProtocol> *)createLayoutAndSaveToStore:(NSDictionary*)layout {
	return [self fromTree:layout];
}

# pragma mark private

- (UIViewController<RNNParentProtocol> *)fromTree:(NSDictionary*)json {
	RNNLayoutNode* node = [RNNLayoutNode create:json];
	
	UIViewController<RNNParentProtocol> *result;
	
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

- (UIViewController<RNNParentProtocol> *)createComponent:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [_optionsManager createOptions:node.data[@"options"]];
	
	RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] init];
	RNNRootViewController* component = [[RNNRootViewController alloc] initWithLayoutInfo:layoutInfo rootViewCreator:_creator eventEmitter:_eventEmitter presenter:presenter options:options];
	
	if (!component.isExternalViewController) {
		CGSize availableSize = UIApplication.sharedApplication.delegate.window.bounds.size;
		[_bridge.uiManager setAvailableSize:availableSize forRootView:component.view];
	}
	
	return (UIViewController<RNNParentProtocol> *)component;
}

- (UIViewController<RNNParentProtocol> *)createExternalComponent:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [_optionsManager createOptions:node.data[@"options"]];
	RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] init];

	UIViewController* externalVC = [_store getExternalComponent:layoutInfo bridge:_bridge];
	
	RNNRootViewController* component = [[RNNRootViewController alloc] initExternalComponentWithLayoutInfo:layoutInfo eventEmitter:_eventEmitter presenter:presenter options:options];
	[component bindViewController:externalVC];
	
	return (UIViewController<RNNParentProtocol> *)component;
}


- (UIViewController<RNNParentProtocol> *)createStack:(RNNLayoutNode*)node {
	RNNNavigationControllerPresenter* presenter = [[RNNNavigationControllerPresenter alloc] init];
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [_optionsManager createOptions:node.data[@"options"]];
	
	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];
	
	RNNNavigationController* stack = [[RNNNavigationController alloc] initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter];
	
	return stack;
}

-(UIViewController<RNNParentProtocol> *)createTabs:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [_optionsManager createOptions:node.data[@"options"]];
	RNNTabBarPresenter* presenter = [[RNNTabBarPresenter alloc] init];

	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];
	
	RNNTabBarController* tabsController = [[RNNTabBarController alloc] initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter eventEmitter:_eventEmitter];
	
	return tabsController;
}

- (UIViewController<RNNParentProtocol> *)createTopTabs:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [_optionsManager createOptions:node.data[@"options"]];
	RNNBasePresenter* presenter = [[RNNBasePresenter alloc] init];

	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];
	
	RNNTopTabsViewController* topTabsController = [[RNNTopTabsViewController alloc] initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter];
	
	return topTabsController;
}

- (UIViewController<RNNParentProtocol> *)createSideMenu:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [_optionsManager createOptions:node.data[@"options"]];
	RNNBasePresenter* presenter = [[RNNBasePresenter alloc] init];

	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];
	
	RNNSideMenuController *sideMenu = [[RNNSideMenuController alloc] initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter];
	
	return sideMenu;
}


- (UIViewController<RNNParentProtocol> *)createSideMenuChild:(RNNLayoutNode*)node type:(RNNSideMenuChildType)type {
	UIViewController<RNNParentProtocol>* childVc = [self fromTree:node.children[0]];
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [_optionsManager createOptions:node.data[@"options"]];

	RNNSideMenuChildVC *sideMenuChild = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:layoutInfo childViewControllers:@[childVc] options:options presenter:[[RNNBasePresenter alloc] init] type:type];
	
	return sideMenuChild;
}

- (UIViewController<RNNParentProtocol> *)createSplitView:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [_optionsManager createOptions:node.data[@"options"]];
	RNNBasePresenter* presenter = [[RNNBasePresenter alloc] init];

	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];

	RNNSplitViewController* splitViewController = [[RNNSplitViewController alloc] initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter];

	return splitViewController;
}

- (NSArray<UIViewController *> *)extractChildrenViewControllersFromNode:(RNNLayoutNode *)node {
	NSMutableArray* childrenArray = [NSMutableArray new];
	for (NSDictionary* child in node.children) {
		UIViewController* childVc = [self fromTree:child];
		[childrenArray addObject:childVc];
	}
	
	return childrenArray;
}

@end
