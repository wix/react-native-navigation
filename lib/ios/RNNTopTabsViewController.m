#import "RNNTopTabsViewController.h"
#import "RNNSegmentedControl.h"
#import "ReactNativeNavigation.h"
#import "RNNComponentViewController.h"
#import "UIViewController+LayoutProtocol.h"

@interface RNNTopTabsViewController () {
	NSArray* _viewControllers;
	UIViewController* _currentViewController;
	RNNSegmentedControl* _segmentedControl;
}

@end

@implementation RNNTopTabsViewController

- (instancetype)init {
	self = [super init];
	
	[self.view setBackgroundColor:[UIColor whiteColor]];
	self.edgesForExtendedLayout = UIRectEdgeNone;
	
	return self;
}

- (UIViewController *)getCurrentChild {
	return _currentViewController;
}

- (void)setCurrentChild:(UIViewController *) childController {
    _currentViewController = childController;
}

- (void)createTabBar: (NSArray<NSString *> *)sectiontitles badges: (NSArray<NSString *> *)sectionbadges{
    UIViewController* viewC = self.parentViewController;
    _segmentedControl = [[RNNSegmentedControl alloc] initWithSectionTitlesAndBadges: sectiontitles badges: sectionbadges];
	_segmentedControl.frame = CGRectMake(0, 0, self.view.bounds.size.width, 50);
	_segmentedControl.selectionIndicatorLocation = HMSegmentedControlSelectionIndicatorLocationBottom;
    _segmentedControl.selectionStyle = HMSegmentedControlSelectionStyleBox;
    _segmentedControl.selectedSegmentIndex = 0;

	[_segmentedControl addTarget:self action:@selector(segmentedControlChangedValue:) forControlEvents:UIControlEventValueChanged];
	[self.view addSubview:_segmentedControl];
    
    
}

- (void)segmentedControlChangedValue:(HMSegmentedControl*)segmentedControl {
	[self setSelectedViewControllerIndex:segmentedControl.selectedSegmentIndex];
}

- (void)createContentView {
    _contentView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 50, self.view.bounds.size.width, self.view.bounds.size.height - 50)];
    _contentView.backgroundColor = [UIColor grayColor];
    
    _contentView.pagingEnabled = YES;
    _contentView.showsHorizontalScrollIndicator = NO;
     CGFloat viewWidth = CGRectGetWidth(self.view.frame);
    _contentView.contentSize = CGSizeMake(viewWidth * _segmentedControl.sectionTitles.count, 200);
    _contentView.delegate = self;
    
    [self.view addSubview: _contentView];
}

- (void)setSelectedViewControllerIndex:(NSUInteger)index {
    CGFloat viewWidth = CGRectGetWidth(self.view.frame);
    [_contentView scrollRectToVisible:CGRectMake(viewWidth * index, 0, viewWidth, CGRectGetHeight(self.view.frame)) animated:NO];
    [self setCurrentChild: _viewControllers[index]];
}

- (void)setViewControllers:(NSArray *)viewControllers {
	_viewControllers = viewControllers;
    NSMutableArray* titleArray = [NSMutableArray array];
    NSMutableArray* badgeArray = [NSMutableArray array];
	for (RNNComponentViewController* childVc in viewControllers) {
        [titleArray addObject: [childVc.resolveOptionsWithDefault.topTab.title getWithDefaultValue:@""]];
        [badgeArray addObject: [childVc.resolveOptionsWithDefault.topTab.badge getWithDefaultValue:@""]];
	}
    [self createTabBar: titleArray badges: badgeArray];
    [self createContentView];

    CGFloat viewWidth = CGRectGetWidth(self.view.frame);
    for(NSInteger index = 0; index < viewControllers.count; index ++){
        RNNComponentViewController* childVc = [viewControllers objectAtIndex: index];
        [childVc.view setFrame: CGRectMake(index * viewWidth, 0, viewWidth, CGRectGetHeight(self.view.frame))];
        [self addChildViewController:childVc];
        [_contentView addSubview:childVc.view];
    }
    
	[self setSelectedViewControllerIndex:0];
   
}

- (void)viewController:(UIViewController*)vc changedTitle:(NSString*)title {
	NSUInteger vcIndex = [_viewControllers indexOfObject:vc];
	[_segmentedControl setTitle:title atIndex:vcIndex];
}

- (void)onButtonPress:(RNNUIBarButtonItem *)barButtonItem
{
    [self.eventEmitter sendOnNavigationButtonPressed:self.layoutInfo.componentId buttonId:barButtonItem.buttonId];
}

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)setTopTabOptions: (RNNNavigationOptions *)options child: (UIViewController *)vc {
//    [self.parentViewController ]
    NSUInteger vcIndex = [_viewControllers indexOfObject:vc];
    if(options.topTab.badge.hasValue){
        [_segmentedControl setBadge:[options.topTab.badge getWithDefaultValue:@""] atIndex:vcIndex];
    }
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    CGFloat pageWidth = _contentView.frame.size.width;
    NSInteger page = _contentView.contentOffset.x / pageWidth;
    [_segmentedControl setSelectedSegmentIndex:page animated:YES];
    [self setCurrentChild: _viewControllers[page]];
}

@end
