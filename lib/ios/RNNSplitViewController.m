#import "RNNSplitViewController.h"

@implementation RNNSplitViewController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNSplitViewControllerPresenter *)presenter {
	self = [super init];

	self.defaultOptions = defaultOptions;
	self.options = options;

	self.presenter = presenter;
	[self.presenter bindViewController:self];
	[self.presenter applyOptionsOnInit:self.options];

	self.layoutInfo = layoutInfo;
	
	self.navigationController.delegate = self;
	
	[self bindChildViewControllers:childViewControllers];

	return self;
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	if (parent) {
		[_presenter applyOptionsOnWillMoveToParentViewController:self.resolveOptions];
	}
}

- (void)onChildWillAppear {
	[_presenter applyOptions:self.resolveOptions];
	[((UISplitViewController<RNNParentProtocol> *)self.parentViewController) onChildWillAppear];
}

- (RNNNavigationOptions *)resolveOptions {
	return [(RNNNavigationOptions *)[self.getCurrentChild.resolveOptions.copy mergeOptions:self.options] withDefault:self.defaultOptions];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[_presenter mergeOptions:options currentOptions:self.options defaultOptions:self.defaultOptions];
	[((UISplitViewController<RNNLayoutProtocol> *)self.parentViewController) mergeOptions:options];
}

- (void)overrideOptions:(RNNNavigationOptions *)options {
	[self.options overrideOptions:options];
}

- (void)bindChildViewControllers:(NSArray<UIViewController<RNNLayoutProtocol> *> *)viewControllers {
	[self setViewControllers:viewControllers];
	UIViewController<UISplitViewControllerDelegate>* masterViewController = viewControllers[0];
	self.delegate = masterViewController;
}

-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
}

- (UIViewController *)getCurrentChild {
	return self.viewControllers[0];
}

- (UIViewController<RNNLeafProtocol> *)getCurrentLeaf {
	return [[self getCurrentChild] getCurrentLeaf];
}

- (UIViewController *)primaryViewControllerForCollapsingSplitViewController:(UISplitViewController *)splitViewController {
	return self.viewControllers[0];
}

- (void)collapseSecondaryViewController:(UIViewController *)secondaryViewController
				 forSplitViewController:(UISplitViewController *)splitViewController {
	return;
}

@end
