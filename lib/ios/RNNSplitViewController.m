#import "RNNSplitViewController.h"

@implementation RNNSplitViewController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options optionsResolver:(RNNParentOptionsResolver *)optionsResolver presenter:(RNNBasePresenter *)presenter {
	self = [super init];
	
	self.presenter = presenter;
	self.options = options;
	self.layoutInfo = layoutInfo;
	self.optionsResolver = optionsResolver;
	
	self.navigationController.delegate = self;
	
	[self bindChildViewControllers:childViewControllers];
	
	return self;
}

- (void)bindChildViewControllers:(NSArray<UIViewController<RNNLayoutProtocol> *> *)viewControllers {
	[self setViewControllers:viewControllers];
	UIViewController<UISplitViewControllerDelegate>* masterViewController = viewControllers[0];
	self.delegate = masterViewController;
}

-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
	[self.options applyOn:self];
}

- (UIViewController *)getLeafViewController {
	return self;
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	[_optionsResolver resolve:self with:self.viewControllers];
	[_presenter present:self.options onViewControllerDidLoad:self];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[self.presenter present:options onViewControllerWillAppear:self];
}

@end
