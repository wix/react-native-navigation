#import "RNNSplitViewController.h"
#import "UIViewController+LayoutProtocol.h"

@implementation RNNSplitViewController

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

@end
