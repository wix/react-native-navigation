#import "RNNTopTabsViewController.h"
#import "RNNSegmentedControl.h"
#import "ReactNativeNavigation.h"

@interface RNNTopTabsViewController () {
	NSArray* _viewControllers;
	UIViewController<RNNRootViewProtocol>* _currentViewController;
	RNNSegmentedControl* _segmentedControl;
}

@end

@implementation RNNTopTabsViewController

- (instancetype)init {
	self = [super init];
	
	[self.view setBackgroundColor:[UIColor whiteColor]];
	self.edgesForExtendedLayout = UIRectEdgeNone;
	
	[self createTabBar];
	[self createContentView];
	
	return self;
}

- (void)createTabBar {
	_segmentedControl = [[RNNSegmentedControl alloc] initWithSectionTitles:@[@"", @"", @""]];
	_segmentedControl.frame = CGRectMake(0, 0, self.view.bounds.size.width, 50);
	_segmentedControl.selectionIndicatorLocation = HMSegmentedControlSelectionIndicatorLocationNone;
	_segmentedControl.selectionStyle = HMSegmentedControlSelectionStyleBox;
	_segmentedControl.selectedSegmentIndex = HMSegmentedControlNoSegment;
	
	[_segmentedControl addTarget:self action:@selector(segmentedControlChangedValue:) forControlEvents:UIControlEventValueChanged];
	[self.view addSubview:_segmentedControl];
}

- (void)segmentedControlChangedValue:(HMSegmentedControl*)segmentedControl {
	[self setSelectedViewControllerIndex:segmentedControl.selectedSegmentIndex];
}

- (void)createContentView {
	_contentView = [[UIView alloc] initWithFrame:CGRectMake(0, 50, self.view.bounds.size.width, self.view.bounds.size.height - 50)];
	_contentView.backgroundColor = [UIColor grayColor];
	[self.view addSubview:_contentView];
}

- (void)setSelectedViewControllerIndex:(NSUInteger)index {
	UIViewController<RNNRootViewProtocol> *toVC = _viewControllers[index];
	[_contentView addSubview:toVC.view];
	[_currentViewController.view removeFromSuperview];
	_currentViewController = toVC;
}

- (void)setViewControllers:(NSArray *)viewControllers {
	_viewControllers = viewControllers;
	for (RNNRootViewController* childVc in viewControllers) {
		[childVc.view setFrame:_contentView.bounds];
		[childVc applyTopTabsOptions];
	}
	
	[self setSelectedViewControllerIndex:0];
}

- (void)viewController:(UIViewController*)vc changedTitle:(NSString*)title {
	NSUInteger vcIndex = [_viewControllers indexOfObject:vc];
	[_segmentedControl setTitle:title atIndex:vcIndex];
}

- (void)viewDidLoad {
    [super viewDidLoad];
}

#pragma mark RNNRootViewProtocol

- (UIViewController *)getLeafViewController {
	return _currentViewController;
}

@end
