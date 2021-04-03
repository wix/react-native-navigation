#import "RNNEventEmitter.h"
#import "RNNLayoutProtocol.h"
#import <UIKit/UIKit.h>

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface UIViewController (LayoutProtocol) <RNNLayoutProtocol>

- (void)render;

- (UIViewController *)getCurrentChild;

- (UIViewController *)presentedComponentViewController;

- (UIViewController *)findViewController:(UIViewController *)child;

- (UIViewController *)topMostViewController;

- (void)destroy;

- (void)mergeOptions:(RNNNavigationOptions *)options;

- (void)mergeChildOptions:(RNNNavigationOptions *)options child:(UIViewController *)child;

- (void)onChildAddToParent:(UIViewController *)child options:(RNNNavigationOptions *)options;

- (UINavigationController *)stack;

- (RNNNavigationOptions *)resolveOptions;

- (RNNNavigationOptions *)resolveOptionsWithDefault;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)prepareForTransition;

- (void)readyForPresentation;

- (void)componentWillAppear;

- (void)componentDidAppear;

- (void)componentDidDisappear;

- (void)screenPopped;

- (void)loadChildren:(NSArray *)children;

@property(nonatomic, retain) RNNBasePresenter *presenter;
@property(nonatomic, retain) RNNLayoutInfo *layoutInfo;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) RNNNavigationOptions *defaultOptions;
@property(nonatomic, strong) RNNEventEmitter *eventEmitter;
@property(nonatomic) id<RNNComponentViewCreator> creator;
@property(nonatomic) RNNReactViewReadyCompletionBlock reactViewReadyCallback;
@property(nonatomic) BOOL waitForRender;
@property(nonatomic) BOOL isChildViewControllersLoaded;
@property(nonatomic, strong) RNNReactView *reactView;

@end
