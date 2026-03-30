#import "RNNComponentPresenter.h"
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNLayoutInfo.h"
#import "RNNLayoutNode.h"
#import "RNNNavigationOptions.h"
#import "RNNUIBarButtonItem.h"
#import "UIViewController+LayoutProtocol.h"

typedef void (^PreviewCallback)(UIViewController *vc);

@interface RNNComponentViewController
    : UIViewController <RNNLayoutProtocol, UIViewControllerPreviewingDelegate,
                        UISearchResultsUpdating, UISearchBarDelegate,
                        UINavigationControllerDelegate, UISplitViewControllerDelegate>

@property(nonatomic, strong) RNNEventEmitter *eventEmitter;
@property(nonatomic, retain) RNNLayoutInfo *layoutInfo;
@property(nonatomic, strong) RNNComponentPresenter *presenter;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) RNNNavigationOptions *defaultOptions;

@property(nonatomic, strong) UIViewController *previewController;
@property(nonatomic, copy) PreviewCallback previewCallback;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                   rootViewCreator:(id<RNNComponentViewCreator>)creator
                      eventEmitter:(RNNEventEmitter *)eventEmitter
                         presenter:(RNNComponentPresenter *)presenter
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)destroyReactView;

- (void)setInterceptTouchOutside:(BOOL)interceptTouchOutside;

@property(nonatomic) BOOL drawBehindTopBar;
@property(nonatomic) BOOL drawBehindBottomTabs;

@end
