#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNAnimator.h"
#import "RNNUIBarButtonItem.h"
#import "RNNLayoutInfo.h"
#import "RNNLayoutProtocol.h"
#import "RNNViewControllerPresenter.h"

typedef void (^PreviewCallback)(UIViewController *vc);

@interface RNNRootViewController : UIViewController	<RNNLayoutProtocol, UIViewControllerPreviewingDelegate, UISearchResultsUpdating, UISearchBarDelegate, UINavigationControllerDelegate, UISplitViewControllerDelegate>

@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, strong) RNNViewControllerPresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;

@property (nonatomic, strong) RNNAnimator* animator;
@property (nonatomic, strong) UIViewController* previewController;
@property (nonatomic, copy) PreviewCallback previewCallback;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
				   rootViewCreator:(id<RNNRootViewCreator>)creator
					  eventEmitter:(RNNEventEmitter*)eventEmitter
						 presenter:(RNNViewControllerPresenter *)presenter
						   options:(RNNNavigationOptions *)options
					defaultOptions:(RNNNavigationOptions *)defaultOptions;

- (instancetype)initExternalComponentWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
									   eventEmitter:(RNNEventEmitter*)eventEmitter
										  presenter:(RNNViewControllerPresenter *)presenter
											options:(RNNNavigationOptions *)options
									 defaultOptions:(RNNNavigationOptions *)defaultOptions;

- (BOOL)isExternalViewController;

- (void)onButtonPress:(RNNUIBarButtonItem *)barButtonItem;

- (void)bindViewController:(UIViewController *)viewController;

@end
