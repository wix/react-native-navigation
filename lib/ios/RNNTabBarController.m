#import "RNNTabBarController.h"

@implementation RNNTabBarController {
	NSUInteger _currentTabIndex;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
			  childViewControllers:(NSArray *)childViewControllers
						   options:(RNNNavigationOptions *)options
					defaultOptions:(RNNNavigationOptions *)defaultOptions
						 presenter:(RNNTabBarPresenter *)presenter
					  eventEmitter:(RNNEventEmitter *)eventEmitter {
	self = [self initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options defaultOptions:defaultOptions presenter:presenter];
	
	_eventEmitter = eventEmitter;
	
	return self;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
			  childViewControllers:(NSArray *)childViewControllers
						   options:(RNNNavigationOptions *)options
					defaultOptions:(RNNNavigationOptions *)defaultOptions
						 presenter:(RNNTabBarPresenter *)presenter {
	self = [super init];
	
	self.delegate = self;
	self.options = options;
	self.defaultOptions = defaultOptions;
	self.layoutInfo = layoutInfo;
	self.presenter = presenter;
	[self.presenter bindViewController:self];
	[self setViewControllers:childViewControllers];
	[self.presenter applyOptionsOnInit:self.options];
	
	return self;
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	if (parent) {
		[_presenter applyOptionsOnWillMoveToParentViewController:self.resolveOptions];
	}
}

- (void)onChildWillAppear {
	[_presenter applyOptions:self.resolveOptions];
	[((UIViewController<RNNParentProtocol> *)self.parentViewController) onChildWillAppear];
}

- (RNNNavigationOptions *)resolveOptions {
	return [(RNNNavigationOptions *)[self.options mergeInOptions:self.getCurrentChild.resolveOptions.copy] withDefault:self.defaultOptions];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[_presenter mergeOptions:options currentOptions:self.options defaultOptions:self.defaultOptions];
	[((UIViewController<RNNLayoutProtocol> *)self.parentViewController) mergeOptions:options];
}

- (void)overrideOptions:(RNNNavigationOptions *)options {
	[self.options overrideOptions:options];
}

- (void)renderTreeAndWait:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
		dispatch_group_t group = dispatch_group_create();
		for (UIViewController<RNNLayoutProtocol>* childViewController in self.childViewControllers) {
			dispatch_group_enter(group);
			dispatch_async(dispatch_get_main_queue(), ^{
				[childViewController renderTreeAndWait:wait perform:^{
					dispatch_group_leave(group);
				}];
			});
		}
		
		dispatch_group_wait(group, DISPATCH_TIME_FOREVER);
		
		dispatch_async(dispatch_get_main_queue(), ^{
			readyBlock();
		});
	});
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
	return self.selectedViewController;
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
