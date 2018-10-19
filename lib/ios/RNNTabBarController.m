#import "RNNTabBarController.h"

@implementation RNNTabBarController {
	NSUInteger _currentTabIndex;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
			  childViewControllers:(NSArray *)childViewControllers
						   options:(RNNNavigationOptions *)options
						 presenter:(RNNTabBarPresenter *)presenter
					  eventEmitter:(RNNEventEmitter *)eventEmitter {
	self = [self initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter];
	
	_eventEmitter = eventEmitter;
	
	return self;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
			  childViewControllers:(NSArray *)childViewControllers
						   options:(RNNNavigationOptions *)options
						 presenter:(RNNTabBarPresenter *)presenter {
	self = [super init];
	
	self.delegate = self;
	self.options = options;
	self.layoutInfo = layoutInfo;
	self.presenter = presenter;
	[self.presenter bindViewController:self];
	[self setViewControllers:childViewControllers];
	[self setTabItemBadges];
	
	return self;
}

-(void)setTabItemBadges {
	if (self.options.bottomTabs.badgeSize.hasValue) {
		for (UITabBarItem* tabBarItem in self.tabBar.items) {
			NSInteger tag = tabBarItem.tag;
			
			CGFloat badgeSize = [self.options.bottomTabs.badgeSize.get doubleValue];
			CGFloat topMargin = (double)5;
			
			NSUInteger index = [self.tabBar.items indexOfObject:tabBarItem];
			NSUInteger tabBarItemCount = self.tabBar.items.count;
			CGFloat halfItemWidth = CGRectGetWidth(self.view.bounds) / (tabBarItemCount * 2);
			CGFloat xOffset = halfItemWidth * (index * 2 + 1);
			CGFloat imageHalfWidth = tabBarItem.selectedImage.size.width / 2;
			
			UIView* customBadge = [[UIView alloc] initWithFrame:CGRectMake(xOffset + imageHalfWidth, topMargin, badgeSize, badgeSize)];
			customBadge.layer.cornerRadius = badgeSize / 2;
			
			UIColor* badgeColor = tabBarItem.badgeColor;
			if (badgeColor == nil) {
				badgeColor = UIColor.redColor;
			}
			customBadge.backgroundColor = badgeColor;
			customBadge.tag = tag * 1000;
			customBadge.hidden = tabBarItem.badgeValue == nil || [tabBarItem.badgeValue isEqualToString:@""];
			tabBarItem.badgeValue = nil;
			
			[self.tabBar addSubview:customBadge];
		}
	}
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	if (parent) {
		[_presenter applyOptionsOnWillMoveToParentViewController:self.options];
	}
}

- (void)onChildWillAppear {
	[_presenter applyOptions:self.resolveOptions];
	[((UIViewController<RNNParentProtocol> *)self.parentViewController) onChildWillAppear];
}

- (RNNNavigationOptions *)resolveOptions {
	return (RNNNavigationOptions *)[self.getCurrentChild.resolveOptions.copy mergeOptions:self.options];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[_presenter mergeOptions:options resolvedOptions:self.resolveOptions];
	[((UIViewController<RNNLayoutProtocol> *)self.parentViewController) mergeOptions:options];
}

- (UITabBarItem *)tabBarItem {
	return super.tabBarItem ? super.tabBarItem : self.viewControllers.lastObject.tabBarItem;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.selectedViewController.supportedInterfaceOrientations;
}

- (void)setSelectedIndexByComponentID:(NSString *)componentID {
	for (id child in self.childViewControllers) {
		UIViewController<RNNLayoutProtocol>* vc = child;

		if ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] && [vc.layoutInfo.componentId isEqualToString:componentID]) {
			[self setSelectedIndex:[self.childViewControllers indexOfObject:child]];
		}
	}
}

- (void)setSelectedIndex:(NSUInteger)selectedIndex {
	_currentTabIndex = selectedIndex;
	[super setSelectedIndex:selectedIndex];
}

- (UIViewController *)getCurrentChild {
	return ((UIViewController<RNNParentProtocol>*)self.selectedViewController).getCurrentChild;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return ((UIViewController<RNNParentProtocol>*)self.selectedViewController).preferredStatusBarStyle;
}

#pragma mark UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
	[_eventEmitter sendBottomTabSelected:@(tabBarController.selectedIndex) unselected:@(_currentTabIndex)];
	_currentTabIndex = tabBarController.selectedIndex;
}

@end
