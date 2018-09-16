#import "RNNRootViewController.h"
#import <React/RCTConvert.h>
#import "RNNAnimator.h"
#import "RNNCustomTitleView.h"
#import "RNNPushAnimation.h"
#import "RNNReactView.h"
#import "RNNParentProtocol.h"

@interface RNNRootViewController() {
	RNNReactView* _customTitleView;
	UIView* _customTopBar;
	UIView* _customTopBarBackground;
	BOOL _isBeingPresented;
}

@property (nonatomic) BOOL isExternalComponent;
@property (nonatomic, copy) RNNReactViewReadyCompletionBlock reactViewReadyBlock;

@end

@implementation RNNRootViewController

@synthesize previewCallback;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
				   rootViewCreator:(id<RNNRootViewCreator>)creator
					  eventEmitter:(RNNEventEmitter*)eventEmitter
			   isExternalComponent:(BOOL)isExternalComponent {
	self = [super init];
	self.eventEmitter = eventEmitter;
	self.creator = creator;
	self.isExternalComponent = isExternalComponent;
	self.layoutInfo = layoutInfo;
	self.layoutInfo.options.delegate = self;
	
	self.animator = [[RNNAnimator alloc] initWithTransitionOptions:self.layoutInfo.options.customTransition];
	
	if (!self.isExternalComponent) {
		self.view = [creator createRootView:self.layoutInfo.name rootViewId:self.layoutInfo.componentId];
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(reactViewReady)
													 name: @"RCTContentDidAppearNotification"
												   object:nil];
	}
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJsReload)
												 name:RCTJavaScriptWillStartLoadingNotification
											   object:nil];
	self.navigationController.delegate = self;

	return self;
}

-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
	_isBeingPresented = YES;
	[self.layoutInfo.options applyOn:self];
}

-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	[self.eventEmitter sendComponentDidAppear:self.layoutInfo.componentId componentName:self.layoutInfo.name];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	_isBeingPresented = NO;
}

-(void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[self.eventEmitter sendComponentDidDisappear:self.layoutInfo.componentId componentName:self.layoutInfo.name];
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	if ([self.parentViewController respondsToSelector:@selector(layoutInfo)]) {
		[self.layoutInfo.options mergeOptions:((id<RNNParentProtocol>)self.parentViewController).layoutInfo.options overrideOptions:NO];
	}
	[self.layoutInfo.options applyOn:self];
}

- (void)reactViewReady {
	if (_reactViewReadyBlock) {
		_reactViewReadyBlock();
		_reactViewReadyBlock = nil;
	}
	
	[[NSNotificationCenter defaultCenter] removeObserver:self name:@"RCTContentDidAppearNotification" object:nil];
}


- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	if (wait && !_isExternalComponent) {
		[self onReactViewReady:readyBlock];
	} else {
		readyBlock();
	}
}

- (UIViewController *)getLeafViewController {
	return self;
}

- (void)onReactViewReady:(RNNReactViewReadyCompletionBlock)readyBlock {
	if (self.isCustomViewController) {
		readyBlock();
	} else {
		self.reactViewReadyBlock = readyBlock;
	}
}

-(void)updateSearchResultsForSearchController:(UISearchController *)searchController {
	[self.eventEmitter sendOnSearchBarUpdated:self.layoutInfo.componentId
										 text:searchController.searchBar.text
									isFocused:searchController.searchBar.isFirstResponder];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
	[self.eventEmitter sendOnSearchBarCancelPressed:self.layoutInfo.componentId];
}

- (void)viewDidLoad {
	[super viewDidLoad];
}

- (void)optionsUpdated {
	[self setCustomNavigationTitleView];
	[self setCustomNavigationBarView];
	[self setCustomNavigationComponentBackground];
}

- (void)setCustomNavigationTitleView {
	if (!_customTitleView && _isBeingPresented) {
		if (self.layoutInfo.options.topBar.title.component.name) {
			_customTitleView = (RNNReactView*)[_creator createRootViewFromComponentOptions:self.layoutInfo.options.topBar.title.component];
			_customTitleView.backgroundColor = UIColor.clearColor;
			[_customTitleView setAlignment:self.layoutInfo.options.topBar.title.component.alignment];
			BOOL isCenter = [self.layoutInfo.options.topBar.title.component.alignment isEqualToString:@"center"];
			__weak RNNReactView *weakTitleView = _customTitleView;
			CGRect frame = self.navigationController.navigationBar.bounds;
			[_customTitleView setFrame:frame];
			[_customTitleView setRootViewDidChangeIntrinsicSize:^(CGSize intrinsicContentSize) {
				if (isCenter) {
					[weakTitleView setFrame:CGRectMake(0, 0, intrinsicContentSize.width, intrinsicContentSize.height)];
				} else {
					[weakTitleView setFrame:frame];
				}
			}];
			
			self.navigationItem.titleView = _customTitleView;
		}
	} else if (_customTitleView && _customTitleView.superview == nil) {
		if ([self.navigationItem.title isKindOfClass:[RNNCustomTitleView class]] && !_customTitleView) {
			self.navigationItem.title = nil;
		}
		self.navigationItem.titleView = _customTitleView;
	}
}

- (void)setCustomNavigationBarView {
	if (!_customTopBar) {
		if (self.layoutInfo.options.topBar.component.name) {
			RCTRootView *reactView = (RCTRootView*)[_creator createRootViewFromComponentOptions:self.layoutInfo.options.topBar.component];
			
			_customTopBar = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:@"fill"];
			reactView.backgroundColor = UIColor.clearColor;
			_customTopBar.backgroundColor = UIColor.clearColor;
			[self.navigationController.navigationBar addSubview:_customTopBar];
		} else if ([[self.navigationController.navigationBar.subviews lastObject] isKindOfClass:[RNNCustomTitleView class]] && !_customTopBar) {
			[[self.navigationController.navigationBar.subviews lastObject] removeFromSuperview];
		}
	} else if (_customTopBar && _customTopBar.superview == nil) {
		if ([[self.navigationController.navigationBar.subviews lastObject] isKindOfClass:[RNNCustomTitleView class]] && !_customTopBar) {
			[[self.navigationController.navigationBar.subviews lastObject] removeFromSuperview];
		}
		[self.navigationController.navigationBar addSubview:_customTopBar];
	}
}

- (void)setCustomNavigationComponentBackground {
	if (!_customTopBarBackground) {
		if (self.layoutInfo.options.topBar.background.component.name) {
			RCTRootView *reactView = (RCTRootView*)[_creator createRootViewFromComponentOptions:self.layoutInfo.options.topBar.background.component];
			
			_customTopBarBackground = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:@"fill"];
			[self.navigationController.navigationBar insertSubview:_customTopBarBackground atIndex:1];
		} else if (self.navigationController.navigationBar.subviews.count && [[self.navigationController.navigationBar.subviews objectAtIndex:1] isKindOfClass:[RNNCustomTitleView class]]) {
			[[self.navigationController.navigationBar.subviews objectAtIndex:1] removeFromSuperview];
		}
		
		if (self.layoutInfo.options.topBar.background.clipToBounds) {
			self.navigationController.navigationBar.clipsToBounds = YES;
		} else {
			self.navigationController.navigationBar.clipsToBounds = NO;
		}
	} if (_customTopBarBackground && _customTopBarBackground.superview == nil) {
		if (self.navigationController.navigationBar.subviews.count && [[self.navigationController.navigationBar.subviews objectAtIndex:1] isKindOfClass:[RNNCustomTitleView class]]) {
			[[self.navigationController.navigationBar.subviews objectAtIndex:1] removeFromSuperview];
		}
		[self.navigationController.navigationBar insertSubview:_customTopBarBackground atIndex:1];
		self.navigationController.navigationBar.clipsToBounds = YES;
	}
}

-(BOOL)isCustomTransitioned {
	return self.layoutInfo.options.customTransition.animations != nil;
}

- (BOOL)isCustomViewController {
	return self.isExternalComponent;
}

- (BOOL)prefersStatusBarHidden {
	if (self.layoutInfo.options.statusBar.visible) {
		return ![self.layoutInfo.options.statusBar.visible boolValue];
	} else if ([self.layoutInfo.options.statusBar.hideWithTopBar boolValue]) {
		return self.navigationController.isNavigationBarHidden;
	}
	
	return NO;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	if (self.layoutInfo.options.statusBar.style && [self.layoutInfo.options.statusBar.style isEqualToString:@"light"]) {
		return UIStatusBarStyleLightContent;
	} else {
		return UIStatusBarStyleDefault;
	}
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.layoutInfo.options.layout.supportedOrientations;
}

- (BOOL)hidesBottomBarWhenPushed
{
	if (self.layoutInfo.options.bottomTabs && self.layoutInfo.options.bottomTabs.visible) {
		return ![self.layoutInfo.options.bottomTabs.visible boolValue];
	}
	return NO;
}

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated{
	RNNRootViewController* vc =  (RNNRootViewController*)viewController;
	if (![vc.layoutInfo.options.topBar.backButton.transition isEqualToString:@"custom"]){
		navigationController.delegate = nil;
	}
}

- (id<UIViewControllerAnimatedTransitioning>)navigationController:(UINavigationController *)navigationController
								  animationControllerForOperation:(UINavigationControllerOperation)operation
											   fromViewController:(UIViewController*)fromVC
												 toViewController:(UIViewController*)toVC {
	{
		if (self.animator) {
			return self.animator;
		} else if (operation == UINavigationControllerOperationPush && self.layoutInfo.options.animations.push.hasCustomAnimation) {
			return [[RNNPushAnimation alloc] initWithScreenTransition:self.layoutInfo.options.animations.push];
		} else if (operation == UINavigationControllerOperationPop && self.layoutInfo.options.animations.pop.hasCustomAnimation) {
			return [[RNNPushAnimation alloc] initWithScreenTransition:self.layoutInfo.options.animations.pop];
		} else {
			return nil;
		}
	}
	return nil;
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.layoutInfo.options.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.layoutInfo.options.animations.dismissModal isDismiss:YES];
}

- (UIViewController *)previewingContext:(id<UIViewControllerPreviewing>)previewingContext viewControllerForLocation:(CGPoint)location{
	return self.previewController;
}


- (void)previewingContext:(id<UIViewControllerPreviewing>)previewingContext commitViewController:(UIViewController *)viewControllerToCommit {
	if (self.previewCallback) {
		self.previewCallback(self);
	}
}

- (void)onActionPress:(NSString *)id {
	[_eventEmitter sendOnNavigationButtonPressed:self.layoutInfo.componentId buttonId:id];
}

- (UIPreviewAction *) convertAction:(NSDictionary *)action {
	NSString *actionId = action[@"id"];
	NSString *actionTitle = action[@"title"];
	UIPreviewActionStyle actionStyle = UIPreviewActionStyleDefault;
	if ([action[@"style"] isEqualToString:@"selected"]) {
		actionStyle = UIPreviewActionStyleSelected;
	} else if ([action[@"style"] isEqualToString:@"destructive"]) {
		actionStyle = UIPreviewActionStyleDestructive;
	}
	
	return [UIPreviewAction actionWithTitle:actionTitle style:actionStyle handler:^(UIPreviewAction * _Nonnull action, UIViewController * _Nonnull previewViewController) {
		[self onActionPress:actionId];
	}];
}

- (NSArray<id<UIPreviewActionItem>> *)previewActionItems {
	NSMutableArray *actions = [[NSMutableArray alloc] init];
	for (NSDictionary *previewAction in self.layoutInfo.options.preview.actions) {
		UIPreviewAction *action = [self convertAction:previewAction];
		NSDictionary *actionActions = previewAction[@"actions"];
		if (actionActions.count > 0) {
			NSMutableArray *group = [[NSMutableArray alloc] init];
			for (NSDictionary *previewGroupAction in actionActions) {
				[group addObject:[self convertAction:previewGroupAction]];
			}
			UIPreviewActionGroup *actionGroup = [UIPreviewActionGroup actionGroupWithTitle:action.title style:UIPreviewActionStyleDefault actions:group];
			[actions addObject:actionGroup];
		} else {
			[actions addObject:action];
		}
	}
	return actions;
}

-(void)onButtonPress:(RNNUIBarButtonItem *)barButtonItem {
	[self.eventEmitter sendOnNavigationButtonPressed:self.layoutInfo.componentId buttonId:barButtonItem.buttonId];
}

/**
 *	fix for #877, #878
 */
-(void)onJsReload {
	[self cleanReactLeftovers];
}

/**
 * fix for #880
 */
-(void)dealloc {
	[self cleanReactLeftovers];
}

-(void)cleanReactLeftovers {
	[[NSNotificationCenter defaultCenter] removeObserver:self];
	[[NSNotificationCenter defaultCenter] removeObserver:self.view];
	self.view = nil;
	self.navigationItem.titleView = nil;
	self.navigationItem.rightBarButtonItems = nil;
	self.navigationItem.leftBarButtonItems = nil;
	_customTopBar = nil;
	_customTitleView = nil;
	_customTopBarBackground = nil;
}

@end
