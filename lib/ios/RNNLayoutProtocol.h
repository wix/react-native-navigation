#import "RNNLayoutInfo.h"
#import "RNNBasePresenter.h"
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@protocol RNNLayoutProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
						   creator:(id<RNNComponentViewCreator>)creator
						   options:(RNNNavigationOptions *)options
					defaultOptions:(RNNNavigationOptions *)defaultOptions
						 presenter:(RNNBasePresenter *)presenter
					  eventEmitter:(RNNEventEmitter *)eventEmitter
			  childViewControllers:(NSArray *)childViewControllers;

- (void)renderTreeAndWait:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (UIViewController<RNNLayoutProtocol> *)getCurrentChild;

- (CGFloat) getTopBarHeight;

- (void)mergeOptions:(RNNNavigationOptions *)options;

- (RNNNavigationOptions *)resolveOptions;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)overrideOptions:(RNNNavigationOptions *)options;

- (void)onChildWillAppear;

@end
