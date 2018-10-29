#import "RNNRootViewController.h"
#import <React/RCTConvert.h>
#import "RNNAnimator.h"
#import "RNNCustomTitleView.h"
#import "RNNPushAnimation.h"
#import "RNNReactView.h"
#import "RNNParentProtocol.h"
#import "RNNTitleViewHelper.h"

@interface RNNRootViewController() {
	RNNReactView* _customTitleView;
	UIView* _customTopBar;
	UIView* _customTopBarBackground;
	BOOL _isBeingPresented;
}

@property (nonatomic, copy) RNNReactViewReadyCompletionBlock reactViewReadyBlock;

@end

@implementation RNNRootViewController

@synthesize previewCallback;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo rootViewCreator:(id<RNNRootViewCreator>)creator eventEmitter:(RNNEventEmitter *)eventEmitter presenter:(RNNViewControllerPresenter *)presenter options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions {
	self = [super init];
	
	self.layoutInfo = layoutInfo;
	self.creator = creator;
	if (self.creator) {
		self.view = [creator createRootView:self.layoutInfo.name rootViewId:self.layoutInfo.componentId];
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(reactViewReady)
													 name: @"RCTContentDidAppearNotification"
												   object:nil];
	}
	
	self.eventEmitter = eventEmitter;
	self.presenter = presenter;
	[self.presenter bindViewController:self viewCreator:self.creator];
	self.options = options;
	self.defaultOptions = defaultOptions;
	
	[self.presenter applyOptionsOnInit:self.resolveOptions];
	
	self.animator = [[RNNAnimator alloc] initWithTransitionOptions:self.resolveOptions.customTransition];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJsReload)
												 name:RCTJavaScriptWillStartLoadingNotification
											   object:nil];
	self.navigationController.delegate = self;
	
	return self;
}

- (instancetype)initExternalComponentWithLayoutInfo:(RNNLayoutInfo *)layoutInfo eventEmitter:(RNNEventEmitter *)eventEmitter presenter:(RNNViewControllerPresenter *)presenter options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions {
	self = [self initWithLayoutInfo:layoutInfo rootViewCreator:nil eventEmitter:eventEmitter presenter:presenter options:options defaultOptions:defaultOptions];
	return self;
}

- (void)bindViewController:(UIViewController *)viewController {
	[self addChildViewController:viewController];
	[self.view addSubview:viewController.view];
	[viewController didMoveToParentViewController:self];
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	if (parent) {
		[_presenter applyOptionsOnWillMoveToParentViewController:self.resolveOptions];
	}
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[_presenter mergeOptions:options currentOptions:self.options defaultOptions:self.defaultOptions];
	[((UIViewController<RNNLayoutProtocol> *)self.parentViewController) mergeOptions:options];
	
	[self initCustomViews];
}

- (void)overrideOptions:(RNNNavigationOptions *)options {
	[self.options overrideOptions:options];
}

- (void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
	_isBeingPresented = YES;
	
	[_presenter applyOptions:self.resolveOptions];
	[((UIViewController<RNNParentProtocol> *)self.parentViewController) onChildWillAppear];
	
	[self initCustomViews];
}

- (RNNNavigationOptions *)resolveOptions {
	return [self.options withDefault:self.defaultOptions];
}

-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	[self.eventEmitter sendComponentDidAppear:self.layoutInfo.componentId componentName:self.layoutInfo.name];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	_isBeingPresented = NO;
}

- (void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[self.eventEmitter sendComponentDidDisappear:self.layoutInfo.componentId componentName:self.layoutInfo.name];
}

- (void)reactViewReady {
	if (_reactViewReadyBlock) {
		_reactViewReadyBlock();
		_reactViewReadyBlock = nil;
	}
	
	[[NSNotificationCenter defaultCenter] removeObserver:self name:@"RCTContentDidAppearNotification" object:nil];
}


- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	if (wait && !self.isExternalViewController) {
		[self onReactViewReady:readyBlock];
	} else {
		readyBlock();
	}
}

- (UIViewController *)getCurrentChild {
	return self;
}

- (void)onReactViewReady:(RNNReactViewReadyCompletionBlock)readyBlock {
	if (self.isExternalViewController) {
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

- (void)initCustomViews {
	[self setCustomNavigationTitleView];
	[self setCustomNavigationBarView];
	[self setCustomNavigationComponentBackground];
	
	if (!_customTitleView) {
		[self setTitleViewWithSubtitle];
	}
}

- (void)setTitleViewWithSubtitle {
	if (self.resolveOptions.topBar.subtitle.text.hasValue) {
		RNNTitleViewHelper* titleViewHelper = [[RNNTitleViewHelper alloc] initWithTitleViewOptions:self.resolveOptions.topBar.title subTitleOptions:self.resolveOptions.topBar.subtitle viewController:self];
		[titleViewHelper setup];
	}
}

- (void)setCustomNavigationTitleView {
	if (!_customTitleView && _isBeingPresented) {
		if (self.resolveOptions.topBar.title.component.name.hasValue) {
			_customTitleView = (RNNReactView*)[_creator createRootViewFromComponentOptions:self.resolveOptions.topBar.title.component];
			_customTitleView.backgroundColor = UIColor.clearColor;
			NSString* alignment = [self.resolveOptions.topBar.title.component.alignment getWithDefaultValue:@""];
			[_customTitleView setAlignment:alignment];
			BOOL isCenter = [alignment isEqualToString:@"center"];
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
		if (self.resolveOptions.topBar.component.name.hasValue) {
			RCTRootView *reactView = (RCTRootView*)[_creator createRootViewFromComponentOptions:self.resolveOptions.topBar.component];
			
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
		if (self.resolveOptions.topBar.background.component.name.hasValue) {
			RCTRootView *reactView = (RCTRootView*)[_creator createRootViewFromComponentOptions:self.resolveOptions.topBar.background.component];
			
			_customTopBarBackground = [[RNNCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:@"fill"];
			[self.navigationController.navigationBar insertSubview:_customTopBarBackground atIndex:1];
		} else if (self.navigationController.navigationBar.subviews.count > 1 && [[self.navigationController.navigationBar.subviews objectAtIndex:1] isKindOfClass:[RNNCustomTitleView class]]) {
			[[self.navigationController.navigationBar.subviews objectAtIndex:1] removeFromSuperview];
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
	return self.resolveOptions.customTransition.animations != nil;
}

- (BOOL)isExternalViewController {
	return !self.creator;
}

- (BOOL)prefersStatusBarHidden {
	if (self.resolveOptions.statusBar.visible.hasValue) {
		return ![self.resolveOptions.statusBar.visible get];
	} else if ([self.resolveOptions.statusBar.hideWithTopBar getWithDefaultValue:NO]) {
		return self.navigationController.isNavigationBarHidden;
	}
	
	return NO;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	if ([[self.resolveOptions.statusBar.style getWithDefaultValue:@"default"] isEqualToString:@"light"]) {
		return UIStatusBarStyleLightContent;
	} else {
		return UIStatusBarStyleDefault;
	}
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.resolveOptions.layout.supportedOrientations;
}

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated{
	RNNRootViewController* vc =  (RNNRootViewController*)viewController;
	if (![[vc.self.resolveOptions.topBar.backButton.transition getWithDefaultValue:@""] isEqualToString:@"custom"]){
		navigationController.delegate = nil;
	}
}

- (id<UIViewControllerAnimatedTransitioning>)navigationController:(UINavigationController *)navigationController
								  animationControllerForOperation:(UINavigationControllerOperation)operation
											   fromViewController:(UIViewController*)fromVC
												 toViewController:(UIViewController*)toVC {
	if (self.animator) {
		return self.animator;
	} else if (operation == UINavigationControllerOperationPush && self.resolveOptions.animations.push.hasCustomAnimation) {
		return [[RNNPushAnimation alloc] initWithScreenTransition:self.resolveOptions.animations.push];
	} else if (operation == UINavigationControllerOperationPop && self.resolveOptions.animations.pop.hasCustomAnimation) {
		return [[RNNPushAnimation alloc] initWithScreenTransition:self.resolveOptions.animations.pop];
	} else {
		return nil;
	}
	
	return nil;
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.resolveOptions.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.resolveOptions.animations.dismissModal isDismiss:YES];
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
	for (NSDictionary *previewAction in self.resolveOptions.preview.actions) {
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
