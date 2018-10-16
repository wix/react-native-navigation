#import "RNNSplitViewController.h"

@implementation RNNSplitViewController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options presenter:(RNNViewControllerPresenter *)presenter {
	self = [super init];
	
	self.presenter = presenter;
	[self.presenter bindViewController:self];
	
	self.options = options;
	self.layoutInfo = layoutInfo;
	
	self.navigationController.delegate = self;
	
	[self bindChildViewControllers:childViewControllers];
	
	return self;
}

- (void)onChildWillAppear:(RNNNavigationOptions *)childOptions withDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	RNNNavigationOptions* resolvedOptions = self.options.copy;
	[resolvedOptions mergeOptions:childOptions overrideOptions:YES];
	
	[_presenter setDefaultOptions:defaultOptions];
	[_presenter applyOptions:resolvedOptions];
	[((UIViewController<RNNParentProtocol> *)self.parentViewController) onChildWillAppear:resolvedOptions withDefaultOptions:defaultOptions];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[_presenter mergeOptions:options];
	[((UIViewController<RNNLayoutProtocol> *)self.parentViewController) mergeOptions:options];
}

- (void)bindChildViewControllers:(NSArray<UIViewController<RNNLayoutProtocol> *> *)viewControllers {
	[self setViewControllers:viewControllers];
	UIViewController<UISplitViewControllerDelegate>* masterViewController = viewControllers[0];
	self.delegate = masterViewController;
}

-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
}

- (UIViewController *)getLeafViewController {
	return self;
}

@end
