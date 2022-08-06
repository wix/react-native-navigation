#import "RNNComponentViewController.h"
static NSString *const RCTSetNonceValueNotification = @"RCTSetNonceValueNotification";

@import ProgrammaticAccessLibrary;

@interface RNNComponentViewController () <PALNonceLoaderDelegate>
/** The nonce loader to use for nonce requests. */
@property(nonatomic) PALNonceLoader *nonceLoader;
/** The nonce manager result from the last successful nonce request. */
@property(nonatomic) PALNonceManager *nonceManager;

@end

@implementation RNNComponentViewController

@synthesize previewCallback;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo rootViewCreator:(id<RNNComponentViewCreator>)creator eventEmitter:(RNNEventEmitter *)eventEmitter presenter:(RNNComponentPresenter *)presenter options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions {
	self = [super initWithLayoutInfo:layoutInfo creator:creator options:options defaultOptions:defaultOptions presenter:presenter eventEmitter:eventEmitter childViewControllers:nil];
    if (@available(iOS 13.0, *)) {
        self.navigationItem.standardAppearance = [UINavigationBarAppearance new];
        self.navigationItem.scrollEdgeAppearance = [UINavigationBarAppearance new];
    }
	return self;
}

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    _defaultOptions = defaultOptions;
	[_presenter setDefaultOptions:defaultOptions];
}

- (void)overrideOptions:(RNNNavigationOptions *)options {
	[self.options overrideOptions:options];
}


- (void)requestNonceManager {
  PALNonceRequest *request = [[PALNonceRequest alloc] init];
  CGSize windowSize = self.view.frame.size;
  request.continuousPlayback = PALFlagOff;
  request.descriptionURL = [NSURL URLWithString:@"https://shahid.net"];
  request.iconsSupported = YES;
  request.playerType = @"tvOS AVPlayer";
  request.playerVersion = @"5.1.25-sh4";
  request.PPID =self.defaultOptions.ppid;
  request.sessionID = self.defaultOptions.sid;
  request.videoPlayerHeight = windowSize.height;
  request.videoPlayerWidth =  windowSize.width;
  request.willAdAutoPlay = PALFlagOn;
  request.willAdPlayMuted = PALFlagOff;

  if (self.nonceManager) {
    // Detach the old nonce manager's gesture recognizer before destroying it.
    [self.view removeGestureRecognizer:self.nonceManager.gestureRecognizer];
    self.nonceManager = nil;
  }
  [self.nonceLoader loadNonceManagerWithRequest:request];
}

#pragma mark - PALNonceLoaderDelegate methods

- (void)nonceLoader:(PALNonceLoader *)nonceLoader
            withRequest:(PALNonceRequest *)request
    didLoadNonceManager:(PALNonceManager *)nonceManager {
    [[NSNotificationCenter defaultCenter] postNotificationName:RCTSetNonceValueNotification
                                                    object:nil
                                                  userInfo:@{@"nonce": nonceManager.nonce}];
    // Capture the created nonce manager and attach its gesture recognizer to the video view.
    self.nonceManager = nonceManager;
    [self.view addGestureRecognizer:self.nonceManager.gestureRecognizer];
}
- (void)nonceLoader:(PALNonceLoader *)nonceLoader
         withRequest:(PALNonceRequest *)request
    didFailWithError:(NSError *)error {
    NSLog(@"Error generating programmatic access nonce: %@", error);
}

- (void)viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	[_presenter applyOptions:self.resolveOptions];

	  if([self.layoutInfo.name isEqual:@"shahidTV.SplashScreen"]){
        self.nonceLoader = [[PALNonceLoader alloc] init];
        self.nonceLoader.delegate = self;
        [self requestNonceManager];
      }
	[self.parentViewController onChildWillAppear];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self componentDidAppear];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self componentDidDisappear];
}

- (void)loadView {
	[self renderReactViewIfNeeded];
}

- (void)render {
    if (!self.waitForRender)
        [self readyForPresentation];

    [self renderReactViewIfNeeded];
}

- (void)renderReactViewIfNeeded {
    if (!self.isViewLoaded) {
        self.view = [self.creator createRootView:self.layoutInfo.name
                                      rootViewId:self.layoutInfo.componentId
                                          ofType:RNNComponentTypeComponent
                             reactViewReadyBlock:^{
            [self->_presenter renderComponents:self.resolveOptions perform:^{
                [self readyForPresentation];
            }];
        }];
    } else {
        [self readyForPresentation];
    }
}

- (UIViewController *)getCurrentChild {
	return nil;
}

-(void)updateSearchResultsForSearchController:(UISearchController *)searchController {
	[self.eventEmitter sendOnSearchBarUpdated:self.layoutInfo.componentId
										 text:searchController.searchBar.text
									isFocused:searchController.searchBar.isFirstResponder];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
	[self.eventEmitter sendOnSearchBarCancelPressed:self.layoutInfo.componentId];
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

# pragma mark - UIViewController overrides

- (void)willMoveToParentViewController:(UIViewController *)parent {
    [self.presenter willMoveToParentViewController:parent];
}
#if !TARGET_OS_TV
- (UIStatusBarStyle)preferredStatusBarStyle {
    return [self.presenter getStatusBarStyle];
}
- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return [self.presenter getOrientation];
}
#endif

- (BOOL)prefersStatusBarHidden {
    return [self.presenter getStatusBarVisibility];
}



- (BOOL)hidesBottomBarWhenPushed {
    return [self.presenter hidesBottomBarWhenPushed];
}

@end
