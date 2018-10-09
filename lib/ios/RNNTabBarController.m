
#import "RNNTabBarController.h"

#define kTabBarHiddenDuration 0.3

@implementation RNNTabBarController {
	NSUInteger _currentTabIndex;
	RNNEventEmitter *_eventEmitter;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
			  childViewControllers:(NSArray *)childViewControllers
						   options:(RNNNavigationOptions *)options
						 presenter:(RNNBasePresenter *)presenter
					  eventEmitter:(RNNEventEmitter *)eventEmitter {
	self = [self initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter];
	
	_eventEmitter = eventEmitter;
	
	return self;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
			  childViewControllers:(NSArray *)childViewControllers
						   options:(RNNNavigationOptions *)options
						 presenter:(RNNBasePresenter *)presenter {
	self = [super init];
	
	self.presenter = presenter;
	self.options = options;
	self.layoutInfo = layoutInfo;
	
	[self setViewControllers:childViewControllers];
	
	return self;
}

- (instancetype)initWithEventEmitter:(id)eventEmitter {
	self = [super init];
	_eventEmitter = eventEmitter;
	self.delegate = self;
	return self;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.selectedViewController.supportedInterfaceOrientations;
}

- (void)setSelectedIndexByComponentID:(NSString *)componentID {
	for (id child in self.childViewControllers) {
		UIViewController<RNNParentProtocol>* vc = child;

		if ([vc.layoutInfo.componentId isEqualToString:componentID]) {
			[self setSelectedIndex:[self.childViewControllers indexOfObject:child]];
		}
	}
}

- (void)setSelectedIndex:(NSUInteger)selectedIndex {
	_currentTabIndex = selectedIndex;
	[super setSelectedIndex:selectedIndex];
}

- (UIViewController *)getLeafViewController {
	return ((UIViewController<RNNParentProtocol>*)self.selectedViewController).getLeafViewController;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return ((UIViewController<RNNParentProtocol>*)self.selectedViewController).preferredStatusBarStyle;
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	[_presenter present:self.options onViewControllerDidLoad:self];
}

#pragma mark UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
	[_eventEmitter sendBottomTabSelected:@(tabBarController.selectedIndex) unselected:@(_currentTabIndex)];
	_currentTabIndex = tabBarController.selectedIndex;
}

@end
