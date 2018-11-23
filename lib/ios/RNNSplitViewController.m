#import "RNNSplitViewController.h"

@implementation RNNSplitViewController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNViewControllerPresenter *)presenter {
	self = [super init];
	
	self.presenter = presenter;
	[self.presenter bindViewController:self];
	
	self.defaultOptions = defaultOptions;
	self.options = options;
	self.layoutInfo = layoutInfo;
	
	self.navigationController.delegate = self;
	
	[self bindChildViewControllers:childViewControllers];
	
	return self;
}

- (void)onChildWillAppear {
	[_presenter applyOptions:self.resolveOptions];
	[((UIViewController<RNNParentProtocol> *)self.parentViewController) onChildWillAppear];
}

- (RNNNavigationOptions *)resolveOptions {
	return [(RNNNavigationOptions *)[self.getCurrentChild.resolveOptions.copy mergeOptions:self.options] withDefault:self.defaultOptions];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[_presenter mergeOptions:options currentOptions:self.options defaultOptions:self.defaultOptions];
	[((UIViewController<RNNLayoutProtocol> *)self.parentViewController) mergeOptions:options];
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

@end
