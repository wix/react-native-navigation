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
#import "RNNRootViewController.h"
#import "UIViewController+SideMenuController.h"
#import "RNNViewControllerPresenter.h"
#import "RNNNavigationControllerPresenter.h"
#import "RNNTabBarPresenter.h"
#import "RNNSideMenuPresenter.h"
#import "RNNSplitViewControllerPresenter.h"

@implementation RNNControllerFactory {
	id<RNNRootViewCreator> _creator;
	RNNExternalComponentStore *_store;
	RCTBridge *_bridge;
	RNNReactComponentRegistry* _componentRegistry;
}

# pragma mark public


- (instancetype)initWithRootViewCreator:(id <RNNRootViewCreator>)creator
						   eventEmitter:(RNNEventEmitter*)eventEmitter
								  store:(RNNExternalComponentStore *)store
					   componentRegistry:(RNNReactComponentRegistry *)componentRegistry
							  andBridge:(RCTBridge *)bridge {
	
	self = [super init];
	
	_creator = creator;
	_eventEmitter = eventEmitter;
	_bridge = bridge;
	_store = store;
	_componentRegistry = componentRegistry;
	
	return self;
}

- (UIViewController *)createLayout:(NSDictionary*)layout {
	UIViewController* layoutViewController = [self fromTree:layout];
	return layoutViewController;
}

- (NSArray<RNNLayoutProtocol> *)createChildrenLayout:(NSArray*)children {
	NSMutableArray<RNNLayoutProtocol>* childViewControllers = [NSMutableArray<RNNLayoutProtocol> new];
	for (NSDictionary* layout in children) {
		[childViewControllers addObject:[self fromTree:layout]];
	}
	return childViewControllers;
}

# pragma mark private

- (UIViewController *)fromTree:(NSDictionary*)json {
	RNNLayoutNode* node = [RNNLayoutNode create:json];
	
	UIViewController *result;
	
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
	
	return result;
}

- (UIViewController *)createComponent:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:node.data[@"options"]];;
	RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] initWithComponentRegistry:_componentRegistry];
	
	RNNRootViewController* component = [[RNNRootViewController alloc] initWithLayoutInfo:layoutInfo rootViewCreator:_creator eventEmitter:_eventEmitter presenter:presenter options:options defaultOptions:_defaultOptions];
	
	return (UIViewController *)component;
}

- (UIViewController *)createExternalComponent:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:node.data[@"options"]];;
	RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] init];
	
	UIViewController* externalVC = [_store getExternalComponent:layoutInfo bridge:_bridge];
	
	RNNRootViewController* component = [[RNNRootViewController alloc] initExternalComponentWithLayoutInfo:layoutInfo eventEmitter:_eventEmitter presenter:presenter options:options defaultOptions:_defaultOptions];
	[component bindViewController:externalVC];
	
	return (UIViewController *)component;
}


- (UIViewController *)createStack:(RNNLayoutNode*)node {
	RNNNavigationControllerPresenter* presenter = [[RNNNavigationControllerPresenter alloc] initWithComponentRegistry:_componentRegistry];
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:node.data[@"options"]];;
	
	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];
	
	RNNNavigationController* stack = [[RNNNavigationController alloc] initWithLayoutInfo:layoutInfo creator:_creator options:options defaultOptions:_defaultOptions presenter:presenter eventEmitter:_eventEmitter childViewControllers:childViewControllers];
	
	return stack;
}

-(UIViewController *)createTabs:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:node.data[@"options"]];;
	RNNTabBarPresenter* presenter = [[RNNTabBarPresenter alloc] init];

	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];
	
	RNNTabBarController* tabsController = [[RNNTabBarController alloc] initWithLayoutInfo:layoutInfo creator:_creator options:options defaultOptions:_defaultOptions presenter:presenter eventEmitter:_eventEmitter childViewControllers:childViewControllers];
	
	return tabsController;
}

- (UIViewController *)createTopTabs:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:node.data[@"options"]];;
	RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] init];

	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];
	
	RNNTopTabsViewController* topTabsController = [[RNNTopTabsViewController alloc] initWithLayoutInfo:layoutInfo creator:_creator options:options defaultOptions:_defaultOptions presenter:presenter eventEmitter:_eventEmitter childViewControllers:childViewControllers];
	
	return topTabsController;
}

- (UIViewController *)createSideMenu:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:node.data[@"options"]];;
	RNNSideMenuPresenter* presenter = [[RNNSideMenuPresenter alloc] init];

	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];
	
	RNNSideMenuController *sideMenu = [[RNNSideMenuController alloc] initWithLayoutInfo:layoutInfo creator:_creator childViewControllers:childViewControllers options:options defaultOptions:_defaultOptions presenter:presenter eventEmitter:_eventEmitter];
	
	return sideMenu;
}


- (UIViewController *)createSideMenuChild:(RNNLayoutNode*)node type:(RNNSideMenuChildType)type {
	UIViewController* childVc = [self fromTree:node.children[0]];
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:node.data[@"options"]];;

	RNNSideMenuChildVC *sideMenuChild = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:layoutInfo creator:_creator options:options defaultOptions:_defaultOptions presenter:[[RNNViewControllerPresenter alloc] init] eventEmitter:_eventEmitter childViewController:childVc type:type];
	
	return sideMenuChild;
}

- (UIViewController *)createSplitView:(RNNLayoutNode*)node {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] initWithNode:node];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:node.data[@"options"]];;
	RNNSplitViewControllerPresenter* presenter = [[RNNSplitViewControllerPresenter alloc] init];

	NSArray *childViewControllers = [self extractChildrenViewControllersFromNode:node];

	RNNSplitViewController* splitViewController = [[RNNSplitViewController alloc] initWithLayoutInfo:layoutInfo creator:_creator options:options defaultOptions:_defaultOptions presenter:presenter eventEmitter:_eventEmitter childViewControllers:childViewControllers];
	
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
