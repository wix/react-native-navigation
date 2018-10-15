#import "RNNSideMenuChildVC.h"

@interface RNNSideMenuChildVC ()

@property (readwrite) RNNSideMenuChildType type;
@property (nonatomic, retain) UIViewController<RNNParentProtocol> *child;

@end

@implementation RNNSideMenuChildVC

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options presenter:(RNNViewControllerPresenter *)presenter type:(RNNSideMenuChildType)type {
	self = [self initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter];
	
	self.type = type;

	return self;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options presenter:(RNNViewControllerPresenter *)presenter {
	self = [super init];
	
	self.child = childViewControllers[0];
	
	self.presenter = presenter;
	[self.presenter bindViewController:self];
	
	
	self.options = options;
	self.layoutInfo = layoutInfo;
	
	[self bindChildViewController:self.child];

	return self;
}

- (void)onChildWillAppear:(RNNNavigationOptions *)childOptions {
	RNNNavigationOptions* resolvedOptions = self.options.copy;
	[resolvedOptions mergeOptions:childOptions overrideOptions:YES];
	
	[_presenter applyOptions:resolvedOptions];
	[((UIViewController<RNNParentProtocol> *)self.parentViewController) onChildWillAppear:resolvedOptions];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[_presenter mergeOptions:options];
	[((UIViewController<RNNLayoutProtocol> *)self.parentViewController) mergeOptions:options];
}

- (UITabBarItem *)tabBarItem {
	return super.tabBarItem ? super.tabBarItem : self.child.tabBarItem;
}

- (void)bindChildViewController:(UIViewController<RNNParentProtocol>*)child {
	self.child = child;
	[self addChildViewController:self.child];
	[self.child.view setFrame:self.view.bounds];
	[self.view addSubview:self.child.view];
	[self.view bringSubviewToFront:self.child.view];
}

- (UIViewController *)getLeafViewController {
	return [self.child getLeafViewController];
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.child.preferredStatusBarStyle;
}

@end
