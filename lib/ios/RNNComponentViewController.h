#import "RNNLayoutNode.h"
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNUIBarButtonItem.h"
#import "RNNLayoutInfo.h"
#import "UIViewController+LayoutProtocol.h"
#import "RNNComponentPresenter.h"

typedef void (^PreviewCallback)(UIViewController *vc);

@interface RNNComponentViewController : UIViewController <RNNLayoutProtocol, UIViewControllerPreviewingDelegate, UISearchResultsUpdating, UISearchBarDelegate, UINavigationControllerDelegate, UISplitViewControllerDelegate>

@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, strong) RNNComponentPresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;

@property (nonatomic, strong) UIViewController* previewController;
@property (nonatomic, copy) PreviewCallback previewCallback;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
				   rootViewCreator:(id<RNNComponentViewCreator>)creator
					  eventEmitter:(RNNEventEmitter*)eventEmitter
						 presenter:(RNNComponentPresenter *)presenter
						   options:(RNNNavigationOptions *)options
					defaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)onButtonPress:(RNNUIBarButtonItem *)barButtonItem;

- (void)destroyReactView;

@end
