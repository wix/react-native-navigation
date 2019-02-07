#import "RNNTopTabsViewController.h"
#import "RNNSegmentedControl.h"
#import "ReactNativeNavigation.h"
#import "RNNRootViewController.h"

@interface RNNTopTabsViewController () {
	NSArray* _viewControllers;
	UIViewController<RNNParentProtocol>* _currentViewController;
	RNNSegmentedControl* _segmentedControl;
}

@end

@implementation RNNTopTabsViewController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNViewControllerPresenter *)presenter {
	self = [self init];
	
	self.presenter = presenter;
	[self.presenter bindViewController:self];
	
	self.defaultOptions = defaultOptions;
	self.options = options;
	self.layoutInfo = layoutInfo;
	
	[self setViewControllers:childViewControllers];
	
	return self;
}

- (instancetype)init {
	self = [super init];
	
	[self.view setBackgroundColor:[UIColor whiteColor]];
	self.edgesForExtendedLayout = UIRectEdgeNone;
	
	[self createTabBar];
	[self createContentView];
	
	return self;
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
	UIViewController<RNNParentProtocol> *toVC = _viewControllers[index];
	[_contentView addSubview:toVC.view];
	[_currentViewController.view removeFromSuperview];
	_currentViewController = toVC;
}

- (void)setViewControllers:(NSArray *)viewControllers {
	_viewControllers = viewControllers;
	for (RNNRootViewController* childVc in viewControllers) {
		[childVc.view setFrame:_contentView.bounds];
//		[childVc.options.topTab applyOn:childVc];
		[self addChildViewController:childVc];
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

#pragma mark RNNParentProtocol

- (UIViewController *)getCurrentChild {
	return _currentViewController;
}

@end
